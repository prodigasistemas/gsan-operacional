package br.gov.pa.cosanpa.gopera.managedBean;

import static br.gov.model.util.Utilitarios.converteParaDataComUltimoDiaMes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
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
import br.gov.model.operacao.EstacaoOperacional;
import br.gov.model.operacao.Hora;
import br.gov.model.operacao.HoraCMB;
import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.RegionalProxy;
import br.gov.model.operacao.TipoUnidadeOperacional;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.model.util.Utilitarios;
import br.gov.servicos.operacao.EstacaoOperacionalRepositorio;
import br.gov.servicos.operacao.HoraCmbRepositorio;
import br.gov.servicos.operacao.HoraRepositorio;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;
import br.gov.servicos.operacao.to.HoraCmbTO;
import br.gov.servicos.operacao.to.HorasCadastroTO;
import br.gov.servicos.operacao.to.HorasListagemTO;

@ManagedBean
@ViewScoped
public class HorasBean extends BaseBean<Hora>{

	private static Logger logger = Logger.getLogger(HorasBean.class); 

	@EJB
	private HoraRepositorio repositorio;
	
	@EJB
	private HoraCmbRepositorio horaCmbRepositorio;
	
	@EJB
	private EstacaoOperacionalRepositorio estacaoRepositorio;
	
    @EJB
    private ProxyOperacionalRepositorio proxy;

	private LazyDataModel<HorasListagemTO> listaConsumo = null;
	
	private HorasListagemTO selecionadoLista = new HorasListagemTO();
	
	private HorasCadastroTO cadastro = new HorasCadastroTO();
	
	private BigDecimal horasMes;
	
	private BigDecimal horasSistema;
	
	private String tipoEstacao;
	
	public HorasBean() {
	}
	
	/*********************************************
	 * PRIVATE METHODS
	 *********************************************/
	
	private void carregar(){
		try {
			this.cadastro = repositorio.obterHora(this.selecionadoLista.getId());
			
			Map<Integer, HoraCmbTO> todosCMBs = new LinkedHashMap<Integer, HoraCmbTO>();
			for (int i = 1; i <= cadastro.getQuantidadeCmb(); i++) {
                todosCMBs.put(i, new HoraCmbTO(null, i, BigDecimal.ZERO));
            }
			
			List<HoraCmbTO> cmbs = horaCmbRepositorio.obterHoraCMBPorEstacao(cadastro.getTipoUnidadeOperacional(), cadastro.getCdUnidadeOperacional(), cadastro.getReferencia());
			
			for (HoraCmbTO cmb : cmbs) {
				todosCMBs.put(cmb.getCmb(), cmb);
			}
			this.cadastro.getCmbs().clear();
			this.cadastro.getCmbs().addAll(todosCMBs.values());
		} catch (Exception e) {
			logger.error(bundle.getText("erro_carregar_horas"), e);
			this.mostrarMensagemErro(bundle.getText("erro_carregar_horas"));
		}
	}

	protected void atualizarListas(){
		if (listaConsumo == null) {  
			listaConsumo = new LazyDataModel<HorasListagemTO>() {
				private static final long serialVersionUID = 1L;

				public List<HorasListagemTO> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
					try {
						String nomeEstacao = filters.get("nomeEstacao") != null ? String.valueOf(filters.get("nomeEstacao")) : "";
						Integer referencia = filters.get("referencia") != null  ? Utilitarios.converteMesAnoParaAnoMes(String.valueOf(filters.get("referencia"))) : null;
						List<HorasListagemTO> lista = repositorio.obterLista(startingAt, maxPerPage, TipoUnidadeOperacional.valueOf(tipoEstacao), nomeEstacao, referencia);
						setRowCount(repositorio.obterQtdRegistros(TipoUnidadeOperacional.valueOf(tipoEstacao), nomeEstacao, referencia));
						setPageSize(maxPerPage);
						return lista;						
					} catch (Exception e) {
						logger.error("Erro ao carregar lista de horas.", e);
					}	
					return null;
				}
				
				public Object getRowKey(HorasListagemTO consumo) {
					return consumo.getId();
				}
				
				public HorasListagemTO getRowData(String consumoId) {
					if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
						Integer id = Integer.valueOf(consumoId);
						
						for (HorasListagemTO consumo : listaConsumo) {
							if(id.equals(consumo.getId())){
								return consumo;
							}
						}
					}
					return null;				
				}
			};
	    }
	}

	public String iniciar() {
		return null;
	}
	
	@PostConstruct
	public void init(){
        fachada = this.repositorio;     
        atualizarListas();
        this.registro = new Hora();
        visualizar();	    
	}
	
	public String novo() {
		this.cadastro = new HorasCadastroTO();
		super.novo();
		return null;
	}
	
	public String consultar() {
		carregar();
		super.consultar();
		return null;
	}
	
    public String cancelar() {
        this.estadoAtual =  this.estadoAnterior;
        setDesabilitaForm(false);
        return null;
    }
	
	public String alterar() {
		carregar();
		super.alterar();
		return null;
	}
	
	public void carregarCMB(){
		try {
			this.cadastro.getCmbs().clear();
			EstacaoOperacional estacao = estacaoRepositorio.buscarPorTipoECodigo(TipoUnidadeOperacional.valueOf(tipoEstacao).getId(), cadastro.getCdUnidadeOperacional());
			
			for(int i = 1; i <= estacao.getQuantidadeCmb(); i++){
			    this.cadastro.getCmbs().add(new HoraCmbTO(null, i, BigDecimal.ZERO));
			}
		} catch (Exception e) {
			logger.error(bundle.getText("erro_carregar_cmbs"), e);
			this.mostrarMensagemErro(bundle.getText("erro_carregar_cmbs"));
		}
	}
	
	public String cadastrar(){
		try {
			if (horasSistema.subtract(horasMes).doubleValue() > 0){
				this.mostrarMensagemErro(bundle.getText("erro_horas_invalidas"));
				return null;
			}
			
			if (!this.getEditando()){
				if(repositorio.existeMesReferencia(TipoUnidadeOperacional.valueOf(this.tipoEstacao).getId()
				        , this.cadastro.getCdUnidadeOperacional()
				        , this.cadastro.getReferencia())){
					this.mostrarMensagemErro(bundle.getText("erro_mes_referencia_ja_cadastrado"));
					return null;
				}
			}		
			if(!validaReferencia(converteParaDataComUltimoDiaMes(this.cadastro.getReferencia()))) return null;
			return super.cadastrar();
		} catch (Exception e) {
		    logger.error(bundle.getText("erro_salvar_horas_trabalhadas"), e);
			this.mostrarMensagemErro(bundle.getText("erro_salvar_horas_trabalhadas"));
		}
		return null;
	}
	
	public String confirmar() {
		try {
		    registro = new Hora();
		    registro.setCodigo(cadastro.getCodigo());
		    registro.setReferencia(cadastro.getReferencia());
		    registro.setParadaPorEnergia(cadastro.getParadaPorEnergia());
		    registro.setParadaPorControle(cadastro.getParadaPorControle());
		    registro.setParadaPorManutencao(cadastro.getParadaPorManutencao());
		    registro.setUsuario(usuarioProxy.getCodigo());
		    registro.setUltimaAlteracao(new Date());
		    registro.setTipoEstacao(TipoUnidadeOperacional.valueOf(tipoEstacao).getId());
		    registro.setIdEstacao(cadastro.getCdUnidadeOperacional());
		    
			for (HoraCmbTO to : cadastro.getCmbs()){
			    HoraCMB cmb = new HoraCMB();
			    cmb.setCodigo(to.getCodigo());
			    cmb.setCmb(to.getCmb());
			    cmb.setMedicao(to.getMedicao());
			    cmb.setHora(registro);
			    registro.addCmb(cmb);
			}			
			return super.confirmarLazy();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar: " + e.getMessage());
		}
		return null;
	}
	
	/*********************************************
	 * GETTERS AND SETTERS
	 *********************************************/
	
	public String getTipoEstacao() {
		return tipoEstacao;
	}

    public HorasCadastroTO getCadastro() {
        return cadastro;
    }

    public void setCadastro(HorasCadastroTO cadastro) {
        this.cadastro = cadastro;
    }

    public void setSelecionadoLista(HorasListagemTO selecionadoLista) {
		this.selecionadoLista = selecionadoLista;
	}

	public void setTipoEstacao(String tipoEstacao) {
		this.tipoEstacao = tipoEstacao;
	}

	public BigDecimal getHorasMes() {
        horasMes = BigDecimal.ZERO;
        Calendar calendario = Calendar.getInstance();
        if (cadastro != null && cadastro.getReferencia() != null){
            calendario.setTime(converteParaDataComUltimoDiaMes(this.cadastro.getReferencia()));
            horasMes = new BigDecimal(calendario.getActualMaximum(Calendar.DAY_OF_MONTH) * 24);
        }
	    
        return horasMes;
    }

    public BigDecimal getHorasSistema() {
        horasSistema = BigDecimal.ZERO;
        if (cadastro != null){
            for (HoraCmbTO cmb : this.cadastro.getCmbs()){
                horasSistema = horasSistema.add(cmb.getMedicao());  
            }               
        }
        
		horasSistema = horasSistema.add(cadastro.getParadaPorEnergia())
                .add(cadastro.getParadaPorControle())
                .add(cadastro.getParadaPorManutencao());
		
		return horasSistema;
	}

	public LazyDataModel<HorasListagemTO> getListaConsumo() {
		return listaConsumo;
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
