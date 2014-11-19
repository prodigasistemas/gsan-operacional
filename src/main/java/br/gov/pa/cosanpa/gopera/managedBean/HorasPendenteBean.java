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
public class HorasPendenteBean extends BaseBean{

	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;
	protected List<LancamentoPendente> lista = new ArrayList<LancamentoPendente>();

	@Override
	public List<LancamentoPendente> getLista() {
		return lista;
	}

	private void carregaListaRegistros(){
		try{
			lista = fachadaProxy.getHorasPendenteUsuario(usuarioProxy,0);
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao carregar Pendências do Usuário.");
			e.printStackTrace();
		}
	}	
	
	public String iniciar() {
		carregaListaRegistros();
		return "HorasPendente.jsf";
	}
	
}
