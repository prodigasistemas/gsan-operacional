package br.gov.pa.cosanpa.gopera.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import br.gov.pa.cosanpa.gopera.fachada.IIndicador;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.model.Indicador;
import br.gov.pa.cosanpa.gopera.model.IndicadorValor;

@Stateless
public class IndicadorEJB implements IIndicador {

	@EJB
	IProxy fachadaProxy;

	@Resource(lookup = "java:/gopera")
	private DataSource dataSource;

	@Override
	public void geraIndicadorMensal(Date dataInicial) throws Exception {
		SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");
		String strDataRef = formataData.format(dataInicial);
		String query = "SELECT operacao.geraIndicador('" + strDataRef + "')";
		fachadaProxy.selectRegistros(query);
	}

	@Override
	public List<Indicador> getIndicadorMensal(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade,
			Integer tipoUnidadeOperacional, Integer codigoUnidadeOperacional, Date dataInicial, Date dataFinal, Integer tipoAgrupamento,
			List<String> indicadores) throws Exception {

		List<Indicador> lista = new ArrayList<Indicador>();
		IndicadorValor indicadorValor = new IndicadorValor();

		SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat formataDataSQL = new SimpleDateFormat("yyyy-MM-dd");
		Connection con = dataSource.getConnection();
		Statement stm = con.createStatement();

		String listaIndicador = indicadores.toString().replace("[", "").replace("]", "");
		String strDataIni = formataData.format(dataInicial);
		String strDataFim = formataData.format(dataFinal);
		

		StringBuilder campos = new StringBuilder();
		if (tipoAgrupamento >= 1) {
			campos.append(" B.greg_id, greg.greg_nmregional,");
		}
		if (tipoAgrupamento >= 2) {
			campos.append(" B.uneg_id, uneg.uneg_nmunidadenegocio,");
		}
		if (tipoAgrupamento >= 3) {
			campos.append(" B.muni_id, muni.muni_nmmunicipio,");
		}
		if (tipoAgrupamento >= 4) {
			campos.append(" B.loca_id, loca.loca_nmlocalidade,");
		}
		if (tipoAgrupamento >= 5) {
			campos.append(" B.unop_tipo, B.unop_id, unop.unop_nmunidadeoperacional, ");
		}
		campos.append(" B.indc_id, B.indm_data, A.indc_seq, A.indc_nome, A.indc_und, A.indc_form, A.indc_resp, A.indc_grp");
		
		StringBuilder filtro = new StringBuilder();
		if (codigoRegional != -1) {
			filtro.append("  AND B.greg_id = " + codigoRegional);
		}
		if (codigoUnidadeNegocio != -1) {
			filtro.append("  AND B.uneg_id = " + codigoUnidadeNegocio);
		}
		if (codigoMunicipio != -1) {
			filtro.append("  AND B.muni_id = " + codigoMunicipio);
		}
		if (codigoLocalidade != -1) {
			filtro.append("  AND B.loca_id = " + codigoLocalidade);
		}
		if (codigoUnidadeOperacional != 0) {
			filtro.append("  AND B.unop_tipo = " + tipoUnidadeOperacional + "  AND B.unop_id = " + codigoUnidadeOperacional);
		}
		
		StringBuilder grupo = new StringBuilder(" GROUP BY ");
		grupo.append(campos);
		
		StringBuilder ordem = new StringBuilder(" ORDER BY ");
		if (tipoAgrupamento >= 1) {
			ordem.append(" greg_id,");
		}
		if (tipoAgrupamento >= 2) {
			ordem.append(" uneg_id,");
		}
		if (tipoAgrupamento >= 3) {
			ordem.append(" muni_id,");
		}
		if (tipoAgrupamento >= 4) {
			ordem.append(" loca_id,");
		}
		if (tipoAgrupamento >= 5) {
			ordem.append(" unop_tipo, unop_id,");
		}
		ordem.append(" indc_seq, indm_data");
		
		StringBuilder query = new StringBuilder();		
		query.append(" SELECT ");		
		query.append(campos)
		.append(" , SUM(B.indm_vlr1) AS indm_vlr1, SUM(B.indm_vlr2) AS indm_vlr2, SUM(B.indm_vlrtotal) AS indm_vlrtotal ")
		.append(" FROM operacao.indicador A ")
		.append(" INNER JOIN operacao.indicador_mensal B ON A.indc_id = B.indc_id ")
		.append(" AND B.indm_data BETWEEN '" + strDataIni + "' AND '" + strDataFim + "' ")
		.append(" INNER JOIN cadastro.gerencia_regional greg on greg.greg_id = B.greg_id  ")
		.append(" INNER JOIN cadastro.unidade_negocio uneg ON uneg.uneg_id = B.uneg_id ")
		.append(" INNER JOIN cadastro.localidade loca ON loca.loca_id = B.loca_id ")
		.append(" INNER JOIN cadastro.municipio muni ON muni.muni_id = B.muni_id ")
		.append(" LEFT JOIN (SELECT eeab_id AS unop_id, eeab_nome AS unop_nmunidadeoperacional, 1 AS unop_tipo FROM operacao.eeab ")
		.append("            UNION ALL ")
		.append("            SELECT eta_id AS unop_id, eta_nome AS unop_nmunidadeoperacional, 2 AS unop_tipo FROM operacao.eta ")
		.append("            UNION ALL ")
		.append("            SELECT eeat_id AS unop_id, eeat_nome AS unop_nmunidadeoperacional, 3 AS unop_tipo FROM operacao.eeat ")
		.append("            UNION ALL ")
		.append("            SELECT rso_id AS unop_id, rso_nome AS unop_nmunidadeoperacional, 4 AS unop_tipo FROM operacao.rso) unop on ")
		.append("   unop.unop_tipo = B.unop_tipo AND unop.unop_id = B.unop_id ")
		.append(" WHERE A.indc_id IN (" + listaIndicador + ")")
		.append(filtro)
		.append(grupo)
		.append(ordem);
		ResultSet rs = stm.executeQuery(query.toString());

		while (rs.next()) {
			if (rs.getString("indm_data") != null) {
				Indicador indicador = new Indicador();
				if (tipoAgrupamento >= 1) {
					indicador.setCodigoRegional(rs.getInt("greg_id"));
					indicador.setNomeRegional(rs.getString("greg_nmregional"));
				}
				if (tipoAgrupamento >= 2) {
					indicador.setCodigoUnidadeNegocio(rs.getInt("uneg_id"));
					indicador.setNomeUnidadeNegocio(rs.getString("uneg_nmunidadenegocio"));
				}
				if (tipoAgrupamento >= 3) {
					indicador.setCodigoMunicipio(rs.getInt("muni_id"));
					indicador.setNomeMunicipio(rs.getString("muni_nmmunicipio"));
				}
				if (tipoAgrupamento >= 4) {
					indicador.setCodigoLocalidade(rs.getInt("loca_id"));
					indicador.setNomeLocalidade(rs.getString("loca_nmlocalidade"));
				}

				if (tipoAgrupamento >= 5) {
					indicador.setTipoUnidadeOperacional(rs.getInt("unop_tipo"));
					indicador.setCodigoUnidadeOperacional(rs.getInt("unop_id"));
					indicador.setNomeUnidadeOperacional(rs.getString("unop_nmunidadeoperacional"));
				}

				// CONFIGURA VALORES AO INDICADOR (REFERENCIA E TOTAIS)
				indicadorValor = new IndicadorValor();

				indicadorValor.setMesReferencia(formataDataSQL.parse(rs.getString("indm_data")));
				Double indicador1 = rs.getDouble("indm_vlr1");
				Double indicador2 = rs.getDouble("indm_vlr2");
				Double indicadorTotal = (indicador2 == 0 ? 0 : indicador1 / indicador2);
				indicadorValor.setIndicador1(indicador1);
				indicadorValor.setIndicador2(indicador2);
				indicadorValor.setTotal(indicadorTotal);

				// CARREGA INDICADOR
				indicador.setCodigo(rs.getInt("indc_id"));
				indicador.setSequencial(rs.getInt("indc_seq"));
				indicador.setNomeIndicador(rs.getString("indc_nome"));
				indicador.setUnidadeIndicador(rs.getString("indc_und"));
				indicador.setFormulaIndicador(rs.getString("indc_form"));
				indicador.setResponsavelIndicador(rs.getString("indc_resp"));

				if (existeIndicadorNaLista(lista, indicador)) { // INDICADOR NAO EXISTE NA LISTA
					recuperaIndicadorNaLista(lista, indicador).getValor().add(indicadorValor);
				}else{
					indicador.getValor().add(indicadorValor);
					lista.add(indicador);
				}
			}
		}
		return lista;
	}
	
	private boolean existeIndicadorNaLista(List<Indicador> lista, Indicador indicador) {
		return (recuperaIndicadorNaLista(lista, indicador) == null)?false:true;
	}

	private Indicador recuperaIndicadorNaLista(List<Indicador> lista, Indicador indicador) {
		Indicador retorno = null;
		for (Integer i = 0; i < lista.size(); i++) {
			Indicador item = lista.get(i); 
			if (mesmoCodigo(item, indicador) 
					&& mesmaRegional(item, indicador)
					&& mesmaUnidadeNegocio(item, indicador)
					&& mesmoMunicipio(item, indicador)
					&& mesmaLocalidade(item, indicador)
					&& mesmaUnidadeOperacional(item, indicador)) {
				retorno = item;
				break;
			}
		}

		return retorno;
	}

	private boolean mesmoCodigo(Indicador item, Indicador indicador){
		return item.getCodigo().intValue() == indicador.getCodigo().intValue();
	}
	
	private boolean mesmaRegional(Indicador item, Indicador indicador){
		return item.getCodigoRegional().intValue() == indicador.getCodigoRegional().intValue();
	}
	
	private boolean mesmaUnidadeNegocio(Indicador item, Indicador indicador){
		return item.getCodigoUnidadeNegocio().intValue() == indicador.getCodigoUnidadeNegocio().intValue();
	}		

	private boolean mesmoMunicipio(Indicador item, Indicador indicador){
		return item.getCodigoMunicipio().intValue() == indicador.getCodigoMunicipio().intValue();
	}
	
	private boolean mesmaLocalidade(Indicador item, Indicador indicador){
		return item.getCodigoLocalidade().intValue() == indicador.getCodigoLocalidade().intValue();
	}		
	
	private boolean mesmaUnidadeOperacional(Indicador item, Indicador indicador){
		boolean retorno = item.getTipoUnidadeOperacional().intValue() == indicador.getTipoUnidadeOperacional().intValue();
		return retorno && item.getCodigoUnidadeOperacional().intValue() == indicador.getCodigoUnidadeOperacional().intValue();
	}		
}
