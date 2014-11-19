package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.gov.model.operacao.UnidadeMedida;
import br.gov.pa.cosanpa.gopera.enums.EstadoManageBeanEnum;
import br.gov.servicos.operacao.UnidadeMedidaRepositorio;

@ManagedBean
@SessionScoped
public class CopyOfUnidadeMedidaBean {

	@EJB
	private UnidadeMedidaRepositorio fachada;
	private EstadoManageBeanEnum estadoAtual = EstadoManageBeanEnum.VISUALIZANDO;
	
	public UnidadeMedida registro = new UnidadeMedida();
	
	private boolean desabilitaForm = false;

	private List<UnidadeMedida> lista = new ArrayList<UnidadeMedida>();
	
	private List<UnidadeMedida> filtro;


	public CopyOfUnidadeMedidaBean() {
		
	}

	public List<UnidadeMedida> getLista() {
		return lista;
	}

	public void setLista(List<UnidadeMedida> lista) {
		this.lista = lista;
	}

	
	public String iniciar() {
		return "UnidadeMedida.jsf";
	}
	
	
	public String confirmar() {
		return "";
	}
	
	public String voltar() {
		return "";
	}
	
	
	public String novo() {
		setEstado(EstadoManageBeanEnum.CADASTRANDO);
		setDesabilitaForm(false);
		
		return null;
	}

	public String excluir() {
		return "";
	}
	
	public String excluirLazy() {
		return "";
	}
	
	public String confirmarLazy() {
		return "";
	}
	
	public String cadastrar() {
		return "";
	}

	public String consultar() {
		return "";
	}
	
	public String alterar() {
		return "";
	}
	
	public String cancelar() {
		return "";
	}
	

	public UnidadeMedida getRegistro() {
		return registro;
	}


	public void setRegistro(UnidadeMedida registro) {
		this.registro = registro;
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
	
	public EstadoManageBeanEnum getEstado() {
		return estadoAtual;
	}

	private void setEstado(EstadoManageBeanEnum estado) {
		this.estadoAtual = estado;
	}

	public List<UnidadeMedida> getFiltro() {
		return filtro;
	}

	public void setFiltro(List<UnidadeMedida> filtro) {
		this.filtro = filtro;
	}

	public boolean isDesabilitaForm() {
		return desabilitaForm;
	}

	public void setDesabilitaForm(boolean desabilitaForm) {
		this.desabilitaForm = desabilitaForm;
	}	
	
	
}
