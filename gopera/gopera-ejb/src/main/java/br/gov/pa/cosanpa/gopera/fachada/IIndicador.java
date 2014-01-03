package br.gov.pa.cosanpa.gopera.fachada;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.Indicador;

@Remote
public interface IIndicador {
	List<Indicador> getIndicadorMensal(Integer codigoRegional,
			Integer codigoUnidadeNegocio, Integer codigoMunicipio,
			Integer codigoLocalidade, Integer tipoUnidadeOperacional,
			Integer codigoUnidadeOperacional, Date dataInicial, Date dataFinal,
			Integer tipoAgrupamento, List<String> indicadores) throws Exception;

	void geraIndicadorMensal(Date dataInicial) throws Exception;
}
