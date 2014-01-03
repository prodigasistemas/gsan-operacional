package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.EEABVolume;

@Remote
public interface IEEABVolume extends IGeneric<EEABVolume> {
	EEABVolume obterEEABVolume(Integer codigo) throws Exception;
	boolean verificaMesReferencia(Integer codigo, String mesReferencia) throws Exception;
	int obterQtdRegistros(Map<String, String> filters) throws Exception;
	List<EEABVolume> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception;
}
