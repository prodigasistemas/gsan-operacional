package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.model.LancamentoPendente;


@SuppressWarnings("rawtypes")
@SessionScoped
@ManagedBean
public class VolumePendenteBean extends BaseBean{

	@EJB
	private IProxy fachadaProxy;
	protected List<LancamentoPendente> lista = new ArrayList<LancamentoPendente>();

	@Override
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
