package br.gov.pa.cosanpa.gopera.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import br.gov.model.operacao.RelatorioGerencial;

public class TestaMarcacaoProdutoQuimico {
	
	Map<String, GrupoProdutoQuimico> map = null;

	@Before
	public void beforeTest(){
		GrupoProdutoQuimico unidOpe01 = new GrupoProdutoQuimico("ETA Bolonha","unidadeoperacional_1_1", 0);
		GrupoProdutoQuimico unidOpe02 = new GrupoProdutoQuimico("ETA 5 Setor","unidadeoperacional_1_2", 0);
		
		GrupoProdutoQuimico localidade = new GrupoProdutoQuimico("Belem", "localidade_belem", 0);
		
		map = new HashMap<String, GrupoProdutoQuimico>();

		ValoresResumo vlrResumo01 = new ValoresResumo(103605.3, 7.26, 752174.478);
		ValoresResumo vlrResumo02 = new ValoresResumo(248709, 2.47, 614311.23);
		ValoresResumo vlrResumo03 = new ValoresResumo(2250, 9.29, 20902.5);

		ResumoProdutoQuimico resumoCloro01 = new ResumoProdutoQuimico("Cloro Gás");
		resumoCloro01.getValores().put("01", vlrResumo01);
		ResumoProdutoQuimico resumoPac01   = new ResumoProdutoQuimico("PAC");
		resumoPac01.getValores().put("01", vlrResumo02);
		ResumoProdutoQuimico resumoPoli01  = new ResumoProdutoQuimico("Polímero");
		resumoPoli01.getValores().put("01", vlrResumo03);
		
		unidOpe01.addResumo(resumoCloro01);
		unidOpe01.addResumo(resumoPac01);
		unidOpe01.addResumo(resumoPoli01);
		
		map.put(unidOpe01.getId(), unidOpe01);
		map.put(unidOpe02.getId(), unidOpe02);		
	}

	@Test
	public void testMesmoGrupoUnidadeOperacional() {
		assertEquals(2, map.keySet().size());
	}

	@Test
	public void testMesmoGrupoUnidadeOperacionalBolonha() {
		assertEquals(3, map.get("unidadeoperacional_1_1").getResumos().size());
	}

	@Test
	public void testResumoMensal() throws Exception {
		Map<String, GrupoProdutoQuimico> mapMensal = new HashMap<String, GrupoProdutoQuimico>();
		
		String nomeGrupo = "localidade_belem";
		
		WebUtil util = new WebUtil();
		
		List<String> campos = new LinkedList<String>();
		campos.add("JAN");
		campos.add("FEV");
		campos.add("MAR");
		
		RelatorioGerencial cloroJAN = new RelatorioGerencial();
		cloroJAN.setDescricaoProduto("cloro");
		cloroJAN.setNomeLocalidade(nomeGrupo);
		cloroJAN.setQtdConsumoJAN(15);
		
		RelatorioGerencial cloroFEV = new RelatorioGerencial();
		cloroFEV.setDescricaoProduto("cloro");
		cloroFEV.setNomeLocalidade(nomeGrupo);
		cloroFEV.setQtdConsumoFEV(25);
		
		RelatorioGerencial cloroMAR = new RelatorioGerencial();
		cloroMAR.setDescricaoProduto("cloro");
		cloroMAR.setNomeLocalidade(nomeGrupo);
		cloroMAR.setQtdConsumoMAR(35);
		
		RelatorioGerencial pacJAN = new RelatorioGerencial();
		pacJAN.setDescricaoProduto("pac");
		pacJAN.setNomeLocalidade(nomeGrupo);
		pacJAN.setQtdConsumoJAN(15);
		
		RelatorioGerencial pacFEV = new RelatorioGerencial();
		pacFEV.setDescricaoProduto("pac");
		pacFEV.setNomeLocalidade(nomeGrupo);
		pacFEV.setQtdConsumoFEV(25);
		
		RelatorioGerencial pacMAR = new RelatorioGerencial();
		pacMAR.setDescricaoProduto("pac");
		pacMAR.setNomeLocalidade(nomeGrupo);
		pacMAR.setQtdConsumoMAR(35);
		
		util.totalizadorGrupoProdutoQuimico(mapMensal, cloroJAN, cloroJAN.getNomeLocalidade(), nomeGrupo, 0, campos);
		util.totalizadorGrupoProdutoQuimico(mapMensal, cloroFEV, cloroFEV.getNomeLocalidade(), nomeGrupo, 0, campos);
		util.totalizadorGrupoProdutoQuimico(mapMensal, cloroMAR, cloroMAR.getNomeLocalidade(), nomeGrupo, 0, campos);
		
		util.totalizadorGrupoProdutoQuimico(mapMensal, pacJAN, pacJAN.getNomeLocalidade(), nomeGrupo, 0, campos);
		util.totalizadorGrupoProdutoQuimico(mapMensal, pacFEV, pacFEV.getNomeLocalidade(), nomeGrupo, 0, campos);
		util.totalizadorGrupoProdutoQuimico(mapMensal, pacMAR, pacMAR.getNomeLocalidade(), nomeGrupo, 0, campos);
		
		Map<String, ResumoProdutoQuimico> resumos = mapMensal.get("localidade_belem").getResumos();
		assertEquals(15, resumos.get("cloro").getValores().get("JAN").getQtdConsumo(), 0);
	}
}
