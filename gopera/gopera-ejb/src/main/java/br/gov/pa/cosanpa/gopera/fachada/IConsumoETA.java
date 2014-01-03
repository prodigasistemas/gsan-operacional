package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.ConsumoETA;
import br.gov.pa.cosanpa.gopera.model.ConsumoETAProduto;
import br.gov.pa.cosanpa.gopera.model.ETA;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.Produto;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;
import br.gov.pa.cosanpa.gopera.model.UsuarioProxy;

@Remote
public interface IConsumoETA extends IGeneric<ConsumoETA> {
	List<Produto> listarProdutos();
	ConsumoETA obterConsumo(Integer codigo) throws Exception;
	List<ConsumoETA> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception;
	int obterQtdRegistros(Map<String, String> filters) throws Exception;
	ConsumoETA obterConsumoLazy(Integer codigo) throws Exception;
	void salvar(ConsumoETAProduto obj) throws Exception;
	void atualizarConsumoProduto(ConsumoETAProduto obj) throws Exception;
	List<RegionalProxy> getListaConsumoETARegional(UsuarioProxy usuario) throws Exception;
	List<UnidadeNegocioProxy> getListaConsumoETAUnidadeNegocio(UsuarioProxy usuario, Integer codigoRegional) throws Exception;
	List<MunicipioProxy> getListaConsumoETAMunicipio(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio) throws Exception;
	List<LocalidadeProxy> getListaConsumoETALocalidade(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio) throws Exception;
	List<ETA> getListaConsumoETA(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception;
}
