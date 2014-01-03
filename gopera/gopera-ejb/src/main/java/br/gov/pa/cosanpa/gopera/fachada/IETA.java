package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.ETA;

@Remote
public interface IETA extends IGeneric<ETA> {

	ETA obterETA(Integer codigo) throws Exception;

	List<ETA> obterListaComCMB(Integer min, Integer max) throws Exception;

	ETA obterETALazy(Integer codigo) throws Exception;
}