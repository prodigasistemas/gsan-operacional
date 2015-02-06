package br.gov.pa.cosanpa.gopera.managedBean;

import static br.gov.model.util.Utilitarios.ano1900;
import static br.gov.model.util.Utilitarios.converteData;
import static br.gov.model.util.Utilitarios.formataData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
import br.gov.model.operacao.Consumo;
import br.gov.model.operacao.ConsumoProduto;
import br.gov.model.operacao.EstacaoOperacional;
import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.Parametro;
import br.gov.model.operacao.Produto;
import br.gov.model.operacao.RegionalProxy;
import br.gov.model.operacao.TipoUnidadeOperacional;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.model.util.FormatoData;
import br.gov.pa.cosanpa.gopera.enums.EstadoManageBeanEnum;
import br.gov.servicos.operacao.ConsumoProdutoRepositorio;
import br.gov.servicos.operacao.ConsumoRepositorio;
import br.gov.servicos.operacao.EstacaoOperacionalRepositorio;
import br.gov.servicos.operacao.ParametroRepositorio;
import br.gov.servicos.operacao.ProdutoRepositorio;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;
import br.gov.servicos.operacao.to.ConsumoCadastroTO;
import br.gov.servicos.operacao.to.ConsumoFiltroTO;
import br.gov.servicos.operacao.to.ConsumoListagemTO;
import br.gov.servicos.operacao.to.ConsumoProdutoListagemTO;
import br.gov.servicos.operacao.to.ConsumoProdutoTO;

@ManagedBean
@ViewScoped
public class ConsumoProdutoBean extends BaseBean<Consumo> {

    private static Logger logger = Logger.getLogger(ConsumoProdutoBean.class); 
	
    private LazyDataModel<ConsumoListagemTO> listaConsumo = null;
    
    private ConsumoListagemTO selecionadoLista = new ConsumoListagemTO();
    
    private String tipoEstacao;
    
	private Date dataBase = null;
	
	private ConsumoCadastroTO cadastro = null;
	
	private List<ConsumoProdutoListagemTO> consumos = null;
	
    private List<Date> datas = new ArrayList<Date>();
    
    private boolean exibeConsumos;
    
    private Integer indice;

    @EJB
    private ConsumoRepositorio repositorio;
    
    @EJB
    private ConsumoProdutoRepositorio consumoProdutoRepositorio;
    
    @EJB
    private EstacaoOperacionalRepositorio estacaoRepositorio;
    
    @EJB
    private ProxyOperacionalRepositorio proxy;
    
    @EJB
    private ProdutoRepositorio produtoRepositorio;
    
    @EJB
    private ParametroRepositorio parametroRepositorio;

    public String iniciar() {
        return null;
    }
    
    @PostConstruct
    public void init(){
        fachada = this.repositorio;     
        atualizarListas();
        this.registro = new Consumo();
        visualizar();       
    }

    public String novo(){
        dataBase = Calendar.getInstance().getTime();
        
        inicializaConsumosProdutos();
        
        this.cadastro = new ConsumoCadastroTO(TipoUnidadeOperacional.valueOf(tipoEstacao).getId());
        this.exibeConsumos = false;
        super.novo();
        return null;
    }
    
    public String consultar() {
        carregar();
        super.consultar();
        return null;
    }
    
    public String alterar(){
        carregar();
        super.alterar();
        return null;
    }
    
    public String cancelar(){
        this.estadoAtual =  this.estadoAnterior;
        setDesabilitaForm(false);
        return null;
    }
        
    public String confirmar() {
        try {
            boolean erro = false;
            for (int i = 0; i < datas.size(); i++) {
                Date data = datas.get(i);
                
                cadastro.setData(data);
                
                if (this.estadoAnterior.equals(EstadoManageBeanEnum.CADASTRANDO)) {
                    if (repositorio.existeCadastroConsumo(cadastro)){
                        this.mostrarMensagemErro(bundle.getText("erro_existe_cadastro_consumo") + ": [" + formataData(data) + "]");
                        erro = true;
                        continue;
                    }
                }
                
                if (!dataConsumoValida(data)){
                    this.mostrarMensagemErro(bundle.getText("erro_data_consumo_invalida") + ": [" + formataData(data) + "]");
                    erro = true;
                    continue;
                }
                
                registro = new Consumo();
                registro.setId(cadastro.getId());
                registro.setData(data);
                registro.setTipoEstacao(TipoUnidadeOperacional.valueOf(tipoEstacao).getId());
                registro.setIdEstacao(cadastro.getCdUnidadeOperacional());
                registro.setUsuario(usuarioProxy.getCodigo());
                registro.setUltimaAlteracao(new Date());
                
                List<ConsumoProduto> consumosProdutos = new ArrayList<ConsumoProduto>();
                
                for (ConsumoProdutoListagemTO to : consumos) {
                    ConsumoProduto consumoProduto = new ConsumoProduto();
                    consumoProduto.setConsumo(registro);
                    consumoProduto.setId(to.getIdConsumoProduto());
                    consumoProduto.setQuantidade(to.getQuantidades()[i]);
                    Produto p = new Produto();
                    p.setCodigo(to.getIdProduto());
                    consumoProduto.setProduto(p);
                    consumosProdutos.add(consumoProduto);
                }
                
                registro.setConsumosProduto(consumosProdutos);
                
                if (this.estadoAnterior.equals(EstadoManageBeanEnum.CADASTRANDO)) {
                    salvar(registro);
                } else {
                    atualizar(registro);
                }                   
            }
            if (!erro){
                mostrarMensagemSucesso(bundle.getText("aviso_operacao_sucesso"));
            }
            finalizar();
            setDesabilitaForm(true);            
        } catch (Exception e) {
            this.mostrarMensagemErro("Erro ao salvar consumo de produtos.");
            logger.error("Erro ao salvar consumo de produtos", e);
        }           
        return null;
    }
    
    public void habilitaConsumos(){
        preencheDatas();
        exibeConsumos = true;
    }
    
    private void preencheDatas() {
        Calendar cal = Calendar.getInstance();
        
        Date hoje = cal.getTime();
        
        cal.setTime(dataBase);
        
        datas.clear();
        
        while(!cal.getTime().after(hoje) && datas.size() < 7){
            datas.add(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
    
    private Map<Integer, ConsumoProdutoListagemTO> inicializaConsumosProdutos(){
        Map<Integer, ConsumoProdutoListagemTO> todosProdutos = new LinkedHashMap<Integer, ConsumoProdutoListagemTO>();
        
        produtoRepositorio.obterLista().forEach(e -> todosProdutos.put(e.getCodigo(), new ConsumoProdutoListagemTO(e.getCodigo(), e.getDescricao())));
        
        consumos = new LinkedList<ConsumoProdutoListagemTO>();
        
        consumos.addAll(todosProdutos.values());

        return todosProdutos;
    }
    
    private void carregar(){
        try {
            this.cadastro = repositorio.obterConsumo(this.selecionadoLista.getId());
            
            this.dataBase = cadastro.getData();
            
            this.exibeConsumos = true;
            
            Map<Integer, ConsumoProdutoListagemTO> todosProdutos = inicializaConsumosProdutos();
            
            List<ConsumoProdutoTO> consumosProduto = consumoProdutoRepositorio.obterConsumoProdutoPorEstacao(TipoUnidadeOperacional.findById(cadastro.getTipoUnidadeOperacional()), cadastro.getCdUnidadeOperacional(), cadastro.getData());
            
            consumosProduto.forEach(e -> {
                ConsumoProdutoListagemTO to = todosProdutos.get(e.getProduto());
                to.setIdConsumoProduto(e.getId());
                to.getQuantidades()[0] = e.getQuantidade();
            });
            
            datas = new ArrayList<Date>();
            datas.add(dataBase);
        } catch (Exception e) {
            logger.error(bundle.getText("erro_carregar_consumo"), e);
            this.mostrarMensagemErro(bundle.getText("erro_carregar_consumo"));
        }
    }

    private boolean dataConsumoValida(Date dataConsumo){
        Parametro dataRetroativa = parametroRepositorio.obterPeloNome(Parametro.Nome.BLOQUEIA_DATA_RETROATIVA);
        if ( dataRetroativa == null || dataRetroativa.getValor().equals("0")){
            return true; 
        }
        else{
            Date referencia = ano1900();
            if (session.getAttribute("referencia") != null){
                referencia = converteData(String.valueOf(session.getAttribute("referencia")), FormatoData.ANO_MES);
            }
            
            //Permitir lançamentos entre Mês de Referência e o Mês Corrente
            if (dataConsumo.after(referencia) && !dataConsumo.after(Calendar.getInstance().getTime())){
                return true;
            }
                        
            return false;
        }
    }
    
    protected void atualizarListas(){
        if (listaConsumo == null) {  
            listaConsumo = new LazyDataModel<ConsumoListagemTO>() {
                private static final long serialVersionUID = 1L;

                public List<ConsumoListagemTO> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    try {
                        String regional       = filters.get("regional") != null ? String.valueOf(filters.get("regional")) : "";
                        String unidadeNegocio = filters.get("unidadeNegocio") != null ? String.valueOf(filters.get("unidadeNegocio")) : "";
                        String municipio      = filters.get("municipio") != null ? String.valueOf(filters.get("municipio")) : "";
                        String localidade     = filters.get("localidade") != null ? String.valueOf(filters.get("localidade")) : "";
                        String nomeEstacao    = filters.get("nomeEstacao") != null ? String.valueOf(filters.get("nomeEstacao")) : "";
                        Date   dataConsumo    = filters.get("dataConsumo") != null  ? (Date) converteData(String.valueOf(filters.get("dataConsumo")), FormatoData.DIA_MES_ANO): null;
                        
                        ConsumoFiltroTO consulta = new ConsumoFiltroTO();
                        consulta.setDataConsumo(dataConsumo);
                        consulta.setLocalidade(localidade);
                        consulta.setMunicipio(municipio);
                        consulta.setNomeEstacao(nomeEstacao);
                        consulta.setRegional(regional);
                        consulta.setTipoEstacao(TipoUnidadeOperacional.valueOf(tipoEstacao));
                        consulta.setUnidadeNegocio(unidadeNegocio);
                        
                        List<ConsumoListagemTO> lista = repositorio.obterLista(startingAt, maxPerPage, consulta);
                        setRowCount(repositorio.obterQtdRegistros(consulta));
                        setPageSize(maxPerPage);
                        return lista;                       
                    } catch (Exception e) {
                        logger.error("Erro ao carregar lista de consumos de produtos.", e);
                    }   
                    return null;
                }
                
                public Object getRowKey(ConsumoListagemTO consumo) {
                    return consumo.getId();
                }
                
                public ConsumoListagemTO getRowData(String consumoId) {
                    if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
                        Integer id = Integer.valueOf(consumoId);
                        
                        for (ConsumoListagemTO consumo : listaConsumo) {
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

	/***********************************************
	 ************* GETTERS AND SETTERS *************
	 ***********************************************/
    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public boolean isExibeConsumos() {
        return exibeConsumos;
    }

    public List<ConsumoProdutoListagemTO> getConsumos() {
        return consumos;
    }

    public void setConsumos(List<ConsumoProdutoListagemTO> consumos) {
        this.consumos = consumos;
    }

    public void setDataBase(Date dataBase) {
        this.dataBase = dataBase;
    }

    public Date getDataBase() {
        return dataBase;
    }

    public List<Date> getDatas() {
        return datas;
    }

    public String getTipoEstacao() {
        return tipoEstacao;
    }

    public void setTipoEstacao(String tipoEstacao) {
        this.tipoEstacao = tipoEstacao;
    }

    public ConsumoListagemTO getSelecionadoLista() {
        return selecionadoLista;
    }

    public void setSelecionadoLista(ConsumoListagemTO selecionadoLista) {
        this.selecionadoLista = selecionadoLista;
    }

    public ConsumoCadastroTO getCadastro() {
        return cadastro;
    }

    public void setCadastro(ConsumoCadastroTO cadastro) {
        this.cadastro = cadastro;
    }

    public LazyDataModel<ConsumoListagemTO> getListaConsumo() {
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
