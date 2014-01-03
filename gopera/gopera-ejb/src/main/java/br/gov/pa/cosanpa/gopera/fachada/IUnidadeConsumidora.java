package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.EEAB;
import br.gov.pa.cosanpa.gopera.model.EEAT;
import br.gov.pa.cosanpa.gopera.model.ETA;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RSO;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.UnidadeConsumidora;
import br.gov.pa.cosanpa.gopera.model.UnidadeConsumidoraOperacional;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;
import br.gov.pa.cosanpa.gopera.model.UsuarioProxy;

@Remote
public interface IUnidadeConsumidora extends IGeneric<UnidadeConsumidora> {
	UnidadeConsumidora obterUnidadeConsumidora(Integer codigo) throws Exception;
	UnidadeConsumidora obterUnidadeConsumidoraUC(Integer codigoUC) throws Exception;
	List<UnidadeConsumidora> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception;
	int obterQtdRegistros(Map<String, String> filters) throws Exception;
	List<RegionalProxy> getListaRegional(UsuarioProxy usuario) throws Exception;
	List<UnidadeNegocioProxy> getListaUnidadeNegocio(UsuarioProxy usuario, Integer codigoRegional) throws Exception;
	List<MunicipioProxy> getListaMunicipio(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio) throws Exception;
	List<LocalidadeProxy> getListaLocalidade(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio) throws Exception;
	List<EEAB> getListaEAB(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception;
	List<EEAT> getListaEAT(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception;
	List<ETA> getListaETA(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception;
	List<RSO> getListaRSO(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception;
	List<UnidadeConsumidoraOperacional> getListaUnidadeOperacional(Integer tipoUnidade, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception;
}
