package br.gov.pa.cosanpa.gopera.util;

import static org.junit.Assert.*;

import org.junit.Test;

import br.gov.pa.cosanpa.gopera.util.OperacionalUtil;

public class TestaCamposForaAgrupamento {

	private WebBundle bundle = new WebBundle();
	
	@Test
	public void testSemAgrupamento() {
		WebUtil util = new WebUtil();
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(bundle.getText("gerencia_regional"));
		builder.append(", ");
		builder.append(bundle.getText("unidade_negocio"));
		builder.append(", ");
		builder.append(bundle.getText("municipio"));
		builder.append(", ");
		builder.append(bundle.getText("localidade"));
		builder.append(", ");
		builder.append(bundle.getText("unidade_operacional"));
		builder.append("]");
		assertEquals(builder.toString(), util.descricoesDeCamposNoAgrupamento(-1).toString());
	}
	
	@Test
	public void testAgrupamentoGerencia() {
		WebUtil util = new WebUtil();
		
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(bundle.getText("unidade_negocio"));
		builder.append(", ");
		builder.append(bundle.getText("municipio"));
		builder.append(", ");
		builder.append(bundle.getText("localidade"));
		builder.append(", ");
		builder.append(bundle.getText("unidade_operacional"));
		builder.append("]");
		assertEquals(builder.toString(), util.descricoesDeCamposNoAgrupamento(1).toString());
	}
	
	@Test
	public void testAgrupamentoUnidadeNegocio() {
		
		WebUtil util = new WebUtil();
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(bundle.getText("municipio"));
		builder.append(", ");
		builder.append(bundle.getText("localidade"));
		builder.append(", ");
		builder.append(bundle.getText("unidade_operacional"));
		builder.append("]");
		assertEquals(builder.toString(), util.descricoesDeCamposNoAgrupamento(2).toString());		
	}
	
	@Test
	public void testAgrupamentoUnidadeOperacional() {
		WebUtil util = new WebUtil();
		
		assertEquals("[]", util.descricoesDeCamposNoAgrupamento(5).toString());		
	}
}
