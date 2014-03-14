package br.gov.pa.cosanpa.gopera.managedBean;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

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
	private UsuarioBean usuarioBean;
	private UsuarioProxy usuarioProxy;
	private String key;
	private String redireciona;
	
    public UsuarioBean() {
    	
    }  
	
	public Integer getCodigo() {
		if (usuarioBean == null){
			return 0;
		}
		return usuarioBean.usuarioProxy.getCodigo();
	}
	
	public void setCodigo(Integer codigo) {
		try{
			usuarioBean = new UsuarioBean();
	    	usuarioBean.usuarioProxy = new UsuarioProxy();
	    	usuarioBean.usuarioProxy.setCodigo(codigo);
	    	usuarioBean.usuarioProxy = fachadaProxy.getPerfilUsuario(usuarioBean.usuarioProxy);
	    	usuarioBean.usuarioProxy = fachadaProxy.getParametrosSistema(usuarioBean.usuarioProxy);
	    	this.setRedireciona(fachadaProxy.getParametroSistema(7));
	    	iniciar();
	    	session.setAttribute("usuarioProxy", usuarioBean.usuarioProxy);
		}catch (Exception e){
			logger.error(bundle.getText("erro_login_usuario"), e);
			mostrarMensagemErro(bundle.getText("erro_login_usuario"));
    	}			
	}
	
    public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRedireciona() {
		return redireciona;
	}

	public void setRedireciona(String redireciona) {
		this.redireciona = redireciona;
	}

	public UsuarioBean getInstancia() {
        synchronized(UsuarioBean.class) {    
        	if (usuarioBean == null){  
        		usuarioBean = new UsuarioBean();
        		usuarioBean.usuarioProxy.setCodigo(0);
        	}
        }
    	return usuarioBean;  
    }

	public UsuarioProxy getUsuarioProxy() {
		if (usuarioBean == null){
			usuarioBean = new UsuarioBean();
			usuarioBean.usuarioProxy = new UsuarioProxy();
			usuarioBean.usuarioProxy.setCodigo(0);
		}
		if (!usuarioBean.usuarioProxy.getLogado()) {
			mostrarMensagemErro(bundle.getText("erro_sessao_expirada"));
		}
		return usuarioBean.usuarioProxy;
	}

	public void setUsuarioProxy(UsuarioProxy usuarioProxy) {
		usuarioBean.usuarioProxy = usuarioProxy;
	}

	@Override
	public String iniciar() {
    	try{		
    		//Setando Localização Default
			 Locale.setDefault(new Locale("pt", "BR"));
    		//Recuperando Pendências de Produto Químico do Usuário
			List<LancamentoPendente> pendencias = fachadaProxy.getConsumoPendenteUsuario(usuarioBean.usuarioProxy,1);
			if (pendencias != null){
				if (pendencias.size() == 0){
					this.mostrarMensagemSucesso(bundle.getText("aviso_nao_existem_pend_prod_quimico"));
				} else {
					this.mostrarMensagemAviso(bundle.getText("aviso_existem_pend_prod_quimico"));
				}
    		}
    		//Recuperando Pendências de Volume de Água do Usuário
			pendencias = fachadaProxy.getVolumePendenteUsuario(usuarioBean.usuarioProxy,1);
			if (pendencias != null){
				if (pendencias.size() == 0){
					this.mostrarMensagemSucesso(bundle.getText("aviso_nao_existem_pend_volume_agua"));
				}else {
					this.mostrarMensagemAviso(bundle.getText("aviso_existem_pend_volume_agua"));
				}
    		}
    		//Recuperando Pendências de Horas Trabalhadas do Usuário
			pendencias = fachadaProxy.getHorasPendenteUsuario(usuarioBean.usuarioProxy,1);
			if (pendencias != null){
				if (pendencias.size() == 0){
					this.mostrarMensagemSucesso(bundle.getText("aviso_nao_existem_pend_horas_trab"));
				} else {
					this.mostrarMensagemAviso(bundle.getText("aviso_existem_pend_horas_trab"));
				}
    		}
			session.setAttribute("referencia", usuarioBean.usuarioProxy.getReferencia().toString());
		}catch (Exception e){
			mostrarMensagemErro(bundle.getText("erro_consulta_pendencias"));
			logger.error(bundle.getText("erro_consulta_pendencias"), e);
		}
		return null;
	}
	
	public void logout() {
		usuarioBean.usuarioProxy.setLogado(false);
		session.invalidate();
	}
	
	public String md5(String s) {
    	MessageDigest m;
    	String retorno = "";
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(s.getBytes(),0,s.length());
			retorno = new BigInteger(1,m.digest()).toString(16);
	    	
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return retorno;
    }
}
