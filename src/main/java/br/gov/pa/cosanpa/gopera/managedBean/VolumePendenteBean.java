package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.gov.model.operacao.LancamentoPendente;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;


@SuppressWarnings("rawtypes")
@SessionScoped
@ManagedBean
public class VolumePendenteBean extends BaseBean{

	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;
	protected List<LancamentoPendente> lista = new ArrayList<LancamentoPendente>();

	
	public List<LancamentoPendente> getLista() {
		return lista;
	}

	private void carregaListaRegistros(){
		try{
			lista = fachadaProxy.getVolumePendenteUsuario(usuarioProxy,0);
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao carregar Pendências do Usuário.");
			e.printStackTrace();
		}
	}	
	
	public String iniciar() {
		carregaListaRegistros();
		return "VolumePendente.jsf";
	}
	
}
