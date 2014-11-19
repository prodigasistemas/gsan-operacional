package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.gov.model.operacao.TabelaPreco;
import br.gov.model.operacao.TabelaPrecoProduto;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;
import br.gov.servicos.operacao.TabelaPrecoRepositorio;

@SessionScoped
@ManagedBean
public class TabelaPrecoBean extends BaseBean<TabelaPreco> {

	@EJB
	private TabelaPrecoRepositorio fachada;
	
	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;
	
	private List<TabelaPrecoProduto> listaTabelaProdutos = new ArrayList<TabelaPrecoProduto>();	
	
	public List<TabelaPrecoProduto> getListaProdutos() {
		if (listaTabelaProdutos.isEmpty()) carregarListaProduto();		
		return listaTabelaProdutos;
	}

	public void setListaProdutos(List<TabelaPrecoProduto> listaProdutos) {
		this.listaTabelaProdutos = listaProdutos;
	}

	private void carregarListaProduto() {
		try {
			listaTabelaProdutos = fachadaProxy.getTabelaProdutos(this.registro); 
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar Lista de Produtos");
			e.printStackTrace();
		}
	}

	private void carregarListaProdutoVigente() {
		try {
			listaTabelaProdutos = fachadaProxy.getTabelaProdutosVigente(); 
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar Lista de Produtos Vigente");
			e.printStackTrace();
		}
	}
	
	@Override
	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new TabelaPreco();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "TabelaPreco.jsf");
		return this.getPaginasRetorno().get("iniciar");
	}

	@Override
	public String novo(){
		carregarListaProdutoVigente();
		this.registro = new TabelaPreco();
		return super.novo();
	}
	
	@Override	
	public String consultar() {
		listaTabelaProdutos.clear();
		carregarListaProduto();
		return super.consultar();
	}

	@Override
	public String alterar() {
		listaTabelaProdutos.clear();
		carregarListaProduto();
		return super.alterar();
	}
	
	@Override
	public String confirmar() {
		try {
			String strValor = "";
			this.registro.getTabelaPrecoProduto().clear();
			TabelaPrecoProduto tabelaPrecoProduto;
			for (int j = 0; j < this.listaTabelaProdutos.size(); j++) {
				tabelaPrecoProduto = new TabelaPrecoProduto();
				tabelaPrecoProduto = this.listaTabelaProdutos.get(j);
				tabelaPrecoProduto.setTabelaPreco(this.registro);
				strValor = tabelaPrecoProduto.getPrecoAux().replace(".", "").replace(",", "."); 
				Double precoCusto = (strValor.isEmpty() ? 0.0f : Double.parseDouble(strValor));
				tabelaPrecoProduto.setPreco(precoCusto);
				this.registro.getTabelaPrecoProduto().add(tabelaPrecoProduto);
			}
			registro.setUsuario(usuarioProxy.getCodigo());
			registro.setUltimaAlteracao(new Date());
			return super.confirmar();
		} catch (Exception e) {
			this.mostrarMensagemErro(e.getMessage());
		}
		return null;		
	}
}
