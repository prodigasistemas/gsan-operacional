package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.EnergiaEletricaDados;

@Remote
public interface IEnergiaEletricaDados extends IGeneric<EnergiaEletricaDados> {
	EnergiaEletricaDados obterDados(Integer codigo) throws Exception;
//	EnergiaEletricaDados CalculaDados(EnergiaEletricaDados dados) throws Exception;
	int obterQtdRegistros(Map<String, String> filters, int codigoEnergia) throws Exception;
	List<EnergiaEletricaDados> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters, int codigoEnergia) throws Exception;
	List<EnergiaEletricaDados> CalculaDados(List<EnergiaEletricaDados> listaDados);
}
