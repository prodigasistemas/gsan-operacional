package br.gov.pa.cosanpa.gopera.managedBean;

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
	
	private UsuarioProxy usuarioProxy = (UsuarioProxy) session.getAttribute("usuarioProxy");

	private List<UnidadeConsumidora> listaUnidadeConsumidora;
	
    private ContratoEnergiaListagemTO selecionadoLista = new ContratoEnergiaListagemTO();
    
    private LazyDataModel<ContratoEnergiaListagemTO> listaContrato = null;
    
    private ContratoEnergiaDemanda demanda = new ContratoEnergiaDemanda();
    
    private void carregar(){
        try {
            this.registro = repositorio.obterContrato(this.selecionadoLista.getCodigo());
        } catch (Exception e) {
            logger.error(bundle.getText("erro_carregar_contrato_energia"), e);
            this.mostrarMensagemErro(bundle.getText("erro_carregar_contrato_energia"));
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
			registro.setUsuario(usuarioProxy.getCodigo());
			registro.setUltimaAlteracao(new Date());
			return super.confirmarLazy();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar");
			logger.error("Erro ao salvar contrato de energia", e);
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
			this.registro.getDemandas().add(demanda);
			demanda = new ContratoEnergiaDemanda();
		} catch (Exception e) {
			this.mostrarMensagemErro(bundle.getText("erro_incluir_demanda"));
			logger.error(bundle.getText("erro_incluir_demanda"), e);
		}
	}	

	public void excluirDemanda() {
		for (ContratoEnergiaDemanda cdemanda : this.registro.getDemandas()) {
			if (cdemanda.getPeriodoInicial().equals(demanda.getPeriodoInicial()) && cdemanda.getPeriodoFinal().equals(demanda.getPeriodoFinal())) {
				this.registro.getDemandas().remove(cdemanda);
				break;
			}
		}
	}

	/**
	 * GETTERS AND SETTERS
	 */
	public LazyDataModel<ContratoEnergiaListagemTO> getListaContrato() {
		return listaContrato;
	}

    public void setSelecionadoLista(ContratoEnergiaListagemTO selecionadoLista) {
        this.selecionadoLista = selecionadoLista;
    }

    public ContratoEnergiaDemanda getDemanda() {
        return demanda;
    }

    public void setDemanda(ContratoEnergiaDemanda demanda) {
        this.demanda = demanda;
    }
}
