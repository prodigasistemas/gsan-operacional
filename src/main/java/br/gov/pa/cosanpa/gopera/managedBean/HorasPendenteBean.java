package br.gov.pa.cosanpa.gopera.managedBean;

import static br.gov.model.util.Utilitarios.formataData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.gov.model.operacao.LancamentoPendente;
import br.gov.model.util.FormatoData;
import br.gov.pa.cosanpa.gopera.servicos.HorasBO;
import br.gov.servicos.operacao.ParametroRepositorio;


@ViewScoped
@ManagedBean
public class HorasPendenteBean extends BaseMensagemBean{

    @EJB
    private HorasBO bo;
    
    @EJB
    private ParametroRepositorio parametroRepositorio;
    
    private String referencia;
	
	protected List<LancamentoPendente> lista = new ArrayList<LancamentoPendente>();

	private void carregaListaRegistros(){
		try{
			lista = bo.obterHorasPendentes();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao carregar Pendências do Usuário.");
			e.printStackTrace();
		}
	}	
	
    @PostConstruct
    public void init() {
        carregaListaRegistros();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        
        referencia = formataData(cal.getTime(), FormatoData.MES_ANO);
    }

    public List<LancamentoPendente> getLista() {
        return lista;
    }

    public String getReferencia() {
        return referencia;
    }
}
