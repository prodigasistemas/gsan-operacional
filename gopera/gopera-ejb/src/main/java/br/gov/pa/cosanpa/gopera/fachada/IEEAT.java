package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.EEAT;

@Remote
public interface IEEAT extends IGeneric<EEAT> {

	EEAT obterEEAT(Integer codigo) throws Exception;

	List<EEAT> obterListaComCMB(Integer min, Integer max) throws Exception;

}
