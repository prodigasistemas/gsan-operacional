package br.gov.pa.cosanpa.gopera.managedBean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import br.gov.pa.cosanpa.gopera.util.WebBundle;

public abstract class BaseMensagemBean {
	
	@EJB
	protected WebBundle bundle;
	
	public BaseMensagemBean() {
	}
	
	public void mostrarMensagemSucesso(String msg) {
		mostrarMensagem(FacesMessage.SEVERITY_INFO, msg);
	}
	
	public void mostrarMensagemErro(String msg) {
		mostrarMensagem(FacesMessage.SEVERITY_ERROR, msg);
	}
	
	public void mostrarMensagemAviso(String msg) {
		mostrarMensagem(FacesMessage.SEVERITY_WARN, msg);
	}

	public void mostrarMensagem(Severity tipo, String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(tipo, msg, ""));
	}
}
