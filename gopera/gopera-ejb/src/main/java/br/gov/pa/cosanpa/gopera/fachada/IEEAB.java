package br.gov.pa.cosanpa.gopera.fachada;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.EEAB;

@Remote
public interface IEEAB extends IGeneric<EEAB> {
	EEAB obterEEAB(Integer codigo) throws Exception;
}
