package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.RSOHoras;

@Remote
public interface IRSOHoras extends IGeneric<RSOHoras> {
	RSOHoras obterRSOHoras(Integer codigo) throws Exception;

	boolean verificaMesReferencia(Integer codigo, String mesReferencia) throws Exception;

	List<RSOHoras> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception;

	int obterQtdRegistros(Map<String, String> filters) throws Exception;
}
