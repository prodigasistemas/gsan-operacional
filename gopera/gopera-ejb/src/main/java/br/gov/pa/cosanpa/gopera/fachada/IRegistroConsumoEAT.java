package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.RegistroConsumoEAT;

@Remote
public interface IRegistroConsumoEAT extends IGeneric<RegistroConsumoEAT> {
	RegistroConsumoEAT obterRegistroConsumo(Integer codigo) throws Exception;

	int obterQtdRegistros(Map<String, String> filters) throws Exception;

	List<RegistroConsumoEAT> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception;
}
