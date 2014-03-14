package br.gov.pa.cosanpa.gopera.managedBean;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.jboss.logging.Logger;

import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.model.LancamentoPendente;
import br.gov.pa.cosanpa.gopera.model.UsuarioProxy;

@SuppressWarnings("rawtypes")
@ManagedBean
@SessionScoped
public class UsuarioBean extends BaseBean {

	private static final Logger logger = Logger.getLogger(UsuarioBean.class);

	@EJB
	private IProxy fachadaProxy;
	private UsuarioProxy usuarioProxy;
	
    public UsuarioBean() {
    	
    }  
	
	public UsuarioProxy getUsuarioProxy() {
		if (usuarioProxy == null){
			iniciar();
		}
		return usuarioProxy;
	}

	public String iniciar() {
    	try{
    		
	    	usuarioProxy = new UsuarioProxy();
    		usuarioProxy.setCodigo(Integer.parseInt(String.valueOf(session.getAttribute("usuario"))));
	    	usuarioProxy = fachadaProxy.getPerfilUsuario(usuarioProxy);
	    	usuarioProxy = fachadaProxy.getParametrosSistema(usuarioProxy);
	    	session.setAttribute("usuarioProxy", usuarioProxy);
    		
    		//Setando Localização Default
			 Locale.setDefault(new Locale("pt", "BR"));
    		//Recuperando Pendências de Produto Químico do Usuário
			List<LancamentoPendente> pendencias = fachadaProxy.getConsumoPendenteUsuario(usuarioProxy, 1);
			if (pendencias != null){
				if (pendencias.size() == 0){
					this.mostrarMensagemSucesso(bundle.getText("aviso_nao_existem_pend_prod_quimico"));
				} else {
					this.mostrarMensagemAviso(bundle.getText("aviso_existem_pend_prod_quimico"));
				}
    		}
    		//Recuperando Pendências de Volume de Água do Usuário
			pendencias = fachadaProxy.getVolumePendenteUsuario(usuarioProxy, 1);
			if (pendencias != null){
				if (pendencias.size() == 0){
					this.mostrarMensagemSucesso(bundle.getText("aviso_nao_existem_pend_volume_agua"));
				}else {
					this.mostrarMensagemAviso(bundle.getText("aviso_existem_pend_volume_agua"));
				}
    		}
    		//Recuperando Pendências de Horas Trabalhadas do Usuário
			pendencias = fachadaProxy.getHorasPendenteUsuario(usuarioProxy, 1);
			if (pendencias != null){
				if (pendencias.size() == 0){
					this.mostrarMensagemSucesso(bundle.getText("aviso_nao_existem_pend_horas_trab"));
				} else {
					this.mostrarMensagemAviso(bundle.getText("aviso_existem_pend_horas_trab"));
				}
    		}
			session.setAttribute("referencia", usuarioProxy.getReferencia().toString());
		}catch (Exception e){
			mostrarMensagemErro(bundle.getText("erro_consulta_pendencias"));
			logger.error(bundle.getText("erro_consulta_pendencias"), e);
		}
		return null;
	}
	
	public void logout() {
		usuarioProxy.setLogado(false);
		session.invalidate();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(fachadaProxy.getParametroSistema(7));
		} catch (Exception e) {
			logger.error("Erro ao redirecionar para gsan", e);
		}
	}
}
