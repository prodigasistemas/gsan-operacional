package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.EEATVolume;

@Remote
public interface IEEATVolume extends IGeneric<EEATVolume> {
	EEATVolume obterEEATVolume(Integer codigo) throws Exception;
	boolean verificaMesReferencia(Integer codigo, String mesReferencia) throws Exception;
	List<EEATVolume> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception;
	int obterQtdRegistros(Map<String, String> filters) throws Exception;
}
