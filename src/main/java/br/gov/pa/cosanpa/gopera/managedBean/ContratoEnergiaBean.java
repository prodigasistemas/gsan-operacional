package br.gov.pa.cosanpa.gopera.managedBean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.jboss.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.model.operacao.ContratoEnergia;
import br.gov.model.operacao.ContratoEnergiaDemanda;
import br.gov.model.operacao.UnidadeConsumidora;
import br.gov.model.operacao.UsuarioProxy;
import br.gov.servicos.operacao.ContratoEnergiaRepositorio;
import br.gov.servicos.operacao.UnidadeConsumidoraRepositorio;

@ManagedBean
@SessionScoped
public class ContratoEnergiaBean extends BaseBean<ContratoEnergia> {
	
	private static Logger logger = Logger.getLogger(ContratoEnergiaBean.class);

	@EJB
	private ContratoEnergiaRepositorio fachada;
	
	@EJB
	private UnidadeConsumidoraRepositorio fachadaUC;
	
	private List<UnidadeConsumidora> listaUnidadeConsumidora;
	private ContratoEnergiaDemanda demanda;
	
	private String tensaoNominal;
	private String tensaoContratada;
	private String frequencia;
	private String perdasTransformacao;
	private String potenciaInstalada;
	private String dataInicial;
	private String dataFinal;
	private String horarioPontaInicial;
	private String horarioPontaFinal;
	private String horarioReservadoInicial;
	private String horarioReservadoFinal;
	private String convencionalVerde;
	private String demandaSecoPonta;
	private String demandaSecoForaPonta;
	private String demandaUmidoPonta;
	private String demandaUmidoForaPonta;	
	private UsuarioProxy usuarioProxy = (UsuarioProxy) session.getAttribute("usuarioProxy");
	private LazyDataModel<ContratoEnergia> listaContrato = null;
	
	public ContratoEnergiaBean() {
		
	}
	
	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		iniciarLazy();
		this.visualizar();
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new ContratoEnergia();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "ContratoEnergia.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}

	private void iniciarLazy(){
		if (listaContrato == null) {  
			listaContrato = new LazyDataModel<ContratoEnergia>() {
				private static final long serialVersionUID = 1L;

				
				public List<ContratoEnergia> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
					try {					
						List<ContratoEnergia> listaLazy = fachada.obterListaLazy(startingAt, maxPerPage, filters);
						setRowCount(fachada.obterQtdRegistros(filters));
						setPageSize(maxPerPage);						
						return listaLazy;						
					} catch (Exception e) {
						e.printStackTrace();
					}	
					return null;
				}
				
				
				public Object getRowKey(ContratoEnergia contrato) {
					return contrato.getCodigo();
				}
				
				
				public ContratoEnergia getRowData(String consumoId) {
					if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
						Integer id = Integer.valueOf(consumoId);
						
						for (ContratoEnergia contrato : listaContrato) {
							if(id.equals(contrato.getCodigo())){
								return contrato;
							}
						}
					}
					return null;				
				}
			};
	    }
	}
	
	
	public String novo() {
		this.tensaoNominal = "0,00";
		this.tensaoContratada = "0,00";
		this.frequencia = "0,00";
		this.perdasTransformacao = "0,00";
		this.potenciaInstalada = "0,00";
		this.dataInicial = "";
		this.dataFinal = "";
		this.demandaSecoPonta = "0";
		this.demandaSecoForaPonta = "0";
		this.demandaUmidoPonta = "0";
		this.demandaUmidoForaPonta = "0";
		this.convencionalVerde = "0";
		this.registro = new ContratoEnergia();
		return super.novo();
	}

	
	public String consultar() {
		carregar();
		return super.consultar();
	}

	
	public String alterar() {
		carregar();
		return super.alterar();
	}
	
	private void carregar(){
		try {
			this.registro = fachada.obterContrato(this.registro.getCodigo());
			DecimalFormat df = new DecimalFormat("#,##0.00");
			this.tensaoNominal = df.format(registro.getTensaoNominal());
			this.tensaoContratada = df.format(registro.getTensaoContratada());
			this.frequencia = df.format(registro.getFrequencia());
			this.perdasTransformacao = df.format(registro.getPerdasTransformacao());
			this.potenciaInstalada = df.format(registro.getPotenciaInstalada());
			SimpleDateFormat f24h = new SimpleDateFormat("HH:mm");
			this.horarioPontaInicial = (registro.getHorarioPontaInicial() == null ? "" : f24h.format(registro.getHorarioPontaInicial()));
			this.horarioPontaFinal = (registro.getHorarioPontaFinal() == null ? "" : f24h.format(registro.getHorarioPontaFinal()));
			this.horarioReservadoInicial = (registro.getHorarioReservadoInicial() == null ? "" : f24h.format(registro.getHorarioReservadoInicial()));
			this.horarioReservadoFinal = (registro.getHorarioReservadoFinal() == null ? "" : f24h.format(registro.getHorarioReservadoFinal()));
			this.dataInicial = "";
			this.dataFinal = "";
			this.demandaSecoPonta = "0";
			this.demandaSecoForaPonta = "0";
			this.demandaUmidoPonta = "0";
			this.demandaUmidoForaPonta = "0";
			this.convencionalVerde = "0";
			
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao carregar Contrato");
		}			
	}
	
	
	public String confirmar() {
		try {
			SimpleDateFormat f24h = new SimpleDateFormat("HH:mm");  

			registro.setHorarioPontaInicial(f24h.parse(this.getHorarioPontaInicial()));
			registro.setHorarioPontaFinal(f24h.parse(this.getHorarioPontaFinal()));
			registro.setHorarioReservadoInicial(f24h.parse(this.getHorarioReservadoInicial()));
			registro.setHorarioReservadoFinal(f24h.parse(this.getHorarioReservadoFinal()));
			
			registro.setTensaoNominal(Double.parseDouble(tensaoNominal.replace(".", "").replace(",", ".")));
			registro.setTensaoContratada(Double.parseDouble(tensaoContratada.replace(".", "").replace(",", ".")));
			registro.setFrequencia(Double.parseDouble(frequencia.replace(".", "").replace(",", ".")));
			registro.setPerdasTransformacao(Double.parseDouble(perdasTransformacao.replace(".", "").replace(",", ".")));
			registro.setPotenciaInstalada(Double.parseDouble(potenciaInstalada.replace(".", "").replace(",", ".")));
			registro.setUsuario(usuarioProxy.getCodigo());
			registro.setUltimaAlteracao(new Date());
			return super.confirmarLazy();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar");
		}
		return null;
	}
	
	public List<UnidadeConsumidora> getListaUnidadeConsumidora() {
		try {
			listaUnidadeConsumidora = fachadaUC.obterLista();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Consultar Lista de Unidades Consumidoras");
		}
		return listaUnidadeConsumidora;
	}
	
	public void setListaUnidadeConsumidora(List<UnidadeConsumidora> listaUnidadeConsumidora) {
		this.listaUnidadeConsumidora = listaUnidadeConsumidora;
	}
	
	public void incluirDemanda() {
		try{
			ContratoEnergiaDemanda cdemanda = new ContratoEnergiaDemanda();
			SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
			cdemanda.setDataInicial(formataData.parse(dataInicial));
			cdemanda.setDataFinal(formataData.parse(dataFinal));
			
			cdemanda.setDemandaSecoPonta(Integer.parseInt(demandaSecoPonta.replace(",", "").replace(".", "")));
			cdemanda.setDemandaSecoForaPonta(Integer.parseInt(demandaSecoForaPonta.replace(",", "").replace(".", "")));
			cdemanda.setDemandaUmidoPonta(Integer.parseInt(demandaUmidoPonta.replace(",", "").replace(".", "")));
			cdemanda.setDemandaUmidoForaPonta(Integer.parseInt(demandaUmidoForaPonta.replace(",", "").replace(".", "")));
			cdemanda.setConvencionalVerde(Integer.parseInt(convencionalVerde.replace(",", "").replace(".", "")));
			cdemanda.setContrato(this.registro);
			this.registro.getDemanda().add(cdemanda);
		} catch (Exception e) {
			this.mostrarMensagemErro(bundle.getText("erro_incluir_demanda"));
			logger.error(bundle.getText("erro_incluir_demanda"), e);
		}
	}	

	public void excluirDemanda() {
		for (ContratoEnergiaDemanda cdemanda : this.registro.getDemanda()) {
			if (cdemanda.getDataInicial().equals(demanda.getDataInicial()) && cdemanda.getDataFinal().equals(demanda.getDataFinal())) {
				this.registro.getDemanda().remove(cdemanda);
				break;
			}
		}
	}

	/**
	 * GETTERS AND SETTERS
	 * @return
	 */
	public LazyDataModel<ContratoEnergia> getListaContrato() {
		return listaContrato;
	}

	public String getTensaoNominal() {
		return tensaoNominal;
	}

	public void setTensaoNominal(String tensaoNominal) {
		this.tensaoNominal = tensaoNominal;
	}

	public String getTensaoContratada() {
		return tensaoContratada;
	}

	public void setTensaoContratada(String tensaoContratada) {
		this.tensaoContratada = tensaoContratada;
	}

	public String getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(String frequencia) {
		this.frequencia = frequencia;
	}

	public String getPerdasTransformacao() {
		return perdasTransformacao;
	}

	public void setPerdasTransformacao(String perdasTransformacao) {
		this.perdasTransformacao = perdasTransformacao;
	}

	public String getPotenciaInstalada() {
		return potenciaInstalada;
	}

	public void setPotenciaInstalada(String potenciaInstalada) {
		this.potenciaInstalada = potenciaInstalada;
	}

	public ContratoEnergiaDemanda getDemanda() {
		return demanda;
	}

	public void setDemanda(ContratoEnergiaDemanda demanda) {
		this.demanda = demanda;
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getDemandaSecoPonta() {
		return demandaSecoPonta;
	}

	public void setDemandaSecoPonta(String demandaSecoPonta) {
		this.demandaSecoPonta = demandaSecoPonta;
	}

	public String getDemandaSecoForaPonta() {
		return demandaSecoForaPonta;
	}

	public void setDemandaSecoForaPonta(String demandaSecoForaPonta) {
		this.demandaSecoForaPonta = demandaSecoForaPonta;
	}

	public String getDemandaUmidoPonta() {
		return demandaUmidoPonta;
	}

	public void setDemandaUmidoPonta(String demandaUmidoPonta) {
		this.demandaUmidoPonta = demandaUmidoPonta;
	}

	public String getDemandaUmidoForaPonta() {
		return demandaUmidoForaPonta;
	}

	public void setDemandaUmidoForaPonta(String demandaUmidoForaPonta) {
		this.demandaUmidoForaPonta = demandaUmidoForaPonta;
	}

	public String getHorarioPontaInicial() {
		return horarioPontaInicial;
	}

	public void setHorarioPontaInicial(String horarioPontaInicial) {
		this.horarioPontaInicial = horarioPontaInicial;
	}

	public String getHorarioPontaFinal() {
		return horarioPontaFinal;
	}

	public void setHorarioPontaFinal(String horarioPontaFinal) {
		this.horarioPontaFinal = horarioPontaFinal;
	}

	public String getHorarioReservadoInicial() {
		return horarioReservadoInicial;
	}

	public void setHorarioReservadoInicial(String horarioReservadoInicial) {
		this.horarioReservadoInicial = horarioReservadoInicial;
	}

	public String getHorarioReservadoFinal() {
		return horarioReservadoFinal;
	}

	public void setHorarioReservadoFinal(String horarioReservadoFinal) {
		this.horarioReservadoFinal = horarioReservadoFinal;
	}

	public String getConvencionalVerde() {
		return convencionalVerde;
	}

	public void setConvencionalVerde(String convencionalVerde) {
		this.convencionalVerde = convencionalVerde;
	}
}
