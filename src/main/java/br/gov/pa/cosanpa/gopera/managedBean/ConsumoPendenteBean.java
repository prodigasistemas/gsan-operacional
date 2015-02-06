package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.gov.model.operacao.LancamentoPendente;
import br.gov.model.operacao.Parametro;
import br.gov.pa.cosanpa.gopera.servicos.ConsumoProdutoBO;
import br.gov.servicos.operacao.ParametroRepositorio;


@ViewScoped
@ManagedBean
public class ConsumoPendenteBean extends BaseMensagemBean{

	@EJB
	private ConsumoProdutoBO bo;
	
    @EJB
    private ParametroRepositorio parametroRepositorio;
	
	protected List<LancamentoPendente> lista = new ArrayList<LancamentoPendente>();
	
	private Date dataPendencia;
	
	private void carregaListaRegistros(){
		try{
			lista = bo.obterConsumosPendentes();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao carregar Pendências do Usuário.");
			e.printStackTrace();
		}
	}	
	
	@PostConstruct
	public void init() {
		carregaListaRegistros();

		Parametro dias = parametroRepositorio.obterPeloNome(Parametro.Nome.DIAS_PENDENCIA_LANCAMENTO);
		
		if (dias != null && dias.getValor() != null){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -Integer.valueOf(dias.getValor()));
            dataPendencia = cal.getTime();
		}
	}
	
	public List<LancamentoPendente> getLista() {
	    return lista;
	}
	
	public Date getDataPendencia(){
	    return dataPendencia;
	}
}
