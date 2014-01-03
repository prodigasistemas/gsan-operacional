package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.ETE;

@Remote
public interface IETE extends IGeneric<ETE> {
	ETE obterETELazy(Integer codigo) throws Exception;

	int obterQtdRegistros(Map<String, String> filters) throws Exception;

	List<ETE> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception;

	List<ETE> getListaETE(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception;
}