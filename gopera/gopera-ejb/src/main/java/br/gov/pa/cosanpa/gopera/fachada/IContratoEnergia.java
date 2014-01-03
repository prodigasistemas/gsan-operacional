package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.ContratoEnergia;

@Remote
public interface IContratoEnergia extends IGeneric<ContratoEnergia> {
	ContratoEnergia obterContrato(Integer codigo) throws Exception;
	int obterQtdRegistros(Map<String, String> filters) throws Exception;
	List<ContratoEnergia> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception;
	ContratoEnergia obterContratoVigente(Integer codigoUC) throws Exception;
}
