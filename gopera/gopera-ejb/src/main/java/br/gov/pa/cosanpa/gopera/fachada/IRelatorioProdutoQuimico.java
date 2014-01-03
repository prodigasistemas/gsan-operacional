package br.gov.pa.cosanpa.gopera.fachada;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.RelatorioGerencial;

@Remote
public interface IRelatorioProdutoQuimico {
	public List<RelatorioGerencial> getConsumoProdutoSintetico(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, Integer tipoUnidadeOperacional, Integer codigoUnidadeOperacional, Date dataInicial, Date dataFinal, Integer tipoAgrupamento, Integer tipoRelatorio) throws Exception;
	public List<RelatorioGerencial> getConsumoProdutoMensal(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, Integer tipoUnidadeOperacional, Integer codigoUnidadeOperacional, Date dataInicial, Date dataFinal, Integer tipoAgrupamento, Integer tipoRelatorio) throws Exception;
	public List<RelatorioGerencial> getConsumoProdutoAnalitico(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, Integer tipoUnidadeOperacional, Integer codigoUnidadeOperacional, Date dataInicial, Date dataFinal, Integer tipoAgrupamento, Integer tipoRelatorio) throws Exception;
}
