package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.RegistroConsumoETA;

@Remote
public interface IRegistroConsumoETA extends IGeneric<RegistroConsumoETA> {
	public RegistroConsumoETA obterRegistroConsumo(Integer codigo) throws Exception;

	List<RegistroConsumoETA> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception;

	int obterQtdRegistros(Map<String, String> filters) throws Exception;
}