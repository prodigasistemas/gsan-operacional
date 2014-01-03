package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.ETAHoras;

@Remote
public interface IETAHoras extends IGeneric<ETAHoras> {
	ETAHoras obterETAHoras(Integer codigo) throws Exception;

	boolean verificaMesReferencia(Integer codigo, String mesReferencia) throws Exception;

	int obterQtdRegistros(Map<String, String> filters) throws Exception;

	List<ETAHoras> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception;
}