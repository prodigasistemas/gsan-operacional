package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.AnaliseClinica;

@Remote
public interface IAnaliseClinica extends IGeneric<AnaliseClinica> {
	AnaliseClinica obterAnaliseClinica(Integer codigo) throws Exception;
	List<AnaliseClinica> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception;
	int obterQtdRegistros(Map<String, String> filters) throws Exception;
	boolean verificaMesReferencia(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, String mesReferencia) throws Exception;
}
