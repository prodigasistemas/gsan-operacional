package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.EEABHoras;

@Remote
public interface IEEABHoras extends IGeneric<EEABHoras> {
	EEABHoras obterEEABHoras(Integer codigo) throws Exception;
	public boolean verificaMesReferencia(Integer codigo, String mesReferencia) throws Exception;
	List<EEABHoras> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception;
	int obterQtdRegistros(Map<String, String> filters) throws Exception;
}
