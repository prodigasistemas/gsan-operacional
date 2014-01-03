package br.gov.pa.cosanpa.gopera.fachada;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface IRelatorioProducao {
	@SuppressWarnings("rawtypes")
	public List<List> getProducaoDistribuicaoMensal(Integer codigoRegional,
			Integer codigoUnidadeNegocio, Integer tipoUnidadeOperacional,
			Integer codigoUnidadeOperacional, Date referencia) throws Exception;
}
