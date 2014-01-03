package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.ConsumoEAT;
import br.gov.pa.cosanpa.gopera.model.ConsumoEATProduto;
import br.gov.pa.cosanpa.gopera.model.EEAT;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.Produto;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;
import br.gov.pa.cosanpa.gopera.model.UsuarioProxy;

@Remote
public interface IConsumoEAT extends IGeneric<ConsumoEAT> {
	List<Produto> listarProdutos();
	ConsumoEAT obterConsumo(Integer codigo) throws Exception;
	List<ConsumoEAT> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception;
	int obterQtdRegistros(Map<String, String> filters) throws Exception;
	ConsumoEAT obterConsumoLazy(Integer codigo) throws Exception;
	void salvar(ConsumoEATProduto obj) throws Exception;
	void atualizarConsumoProduto(ConsumoEATProduto obj) throws Exception;
	List<RegionalProxy> getListaConsumoEATRegional(UsuarioProxy usuario) throws Exception;
	List<UnidadeNegocioProxy> getListaConsumoEATUnidadeNegocio(UsuarioProxy usuario, Integer codigoRegional) throws Exception;
	List<MunicipioProxy> getListaConsumoEATMunicipio(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio) throws Exception;
	List<LocalidadeProxy> getListaConsumoEATLocalidade(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio) throws Exception;
	List<EEAT> getListaConsumoEAT(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception;
}
