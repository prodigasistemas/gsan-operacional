package br.gov.pa.cosanpa.gopera.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.fachada.IRelatorioProdutoQuimico;
import br.gov.pa.cosanpa.gopera.model.RelatorioGerencial;
import br.gov.pa.cosanpa.gopera.util.DateUtil;
import br.gov.pa.cosanpa.gopera.util.ElementoMensal;
import br.gov.pa.cosanpa.gopera.util.Mes;
import br.gov.pa.cosanpa.gopera.util.OperacionalUtil;

@Stateless
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RelatorioProdutoQuimicoEJB implements IRelatorioProdutoQuimico {

	@EJB
	IProxy fachadaProxy;
	
	public List<RelatorioGerencial> getConsumoProdutoSintetico(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, Integer tipoUnidadeOperacional, Integer codigoUnidadeOperacional,
													  Date dataInicial, Date dataFinal, Integer tipoAgrupamento, Integer tipoRelatorio
													) throws Exception {
		
		SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");
		String dataIni = formataData.format(dataInicial);
		String dataFim = formataData.format(dataFinal);
		
		String query = "";
		String queryAux;
		queryAux = "SUM(B.conp_quantidade), AVG(" +
				   "	(SELECT tbpp_preco" + 
				   " FROM operacao.tabelapreco_produto X" +	
				   " INNER JOIN operacao.tabelapreco Z ON X.tabp_id = Z.tabp_id" +	          
				   " WHERE Z.tabp_vigencia <= B.cons_data" +
				   "  AND X.prod_id = C.prod_id" +
				   " ORDER BY Z.tabp_vigencia DESC LIMIT 1))";
		
		queryAux = queryAux + 		
				" , C.prod_nmproduto, D.umed_sigla" +
				" FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
				" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade" +
				" FROM cadastro.gerencia_regional A " +
				"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
				"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
				"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
				"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A " +
				"INNER JOIN (SELECT A.greg_id, A.uneg_id, A.muni_id, A.loca_id," +
				"				    C.eta_id AS unop_id, C.eta_nome AS unop_nmunidadeoperacional, 2 AS unop_tipo," +
				"				    A.cons_data, B.prod_id, B.conp_quantidade" +
				"			   FROM	operacao.consumoeta A" + 
				"			  INNER JOIN operacao.consumoeta_produto B ON A.cons_id = B.cons_id" +
				"			  INNER JOIN operacao.eta C ON A.eta_id = C.eta_id" +
				"			  UNION ALL	" +				
				"			  SELECT A.greg_id, A.uneg_id, A.muni_id, A.loca_id," +
				"				    C.eeat_id AS unop_id, C.eeat_nome AS unop_nmunidadeoperacional, 3 AS unop_tipo," +
				"				    A.cons_data, B.prod_id, B.conp_quantidade" +
				"			   FROM	operacao.consumoeat A" + 
				"			  INNER JOIN operacao.consumoeat_produto B ON A.cons_id = B.cons_id" +
				"			  INNER JOIN operacao.eeat C ON A.eat_id = C.eeat_id)" +				
				"  B ON A.greg_id = B.greg_id AND A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id " + 
				"INNER JOIN operacao.produto C ON B.prod_id = C.prod_id " +
				"INNER JOIN operacao.unidademedida D ON C.umed_id = D.umed_id " +
				"WHERE B.cons_data BETWEEN '" + dataIni + "' AND '" + dataFim + "'" +
		        "  AND B.conp_quantidade <> 0";
		
		//CABECALHO DO GRUPO
		query = " SELECT "; 
		if(tipoAgrupamento >= 1){//GERENCIA REGIONAL
			query = query + " A.greg_id, A.greg_nmregional,"; 
		}
		if(tipoAgrupamento >= 2){//GERENCIA REGIONAL + UNIDADE NEGOCIO
			query = query + " A.uneg_id, A.uneg_nmunidadenegocio,";  
		}
		if(tipoAgrupamento >= 3){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO
			query = query + " A.muni_id, A.muni_nmmunicipio,"; 
		}
		if(tipoAgrupamento >= 4){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO + LOCALIDADE
			query = query + " A.loca_id, A.loca_nmlocalidade,"; 
		}
		if(tipoAgrupamento >= 5){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO + LOCALIDADE + SISTEMA ABASTECIMENTO
			query = query + " B.unop_id, B.unop_nmunidadeoperacional, "; 
		}
		
		query = query + queryAux;

		//FILTROS	
		if (codigoRegional != -1){
			query = query + "  AND A.greg_id = " + codigoRegional;
		}

		if (codigoUnidadeNegocio != -1){
			query = query + "  AND A.uneg_id = " + codigoUnidadeNegocio;
		}
		if (codigoMunicipio != -1){
			query = query + "  AND A.muni_id = " + codigoMunicipio;
		}
		if (codigoLocalidade != -1){
			query = query + "  AND A.loca_id = " + codigoLocalidade;
		}
		if (codigoUnidadeOperacional != -1){
			query = query + "  AND B.unop_id = " + codigoUnidadeOperacional
						  + "  AND B.unop_tipo = " + tipoUnidadeOperacional;
		}
		
		//AGRUPAMENTOS
		query = query + " GROUP BY ";
		if(tipoAgrupamento >= 1){//GERENCIA REGIONAL
			query = query + " A.greg_id, A.greg_nmregional,"; 
		}
		if(tipoAgrupamento >= 2){//GERENCIA REGIONAL + UNIDADE NEGOCIO
			query = query + " A.uneg_id, A.uneg_nmunidadenegocio,";  
		}
		if(tipoAgrupamento >= 3){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO
			query = query + " A.muni_id, A.muni_nmmunicipio,"; 
		}
		if(tipoAgrupamento >= 4){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO + LOCALIDADE
			query = query + " A.loca_id, A.loca_nmlocalidade,"; 
		}
		if(tipoAgrupamento >= 5){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO + LOCALIDADE + SISTEMA ABASTECIMENTO
			query = query + " B.unop_tipo, B.unop_id, B.unop_nmunidadeoperacional, "; 
		}
		query = query + " C.prod_nmproduto, D.umed_sigla";

		//ORDENACAO
		query = query + " ORDER BY ";
		if(tipoAgrupamento >= 1){//GERENCIA REGIONAL
			query = query + " A.greg_id,"; 
		}
		if(tipoAgrupamento >= 2){//GERENCIA REGIONAL + UNIDADE NEGOCIO
			query = query + " A.uneg_id,";  
		}
		if(tipoAgrupamento >= 3){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO
			query = query + " A.muni_id,"; 
		}
		if(tipoAgrupamento >= 4){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO + LOCALIDADE
			query = query + " A.loca_id,"; 
		}
		if(tipoAgrupamento >= 5){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO + LOCALIDADE + SISTEMA ABASTECIMENTO
			query = query + " B.unop_tipo, B.unop_id,"; 
		}
		query = query + " C.prod_nmproduto";
		
		List<List> valores = fachadaProxy.selectRegistros(query);
		List<RelatorioGerencial> lista = new ArrayList<RelatorioGerencial>();
		
		if (valores == null) {
			return lista;
		}
		
		Integer intColConsumo;
		for (List colunas : valores) {
			RelatorioGerencial relatorioGerencial = new RelatorioGerencial();
			intColConsumo = 0;
			if (tipoAgrupamento >= 1){
				relatorioGerencial.setCodigoRegional(Integer.parseInt(colunas.get(0).toString()));
				relatorioGerencial.setNomeRegional(colunas.get(1).toString());
				intColConsumo = 2;
			}
			if (tipoAgrupamento >= 2){
				relatorioGerencial.setCodigoUnidadeNegocio(Integer.parseInt(colunas.get(2).toString()));
				relatorioGerencial.setNomeUnidadeNegocio(colunas.get(3).toString());
				intColConsumo = 4;
			}
			if(tipoAgrupamento >= 3){
				relatorioGerencial.setCodigoMunicipio(Integer.parseInt(colunas.get(4).toString()));
				relatorioGerencial.setNomeMunicipio(colunas.get(5).toString());
				intColConsumo = 6;
			}
			if (tipoAgrupamento >= 4){
				relatorioGerencial.setCodigoLocalidade(Integer.parseInt(colunas.get(6).toString()));
				relatorioGerencial.setNomeLocalidade(colunas.get(7).toString());
				intColConsumo = 8;
			}
			if (tipoAgrupamento >= 5){
				relatorioGerencial.setCodigoUnidadeOperacional(Integer.parseInt(colunas.get(8).toString()));
				relatorioGerencial.setNomeUnidadeOperacional(colunas.get(9).toString());
				intColConsumo = 10;
			}	
			
			Double qtdConsumo, valorUnitario;
			if (colunas.get(intColConsumo) == null){
				qtdConsumo = 0d;
				valorUnitario = 0d;
			}else{
				qtdConsumo = Double.parseDouble(colunas.get(intColConsumo).toString());
				valorUnitario = Double.parseDouble(colunas.get(intColConsumo + 1).toString());
			}
			relatorioGerencial.setQtdConsumo(qtdConsumo);
			relatorioGerencial.setValorUnitario(valorUnitario);
			relatorioGerencial.setValorTotal(qtdConsumo * valorUnitario);
			if (tipoRelatorio == 1){//FÍSICO
				relatorioGerencial.setDescricaoProduto(colunas.get(intColConsumo + 2).toString() + " - " + colunas.get(intColConsumo + 3).toString());
			}
			else{//FINANCEIRO
				relatorioGerencial.setDescricaoProduto(colunas.get(intColConsumo + 2).toString() + " - R$");
			}

			lista.add(relatorioGerencial);
		}
		return lista;
	}
	
	public List<RelatorioGerencial> getConsumoProdutoMensal(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, Integer tipoUnidadeOperacional, Integer codigoUnidadeOperacional,
			  Date dataInicial, Date dataFinal, Integer tipoAgrupamento, Integer tipoRelatorio
			) throws Exception {

		SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");
		String dataIni = formataData.format(dataInicial);
		String dataFim = formataData.format(dataFinal);
		
		StringBuilder query = new StringBuilder();
		
		StringBuilder queryAux = new StringBuilder();
		if (tipoRelatorio == 1){//FÍSICO
			queryAux.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 1 THEN B.conp_quantidade ELSE 0 END) AS qtdJAN,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 2 THEN B.conp_quantidade ELSE 0 END) AS qtdFEV,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 3 THEN B.conp_quantidade ELSE 0 END) AS qtdMAR,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 4 THEN B.conp_quantidade ELSE 0 END) AS qtdABR,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 5 THEN B.conp_quantidade ELSE 0 END) AS qtdMAI,\n")                  
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 6 THEN B.conp_quantidade ELSE 0 END) AS qtdJUN,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 7 THEN B.conp_quantidade ELSE 0 END) AS qtdJUL,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 8 THEN B.conp_quantidade ELSE 0 END) AS qtdAGO,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 9 THEN B.conp_quantidade ELSE 0 END) AS qtdSET,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 10 THEN B.conp_quantidade ELSE 0 END) AS qtdOUT,\n")              
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 11 THEN B.conp_quantidade ELSE 0 END) AS qtdNOV,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 12 THEN B.conp_quantidade ELSE 0 END) AS qtdDEZ,\n");
		}else{//FINANCEIRO
			queryAux.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 1 THEN B.conp_quantidade * (SELECT tbpp_preco FROM operacao.tabelapreco_produto X INNER JOIN operacao.tabelapreco Z ON X.tabp_id = Z.tabp_id WHERE Z.tabp_vigencia <= B.cons_data AND X.prod_id = C.prod_id ORDER BY Z.tabp_vigencia DESC LIMIT 1) ELSE 0 END) AS qtdJAN,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 2 THEN B.conp_quantidade * (SELECT tbpp_preco FROM operacao.tabelapreco_produto X INNER JOIN operacao.tabelapreco Z ON X.tabp_id = Z.tabp_id WHERE Z.tabp_vigencia <= B.cons_data AND X.prod_id = C.prod_id ORDER BY Z.tabp_vigencia DESC LIMIT 1) ELSE 0 END) AS qtdFEV,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 3 THEN B.conp_quantidade * (SELECT tbpp_preco FROM operacao.tabelapreco_produto X INNER JOIN operacao.tabelapreco Z ON X.tabp_id = Z.tabp_id WHERE Z.tabp_vigencia <= B.cons_data AND X.prod_id = C.prod_id ORDER BY Z.tabp_vigencia DESC LIMIT 1) ELSE 0 END) AS qtdMAR,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 4 THEN B.conp_quantidade * (SELECT tbpp_preco FROM operacao.tabelapreco_produto X INNER JOIN operacao.tabelapreco Z ON X.tabp_id = Z.tabp_id WHERE Z.tabp_vigencia <= B.cons_data AND X.prod_id = C.prod_id ORDER BY Z.tabp_vigencia DESC LIMIT 1) ELSE 0 END) AS qtdABR,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 5 THEN B.conp_quantidade * (SELECT tbpp_preco FROM operacao.tabelapreco_produto X INNER JOIN operacao.tabelapreco Z ON X.tabp_id = Z.tabp_id WHERE Z.tabp_vigencia <= B.cons_data AND X.prod_id = C.prod_id ORDER BY Z.tabp_vigencia DESC LIMIT 1) ELSE 0 END) AS qtdMAI,\n")                  
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 6 THEN B.conp_quantidade * (SELECT tbpp_preco FROM operacao.tabelapreco_produto X INNER JOIN operacao.tabelapreco Z ON X.tabp_id = Z.tabp_id WHERE Z.tabp_vigencia <= B.cons_data AND X.prod_id = C.prod_id ORDER BY Z.tabp_vigencia DESC LIMIT 1) ELSE 0 END) AS qtdJUN,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 7 THEN B.conp_quantidade * (SELECT tbpp_preco FROM operacao.tabelapreco_produto X INNER JOIN operacao.tabelapreco Z ON X.tabp_id = Z.tabp_id WHERE Z.tabp_vigencia <= B.cons_data AND X.prod_id = C.prod_id ORDER BY Z.tabp_vigencia DESC LIMIT 1) ELSE 0 END) AS qtdJUL,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 8 THEN B.conp_quantidade * (SELECT tbpp_preco FROM operacao.tabelapreco_produto X INNER JOIN operacao.tabelapreco Z ON X.tabp_id = Z.tabp_id WHERE Z.tabp_vigencia <= B.cons_data AND X.prod_id = C.prod_id ORDER BY Z.tabp_vigencia DESC LIMIT 1) ELSE 0 END) AS qtdAGO,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 9 THEN B.conp_quantidade * (SELECT tbpp_preco FROM operacao.tabelapreco_produto X INNER JOIN operacao.tabelapreco Z ON X.tabp_id = Z.tabp_id WHERE Z.tabp_vigencia <= B.cons_data AND X.prod_id = C.prod_id ORDER BY Z.tabp_vigencia DESC LIMIT 1) ELSE 0 END) AS qtdSET,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 10 THEN B.conp_quantidade * (SELECT tbpp_preco FROM operacao.tabelapreco_produto X INNER JOIN operacao.tabelapreco Z ON X.tabp_id = Z.tabp_id WHERE Z.tabp_vigencia <= B.cons_data AND X.prod_id = C.prod_id ORDER BY Z.tabp_vigencia DESC LIMIT 1) ELSE 0 END) AS qtdOUT,\n")              
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 11 THEN B.conp_quantidade * (SELECT tbpp_preco FROM operacao.tabelapreco_produto X INNER JOIN operacao.tabelapreco Z ON X.tabp_id = Z.tabp_id WHERE Z.tabp_vigencia <= B.cons_data AND X.prod_id = C.prod_id ORDER BY Z.tabp_vigencia DESC LIMIT 1) ELSE 0 END) AS qtdNOV,\n")
			.append("SUM(CASE EXTRACT(MONTH FROM B.cons_data) WHEN 12 THEN B.conp_quantidade * (SELECT tbpp_preco FROM operacao.tabelapreco_produto X INNER JOIN operacao.tabelapreco Z ON X.tabp_id = Z.tabp_id WHERE Z.tabp_vigencia <= B.cons_data AND X.prod_id = C.prod_id ORDER BY Z.tabp_vigencia DESC LIMIT 1) ELSE 0 END) AS qtdDEZ,\n");
		}          
		
		queryAux.append("C.prod_nmproduto, D.umed_sigla\n")
		.append(" FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio,\n") 
		.append(" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade \n")
		.append(" FROM cadastro.gerencia_regional A \n")
		.append("INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id \n")
		.append("INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID \n")
		.append("INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id \n")
		.append("INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A \n")
		.append("INNER JOIN (SELECT A.greg_id, A.uneg_id, A.muni_id, A.loca_id,\n")
		.append("				    C.eta_id AS unop_id, C.eta_nome AS unop_nmunidadeoperacional, 2 AS unop_tipo,\n")
		.append("				    A.cons_data, B.prod_id, B.conp_quantidade\n")
		.append("            FROM operacao.consumoeta A\n")
		.append("            INNER JOIN operacao.consumoeta_produto B ON A.cons_id = B.cons_id\n")
		.append("            INNER JOIN operacao.eta C ON A.eta_id = C.eta_id\n")
		.append("            UNION ALL	\n")
		.append("            SELECT A.greg_id, A.uneg_id, A.muni_id, A.loca_id,\n")
		.append("                   C.eeat_id AS unop_id, C.eeat_nome AS unop_nmunidadeoperacional, 3 AS unop_tipo,\n")
		.append("                   A.cons_data, B.prod_id, B.conp_quantidade\n")
		.append("            FROM operacao.consumoeat A\n")
		.append("            INNER JOIN operacao.consumoeat_produto B ON A.cons_id = B.cons_id\n")
		.append("            INNER JOIN operacao.eeat C ON A.eat_id = C.eeat_id)\n")	
		.append("  B ON A.greg_id = B.greg_id AND A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id \n") 
		.append("INNER JOIN operacao.produto C ON B.prod_id = C.prod_id \n")
		.append("INNER JOIN operacao.unidademedida D ON C.umed_id = D.umed_id \n")
		.append("WHERE B.cons_data BETWEEN '" + dataIni + "' AND '" + dataFim + "'\n")
		.append("  AND B.conp_quantidade <> 0\n");
		
		//CABECALHO DO GRUPO
		query.append(" SELECT "); 
		if(tipoAgrupamento >= 1){//GERENCIA REGIONAL
			query.append(" A.greg_id, A.greg_nmregional,"); 
		}
		if(tipoAgrupamento >= 2){//GERENCIA REGIONAL + UNIDADE NEGOCIO
			query.append(" A.uneg_id, A.uneg_nmunidadenegocio,");  
		}
		if(tipoAgrupamento >= 3){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO
			query.append(" A.muni_id, A.muni_nmmunicipio,"); 
		}
		if(tipoAgrupamento >= 4){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO + LOCALIDADE
			query.append(" A.loca_id, A.loca_nmlocalidade,"); 
		}
		if(tipoAgrupamento >= 5){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO + LOCALIDADE + SISTEMA ABASTECIMENTO
			query.append(" B.unop_id, B.unop_nmunidadeoperacional, "); 
		}
		
		query.append(queryAux);

		//FILTROS	
		if (codigoRegional != -1){
			query.append("  AND A.greg_id = " + codigoRegional);
		}

		if (codigoUnidadeNegocio != -1){
			query.append("  AND A.uneg_id = " + codigoUnidadeNegocio);
		}
		if (codigoMunicipio != -1){
			query.append("  AND A.muni_id = " + codigoMunicipio);
		}
		if (codigoLocalidade != -1){
			query.append("  AND A.loca_id = " + codigoLocalidade);
		}
		if (codigoUnidadeOperacional != -1){
			query.append("  AND B.unop_id = " + codigoUnidadeOperacional
						  + "  AND B.unop_tipo = " + tipoUnidadeOperacional);
		}
		
		//AGRUPAMENTOS
		query.append(" \nGROUP BY ");
		if(tipoAgrupamento >= 1){//GERENCIA REGIONAL
			query.append(" A.greg_id, A.greg_nmregional,"); 
		}
		if(tipoAgrupamento >= 2){//GERENCIA REGIONAL + UNIDADE NEGOCIO
			query.append(" A.uneg_id, A.uneg_nmunidadenegocio,");  
		}
		if(tipoAgrupamento >= 3){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO
			query.append(" A.muni_id, A.muni_nmmunicipio,"); 
		}
		if(tipoAgrupamento >= 4){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO + LOCALIDADE
			query.append(" A.loca_id, A.loca_nmlocalidade,"); 
		}
		if(tipoAgrupamento >= 5){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO + LOCALIDADE + SISTEMA ABASTECIMENTO
			query.append(" B.unop_tipo, B.unop_id, B.unop_nmunidadeoperacional, "); 
		}
		query.append(" C.prod_nmproduto, D.umed_sigla\n");

		//ORDENACAO
		query.append(" ORDER BY ");
		if(tipoAgrupamento >= 1){//GERENCIA REGIONAL
			query.append(" A.greg_id,"); 
		}
		if(tipoAgrupamento >= 2){//GERENCIA REGIONAL + UNIDADE NEGOCIO
			query.append(" A.uneg_id,");  
		}
		if(tipoAgrupamento >= 3){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO
			query.append(" A.muni_id,"); 
		}
		if(tipoAgrupamento >= 4){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO + LOCALIDADE
			query.append(" A.loca_id,"); 
		}
		if(tipoAgrupamento >= 5){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO + LOCALIDADE + SISTEMA ABASTECIMENTO
			query.append(" B.unop_tipo, B.unop_id,"); 
		}
		query.append(" C.prod_nmproduto");
		
		List<List> valores = fachadaProxy.selectRegistros(query.toString());
		List<RelatorioGerencial> lista = new ArrayList<RelatorioGerencial>();
		
		if (valores == null) {
			return lista;
		}
		
		Integer intColConsumo;
		
		List<Mes> mesesIntervalo = (new DateUtil()).numeralMesesPeriodo(dataInicial, dataFinal);
		
		for (List colunas : valores) {
			RelatorioGerencial relatorioGerencial = new RelatorioGerencial();
			intColConsumo = 0;
			if (tipoAgrupamento >= 1){
				relatorioGerencial.setCodigoRegional(Integer.parseInt(colunas.get(0).toString()));
				relatorioGerencial.setNomeRegional(colunas.get(1).toString());
				intColConsumo = 2;
			}
			if (tipoAgrupamento >= 2){
				relatorioGerencial.setCodigoUnidadeNegocio(Integer.parseInt(colunas.get(2).toString()));
				relatorioGerencial.setNomeUnidadeNegocio(colunas.get(3).toString());
				intColConsumo = 4;
			}
			if(tipoAgrupamento >= 3){
				relatorioGerencial.setCodigoMunicipio(Integer.parseInt(colunas.get(4).toString()));
				relatorioGerencial.setNomeMunicipio(colunas.get(5).toString());
				intColConsumo = 6;
			}
			if (tipoAgrupamento >= 4){
				relatorioGerencial.setCodigoLocalidade(Integer.parseInt(colunas.get(6).toString()));
				relatorioGerencial.setNomeLocalidade(colunas.get(7).toString());
				intColConsumo = 8;
			}
			if (tipoAgrupamento >= 5){
				relatorioGerencial.setCodigoUnidadeOperacional(Integer.parseInt(colunas.get(8).toString()));
				relatorioGerencial.setNomeUnidadeOperacional(colunas.get(9).toString());
				intColConsumo = 10;
			}		
			for (Integer intI = intColConsumo; intI <=12; intI++ ){
				if (colunas.get(intI) == null){
					colunas.set(intI, 0.0d);
				}
			}
			
			int indiceColuna = intColConsumo;
			
			for (Mes mesIntervalo : mesesIntervalo) {
				ElementoMensal eleMes = new ElementoMensal();
				eleMes.setMes(mesIntervalo.getNome());
				double valor = Double.parseDouble(colunas.get(indiceColuna + mesIntervalo.getNumeral() - 1).toString());
				eleMes.setQuantidade(valor);
				relatorioGerencial.addQuantidadeMensal(eleMes);
			}
			
			int complemento = 12 - relatorioGerencial.getQuantidadesMensal().size() ;
			
			for(int i = 0; i < complemento; i++){
				relatorioGerencial.addQuantidadeMensal(new ElementoMensal());
			}
			
			relatorioGerencial.setQtdConsumoJAN(Double.parseDouble(colunas.get(intColConsumo++).toString()));
			relatorioGerencial.setQtdConsumoFEV(Double.parseDouble(colunas.get(intColConsumo++).toString()));
			relatorioGerencial.setQtdConsumoMAR(Double.parseDouble(colunas.get(intColConsumo++).toString()));
			relatorioGerencial.setQtdConsumoABR(Double.parseDouble(colunas.get(intColConsumo++).toString()));
			relatorioGerencial.setQtdConsumoMAI(Double.parseDouble(colunas.get(intColConsumo++).toString()));
			relatorioGerencial.setQtdConsumoJUN(Double.parseDouble(colunas.get(intColConsumo++).toString()));
			relatorioGerencial.setQtdConsumoJUL(Double.parseDouble(colunas.get(intColConsumo++).toString()));
			relatorioGerencial.setQtdConsumoAGO(Double.parseDouble(colunas.get(intColConsumo++).toString()));
			relatorioGerencial.setQtdConsumoSET(Double.parseDouble(colunas.get(intColConsumo++).toString()));
			relatorioGerencial.setQtdConsumoOUT(Double.parseDouble(colunas.get(intColConsumo++).toString()));
			relatorioGerencial.setQtdConsumoNOV(Double.parseDouble(colunas.get(intColConsumo++).toString()));
			relatorioGerencial.setQtdConsumoDEZ(Double.parseDouble(colunas.get(intColConsumo++).toString()));
			
			
			if (tipoRelatorio == 1){//FÍSICO
				relatorioGerencial.setDescricaoProduto(colunas.get(intColConsumo).toString() + " - " + colunas.get(intColConsumo + 1).toString());
			}
			else{//FINANCEIRO
				relatorioGerencial.setDescricaoProduto(colunas.get(intColConsumo).toString() + " - R$");
			}
					
			lista.add(relatorioGerencial);
		}
	return lista;
}
	
public List<RelatorioGerencial> getConsumoProdutoAnalitico(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, Integer tipoUnidadeOperacional, Integer codigoUnidadeOperacional,
		  Date dataInicial, Date dataFinal, Integer tipoAgrupamento	, Integer tipoRelatorio
		) throws Exception {
	
	SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");
	String dataIni = formataData.format(dataInicial);
	String dataFim = formataData.format(dataFinal);
	
	String query = " SELECT A.*, B.unop_id, B.unop_nmunidadeoperacional, B.cons_data, B.conp_quantidade, C.prod_nmproduto," +
				"	(SELECT tbpp_preco" + 
				"  	   FROM operacao.tabelapreco_produto X" +	
				" 	  INNER JOIN operacao.tabelapreco Z ON X.tabp_id = Z.tabp_id" +	          
			    "  	  WHERE Z.tabp_vigencia <= B.cons_data" +
				" 	    AND X.prod_id = C.prod_id" + 
				" 	  ORDER BY Z.tabp_vigencia DESC LIMIT 1) AS tbpp_preco, B.unop_tipo, D.umed_sigla" + 	
				" FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
				" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade " +
				" FROM cadastro.gerencia_regional A " +
				"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
				"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
				"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
				"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A " +
				"INNER JOIN (SELECT A.greg_id, A.uneg_id, A.muni_id, A.loca_id," +
				"				    C.eta_id AS unop_id, C.eta_nome AS unop_nmunidadeoperacional, 2 AS unop_tipo," +
				"				    A.cons_data, B.prod_id, B.conp_quantidade" +
				"			   FROM	operacao.consumoeta A" + 
				"			  INNER JOIN operacao.consumoeta_produto B ON A.cons_id = B.cons_id" +
				"			  INNER JOIN operacao.eta C ON A.eta_id = C.eta_id" +
				"			  UNION ALL	" +				
				"			  SELECT A.greg_id, A.uneg_id, A.muni_id, A.loca_id," +
				"				    C.eeat_id AS unop_id, C.eeat_nome AS unop_nmunidadeoperacional, 3 AS unop_tipo," +
				"				    A.cons_data, B.prod_id, B.conp_quantidade" +
				"			   FROM	operacao.consumoeat A" + 
				"			  INNER JOIN operacao.consumoeat_produto B ON A.cons_id = B.cons_id" +
				"			  INNER JOIN operacao.eeat C ON A.eat_id = C.eeat_id)" +				
				"  B ON A.greg_id = B.greg_id AND A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id " + 
				"INNER JOIN operacao.produto C ON B.prod_id = C.prod_id " +
				"INNER JOIN operacao.unidademedida D ON C.umed_id = D.umed_id " +
				"WHERE B.cons_data BETWEEN '" + dataIni + "' AND '" + dataFim + "'" +
		        "  AND B.conp_quantidade <> 0";
	
	if (codigoRegional != -1){
		query = query + " AND A.greg_id = " + codigoRegional;
	}
	if (codigoUnidadeNegocio != -1){
		query = query + "  AND A.uneg_id = " + codigoUnidadeNegocio;
	}
	if (codigoMunicipio != -1){
		query = query + "  AND A.muni_id = " + codigoMunicipio;
	}
	if (codigoLocalidade != -1){
		query = query + "  AND A.loca_id = " + codigoLocalidade;
	}
	if (codigoUnidadeOperacional != -1){
		query = query + "  AND B.unop_id = " + codigoUnidadeOperacional
					  + "  AND B.unop_tipo = " + tipoUnidadeOperacional;
	}
	
	query += " ORDER BY ";
	query += (new OperacionalUtil()).ordenarCampos(tipoAgrupamento);
	query +=" , C.prod_nmproduto ";
	
	List<List> valores = fachadaProxy.selectRegistros(query);
	List<RelatorioGerencial> lista = new ArrayList<RelatorioGerencial>();
	
	if (valores == null) {
		return lista;
	}
	
	for (List colunas : valores) {
		RelatorioGerencial relatorioGerencial = new RelatorioGerencial();
		relatorioGerencial.setCodigoRegional(Integer.parseInt(colunas.get(0).toString()));
		relatorioGerencial.setNomeRegional(colunas.get(1).toString());
		relatorioGerencial.setCodigoUnidadeNegocio(Integer.parseInt(colunas.get(2).toString()));
		relatorioGerencial.setNomeUnidadeNegocio(colunas.get(3).toString());
		relatorioGerencial.setCodigoMunicipio(Integer.parseInt(colunas.get(4).toString()));
		relatorioGerencial.setNomeMunicipio(colunas.get(5).toString());
		relatorioGerencial.setCodigoLocalidade(Integer.parseInt(colunas.get(6).toString()));
		relatorioGerencial.setNomeLocalidade(colunas.get(7).toString());
		relatorioGerencial.setCodigoUnidadeOperacional(Integer.parseInt(colunas.get(8).toString()));
		relatorioGerencial.setNomeUnidadeOperacional(colunas.get(9).toString());
		
		formataData = new SimpleDateFormat("yyyy-MM-dd");
		Date datConsumo = formataData.parse(colunas.get(10).toString());
		relatorioGerencial.setDataConsumo(datConsumo);

		Double qtdConsumo = Double.parseDouble(colunas.get(11).toString());
		Double valorUnitario;
		if (colunas.get(13) == null){
			valorUnitario = 0.0d;
		}else{
			valorUnitario = Double.parseDouble(colunas.get(13).toString());
		}
		relatorioGerencial.setQtdConsumo(qtdConsumo);
		relatorioGerencial.setValorUnitario(valorUnitario) ;

		if (tipoRelatorio == 1){//FÍSICO
			relatorioGerencial.setDescricaoProduto(colunas.get(12).toString() + " - " + colunas.get(15).toString());
		}
		else{//FINANCEIRO
			relatorioGerencial.setDescricaoProduto(colunas.get(12).toString() + " - R$");
		}
		relatorioGerencial.setTipoUnidadeOperacional(Integer.parseInt(colunas.get(14).toString()));
		relatorioGerencial.setValorTotal(qtdConsumo * valorUnitario);
		
		lista.add(relatorioGerencial);
	}
	return lista;
}
/*
public List<Consumo> getUnidadeOperacionalUsuario(UsuarioProxy usuario) throws Exception {

	String localidade = "";
	String query = "SELECT A.* FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
			" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade, J.sabs_id, J.sabs_dssistemaabastecimento " +
			" FROM cadastro.gerencia_regional A " +
			"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
			"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
			"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
			"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id " +
			"INNER JOIN cadastro.quadra F ON D.stcm_id = F.stcm_id " +
			"INNER JOIN cadastro.quadra_face G ON F.qdra_id = G.qdra_id " +
			"INNER JOIN operacional.distrito_operacional H ON G.diop_id = H.diop_id " +
			"INNER JOIN operacional.setor_abastecimento I ON H.stab_id = I.stab_id " +
			"INNER JOIN operacional.sistema_abastecimento J ON I.sabs_id = J.sabs_id) AS A "; 

	//Se Perfil de Gerente, acesso a gerência Regional
	if (usuario.getPerfil() == PerfilBeanEnum.GERENTE){
		query = query + " WHERE A.greg_id = " + usuario.getRegionalProxy().getCodigo();
	}
	else if (usuario.getPerfil() == PerfilBeanEnum.SUPERVISOR || usuario.getPerfil() == PerfilBeanEnum.COORDENADOR){
		for (LocalidadeProxy colunas : usuario.getLocalidadeProxy()) {
			localidade = localidade + colunas.getCodigo() + ",";
		}
		localidade = localidade.substring(0, localidade.length() - 1);
		query = query + " WHERE A.loca_id IN (" + localidade + ")";		
	}
	
	List<List> valores = fachadaProxy.selectRegistros(query);
	List<Consumo> lista = new ArrayList<Consumo>();
	
	if (valores == null) {
		return lista;
	}

	Integer intI = 1;	
	for (List colunas : valores) {
		Consumo consumo = new Consumo();
		consumo.setCodigo(intI++);
		consumo.setRegionalProxy(new RegionalProxy(Integer.parseInt(colunas.get(0).toString()),colunas.get(1).toString()));
		consumo.setUnidadeNegocioProxy(new UnidadeNegocioProxy(Integer.parseInt(colunas.get(2).toString()),colunas.get(3).toString()));
		consumo.setMunicipioProxy(new MunicipioProxy(Integer.parseInt(colunas.get(4).toString()),colunas.get(5).toString()));
		consumo.setLocalidadeProxy(new LocalidadeProxy(Integer.parseInt(colunas.get(6).toString()),colunas.get(7).toString()));
		consumo.setUnidadeOperacionalProxy(new UnidadeOperacionalProxy(Integer.parseInt(colunas.get(8).toString()),colunas.get(9).toString()));
		lista.add(consumo);
	}
	
	return lista;
}
*/
}
