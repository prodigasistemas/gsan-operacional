package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.gov.model.operacao.LancamentoPendente;
import br.gov.pa.cosanpa.gopera.servicos.VolumeBO;
import br.gov.servicos.operacao.ParametroRepositorio;


@ViewScoped
@ManagedBean
public class VolumePendenteBean extends BaseMensagemBean{

	@EJB
	private VolumeBO bo;
	
	@EJB
	private ParametroRepositorio parametroRepositorio;
	
	protected List<LancamentoPendente> lista = new ArrayList<LancamentoPendente>();


	@PostConstruct
    public void init() {
        carregaListaRegistros();
    }
	
	private void carregaListaRegistros(){
		try{
			lista = bo.obterVolumesPendentes();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao carregar Pendências do Usuário.");
			e.printStackTrace();
		}
	}	
	
	public List<LancamentoPendente> getLista(){
		return this.lista;
	}
}
