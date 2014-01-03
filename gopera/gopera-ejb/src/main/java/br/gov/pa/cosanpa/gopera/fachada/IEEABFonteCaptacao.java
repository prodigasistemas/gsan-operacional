package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.EEABFonteCaptacao;

@Remote
public interface IEEABFonteCaptacao extends IGeneric<EEABFonteCaptacao> {
	List<EEABFonteCaptacao> obterEEABFonteCaptacaoPorEEAB(Integer codigo) throws Exception;
}
