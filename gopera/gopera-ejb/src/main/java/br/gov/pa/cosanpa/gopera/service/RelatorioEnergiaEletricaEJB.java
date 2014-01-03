package br.gov.pa.cosanpa.gopera.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.pa.cosanpa.gopera.fachada.IEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.fachada.IEnergiaEletricaDados;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.fachada.IRelatorioEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.EnergiaAnalise;
import br.gov.pa.cosanpa.gopera.model.EnergiaAnaliseDados;
import br.gov.pa.cosanpa.gopera.model.EnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.EnergiaEletricaDados;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.RelatorioEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@Stateless
@SuppressWarnings({ "rawtypes"})
public class RelatorioEnergiaEletricaEJB implements IRelatorioEnergiaEletrica {

	@EJB
	IProxy fachadaProxy;
	@EJB
	IEnergiaEletricaDados fachadaEnergiaEletricaDados;
	@EJB
	IEnergiaEletrica fachadaEnergiaEletrica;	
	@PersistenceContext
	private EntityManager entity;

	@Override	
	public List<RelatorioEnergiaEletrica> getEnergiaEletricaPeriodo(Date referenciaInicial, 
															   Date referenciaFinal,
															   Integer codigoRegional,
															   Integer codigoUnidadeNegocio,
															   Integer codigoMunicipio,
															   Integer codigoLocalidade) throws Exception {
		
		SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");
		String refIni = formataData.format(referenciaInicial);
		String refFim = formataData.format(referenciaFinal);
		
		String query = "";
		
		query = "SELECT to_char(B.enel_referencia, 'MM/yyyy'), A.enld_uc, C.ucon_nmconsumidora," +
				"       (A.enld_c_kwh_cv + A.enld_c_kwh_fs + A.enld_c_kwh_fu + A.enld_c_kwh_ps + A.enld_c_kwh_pu) as consumo," +
				" 	    (A.enld_vlr_DRe_Cv + A.enld_vlr_DRe_Pt + A.enld_vlr_DRe_FP + A.enld_vlr_ERe_FP + A.enld_vlr_ERe_Cv + A.enld_vlr_ERe_Pt) as totalfatorpotencia," +
				"		A.enld_vlr_TotalP as valorTotal," +				
				" 		(enld_Dem_Ut_Cv + enld_Dem_Ut_FP + enld_Dem_Ut_Pt) as ultrapassagem_kwh,"+
				" 		(enld_vlr_Ult_Pt + enld_vlr_Ult_FP + enld_vlr_Ult_Cv) as ultrapassagem_valor"+
				"  FROM operacao.energiaeletrica_dados A" +
				" INNER JOIN operacao.energiaeletrica B ON A.enel_id = B.enel_id" +
				"  LEFT JOIN operacao.unidade_consumidora C ON A.ucon_id = C.ucon_id" +
				" WHERE B.enel_referencia BETWEEN '" + refIni + "' AND '" + refFim + "'";

		query += (codigoRegional != -1 ? " AND C.greg_id = " + codigoRegional : "");
		query += (codigoUnidadeNegocio != -1 ? " AND C.uneg_id = " + codigoUnidadeNegocio : "");
		query += (codigoMunicipio != -1 ? " AND C.muni_id = " + codigoMunicipio : "");
		query += (codigoLocalidade != -1 ? " AND C.loca_id = " + codigoLocalidade : "");
		query += " ORDER BY A.enld_uc, B.enel_referencia";
		

	    List<List> valores = fachadaProxy.selectRegistros(query);
		List<RelatorioEnergiaEletrica> lista = new ArrayList<RelatorioEnergiaEletrica>();
		
		if (valores == null) {
			return lista;
		}
		
		for (List colunas : valores) {
			RelatorioEnergiaEletrica relatorioAnalise = new RelatorioEnergiaEletrica();
			relatorioAnalise.setReferencia(colunas.get(0).toString());
			relatorioAnalise.setCodigoUC(Integer.parseInt(colunas.get(1).toString()));
			if (colunas.get(2) != null) { //NOME UC
				relatorioAnalise.setNomeUC(colunas.get(2).toString());
			}
			relatorioAnalise.setConsumoKwh(Double.parseDouble(colunas.get(3).toString()));
			relatorioAnalise.setAjusteFatorPotencia(Double.parseDouble(colunas.get(4).toString()));
			relatorioAnalise.setTotal(Double.parseDouble(colunas.get(5).toString()));			
			relatorioAnalise.setUltrapassagemKwh(Double.parseDouble(colunas.get(6).toString()));
			relatorioAnalise.setUltrapassagemR$(Double.parseDouble(colunas.get(7).toString()));
			lista.add(relatorioAnalise);
		}
		return lista;
	}	
	
	@Override	
	public List<RelatorioEnergiaEletrica> getEnergiaEletricaUC(Date dataReferencia, 
															   Integer tipoRelatorio, 
															   Integer codigoRegional,
															   Integer codigoUnidadeNegocio,
															   Integer codigoMunicipio,
															   Integer codigoLocalidade) throws Exception {
		
		SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");
		String dataAux = formataData.format(dataReferencia);
		
		String query = "";
		
		if (tipoRelatorio == 0) {
			query = "SELECT A.enld_uc, C.ucon_nmconsumidora, A.enld_fatura, A.enld_vlr_TotalP as valorTotal," +
					"       (A.enld_c_kwh_cv + A.enld_c_kwh_fs + A.enld_c_kwh_fu + A.enld_c_kwh_ps + A.enld_c_kwh_pu) as consumo," +
					" 	    (A.enld_vlr_DRe_Cv + A.enld_vlr_DRe_Pt + A.enld_vlr_DRe_FP + A.enld_vlr_ERe_FP + A.enld_vlr_ERe_Cv + A.enld_vlr_ERe_Pt) as totalfatorpotencia" +
					"  FROM operacao.energiaeletrica_dados A" +
					" INNER JOIN operacao.energiaeletrica B ON A.enel_id = B.enel_id" +
					"  LEFT JOIN operacao.unidade_consumidora C ON A.ucon_id = C.ucon_id" +
					" WHERE B.enel_referencia = '" + dataAux + "'";

			query += (codigoRegional != -1 ? " AND C.greg_id = " + codigoRegional : "");
			query += (codigoUnidadeNegocio != -1 ? " AND C.uneg_id = " + codigoUnidadeNegocio : "");
			query += (codigoMunicipio != -1 ? " AND C.muni_id = " + codigoMunicipio : "");
			query += (codigoLocalidade != -1 ? " AND C.loca_id = " + codigoLocalidade : "");
			query += " ORDER BY A.enld_uc";
		}
		else if (tipoRelatorio == 1) { //UC´s NÃO CADASTRADAS
			query = "SELECT A.enld_uc, A.enld_nome, A.enld_fatura, A.enld_vlr_TotalP, A.enld_c_kwh_cv" +
					   "  FROM operacao.energiaeletrica_dados A" +
					   " INNER JOIN operacao.energiaeletrica B ON A.enel_id = B.enel_id" +
					   " WHERE B.enel_referencia = '" + dataAux + "'" + 
					   " AND A.enld_uc NOT IN (SELECT ucon_uc from operacao.unidade_consumidora)";
			query += " ORDER BY A.enld_uc";
		}
		else if (tipoRelatorio == 2) { //UC´s NÃO FATURADAS
			query = "SELECT ucon_uc, ucon_nmconsumidora FROM operacao.unidade_consumidora" +
						" WHERE ucon_ativo = true AND ucon_uc not in (SELECT A.enld_uc " +
						" FROM operacao.energiaeletrica_dados A " +
						" INNER JOIN operacao.energiaeletrica B ON A.enel_id = B.enel_id " +
						" WHERE B.enel_referencia = '" + dataAux + "')";
		
			query += " ORDER BY ucon_uc";
		}
		else if (tipoRelatorio == 3) {
			query = "SELECT A.enld_dataleitura, A.enld_uc, A.enld_fatura, A.enld_nome," +
						" A.enld_endereco, A.enld_bairro, A.enld_cep, A.enld_codgrupo," +
						" A.enld_codtipo, A.enld_c_kwh_cv, A.enld_c_kwh_fs, A.enld_c_kwh_fu," +
						" A.enld_c_kwh_ps, A.enld_c_kwh_pu, A.enld_dcv, A.enld_dfs, A.enld_dfu," +
						" A.enld_dps, A.enld_dpu, A.enld_dem_fat_cv, A.enld_dem_fat_fp," +
						" A.enld_dem_fat_pt, A.enld_dem_med_cv, A.enld_dem_med_fp, A.enld_dem_med_pt," +
						" A.enld_dem_ut_cv, A.enld_dem_ut_fp, A.enld_dem_ut_pt, A.enld_fcarga," +
						" A.enld_fpot_fpa, A.enld_fpot_cv, A.enld_fpot_pt, A.enld_vlr_ult_fp," +
						" A.enld_vlr_ult_pt, A.enld_vlr_dem_cv, A.enld_vlr_dem_fp, A.enld_vlr_dem_pt," +
						" A.enld_vlr_ult_cv, A.enld_vlr_multas, A.enld_vlr_kwh_fs, A.enld_vlr_kwh_fu," +
						" A.enld_vlr_kwh_cv, A.enld_vlr_kwh_ps, A.enld_vlr_kwh_pu, A.enld_vlr_icms," +
						" A.enld_vlr_dre_cv, A.enld_vlr_dre_pt, A.enld_vlr_dre_fp, A.enld_vlr_ere_fp," +
						" A.enld_vlr_ere_cv, A.enld_vlr_ere_pt, A.enld_vlr_TotalP" +
						" FROM operacao.energiaeletrica_dados A" +
						" INNER JOIN operacao.energiaeletrica B ON A.enel_id = B.enel_id" +
						" WHERE B.enel_referencia = '" + dataAux + "'" +
						" ORDER BY A.enld_uc";
		}
		
		List<List> valores = fachadaProxy.selectRegistros(query);
		List<RelatorioEnergiaEletrica> lista = new ArrayList<RelatorioEnergiaEletrica>();
		
		if (valores == null) {
			return lista;
		}
		
		if (tipoRelatorio == 0) {
			for (List colunas : valores) {
				RelatorioEnergiaEletrica relatorioGerencial = new RelatorioEnergiaEletrica();
				relatorioGerencial.setDataReferencia(dataReferencia);
				relatorioGerencial.setCodigoUC(Integer.parseInt(colunas.get(0).toString()));
				if (colunas.get(1) != null) relatorioGerencial.setNomeUC(colunas.get(1).toString());
				relatorioGerencial.setFatura(colunas.get(2).toString());
				Double vlrAux = Double.parseDouble(colunas.get(3).toString());
				relatorioGerencial.setValorTotal(vlrAux);
				Double consumoKwh = Double.parseDouble(colunas.get(4).toString());
				relatorioGerencial.setConsumoKwh(consumoKwh);			
				Double ajusteFatorPotencia = Double.parseDouble(colunas.get(5).toString());
				relatorioGerencial.setAjusteFatorPotencia(ajusteFatorPotencia);
				lista.add(relatorioGerencial);
			}
		}
		else if (tipoRelatorio == 1) {
			for (List colunas : valores) {
				RelatorioEnergiaEletrica relatorioGerencial = new RelatorioEnergiaEletrica();
				relatorioGerencial.setDataReferencia(dataReferencia);
				relatorioGerencial.setCodigoUC(Integer.parseInt(colunas.get(0).toString()));
				relatorioGerencial.setNomeUC(colunas.get(1).toString());
				relatorioGerencial.setFatura(colunas.get(2).toString());
		
				Double vlrAux = Double.parseDouble(colunas.get(3).toString());
				relatorioGerencial.setValorTotal(vlrAux);
				lista.add(relatorioGerencial);
			}
		}
		
		else if (tipoRelatorio == 2) {
			for (List colunas : valores) {
				RelatorioEnergiaEletrica relatorioGerencial = new RelatorioEnergiaEletrica();
				relatorioGerencial.setDataReferencia(dataReferencia);
				relatorioGerencial.setCodigoUC(Integer.parseInt(colunas.get(0).toString()));
				relatorioGerencial.setNomeUC(colunas.get(1).toString());
				lista.add(relatorioGerencial);
			}
		}
		
		return lista;
	}
	
	public List<EnergiaEletricaDados> getEnergiaEletricaDados(Date dataReferencia) throws Exception {
		try{
			List<EnergiaEletricaDados> energiaEletricaDados = new ArrayList<EnergiaEletricaDados>();
			EnergiaEletrica energiaEletrica = fachadaEnergiaEletrica.obterEnergiaPorData(dataReferencia);
			if (energiaEletrica != null){
				energiaEletricaDados = energiaEletrica.getDados(); 
				energiaEletricaDados  = fachadaEnergiaEletricaDados.CalculaDados(energiaEletricaDados);
			}	
			return energiaEletricaDados;
		}
		catch (Exception e) {
			throw new Exception("Nenhuma Referência Encontrada");
		}
	}
	
	public List<RelatorioEnergiaEletrica> getEnergiaEletricaAnalise(Date dataReferenciaInicial, Date dataReferenciaFinal, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception {
		SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");
		String dataAux1 = formataData.format(dataReferenciaInicial);
		String dataAux2 = formataData.format(dataReferenciaFinal);
		String query;
		
		query = "SELECT " +
					" to_char(A.enel_referencia, 'MM/yyyy')" +
					" ,A.enel_referencia " +
					" ,B.enld_uc" +
					" ,C.ucon_nmconsumidora" +
					" ,(B.enld_c_kwh_cv + B.enld_c_kwh_fs + B.enld_c_kwh_fu + B.enld_c_kwh_ps + B.enld_c_kwh_pu) as consumo" +
					" ,B.enld_dataleitura" +
					" ,(B.enld_vlr_DRe_Cv + B.enld_vlr_DRe_Pt + B.enld_vlr_DRe_FP + B.enld_vlr_ERe_FP + B.enld_vlr_ERe_Cv + B.enld_vlr_ERe_Pt) as TotalAjusteFatorPotencia" +
					" ,(enld_Dem_Ut_Cv + enld_Dem_Ut_FP + enld_Dem_Ut_Pt) as ultrapassagem_kwh"+
					" ,(enld_vlr_Ult_Pt + enld_vlr_Ult_FP + enld_vlr_Ult_Cv) as ultrapassagem_valor"+
					" ,B.enld_vlr_TotalP" +
					" ,D.muni_nmmunicipio" +
					" ,E.loca_nmlocalidade" +
					" FROM operacao.energiaeletrica A" +
					" INNER JOIN operacao.energiaeletrica_dados B ON B.enel_id = A.enel_id" +
					" INNER JOIN operacao.unidade_consumidora C ON C.ucon_uc = B.enld_uc" +
					" INNER JOIN cadastro.municipio D ON C.muni_id = D.muni_id" +
					" INNER JOIN cadastro.localidade E ON E.loca_id = C.loca_id" +
					" WHERE A.enel_referencia BETWEEN '" + dataAux1 + "' AND '" + dataAux2 + "'" +
					" AND D.muni_id = " + codigoMunicipio;
					
					if(codigoLocalidade != -1) {
						query += " AND E.loca_id = " + codigoLocalidade;
					}
	    
	    List<List> valores = fachadaProxy.selectRegistros(query);
		List<RelatorioEnergiaEletrica> lista = new ArrayList<RelatorioEnergiaEletrica>();
		
		if (valores == null) {
			return lista;
		}
		
		for (List colunas : valores) {
			RelatorioEnergiaEletrica relatorioAnalise = new RelatorioEnergiaEletrica();
			relatorioAnalise.setReferencia(colunas.get(0).toString());
			relatorioAnalise.setCodigoUC(Integer.parseInt(colunas.get(1).toString()));
			relatorioAnalise.setNomeUC(colunas.get(2).toString());
			relatorioAnalise.setConsumo(Double.parseDouble(colunas.get(3).toString()));
			relatorioAnalise.setAjusteFatorPotencia(Double.parseDouble(colunas.get(5).toString()));
			relatorioAnalise.setTotal(Double.parseDouble(colunas.get(6).toString()));
			relatorioAnalise.setMunicipio(colunas.get(7).toString());
			relatorioAnalise.setLocalidade(colunas.get(8).toString());
			lista.add(relatorioAnalise);
		}
		
		return lista; 
	}
	
	@Override
	public String queryEnergiaEletricaAnalise(Date dataReferenciaInicial, Date dataReferenciaFinal, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception {
		SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");
		String dataAux1 = formataData.format(dataReferenciaInicial);
		String dataAux2 = formataData.format(dataReferenciaFinal);
		String query;
		
		query = "SELECT " +
				" to_char(A.enel_referencia, 'MM/yyyy') as mes" +
				" ,A.enel_referencia " +
				" ,B.enld_uc" +
				" ,C.ucon_nmconsumidora" +
				" ,(B.enld_c_kwh_cv + B.enld_c_kwh_fs + B.enld_c_kwh_fu + B.enld_c_kwh_ps + B.enld_c_kwh_pu) as consumo " +
				" ,(B.enld_vlr_DRe_Cv + B.enld_vlr_DRe_Pt + B.enld_vlr_DRe_FP + B.enld_vlr_ERe_FP + B.enld_vlr_ERe_Cv + B.enld_vlr_ERe_Pt) as totalfatorpotencia" +
				" ,B.enld_vlr_TotalP as valortotal" +
				" ,(enld_Dem_Ut_Cv + enld_Dem_Ut_FP + enld_Dem_Ut_Pt) as ultrapassagem_kwh"+
				" ,(enld_vlr_Ult_Pt + enld_vlr_Ult_FP + enld_vlr_Ult_Cv) as ultrapassagem_valor"+
				" ,DATE_PART('day',B.enld_dataleitura::timestamp - COALESCE ((select enld_dataleitura from operacao.energiaeletrica_dados where enld_uc = B.enld_uc and enld_dataleitura < B.enld_dataleitura order by enld_dataleitura DESC LIMIT 1), B.enld_dataleitura - INTERVAL '30 DAYS')::timestamp) as diasfaturados " +
				" ,G.greg_id, G.greg_nmregional" +
				" ,F.uneg_id, F.uneg_nmunidadenegocio" +
				" ,D.muni_id, D.muni_nmmunicipio" +
				" ,E.loca_id, E.loca_nmlocalidade" +
				" FROM operacao.energiaeletrica A" +
				" INNER JOIN operacao.energiaeletrica_dados B ON B.enel_id = A.enel_id" +
				" INNER JOIN operacao.unidade_consumidora C ON C.ucon_uc = B.enld_uc" +
				" INNER JOIN cadastro.municipio D ON C.muni_id = D.muni_id" +
				" INNER JOIN cadastro.localidade E ON E.loca_id = C.loca_id" +
				" INNER JOIN cadastro.unidade_negocio F ON F.uneg_id = C.uneg_id" +
				" INNER JOIN cadastro.gerencia_regional G ON G.greg_id = C.greg_id" +
				" WHERE A.enel_referencia BETWEEN '" + dataAux1 + "' AND '" + dataAux2 + "'";
		
		if(codigoRegional != -1) query += " AND  C.greg_id = " + codigoRegional;
		if(codigoUnidadeNegocio != -1) query += " AND C.uneg_id = " + codigoUnidadeNegocio;
		if(codigoMunicipio != -1) query += " AND C.muni_id = " + codigoMunicipio;
		if(codigoLocalidade != -1) query += " AND C.loca_id = " + codigoLocalidade;
		
		query+= " ORDER BY D.muni_nmmunicipio, E.loca_nmlocalidade";
		return query;
	}

	@SuppressWarnings("static-access")
	@Override
	public List<EnergiaAnalise> getRelatorioAnalise(Date dataInicial, Date dataFinal, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, List<String> dados) throws Exception {
		SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");
		String dataIni = formataData.format(dataInicial);
		String dataFim = formataData.format(dataFinal);
		String codigoDado, nomeDado = "";
		Date dataRef;
		Calendar ref1 = Calendar.getInstance();  
		Calendar ref2 = Calendar.getInstance();
		Double valorMes;
		
		String query = "SELECT DISTINCT " +
				"  G.greg_id, G.greg_nmregional" +
				" ,F.uneg_id, F.uneg_nmunidadenegocio" +
				" ,D.muni_id, D.muni_nmmunicipio" +
				" ,E.loca_id, E.loca_nmlocalidade" +
				" ,B.enld_uc" +
				" ,C.ucon_nmconsumidora" +
				" FROM operacao.energiaeletrica A" +
				" INNER JOIN operacao.energiaeletrica_dados B ON B.enel_id = A.enel_id" +
				" INNER JOIN operacao.unidade_consumidora C ON C.ucon_uc = B.enld_uc" +
				" INNER JOIN cadastro.municipio D ON C.muni_id = D.muni_id" +
				" INNER JOIN cadastro.localidade E ON E.loca_id = C.loca_id" +
				" INNER JOIN cadastro.unidade_negocio F ON F.uneg_id = C.uneg_id" +
				" INNER JOIN cadastro.gerencia_regional G ON G.greg_id = C.greg_id" +
				" WHERE A.enel_referencia BETWEEN '" + dataIni + "' AND '" + dataFim + "'"; 
				
		if(codigoRegional != -1) query += " AND  C.greg_id = " + codigoRegional;
		if(codigoUnidadeNegocio != -1) query += " AND C.uneg_id = " + codigoUnidadeNegocio;
		if(codigoMunicipio != -1) query += " AND C.muni_id = " + codigoMunicipio;
		if(codigoLocalidade != -1) query += " AND C.loca_id = " + codigoLocalidade;
		
		query += " ORDER BY G.greg_id, F.uneg_id, D.muni_id, E.loca_id, B.enld_uc";
    
	    List<List> valores = fachadaProxy.selectRegistros(query);
		List<EnergiaAnalise> lista = new ArrayList<EnergiaAnalise>();
		
		if (valores == null) {
			return null;
		}
		
		//CARREGANDO LISTA DE UNIDADES CONSUMIDORAS
		for (List colunas : valores) {
			EnergiaAnalise energiaAnalise = new EnergiaAnalise();
			energiaAnalise.setRegional(new RegionalProxy(Integer.parseInt(colunas.get(0).toString()), colunas.get(1).toString()));
			energiaAnalise.setUnidadeNegocio(new UnidadeNegocioProxy(Integer.parseInt(colunas.get(2).toString()), colunas.get(3).toString()));
			energiaAnalise.setMunicipio(new MunicipioProxy(Integer.parseInt(colunas.get(4).toString()), colunas.get(5).toString()));
			energiaAnalise.setLocalidade(new LocalidadeProxy(Integer.parseInt(colunas.get(6).toString()), colunas.get(7).toString()));
			energiaAnalise.setCodigoUC(Integer.parseInt(colunas.get(8).toString()));
			energiaAnalise.setNomeUC(colunas.get(9).toString());
			List<EnergiaAnaliseDados> meses = new ArrayList<EnergiaAnaliseDados>();

			for (int j = 0; j < dados.size(); j++) {
				codigoDado = dados.get(j);
				valorMes = 0.0;
				if (codigoDado.equals("1")) nomeDado = "Consumo";
				else if (codigoDado.equals("2")) nomeDado = "Ajuste Fator Potência";
				else if (codigoDado.equals("3")) nomeDado = "Total R$";
				
				energiaAnalise.setCodigoDado(Integer.parseInt(codigoDado));
				energiaAnalise.setNomeDado(nomeDado);
				
				ref1.setTime(dataInicial);   
				ref2.setTime(dataFinal);
				Integer intervalo = (ref2.get(ref2.MONTH) - ref1.get(ref1.MONTH)) + 1;  
			    for (Integer intI = 0; intI < intervalo; intI++) {  
					EnergiaAnaliseDados energiaAnaliseDados = new EnergiaAnaliseDados();
					dataRef = ref1.getTime();
					String strDataRef = formataData.format(ref1.getTime());
					
					TypedQuery<EnergiaEletricaDados> queryAux = entity.createQuery("select c2 from EnergiaEletrica c1 inner join c1.dados c2 where c1.dataReferencia = '" + strDataRef + "' AND c2.uc = '" + energiaAnalise.getCodigoUC() + "'",EnergiaEletricaDados.class);
					EnergiaEletricaDados energiaEletricaDados = queryAux.getSingleResult();
					
					if (codigoDado.equals("1")) valorMes = energiaEletricaDados.getC_Kwh_Cv();
					else if (codigoDado.equals("2")) valorMes = energiaEletricaDados.getC_Kwh_Cv();
					else if (codigoDado.equals("3")) valorMes = energiaEletricaDados.getC_Kwh_Cv();
					//ADICIONANDO DADOS DO MÊS
					energiaAnaliseDados.setMesReferencia(dataRef);
					energiaAnaliseDados.setValor(valorMes);
					meses.add(energiaAnaliseDados);					
					//INCREMENTANDO DATA
					ref1.add(ref1.MONTH, 1);
					}
				}	
				energiaAnalise.setMeses(meses);
				lista.add(energiaAnalise);
		}
		
		return null;
	}
	
	
	@Override
	public List<List> getMesesPeriodo(String query) throws Exception {
		List<List> listameses;
		listameses = fachadaProxy.selectRegistros("SELECT DISTINCT X.mes, X.enel_referencia FROM (" + query + ") AS X ORDER BY X.enel_referencia");
		return listameses;
	}
	
	@Override
	public List<List> getUCs(String query) throws Exception {
		List<List> listameses;
		listameses = fachadaProxy.selectRegistros("SELECT DISTINCT X.enld_uc, X.loca_nmlocalidade, X.muni_nmmunicipio FROM (" + query + ") AS X ORDER BY X.muni_nmmunicipio, X.loca_nmlocalidade, X.enld_uc");
		return listameses;
	}
	
	@Override
	public List<List> getDados(String query, String mes, String uc) throws Exception {
		List<List> listameses;
//		listameses = fachadaProxy.selectRegistros("SELECT DISTINCT X.consumo/diasfaturados*30 as consumoKwh ,X.totalfatorpotencia ,X.valortotal/diasfaturados*30 as valortotal FROM (" + query + ") AS X " + 
		listameses = fachadaProxy.selectRegistros("SELECT DISTINCT X.consumo,  X.ultrapassagem_kwh, X.ultrapassagem_valor, X.totalfatorpotencia, X.valortotal FROM (" + query + ") AS X " +
		" WHERE X.mes = '" + mes + "' AND X.enld_uc = " + uc);
		return listameses;
	}

	@Override
	public List<List> getCountLocalidade(String query, String municipio) throws Exception {
		String consulta = "SELECT COUNT(*) as count from (" + query + ") AS X " +
						  " WHERE X.muni_nmmunicipio = '" + municipio + "'";
		List<List> listameses;
		listameses = fachadaProxy.selectRegistros(consulta);
		return listameses;
	}
	
	@Override
	public List<List> getCountUcs(String query, String localidade) throws Exception {
		String consulta = "SELECT distinct COUNT(X.enld_uc) as count from (" + query + ") AS X " + 
				" WHERE X.loca_nmlocalidade = '" + localidade + "'";
		List<List> listameses;
		listameses = fachadaProxy.selectRegistros(consulta);
		return listameses;
	}
}
