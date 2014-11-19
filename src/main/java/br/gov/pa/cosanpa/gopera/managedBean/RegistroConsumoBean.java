package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.gov.model.operacao.Produto;
import br.gov.model.operacao.RegistroConsumo;
import br.gov.servicos.operacao.RegistroConsumoRepositorio;

@ManagedBean
@SessionScoped
public class RegistroConsumoBean extends BaseBean<RegistroConsumo> {

	@EJB
	private RegistroConsumoRepositorio fachada;
	
	private List<Produto> listaProdutos = new ArrayList<Produto>();
	private List<String> produtosSelecionados = new ArrayList<String>();

	public RegistroConsumoBean() {
		limparLista();
	}

	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new RegistroConsumo();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "RegistroConsumo.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");
	}
	
	private void limparLista() {
		produtosSelecionados.clear();
		listaProdutos.clear();
	}

	public List<Produto> getListaProdutos() {
		carregarListaProduto();
		return listaProdutos;
	}
	
	private void carregarListaProduto() {
		listaProdutos = fachada.listarProdutos();
	}

	public List<String> getProdutosSelecionados() {
		return produtosSelecionados;
	}

	public void setProdutosSelecionados(List<String> produtosSelecionados) {
		this.produtosSelecionados = produtosSelecionados;
	}
	
	@Override
	public String novo() {
		limparLista();
		this.registro = new RegistroConsumo();
		return super.novo();
	}

	@Override
	public String confirmar() {
		try {
			adicionarProdutos();
			registro.setUsuario(usuarioProxy.getCodigo());
			registro.setUltimaAlteracao(new Date());
			return super.confirmar();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar");
			e.printStackTrace();
		}
		return null;
	}

	private void adicionarProdutos() {
		registro.getProdutos().clear();
		for (String itemSelecionado : this.getProdutosSelecionados()) {
			for (Produto produto : this.listaProdutos) {
				if (produto.getCodigo().equals(Integer.parseInt(itemSelecionado))) {
					registro.getProdutos().add(produto);	
				}
			}
		}
	}
	
	private void consultarProdutos() {
		this.produtosSelecionados.clear();
		for (Produto consumo : this.registro.getProdutos()) {
			produtosSelecionados.add(consumo.getCodigo().toString());
		}
	}

	@Override	
	public String consultar() {
		consultarProdutos();
		return super.consultar();
	}

	@Override	
	public String alterar() {
		consultarProdutos();
		return super.alterar();
	}

	public RegistroConsumo getRegistroConsumo() {
		if (registro == null) {
			registro = new RegistroConsumo();
		}
		return registro;
	}

	public void setRegistroConsumo(RegistroConsumo registroConsumo) {
		this.registro = registroConsumo;
	}
}
