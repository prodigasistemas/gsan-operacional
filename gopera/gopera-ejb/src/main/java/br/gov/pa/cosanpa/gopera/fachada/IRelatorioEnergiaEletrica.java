package br.gov.pa.cosanpa.gopera.fachada;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.DadosRelatorioEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.EnergiaAnalise;
import br.gov.pa.cosanpa.gopera.model.EnergiaEletricaDados;
import br.gov.pa.cosanpa.gopera.model.RelatorioEnergiaEletrica;

@Remote
@SuppressWarnings("rawtypes")
public interface IRelatorioEnergiaEletrica {
	public List<EnergiaEletricaDados> getEnergiaEletricaDados(Date dataReferencia) throws Exception;
	public List<RelatorioEnergiaEletrica> getEnergiaEletricaAnalise(Date dataReferenciaInicial, Date dataReferenciaFinal, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception;
	public  List<DadosRelatorioEnergiaEletrica> analiseEnergiaEletrica(Date dataReferenciaInicial, Date dataReferenciaFinal, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception;
	public List<List> getMesesPeriodo(String query) throws Exception;
	public List<List> getUCs(String query) throws Exception;
	public List<List> getDados(String query, String mes, String uc) throws Exception;
	public List<List> getCountUcs(String query, String localidade) throws Exception;
	public  List<EnergiaAnalise> getRelatorioAnalise(Date dataInicial, Date dataFinal,
			Integer codigoRegional, Integer codigoUnidadeNegocio,
			Integer codigoMunicipio, Integer codigoLocalidade,
			List<String> dados) throws Exception;
	public List<List> getCountLocalidade(String query, String municipio) throws Exception;
	public List<RelatorioEnergiaEletrica> getEnergiaEletricaUC(Date dataReferencia, Integer tipoRelatorio, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception;
	public List<RelatorioEnergiaEletrica> getEnergiaEletricaPeriodo(Date referenciaInicial, Date referenciaFinal, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, List<String> unidadesConsumidorasSelecionadas) throws Exception;
}