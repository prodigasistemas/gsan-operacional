package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.Date;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.gov.pa.cosanpa.gopera.fachada.IUnidadeMedida;
import br.gov.pa.cosanpa.gopera.model.UnidadeMedida;

@ManagedBean
@SessionScoped
public class UnidadeMedidaBean extends BaseBean<UnidadeMedida> {

	@EJB
	private IUnidadeMedida fachada;
	
	public UnidadeMedidaBean() {
		
	}

	@Override
	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new UnidadeMedida();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "UnidadeMedida.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}
	
	@Override
	public String confirmar() {
		try {
			registro.setUsuario(usuarioProxy);
			registro.setUltimaAlteracao(new Date());
			return super.confirmar();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar");
		}
		return null;
	}
	
	@Override
	public String novo() {
		this.registro = new UnidadeMedida();
		return super.novo();
	}
}
