package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.RSOVolume;

@Remote
public interface IRSOVolume extends IGeneric<RSOVolume> {
	public RSOVolume obterRSOVolume(Integer codigo) throws Exception;

	boolean verificaMesReferencia(Integer codigo, String mesReferencia) throws Exception;

	List<RSOVolume> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception;

	int obterQtdRegistros(Map<String, String> filters) throws Exception;
}
