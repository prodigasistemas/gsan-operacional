package br.gov.pa.cosanpa.gopera.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.fachada.IRelatorioProducao;

@Stateless
@SuppressWarnings({ "rawtypes"})
public class RelatorioProducaoEJB implements IRelatorioProducao {

	@EJB
	IProxy fachadaProxy;
	
	@Override
	public List<List> getProducaoDistribuicaoMensal(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer tipoUnidadeOperacional, Integer codigoUnidadeOperacional, Date referencia ) throws Exception {
		
		SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");
		String dataRef = formataData.format(referencia);
		
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM (")
		.append("	SELECT J.greg_id, J.greg_nmregional, A.uneg_id, A.uneg_nmunidadenegocio, L.loca_id, L.loca_nmlocalidade, D.unop_tipo, D.unop_id, D.unop_nmunidadeoperacional, K.referencia,")
		.append("	       K.unop_dtmedicao, H.unop_pressao, G.unop_vazao, I.unop_horas,")
		.append("	       (CASE WHEN K.unop_volume = 0 THEN I.unop_horas * G.unop_vazao ELSE K.unop_volume END) AS unop_volume")
		.append("	  FROM cadastro.unidade_negocio A")
		.append("	 INNER JOIN operacao.unidade_consumidora B ON A.uneg_id = B.uneg_id")
		.append("	 INNER JOIN operacao.unidade_consumidora_operacional C ON B.ucon_id = C.ucon_id")
		.append("	 INNER JOIN cadastro.localidade L ON B.loca_id = L.loca_id")
		.append("	 INNER JOIN (SELECT eeab_id AS unop_id, eeab_nome AS unop_nmunidadeoperacional, 1 AS unop_tipo FROM operacao.eeab")
		.append("		      UNION ALL")
		.append("		     SELECT eta_id AS unop_id, eta_nome AS unop_nmunidadeoperacional, 2 AS unop_tipo FROM operacao.eta")
		.append("	              UNION ALL")
		.append("	             SELECT eeat_id AS unop_id, eeat_nome AS unop_nmunidadeoperacional, 3 AS unop_tipo FROM operacao.eeat")
		.append("		      UNION ALL")
		.append("		     SELECT rso_id AS unop_id, rso_nome AS unop_nmunidadeoperacional, 4 AS unop_tipo FROM operacao.rso) D ON C.ucop_idoperacional = D.unop_id AND C.ucop_tipooperacional = D.unop_tipo")
		.append("	 INNER JOIN (SELECT A.eabv_referencia AS referencia, A.eeab_id AS unop_id, A.eabv_tmmedicao AS unop_dtmedicao, A.eabv_volume AS unop_volume, 1 AS unop_tipo")
		.append("		       FROM operacao.eeab_volume A")
		.append("	              UNION ALL ")
		.append("		     SELECT A.etav_referencia, A.eta_id, A.etav_tmmedicao, A.etav_volume, 2 AS unop_tipo")
		.append("		       FROM operacao.eta_volume A")
		.append("		      UNION ALL")
		.append("		     SELECT A.eatv_referencia, A.eeat_id, A.eatv_tmmedicao, A.eatv_volume, 3 AS unop_tipo")
		.append("		       FROM operacao.eeat_volume A) K ON D.unop_id = K.unop_id AND D.unop_tipo = K.unop_tipo")	     
		.append("	  LEFT JOIN (SELECT A.eabv_referencia AS referencia, A.eeab_id AS unop_id, B.eabs_volume AS unop_vazao, 1 AS unop_tipo")
		.append("		       FROM operacao.eeab_volume A")
		.append("		      INNER JOIN operacao.eeab_volume_saida B ON A.eabv_id = B.eabv_id") 
		.append("		      WHERE B.mmed_tipomedicao = 1 ")
		.append("	              UNION ALL ")
		.append("		     SELECT A.etav_referencia, A.eta_id, B.etas_volume, 2 AS unop_tipo")
		.append("		       FROM operacao.eta_volume A")
		.append("		      INNER JOIN operacao.eta_volume_saida B ON A.etav_id = B.etav_id")
		.append("		      WHERE B.mmed_tipomedicao = 1")
		.append("		      UNION ALL")
		.append("		     SELECT A.eatv_referencia, A.eeat_id, B.eats_volume, 3 AS unop_tipo")
		.append("		       FROM operacao.eeat_volume A")
		.append("		      INNER JOIN operacao.eeat_volume_saida B ON A.eatv_id = B.eatv_id")
		.append("		      WHERE B.mmed_tipomedicao = 1) G ON D.unop_id = G.unop_id AND D.unop_tipo = G.unop_tipo AND G.referencia = K.referencia")
		.append("	  LEFT JOIN (SELECT A.eabv_referencia AS referencia, A.eeab_id AS unop_id, B.eabs_volume AS unop_pressao, 1 AS unop_tipo")
		.append("		       FROM operacao.eeab_volume A")
		.append("		      INNER JOIN operacao.eeab_volume_saida B ON A.eabv_id = B.eabv_id") 
		.append("		      WHERE B.mmed_tipomedicao = 2 ")
		.append("	              UNION ALL ")
		.append("		     SELECT A.etav_referencia, A.eta_id, B.etas_volume, 2 AS unop_tipo")
		.append("		       FROM operacao.eta_volume A")
		.append("		      INNER JOIN operacao.eta_volume_saida B ON A.etav_id = B.etav_id")
		.append("		      WHERE B.mmed_tipomedicao = 2")
		.append("		      UNION ALL")
		.append("		     SELECT A.eatv_referencia, A.eeat_id, B.eats_volume, 3 AS unop_tipo")
		.append("		       FROM operacao.eeat_volume A")
		.append("		      INNER JOIN operacao.eeat_volume_saida B ON A.eatv_id = B.eatv_id")
		.append("		      WHERE B.mmed_tipomedicao = 2) H ON D.unop_id = H.unop_id AND D.unop_tipo = H.unop_tipo AND H.referencia = K.referencia")
		.append("	  LEFT JOIN (   SELECT unop_id, unop_tipo, referencia, SUM(total_trabalhado) AS unop_horas")
		.append("			  FROM (")
		.append("			SELECT 1 AS unop_tipo, B.eeab_id AS unop_id, A.eabc_qtdcmb * A.eabc_horacmb AS total_trabalhado, B.eabh_referencia AS referencia FROM operacao.eeab_horas_cmb A INNER JOIN operacao.eeab_horas b ON A.eebh_id = B.eabh_id")
		.append("			UNION ALL		")
		.append("			SELECT 2 AS unop_tipo, B.eta_id AS unop_id, A.etac_qtdcmb * A.etac_horacmb AS total_trabalhado, B.etah_referencia AS referencia FROM operacao.eta_horas_cmb A INNER JOIN operacao.eta_horas b ON A.etah_id = B.etah_id")
		.append("			UNION ALL")
		.append("			SELECT 3 AS unop_tipo, B.eeat_id AS unop_id, A.eatc_qtdcmb * A.eatc_horacmb AS total_trabalhado, B.eath_referencia AS referencia FROM operacao.eeat_horas_cmb A INNER JOIN operacao.eeat_horas b ON A.eath_id = B.eath_id")
		.append("			UNION ALL")
		.append("			SELECT 4 AS unop_tipo, B.rso_id AS unop_id, A.rsoc_qtdcmb * A.rsoc_horacmb AS total_trabalhado, B.rsoh_referencia AS referencia FROM operacao.rso_horas_cmb A INNER JOIN operacao.rso_horas b ON A.rsoh_id = B.rsoh_id) A")
		.append("			GROUP BY unop_id, unop_tipo, referencia) I ON D.unop_id = I.unop_id AND D.unop_tipo = I.unop_tipo AND I.referencia = K.referencia")
		.append("	  LEFT JOIN cadastro.gerencia_regional J ON A.greg_id = J.greg_id) A")
		.append("	 WHERE referencia = '" + dataRef + "'");

		//FILTROS	
		if (codigoRegional != -1){
			builder.append("  AND greg_id = " + codigoRegional);
		}
		if (codigoUnidadeNegocio != -1){
			builder.append("  AND uneg_id = " + codigoUnidadeNegocio);
		}
		if (codigoUnidadeOperacional != -1){
			builder.append("  AND unop_id = " + codigoUnidadeOperacional);
		}
		if (tipoUnidadeOperacional != -1){
			builder.append("  AND unop_tipo = " + tipoUnidadeOperacional);
		}
		
		builder.append(" ORDER BY greg_nmregional, uneg_nmunidadenegocio, loca_nmlocalidade, unop_nmunidadeoperacional"); 
		
		List<List> valores = fachadaProxy.selectRegistros(builder.toString());
		return valores;
	}
}
