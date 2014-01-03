package br.gov.pa.cosanpa.gopera.test;

import static org.junit.Assert.*;

import org.junit.Test;

import br.gov.pa.cosanpa.gopera.util.OperacionalUtil;

public class TestaAgrupamento {

	@Test
	public void testSemAgrupamento() {
		OperacionalUtil util = new OperacionalUtil();
		assertEquals("B.cons_data", util.ordenarCampos(-1));
	}
	
	@Test
	public void testAgrupamentoGerencia() {
		OperacionalUtil util = new OperacionalUtil();
		assertEquals("A.greg_id, B.cons_data", util.ordenarCampos(1));
	}
	
	@Test
	public void testAgrupamentoUnidadeNegocio() {
		OperacionalUtil util = new OperacionalUtil();
		assertEquals("A.greg_id, A.uneg_id, B.cons_data", util.ordenarCampos(2));
	}
	
	@Test
	public void testAgrupamentoUnidadeOperacional() {
		OperacionalUtil util = new OperacionalUtil();
		assertEquals("A.greg_id, A.uneg_id, A.muni_id, A.loca_id, B.unop_tipo, B.unop_id, B.cons_data", util.ordenarCampos(5));
	}
}
