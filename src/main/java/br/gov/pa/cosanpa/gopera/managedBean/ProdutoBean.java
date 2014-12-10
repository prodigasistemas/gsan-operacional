package br.gov.pa.cosanpa.gopera.managedBean;


import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.gov.model.operacao.Produto;
import br.gov.model.operacao.UnidadeMedida;
import br.gov.model.operacao.UsuarioProxy;
import br.gov.servicos.operacao.ProdutoRepositorio;

@ManagedBean
@SessionScoped
public class ProdutoBean extends BaseBean<Produto> {

	@EJB
	private ProdutoRepositorio fachada;
	
	private List<UnidadeMedida> unidadeMedidas;
	
	private UsuarioProxy usuarioProxy = (UsuarioProxy) session.getAttribute("usuarioProxy");
	
	public ProdutoBean() {
		
	}
	
	
	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new Produto();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "Produto.jsf");
		this.getPaginasRetorno().put("novo", "Produto_Cadastro.jsf");
		this.getPaginasRetorno().put("confirmar", "Produto_Cadastro.jsf");		
		this.getPaginasRetorno().put("editar", "Produto_Cadastro.jsf");
		this.getPaginasRetorno().put("consultar", "Produto_Cadastro.jsf");
		this.getPaginasRetorno().put("excluir", "Produto.jsf");
		this.getPaginasRetorno().put("voltar", "Produto.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}

	
	public String confirmar() {
		try {
			registro.setUsuario(usuarioProxy.getCodigo());
			registro.setUltimaAlteracao(new Date());
			return super.confirmar();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar");
		}
		return null;
	}
	
	public List<UnidadeMedida> getUnidadeMedidas() {
		unidadeMedidas = fachada.listarUnidadeMedidas();
		return unidadeMedidas;
	}
	public void setUnidadeMedidas(List<UnidadeMedida> unidadeMedidas) {
		this.unidadeMedidas = unidadeMedidas;
	}
}
