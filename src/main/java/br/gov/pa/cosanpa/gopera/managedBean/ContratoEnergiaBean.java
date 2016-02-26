package br.gov.pa.cosanpa.gopera.managedBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.jboss.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.model.operacao.ContratoEnergia;
import br.gov.model.operacao.ContratoEnergiaDemanda;
import br.gov.model.operacao.UnidadeConsumidora;
import br.gov.model.operacao.UsuarioProxy;
import br.gov.model.util.Utilitarios;
import br.gov.servicos.operacao.ContratoEnergiaRepositorio;
import br.gov.servicos.operacao.UnidadeConsumidoraRepositorio;
import br.gov.servicos.operacao.to.ContratoEnergiaListagemTO;

@ManagedBean
@ViewScoped
public class ContratoEnergiaBean extends BaseBean<ContratoEnergia> {
	
	private static Logger logger = Logger.getLogger(ContratoEnergiaBean.class);

	@EJB
	private ContratoEnergiaRepositorio repositorio;
	
	@EJB
	private UnidadeConsumidoraRepositorio fachadaUC;
	
	private List<UnidadeConsumidora> listaUnidadeConsumidora;
	private ContratoEnergiaDemanda demanda;
	
	private String tensaoNominal;
	private String tensaoContratada;
	private String frequencia;
	private String perdasTransformacao;
	private String potenciaInstalada;
	private Integer periodoInicial;
	private Integer periodoFinal;
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
	
    private ContratoEnergiaListagemTO selecionadoLista = new ContratoEnergiaListagemTO();
    
    private LazyDataModel<ContratoEnergiaListagemTO> listaContrato = null;
    
    private void carregar(){
        try {
            this.registro = repositorio.obterContrato(this.selecionadoLista.getCodigo());
            
        } catch (Exception e) {
            logger.error(bundle.getText("erro_carregar_rede_instalada"), e);
            this.mostrarMensagemErro(bundle.getText("erro_carregar_rede_instalada"));
        }
    }    
	
    protected void atualizarListas(){
        if (listaContrato == null) {  
            listaContrato = new LazyDataModel<ContratoEnergiaListagemTO>() {
                private static final long serialVersionUID = -2632400661928403340L;

                public List<ContratoEnergiaListagemTO> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    try {
                        String numeroContrato   = filters.get("numeroContrato") != null ? String.valueOf(filters.get("numeroContrato")) : null;
                        Integer uc              = filters.get("uc") != null ? Integer.valueOf(String.valueOf(filters.get("uc"))) : null;
                        Integer vigenciaInicial = filters.get("vigenciaInicial") != null  ? Utilitarios.converteMesAnoParaAnoMes(String.valueOf(filters.get("vigenciaInicial"))) : null;
                        Integer vigenciaFinal   = filters.get("vigenciaFinal")   != null  ? Utilitarios.converteMesAnoParaAnoMes(String.valueOf(filters.get("vigenciaFinal"))) : null;
                        
                        ContratoEnergiaListagemTO to = new ContratoEnergiaListagemTO();
                        to.setNumeroContrato(numeroContrato);
                        to.setUc(uc);
                        to.setVigenciaInicial(vigenciaInicial);
                        to.setVigenciaFinal(vigenciaFinal);
                        
                        List<ContratoEnergiaListagemTO> lista = repositorio.obterLista(startingAt, maxPerPage, to);
                        setRowCount(repositorio.obterQtdRegistros(to));
                        setPageSize(maxPerPage);
                        return lista;                       
                    } catch (Exception e) {
                        logger.error("Erro ao carregar lista de rede instalada.", e);
                    }   
                    return null;
                }
                
                public Object getRowKey(ContratoEnergiaListagemTO item) {
                    return item.getCodigo();
                }
                
                public ContratoEnergiaListagemTO getRowData(String id) {
                    if (id != null && !id.equals("") && !id.equals("null")) {
                        for (ContratoEnergiaListagemTO item : listaContrato) {
                            if(Integer.valueOf(id).equals(item.getCodigo())){
                                return item;
                            }
                        }
                    }
                    return null;                
                }
            };
        }
    }
    
    @PostConstruct
    public void init(){
        fachada = this.repositorio;     
        atualizarListas();
        this.registro = new ContratoEnergia();
        visualizar();       
    }
	
	
	public ContratoEnergiaBean() {
		
	}
	
	public String iniciar() {
	    return null;
	}

	public String novo() {
		this.tensaoNominal = "0,00";
		this.tensaoContratada = "0,00";
		this.frequencia = "0,00";
		this.perdasTransformacao = "0,00";
		this.potenciaInstalada = "0,00";
		this.periodoInicial = 0;
		this.periodoFinal = 0;
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
		super.consultar();
		return null;
	}

	
	public String alterar() {
		carregar();
		super.alterar();
		return null;
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
			cdemanda.setPeriodoInicial(periodoInicial);
			cdemanda.setPeriodoFinal(periodoFinal);
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
			if (cdemanda.getPeriodoInicial().equals(demanda.getPeriodoInicial()) && cdemanda.getPeriodoFinal().equals(demanda.getPeriodoFinal())) {
				this.registro.getDemanda().remove(cdemanda);
				break;
			}
		}
	}

	/**
	 * GETTERS AND SETTERS
	 * @return
	 */
	
	public LazyDataModel<ContratoEnergiaListagemTO> getListaContrato() {
		return listaContrato;
	}

    public void setSelecionadoLista(ContratoEnergiaListagemTO selecionadoLista) {
        this.selecionadoLista = selecionadoLista;
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

	public Integer getPeriodoInicial() {
        return periodoInicial;
    }

    public void setPeriodoInicial(Integer periodoInicial) {
        this.periodoInicial = periodoInicial;
    }

    public Integer getPeriodoFinal() {
        return periodoFinal;
    }

    public void setPeriodoFinal(Integer periodoFinal) {
        this.periodoFinal = periodoFinal;
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
