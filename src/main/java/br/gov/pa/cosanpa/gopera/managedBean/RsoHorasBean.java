package br.gov.pa.cosanpa.gopera.managedBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.RSO;
import br.gov.model.operacao.RSOHoras;
import br.gov.model.operacao.RSOHorasCMB;
import br.gov.model.operacao.RegionalProxy;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.servicos.operacao.RsoHorasRepositorio;
import br.gov.servicos.operacao.RsoRepositorio;
import br.gov.servicos.operacao.UnidadeConsumidoraRepositorio;

@ManagedBean
@SessionScoped
public class RsoHorasBean extends BaseBean<RSOHoras> {

	@EJB
	private RsoHorasRepositorio fachada;
	
	@EJB
	private RsoRepositorio fachadaRSO;
	
	@EJB
	private UnidadeConsumidoraRepositorio fachadaUC;
	
	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();	
	private List<RSO> listaRSO = new ArrayList<RSO>();
	private LazyDataModel<RSOHoras> listaConsumo = null;
	private Double horasMes;
	private Double horasTrabalhadas;
	
	public RsoHorasBean() {
		
	}

	public Double getHorasMes() {
		return horasMes;
	}

	public void setHorasMes(Double horasMes) {
		this.horasMes = horasMes;
	}

	public Double getHorasTrabalhadas() {
		return horasTrabalhadas;
	}

	public void setHorasTrabalhadas(Double horasTrabalhadas) {
		this.horasTrabalhadas = horasTrabalhadas;
	}

	public LazyDataModel<RSOHoras> getListaConsumo() {
		return listaConsumo;
	}
	
	public List<RegionalProxy> getRegionais() {
		try {
			regionais = fachadaUC.getListaRegional(usuarioProxy);
			return regionais; 
		} catch (Exception e) {
			mostrarMensagemErro("Erro ao consultar sistema externo.");
		}
		return regionais;
	}

	public List<UnidadeNegocioProxy> getUnidadesNegocio() {
		if ((this.registro.getRegionalProxy().getCodigo() != null)) {
			try {
				this.unidadesNegocio =  fachadaUC.getListaUnidadeNegocio(usuarioProxy, 
											 						    this.registro.getRegionalProxy().getCodigo());
				return unidadesNegocio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		return unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	}

	public List<MunicipioProxy> getMunicipios() {
		if (this.registro.getUnidadeNegocioProxy().getCodigo() != null) {
			try {
				this.municipios =  fachadaUC.getListaMunicipio(usuarioProxy, 
															 this.registro.getRegionalProxy().getCodigo(), 
															 this.registro.getUnidadeNegocioProxy().getCodigo());
				return this.municipios;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.municipios = new ArrayList<MunicipioProxy>();
		return this.municipios;
	}

	public List<LocalidadeProxy> getLocalidades() {
		if (this.registro.getMunicipioProxy().getCodigo() != null) {
			try {
				this.localidades =  fachadaUC.getListaLocalidade(usuarioProxy, 
															   this.registro.getRegionalProxy().getCodigo(), 
															   this.registro.getUnidadeNegocioProxy().getCodigo(), 
															   this.registro.getMunicipioProxy().getCodigo());
				return this.localidades;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.localidades = new ArrayList<LocalidadeProxy>();
		return this.localidades;
	}

	public List<RSO> getListaRSO() {
		if (this.registro.getLocalidadeProxy().getCodigo() != null) {
			try {
				this.listaRSO = fachadaUC.getListaRSO(usuarioProxy, 
												     this.registro.getRegionalProxy().getCodigo(), 
												     this.registro.getUnidadeNegocioProxy().getCodigo(), 
												     this.registro.getMunicipioProxy().getCodigo(),
												     this.registro.getLocalidadeProxy().getCodigo());
				return this.listaRSO;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar ETA´s.");
			}
		}
		listaRSO = new ArrayList<RSO>();
		return listaRSO;
	}
	
	@Override
	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		iniciarLazy();
		this.visualizar();	
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new RSOHoras();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "RsoHoras.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}

	private void iniciarLazy(){
		if (listaConsumo == null) {  
			listaConsumo = new LazyDataModel<RSOHoras>() {
				private static final long serialVersionUID = 1L;

				@Override
				public List<RSOHoras> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
					try {					
						List<RSOHoras> listaLazy = fachada.obterListaLazy(startingAt, maxPerPage, filters);
						setRowCount(fachada.obterQtdRegistros(filters));
						setPageSize(maxPerPage);						
						return listaLazy;						
					} catch (Exception e) {
						e.printStackTrace();
					}	
					return null;
				}
				
				@Override
				public Object getRowKey(RSOHoras consumo) {
					return consumo.getCodigo();
				}
				
				@Override
				public RSOHoras getRowData(String consumoId) {
					if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
						Integer id = Integer.valueOf(consumoId);
						
						for (RSOHoras consumo : listaConsumo) {
							if(id.equals(consumo.getCodigo())){
								return consumo;
							}
						}
					}
					return null;				
				}
			};
	    }
	}
	
	@Override
	public String novo() {
		this.registro = new RSOHoras();
		carregarHoras();
		return super.novo();
	}
	
	@Override
	public String consultar() {
		carregar();
		return super.consultar();
	}
	
	@Override
	public String alterar() {
		carregar();
		return super.alterar();
	}
	
	public void carregar(){
		try {
			this.registro = fachada.obterRSOHoras(this.registro.getCodigo());
			DecimalFormat df = new DecimalFormat("#,##0.00");
			for (RSOHorasCMB cmb : this.registro.getCmb()){
				cmb.setHoraAux(df.format(cmb.getHora()));		
			}			
			carregarHoras();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao carregar Horas RSO");
		}
	}
	
	public void carregarCMB(){
		try {
			RSO rso = fachadaRSO.obterRSO(this.registro.getRso().getCodigo());
			this.registro.getCmb().clear();
			for (Integer intI = 1; intI <= rso.getCmbQuantidade(); intI++){
				RSOHorasCMB cmb = new RSOHorasCMB();
				cmb.setQuantidade(intI);
				cmb.setRsohoras(this.registro);
				this.registro.getCmb().add(cmb);
			}
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar CMB");
		}
	}
	
	public void carregarHoras(){
		Calendar calendario = Calendar.getInstance();
		if (this.registro.getReferencia() != null){
			calendario.setTime(this.registro.getReferencia());
		}
				
		horasMes = (double) calendario.getActualMaximum(Calendar.DAY_OF_MONTH) * 24;
		
		horasTrabalhadas = 0.0;
		for (RSOHorasCMB cmb : this.registro.getCmb()){
			horasTrabalhadas = horasTrabalhadas + Double.parseDouble(cmb.getHoraAux().replace(".", "").replace(",", "."));	
		}				
	}
	
	@Override
	public String cadastrar(){
		try {		
			if (horasTrabalhadas > horasMes){
				this.mostrarMensagemErro("Quantidade de Horas Trabalhadas não pode ser superior a Quantidade de Horas do Mês.");
				return null;
			}
			//Verifica se não está cadastrado para o mes de referencia corrente
			if (!this.getEditando()){
				if(!fachada.verificaMesReferencia(this.registro.getRso().getCodigo(), this.filtroData(this.registro.getReferencia(), "yyyy-MM-dd")) ){
					this.mostrarMensagemErro("Mês de referência já cadastrado!");
					return null;
				}
			}		
			if(!validaReferencia(this.registro.getReferencia())) return null;
			return super.cadastrar();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar: " + e.getMessage());
		}
		return null;
	}
	
	@Override
	public String confirmar() {
		try {
			for (RSOHorasCMB cmb : this.registro.getCmb()){
				cmb.setHora(Double.parseDouble(cmb.getHoraAux().replace(".", "").replace(",", ".")));	
			}			
			registro.setUsuario(usuarioProxy.getCodigo());
			registro.setUltimaAlteracao(new Date());
			return super.confirmarLazy();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar: " + e.getMessage());
		}
		return null;
	}
}
