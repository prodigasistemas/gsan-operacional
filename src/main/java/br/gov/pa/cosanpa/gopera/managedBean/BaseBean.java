package br.gov.pa.cosanpa.gopera.managedBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import br.gov.model.operacao.UsuarioProxy;
import br.gov.model.util.GenericRepository;
import br.gov.pa.cosanpa.gopera.enums.EstadoManageBeanEnum;
import br.gov.pa.cosanpa.gopera.util.WebUtil;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;

public abstract class BaseBean<T> extends BaseMensagemBean{
	
	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;
	
	private static Logger logger = Logger.getLogger(BaseBean.class);
	
	protected GenericRepository<Integer, T> fachada;
	public Integer pk;
	public T registro;
	protected EstadoManageBeanEnum estadoAtual = EstadoManageBeanEnum.VISUALIZANDO;
	protected EstadoManageBeanEnum estadoAnterior = null;
	private boolean desabilitaForm = false;
	protected Map<String, String> paginasRetorno = new HashMap<String, String>();
	private List<T> lista = new ArrayList<T>();
	private List<T> filtro;
	protected HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	public UsuarioProxy usuarioProxy = null;
	
	public BaseBean() {
		if (session != null && session.getAttribute("usuarioProxy") != null){
			usuarioProxy = (UsuarioProxy) session.getAttribute("usuarioProxy");
		}
	}
	
	
	abstract public String iniciar();
	
	protected void salvar(T obj) throws Exception {
		fachada.salvar(obj);
	}
	
	protected void atualizar(T obj) throws Exception {
		fachada.atualizar(obj);
	}
	
	public EstadoManageBeanEnum getEstadoAtual() {
		return estadoAtual;
	}

	public void setEstadoAtual(EstadoManageBeanEnum estadoAtual) {
		this.estadoAtual = estadoAtual;
	}

	public EstadoManageBeanEnum getEstadoAnterior() {
		return estadoAnterior;
	}

	public void setEstadoAnterior(EstadoManageBeanEnum estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}

	@SuppressWarnings("unchecked")
	public String novo() {
		try {
			this.registro = (T) this.registro.getClass().newInstance();
			setEstado(EstadoManageBeanEnum.CADASTRANDO);
			setDesabilitaForm(false);
			return paginasRetorno.get("novo") != null ? paginasRetorno.get("novo") : "";
		} catch (Exception e) {
			logger.error(bundle.getText("erro_criar_registro"), e);
			mostrarMensagemErro(bundle.getText("erro_criar_registro"));
		}
		return "";
	}
	
	@SuppressWarnings("unchecked")
	public String excluir() {
		try {
			visualizar();
			this.fachada.excluir(pk);
			this.registro = (T) this.registro.getClass().newInstance();
			atualizarListas();
			mostrarMensagemSucesso(bundle.getText("aviso_operacao_sucesso"));
			return paginasRetorno.get("excluir") != null ? paginasRetorno.get("excluir") : "";
		} catch (Exception e) {
			logger.error(bundle.getText("erro_excluir_registro"), e);
			mostrarMensagemErro(bundle.getText("erro_excluir_registro"));
		}
		return "";
	}
	
	public String confirmar() {
		try {
			if (this.estadoAnterior.equals(EstadoManageBeanEnum.CADASTRANDO)) {
				salvar(registro);
			} else {
				atualizar(registro);
			}
			mostrarMensagemSucesso(bundle.getText("aviso_operacao_sucesso"));
			finalizar();
			atualizarListas();
			setDesabilitaForm(true);
			return paginasRetorno.get("salvar") != null ? paginasRetorno.get("salvar") : "";
		} catch (Exception e) {
			logger.error(bundle.getText("erro_salvar"), e);
			mostrarMensagemErro(bundle.getText("erro_salvar"));
		}
		return "";
	}
	
	public String confirmarLazy() {
		try {
			if (this.estadoAnterior.equals(EstadoManageBeanEnum.CADASTRANDO)) {
				salvar(registro);
			} else {
				atualizar(registro);
			}
			mostrarMensagemSucesso(bundle.getText("aviso_operacao_sucesso"));
			finalizar();
			setDesabilitaForm(true);
			return paginasRetorno.get("salvar") != null ? paginasRetorno.get("salvar") : "";
		} catch (Exception e) {
			logger.error(bundle.getText("erro_salvar"), e);
			mostrarMensagemErro(bundle.getText("erro_salvar"));
		}
		return "";
	}
	
	public String cadastrar() {
		this.estadoAnterior = this.estadoAtual;
		mostrarMensagemAviso(bundle.getText("aviso_confirmar_operacao"));
		setEstado(EstadoManageBeanEnum.CONFIRMANDO);
		setDesabilitaForm(true);
		return paginasRetorno.get("confirmar") != null ? paginasRetorno.get("confirmar") : "";
	}

	public String alterar() {
		this.estadoAnterior = this.estadoAtual;
		setEstado(EstadoManageBeanEnum.EDITANDO);
		setDesabilitaForm(false);
		return paginasRetorno.get("editar") != null ? paginasRetorno.get("editar") : "";
	}

	public String consultar() {
		setDesabilitaForm(true);
		this.setEstado(EstadoManageBeanEnum.CONSULTANDO);
		return paginasRetorno.get("consultar") != null ? paginasRetorno.get("consultar") : "";
	}
	
	public GenericRepository<Integer, T> getFachada() {
		return fachada;
	}

	public void setFachada(GenericRepository<Integer, T> fachada) {
		this.fachada = fachada;
		atualizarListas();
		visualizar();
	}

	public T getRegistro() {
		return registro;
	}

	public void setRegistro(T registro) {
		this.registro = registro;
	}

	protected void atualizarListas() {
		try {
			this.lista = this.fachada.obterLista();
			this.filtro = lista;
		} catch (Exception e) {
			logger.error(bundle.getText("erro_atualizar_listagem"), e);
			mostrarMensagemErro(e.getMessage());
		}
	}
	
	public EstadoManageBeanEnum getEstado() {
		return estadoAtual;
	}

	private void setEstado(EstadoManageBeanEnum estado) {
		this.estadoAtual = estado;
	}
	
	public boolean getVisualizando() {
		return this.estadoAtual.equals(EstadoManageBeanEnum.VISUALIZANDO);
	}
	
	public boolean getConfirmando() {
		return this.estadoAtual.equals(EstadoManageBeanEnum.CONFIRMANDO);
	}

	public boolean getFinalizado() {
		return this.estadoAtual.equals(EstadoManageBeanEnum.FINALIZADO);
	}

	public boolean getCadastrando() {
		return getEstado().equals(EstadoManageBeanEnum.CADASTRANDO);
	}

	public boolean getEditando() {
		return getEstado().equals(EstadoManageBeanEnum.EDITANDO);
	}

	public boolean getConsultando() {
		return getEstado().equals(EstadoManageBeanEnum.CONSULTANDO);
	}
	
	public void visualizar() {
		this.setEstado(EstadoManageBeanEnum.VISUALIZANDO);
	}

	public void finalizar() {
		this.setEstado(EstadoManageBeanEnum.FINALIZADO);
	}
	
	public String cancelar() {
		this.estadoAtual =  this.estadoAnterior;
		setDesabilitaForm(false);
		atualizarListas();
		return paginasRetorno.get("cancelar") != null ? paginasRetorno.get("cancelar") : "";
	}
	
	public String cancelarLazy() {
		this.estadoAtual =  this.estadoAnterior;
		setDesabilitaForm(false);
		return paginasRetorno.get("cancelar") != null ? paginasRetorno.get("cancelar") : "";
	}
	
	public String voltar() {
		this.setEstado(EstadoManageBeanEnum.VISUALIZANDO);
		return paginasRetorno.get("voltar") != null ? paginasRetorno.get("voltar") : "";
	}
	
	public Map<String, String> getPaginasRetorno() {
		return paginasRetorno;
	}

	public void setPaginasRetorno(Map<String, String> paginasRetorno) {
		this.paginasRetorno = paginasRetorno;
	}

	public List<T> getLista() {
		return lista;
	}

	public void setLista(List<T> lista) {
		this.lista = lista;
	}

	public List<T> getFiltro() {
		return filtro;
	}

	public void setFiltro(List<T> filtro) {
		this.filtro = filtro;
	}

	public boolean getDesabilitaForm() {
		return desabilitaForm;
	}

	public void setDesabilitaForm(boolean desabilitaForm) {
		this.desabilitaForm = desabilitaForm;
	}
	
	public String filtroData(Date date, String formato){
		SimpleDateFormat formataData = new SimpleDateFormat(formato);
	    return date != null ? formataData.format(date) : "";
	}
	
	public Date primeiroDiaMes(String referencia){
		return (new WebUtil()).primeiroDiaMes(referencia);
	}
	
	public Date ultimoDiaMes(String referencia){
		return (new WebUtil()).ultimoDiaMes(referencia);
	}

	public boolean validaReferencia(Date datLancamento){
		try{
			String bloqueiaDataRetroativa = fachadaProxy.getParametroSistema(8);
			if (bloqueiaDataRetroativa.equals("1")){
				Integer dataReferencia = Integer.parseInt((String) session.getAttribute("referencia"));
				SimpleDateFormat formataData = new SimpleDateFormat("yyyyMM");	
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(datLancamento);
				Integer dataLancamento = Integer.parseInt(formataData.format(gc.getTime()));
				SimpleDateFormat formataData2 = new SimpleDateFormat("MM/yyyy");
				String strDataLancamento = formataData2.format(gc.getTime());
				gc.setTime(new Date());
				Integer dataCorrente = Integer.parseInt(formataData.format(gc.getTime()));
				//Permitir lançamentos entre Mês de Referência e o Mês Corrente
				if (dataLancamento >= dataReferencia && dataLancamento <= dataCorrente){
					return true;
				}
				this.mostrarMensagemErro( bundle.getText("erro_mes_referencia_fora_periodo_permitido") + " (" + strDataLancamento + ")");
				return false;
			}
			else
				return true;
		} catch (Exception e) {
			logger.error(bundle.getText("erro_validar_referencia"), e);
			this.mostrarMensagemErro(bundle.getText("erro_validar_referencia"));
			return false;
		}			
	}
	
	public void setPk(Integer pk){
		this.pk = pk;
	}
}
