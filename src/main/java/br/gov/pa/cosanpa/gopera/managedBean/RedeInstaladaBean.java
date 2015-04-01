package br.gov.pa.cosanpa.gopera.managedBean;

import static br.gov.model.util.Utilitarios.ano1900;
import static br.gov.model.util.Utilitarios.formataData;

import java.util.ArrayList;
import java.util.Calendar;
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

import br.gov.model.exception.ErroConsultaSistemaExterno;
import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.Parametro;
import br.gov.model.operacao.RedeInstalada;
import br.gov.model.operacao.RegionalProxy;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.model.util.FormatoData;
import br.gov.model.util.Utilitarios;
import br.gov.servicos.operacao.ParametroRepositorio;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;
import br.gov.servicos.operacao.RedeInstaladaRepositorio;
import br.gov.servicos.operacao.to.RedeInstaladaCadastroTO;
import br.gov.servicos.operacao.to.RedeInstaladaListagemTO;

@ManagedBean
@ViewScoped
public class RedeInstaladaBean extends BaseBean<RedeInstalada> {

    private static Logger logger = Logger.getLogger(RedeInstaladaBean.class); 

	@EJB
	private RedeInstaladaRepositorio repositorio;
	
	@EJB
	private ProxyOperacionalRepositorio proxy;

    private RedeInstaladaListagemTO selecionadoLista = new RedeInstaladaListagemTO();
    
    private RedeInstaladaCadastroTO cadastro = new RedeInstaladaCadastroTO();
	
	private LazyDataModel<RedeInstaladaListagemTO> listaConsumo = null;

    @EJB
    private ParametroRepositorio parametroRepositorio;

	/*********************************************
     * PRIVATE METHODS
     *********************************************/
    private void carregar(){
        try {
            this.cadastro = repositorio.obterRedeInstalada(this.selecionadoLista.getId());
            
        } catch (Exception e) {
            logger.error(bundle.getText("erro_carregar_rede_instalada"), e);
            this.mostrarMensagemErro(bundle.getText("erro_carregar_rede_instalada"));
        }
    }

    public boolean validaReferencia(Integer lancamento){
        Parametro dataRetroativa = parametroRepositorio.obterPeloNome(Parametro.Nome.BLOQUEIA_DATA_RETROATIVA);
        
        if ( dataRetroativa == null || dataRetroativa.getValor().equals("0")){
            return true; 
        }
        else{
            Integer referencia = Integer.valueOf(formataData(ano1900(), FormatoData.ANO_MES));
            
            if (session.getAttribute("referencia") != null){
                referencia = Integer.valueOf(String.valueOf(session.getAttribute("referencia")));
            }
            
            Integer hoje = Integer.valueOf(formataData(Calendar.getInstance().getTime(), FormatoData.ANO_MES));
            
            if (lancamento > referencia && lancamento <= hoje){
                return true;
            }
                        
            return false;
        }
    }
    
    protected void atualizarListas(){
        if (listaConsumo == null) {  
            listaConsumo = new LazyDataModel<RedeInstaladaListagemTO>() {
                private static final long serialVersionUID = 1L;

                public List<RedeInstaladaListagemTO> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    try {
                        String regional = filters.get("nomeRegional") != null ? String.valueOf(filters.get("nomeRegional")) : null;
                        String unidadeNegocio = filters.get("nomeUnidadeNegocio") != null ? String.valueOf(filters.get("nomeUnidadeNegocio")) : null;
                        String municipio = filters.get("nomeMunicipio") != null ? String.valueOf(filters.get("nomeMunicipio")) : null;
                        String localidade = filters.get("nomeLocalidade") != null ? String.valueOf(filters.get("nomeLocalidade")) : null;
                        Integer referencia = filters.get("referencia") != null  ? Utilitarios.converteMesAnoParaAnoMes(String.valueOf(filters.get("referencia"))) : null;
                        
                        RedeInstaladaListagemTO to = new RedeInstaladaListagemTO();
                        to.setNomeRegional(regional);
                        to.setNomeUnidadeNegocio(unidadeNegocio);
                        to.setNomeMunicipio(municipio);
                        to.setNomeLocalidade(localidade);
                        to.setReferencia(referencia);
                        
                        List<RedeInstaladaListagemTO> lista = repositorio.obterLista(startingAt, maxPerPage, to);
                        setRowCount(repositorio.obterQtdRegistros(to));
                        setPageSize(maxPerPage);
                        return lista;                       
                    } catch (Exception e) {
                        logger.error("Erro ao carregar lista de rede instalada.", e);
                    }   
                    return null;
                }
                
                public Object getRowKey(RedeInstaladaListagemTO item) {
                    return item.getId();
                }
                
                public RedeInstaladaListagemTO getRowData(String id) {
                    if (id != null && !id.equals("") && !id.equals("null")) {
                        for (RedeInstaladaListagemTO item : listaConsumo) {
                            if(Integer.valueOf(id).equals(item.getId())){
                                return item;
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
        this.registro = new RedeInstalada();
        visualizar();       
    }
    
    public String novo() {
        this.cadastro = new RedeInstaladaCadastroTO();
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

	
	public String cadastrar() {
		try {
			//Verifica se não está cadastrado para o mes de referencia corrente
			if (!this.getEditando()){
				if(repositorio.existeCadastroNestaReferencia(this.cadastro.getCdRegional(),
												  this.cadastro.getCdUnidadeNegocio(),
												  this.cadastro.getCdMunicipio(),
												  this.cadastro.getCdLocalidade(),
												  this.cadastro.getReferencia())){
					throw new Exception(bundle.getText("erro_mes_referencia_ja_cadastrado"));
				}
			}
			if(!validaReferencia(this.registro.getReferencia())) return null;
			return super.cadastrar();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar: " + e.getMessage());
				
		}
		return null;
	}
	
	
	public String confirmar() {
        try {
            registro = new RedeInstalada();
            registro.setCodigo(cadastro.getIdRedeInstalada());
            registro.setRegional(new RegionalProxy(cadastro.getCdRegional()));
            registro.setUnidadeNegocio(new UnidadeNegocioProxy(cadastro.getCdUnidadeNegocio()));
            registro.setMunicipio(new MunicipioProxy(cadastro.getCdMunicipio()));
            registro.setLocalidade(new LocalidadeProxy(cadastro.getCdLocalidade()));
            registro.setReferencia(cadastro.getReferencia());
            registro.setRedeCadastrada(cadastro.getRedeCadastrada());
            registro.setRedeExistente(cadastro.getRedeExistente());
            registro.setUsuario(usuarioProxy.getCodigo());
            registro.setUltimaAlteracao(new Date());
            return super.confirmarLazy();
        } catch (Exception e) {
            this.mostrarMensagemErro("Erro ao Salvar: " + e.getMessage());
        }
        return null;
	}

	/*********************************************
     * GETTERS AND SETTERS
     *********************************************/
	
    public LazyDataModel<RedeInstaladaListagemTO> getListaConsumo() {
        return listaConsumo;
    }
    
    public RedeInstaladaCadastroTO getCadastro() {
        return cadastro;
    }

    public void setCadastro(RedeInstaladaCadastroTO cadastro) {
        this.cadastro = cadastro;
    }

    public void setSelecionadoLista(RedeInstaladaListagemTO selecionadoLista) {
        this.selecionadoLista = selecionadoLista;
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
}
