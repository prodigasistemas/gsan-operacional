package br.gov.pa.cosanpa.gopera.fachada;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.ConsumoEAT;
import br.gov.pa.cosanpa.gopera.model.ConsumoETA;
import br.gov.pa.cosanpa.gopera.model.EEABFonteCaptacao;
import br.gov.pa.cosanpa.gopera.model.EEATFonteCaptacao;
import br.gov.pa.cosanpa.gopera.model.FonteCaptacaoProxy;
import br.gov.pa.cosanpa.gopera.model.LancamentoPendente;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.Produto;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.SistemaAbastecimentoProxy;
import br.gov.pa.cosanpa.gopera.model.TabelaPreco;
import br.gov.pa.cosanpa.gopera.model.TabelaPrecoProduto;
import br.gov.pa.cosanpa.gopera.model.UnidadeConsumidoraOperacional;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;
import br.gov.pa.cosanpa.gopera.model.UsuarioProxy;

@Remote
public interface IProxy {

	public String[] getConsumoSistemaAbastecimento(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, Integer codigoSistemaAbastecimento, Date dataConsumo, Integer codigoProduto) throws Exception;
	public String[] getConsumoETA(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, Integer codigoETA, Date dataConsumo, Integer codigoProduto) throws Exception;
	public String[] getConsumoEAT(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, Integer codigoEAT, Date dataConsumo, Integer codigoProduto) throws Exception;
	public SistemaAbastecimentoProxy getSistemaAbastecimento(Integer codigo) throws Exception;
	public UnidadeNegocioProxy getUnidadeNegocio(Integer codigo) throws Exception;
	public LocalidadeProxy getLocalidade(Integer codigo) throws Exception;
	public MunicipioProxy getMunicipio(Integer codigo) throws Exception;
	public RegionalProxy getRegional(Integer codigo) throws Exception;
	public String getFonteCaptacaoEEAB(Integer tipoFonte, Integer codigoFonte) throws Exception;
	public String getFonteCaptacaoEEAT(Integer tipoFonte, Integer codigoFonte) throws Exception;
	public String getUnidadeOperacional(Integer tipoUnidade, Integer codigoUnidade) throws Exception;
	public boolean getEnergiaEletricaVerificaData(Date dataReferencia) throws Exception;
	public String getVerificaMacroMedidorCadastrado(Integer codigo,  Integer codigoUnidade, Integer tipoUnidade) throws Exception;
	public String getParametroSistema(Integer codigo) throws Exception;	
	//Listas
	public List<RegionalProxy> getListaRegional() throws Exception;
	public List<UnidadeNegocioProxy> getListaUnidadeNegocio(Integer codigoRegional) throws Exception;
	public List<MunicipioProxy> getListaMunicipio(Integer codigoRegional, Integer codigoUnidadeNegocio) throws Exception;
	public List<LocalidadeProxy> getListaLocalidade(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio) throws Exception;
	public List<SistemaAbastecimentoProxy> getListaSistemaAbastecimento(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer localidade) throws Exception;
	public List<Produto> getProdutosSistemaAbastecimento(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, Integer codigoSistemaAbastecimento, Date dataInicial, Date dataFinal) throws Exception;
	public List<ConsumoETA> getListaConsumoETAUsuario(UsuarioProxy usuario) throws Exception;
	public List<ConsumoEAT> getListaConsumoEATUsuario(UsuarioProxy usuario) throws Exception;
	public List<Produto> getListaProdutoETA(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, Integer codigoETA, Date dataInicial, Date dataFinal) throws Exception;
	public List<Produto> getListaProdutoEAT(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, Integer codigoEAT, Date dataInicial, Date dataFinal) throws Exception;
	public List<FonteCaptacaoProxy> getListaFonteCaptacao() throws Exception;
	public List<EEABFonteCaptacao> getListaFonteCaptacaoEEAB(Integer tipoFonte) throws Exception;
	public List<EEATFonteCaptacao> getListaFonteCaptacaoEEAT(Integer tipoFonte) throws Exception;
	public List<UnidadeConsumidoraOperacional> getListaUnidadeOperacional(Integer tipoUnidade) throws Exception;
	public List<LancamentoPendente> getConsumoPendenteUsuario(UsuarioProxy usuarioProxy, Integer tipoConsulta) throws Exception;
	public List<LancamentoPendente> getVolumePendenteUsuario(UsuarioProxy usuarioProxy, Integer tipoConsulta) throws Exception;
	public List<LancamentoPendente> getHorasPendenteUsuario(UsuarioProxy usuarioProxy, Integer tipoConsulta) throws Exception;
	//Tabela de Preços
	public List<TabelaPrecoProduto> getTabelaProdutos(TabelaPreco tabelaPreco) throws Exception;
	public List<TabelaPrecoProduto> getTabelaProdutosVigente() throws Exception;
	//Usuarios
	public UsuarioProxy getPerfilUsuario(UsuarioProxy usuarioProxy) throws Exception;
	public UsuarioProxy getParametrosSistema(UsuarioProxy usuarioProxy) throws Exception;
	@SuppressWarnings("rawtypes")
	public List<List> selectRegistros(String sql) throws Exception;
	public RegionalProxy getRegionalUnidadeNegocio(Integer codigoUnidadeNegocio) throws Exception;
}
