package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.EEATHoras;

@Remote
public interface IEEATHoras extends IGeneric<EEATHoras> {
	EEATHoras obterEEATHoras(Integer codigo) throws Exception;
	boolean verificaMesReferencia(Integer codigo, String mesReferencia) throws Exception;
	List<EEATHoras> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception;
	int obterQtdRegistros(Map<String, String> filters) throws Exception;
}
