package br.gov.pa.cosanpa.gopera.managedBean;

import static br.gov.model.util.Utilitarios.converteParaDataComUltimoDiaMes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.jboss.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.model.exception.ErroConsultaSistemaExterno;
import br.gov.model.operacao.EEAB;
import br.gov.model.operacao.EEABFonteCaptacao;
import br.gov.model.operacao.EEABMedidor;
import br.gov.model.operacao.EEABVolumeEntrada;
import br.gov.model.operacao.EEABVolumeSaida;
import br.gov.model.operacao.EstacaoOperacional;
import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MedidorUnidadeOperacional;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.RegionalProxy;
import br.gov.model.operacao.TipoUnidadeOperacional;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.model.operacao.Volume;
import br.gov.model.util.Utilitarios;
import br.gov.servicos.operacao.EstacaoOperacionalRepositorio;
import br.gov.servicos.operacao.MedidorUnidadeOperacionalRepositorio;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;
import br.gov.servicos.operacao.VolumeRepositorio;
import br.gov.servicos.operacao.to.VolumeCadastroTO;
import br.gov.servicos.operacao.to.VolumeFluxoTO;
import br.gov.servicos.operacao.to.VolumeListagemTO;

@ManagedBean
@ViewScoped
public class VolumeBean extends BaseBean<Volume>{
	
	private static Logger logger = Logger.getLogger(VolumeBean.class);
	
	@EJB
	private VolumeRepositorio repositorio;
	
	@EJB
	private ProxyOperacionalRepositorio proxy;
	
	@EJB
	private EstacaoOperacionalRepositorio estacaoRepositorio;
	
	@EJB
	private MedidorUnidadeOperacionalRepositorio medidorRepositorio;

	private LazyDataModel<VolumeListagemTO> listaVolume = null;
	
	private String tipoEstacao;
	
	private VolumeListagemTO selecionadoLista = new VolumeListagemTO();
	
	private VolumeCadastroTO cadastro = new VolumeCadastroTO();
	
	public VolumeBean(){
		
	}
	
	public String iniciar() {
		return null;
	}
	
	@PostConstruct
	public void init(){
        fachada = this.repositorio;     
        atualizarListas();
        this.registro = new Volume();
        visualizar();	    
	}
	
	public String novo() {
		this.cadastro = new VolumeCadastroTO();
		super.novo();
		return null;
	}
	
	@Override
	protected void atualizarListas() {
		if (listaVolume == null) {  
			listaVolume = new LazyDataModel<VolumeListagemTO>() {
				private static final long serialVersionUID = 1L;

				public List<VolumeListagemTO> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
					try {
						String nomeEstacao = filters.get("nomeEstacao") != null ? String.valueOf(filters.get("nomeEstacao")):"";
						Integer referencia = filters.get("referencia") != null  ? Utilitarios.converteMesAnoParaAnoMes(String.valueOf(filters.get("referencia"))):null;
						List<VolumeListagemTO> lista = repositorio.obterLista(startingAt, maxPerPage, TipoUnidadeOperacional.valueOf(tipoEstacao), nomeEstacao, referencia);
						setRowCount(repositorio.obterQtdRegistros(TipoUnidadeOperacional.valueOf(tipoEstacao), nomeEstacao, referencia));
						setPageSize(maxPerPage);
						return lista;						
					} catch (Exception e) {
						logger.error("Erro ao carregar lista de horas.", e);
					}	
					return null;
				}
				
				public Object getRowKey(VolumeListagemTO consumo) {
					return consumo.getId();
				}
				
				public VolumeListagemTO getRowData(String volumeId) {
					if (volumeId != null && !volumeId.equals("") && !volumeId.equals("null")) {
						Integer id = Integer.valueOf(volumeId);
						
						for (VolumeListagemTO volume : listaVolume) {
							if(id.equals(volume.getId())){
								return volume;
							}
						}
					}
					return null;				
				}
			};
		}
		
	}
	
	private void carregar(){
		try {
			this.cadastro = repositorio.obterVolume(this.selecionadoLista.getId());
			
			
//			this.cadastro.getCmbs().clear();
//			this.cadastro.getCmbs().addAll(todosCMBs.values());
		} catch (Exception e) {
			logger.error(bundle.getText("erro_carregar_volume"), e);
			this.mostrarMensagemErro(bundle.getText("erro_carregar_volume"));
		}
	}
	
	public String cadastrar(){
		try {
//			if (!this.getEditando()){
//				if(repositorio.existeMesReferencia(TipoUnidadeOperacional.valueOf(this.tipoEstacao).getId()
//				        , this.cadastro.getCdUnidadeOperacional()
//				        , this.cadastro.getReferencia())){
//					this.mostrarMensagemErro(bundle.getText("erro_mes_referencia_ja_cadastrado"));
//					return null;
//				}
//			}		
			if(!validaReferencia(converteParaDataComUltimoDiaMes(this.cadastro.getReferencia()))) return null;
			return super.cadastrar();
		} catch (Exception e) {
		    logger.error(bundle.getText("erro_salvar_horas_trabalhadas"), e);
			this.mostrarMensagemErro(bundle.getText("erro_salvar_horas_trabalhadas"));
		}
		return null;
	}
	
	public void carregarFluxoSaida(){
		try {
			 VolumeCadastroTO volumeAux = repositorio.obterVolumePor(TipoUnidadeOperacional.valueOf(tipoEstacao).getId(), cadastro.getTipoUnidadeOperacional());
			
			this.cadastro.getVolumeFluxo().clear();
			this.cadastro.setMedidores(volumeAux.getMedidores());
		} catch (Exception e) {
			logger.error(bundle.getText("erro_carregar_medidor"), e);
			this.mostrarMensagemErro(bundle.getText("erro_carregar_medidor"));
		}
	}
	
	public String consultar(){
		carregar();
		super.consultar();
		return null;
	}

	public VolumeRepositorio getRepositorio() {
		return repositorio;
	}

	public void setRepositorio(VolumeRepositorio repositorio) {
		this.repositorio = repositorio;
	}

	public LazyDataModel<VolumeListagemTO> getListaVolume() {
		return listaVolume;
	}

	public void setListaVolume(LazyDataModel<VolumeListagemTO> listaVolume) {
		this.listaVolume = listaVolume;
	}

	public String getTipoEstacao() {
		return tipoEstacao;
	}

	public void setTipoEstacao(String tipoEstacao) {
		this.tipoEstacao = tipoEstacao;
	}

	public void setSelecionadoLista(VolumeListagemTO selecionadoLista) {
		this.selecionadoLista = selecionadoLista;
	}

	public VolumeCadastroTO getCadastro() {
		return cadastro;
	}

	public void setCadastro(VolumeCadastroTO cadastro) {
		this.cadastro = cadastro;
	}
	
	public List<RegionalProxy> getRegionais() {
        try {
            return proxy.getListaRegional();
        } catch (Exception e) {
            throw new ErroConsultaSistemaExterno(e);
        }
    }

    public List<UnidadeNegocioProxy> getUnidadesNegocio() {
        List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
        if (cadastro != null && cadastro.getCdRegional() != null) {
            try {
                unidadesNegocio = proxy.getListaUnidadeNegocio(cadastro.getCdRegional());
            } catch (Exception e) {
                throw new ErroConsultaSistemaExterno(e);
            }
        }
        return unidadesNegocio;
    }

    public List<MunicipioProxy> getMunicipios() {
        List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
        if (cadastro != null && cadastro.getCdUnidadeNegocio() != null) {
            try {
                municipios = proxy.getListaMunicipio(cadastro.getCdRegional(), cadastro.getCdUnidadeNegocio());
            } catch (Exception e) {
                throw new ErroConsultaSistemaExterno(e);
            }
        }
        
        return municipios;
    }

    public List<LocalidadeProxy> getLocalidades() {
        List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
        if (cadastro != null && cadastro.getCdMunicipio() != null) {
            try {
                localidades = proxy.getListaLocalidade(cadastro.getCdRegional(), cadastro.getCdUnidadeNegocio(), cadastro.getCdMunicipio());
            } catch (Exception e) {
                throw new ErroConsultaSistemaExterno(e);
            }
        }
        return localidades;
    }

    public List<EstacaoOperacional> getEstacoesOperacionais() {
        List<EstacaoOperacional> estacoesOperacionais = new ArrayList<EstacaoOperacional>();
        if (cadastro != null && cadastro.getCdLocalidade() != null) {
            try {
                estacoesOperacionais = estacaoRepositorio.estacoes(cadastro.getCdRegional(), cadastro.getCdUnidadeNegocio(), cadastro.getCdMunicipio(), cadastro.getCdLocalidade(), TipoUnidadeOperacional.valueOf(tipoEstacao).getId());
            } catch (Exception e) {
                throw new ErroConsultaSistemaExterno(e);
            }
        }
        return estacoesOperacionais;
    }
	
}
