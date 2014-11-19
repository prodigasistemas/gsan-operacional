package br.gov.pa.cosanpa.gopera.managedBean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.jboss.logging.Logger;

import br.gov.model.operacao.Indicador;
import br.gov.servicos.operacao.IndicadorRepositorio;

@ManagedBean
@SessionScoped
public class IndicadorGeracaoBean extends BaseBean<Indicador> {
	private static Logger logger = Logger.getLogger(IndicadorGeracaoBean.class);

	private String referencia;

	@EJB
	private IndicadorRepositorio fachadaInd;

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	@Override
	public String iniciar() {
		return "IndicadoresGeracao.jsf";
	}

	public void gerarIndicadores() throws Exception {
		logger.info("Solicitada geracao de indicadores");
		try {
			if (validaReferencia(primeiroDiaMes(referencia))) {
				fachadaInd.geraIndicadorMensal(primeiroDiaMes(referencia));
				mostrarMensagemSucesso("Indicadores gerados com sucesso!");
			}
		} catch (Exception e) {
			logger.error(bundle.getText("erro_geracao_indicadores"), e);
			mostrarMensagemErro(bundle.getText("erro_geracao_indicadores"));
		}
	}
}
