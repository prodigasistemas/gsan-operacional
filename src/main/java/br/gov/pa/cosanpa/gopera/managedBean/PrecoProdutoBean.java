package br.gov.pa.cosanpa.gopera.managedBean;

import static br.gov.model.util.Utilitarios.converteParaDataComUltimoDiaMes;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.jboss.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.model.MergeProperties;
import br.gov.model.operacao.PrecoProduto;
import br.gov.model.operacao.Produto;
import br.gov.model.operacao.TipoUnidadeOperacional;
import br.gov.servicos.operacao.PrecoProdutoRepositorio;
import br.gov.servicos.operacao.ProdutoRepositorio;
import br.gov.servicos.operacao.to.PrecoProdutoCadastroTO;
import br.gov.servicos.operacao.to.PrecoProdutoListagemTO;

@SessionScoped
@ManagedBean
public class PrecoProdutoBean extends BaseBean<PrecoProduto> {
    private static Logger logger = Logger.getLogger(PrecoProdutoBean.class);

    @EJB
    private PrecoProdutoRepositorio repositorio;

    @EJB
    private ProdutoRepositorio repositorioProduto;
    
    private PrecoProdutoListagemTO selecionado = null;
    
    private PrecoProdutoCadastroTO cadastro = null;

    private LazyDataModel<PrecoProdutoListagemTO> relatorio = null;

    protected void atualizarListas() {
        if (relatorio == null) {
            relatorio = new LazyDataModel<PrecoProdutoListagemTO>() {

                public List<PrecoProdutoListagemTO> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    try {
                        String nomeProduto = filters.get("produto") != null ? String.valueOf(filters.get("produto")) : "";
                        List<PrecoProdutoListagemTO> lista = repositorio.obterLista(startingAt, maxPerPage, nomeProduto);
                        setRowCount(repositorio.obterQtdRegistros(nomeProduto));
                        setPageSize(maxPerPage);
                        return lista;
                    } catch (Exception e) {
                        logger.error("Erro ao carregar lista de precos.", e);
                    }
                    return null;
                }

                public Object getRowKey(PrecoProdutoListagemTO to) {
                    return to.getId();
                }

                public PrecoProdutoListagemTO getRowData(String rowId) {
                    if (rowId != null && !rowId.equals("") && !rowId.equals("null")) {
                        Integer id = Integer.valueOf(rowId);

                        for (PrecoProdutoListagemTO to : relatorio) {
                            if (id.equals(to.getId())) {
                                return to;
                            }
                        }
                    }
                    return null;
                }
            };
        }
    }

    public String iniciar() {
        this.fachada = repositorio;
        this.cadastro = new PrecoProdutoCadastroTO();
        this.registro = new PrecoProduto();
        atualizarListas();
        visualizar();
        this.getPaginasRetorno().put("iniciar", "PrecoProduto.jsf");
        return this.getPaginasRetorno().get("iniciar");
    }

    public String novo() {
        this.cadastro = new PrecoProdutoCadastroTO();
        return super.novo();
    }

    public String consultar() {
        carregar();
        return super.consultar();
    }
    
    public String cancelar() {
        this.estadoAtual =  this.estadoAnterior;
        setDesabilitaForm(false);
        return paginasRetorno.get("cancelar") != null ? paginasRetorno.get("cancelar") : "";
    }
    
    public String alterar() {
        carregar();
        return super.alterar();
    }
    
    private void carregar(){
        try {
            this.cadastro = repositorio.obterPreco(this.selecionado.getId());
        } catch (Exception e) {
            logger.error(bundle.getText("erro_carregar_horas_eta"), e);
            this.mostrarMensagemErro(bundle.getText("erro_carregar_horas_eta"));
        }
    }
    
    public String confirmar() {
        Produto produto = new Produto();
        produto.setCodigo(cadastro.getIdProduto());
        registro = new PrecoProduto();
        registro.setVigencia(cadastro.getVigencia());
        registro.setPreco(cadastro.getPreco());
        registro.setProduto(produto);
        registro.setUltimaAlteracao(new Date());
        registro.setUsuario(usuarioProxy.getCodigo());

        
        return super.confirmarLazy();
    }
    
    public String cadastrar(){
        try {
            if (!this.getEditando()){
                if(repositorio.existePrecoDeProdutoNaVigencia(cadastro.getIdProduto(), cadastro.getVigencia())){
                    this.mostrarMensagemErro(bundle.getText("erro_preco_produto_cadastrado"));
                    return null;
                }
            }       
            return super.cadastrar();
        } catch (Exception e) {
            logger.error(bundle.getText("erro_salvar_produtos_quimicos"), e);
            this.mostrarMensagemErro(bundle.getText("erro_salvar_produtos_quimicos"));
        }
        return null;
    }
    
    public List<Produto> getProdutos(){
        return repositorioProduto.obterLista();
    }

    public PrecoProdutoListagemTO getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(PrecoProdutoListagemTO selecionado) {
        this.selecionado = selecionado;
    }

    public LazyDataModel<PrecoProdutoListagemTO> getRelatorio() {
        return relatorio;
    }

    public PrecoProdutoCadastroTO getCadastro() {
        return cadastro;
    }

    public void setCadastro(PrecoProdutoCadastroTO cadastro) {
        this.cadastro = cadastro;
    }
}
