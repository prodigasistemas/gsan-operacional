package br.gov.pa.cosanpa.gopera.test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import br.gov.pa.cosanpa.gopera.util.DateUtil;
import br.gov.pa.cosanpa.gopera.util.Mes;

public class TestePosicaoMeses {

	@Test
	public void testNovembro2012Abril2013() {
	    Calendar cal = Calendar.getInstance();
	    cal.set(2012, 10, 01);
	    Date dataInicial = cal.getTime();
	    cal.set(2013, 03, 01);
	    Date dataFinal = cal.getTime();

		DateUtil util = new DateUtil();
		
		StringBuilder builder = new StringBuilder();
		builder.append("123456");
		
		assertEquals(builder.toString(), buildRetorno(util.mesesPeriodo(dataInicial, dataFinal)));
	}

	@Test
	public void testJaneiro2013Dezembro2013() {
		Calendar cal = Calendar.getInstance();
		cal.set(2013, 00, 01);
		Date dataInicial = cal.getTime();
		cal.set(2013, 11, 01);
		Date dataFinal = cal.getTime();
		
		DateUtil util = new DateUtil();
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("123456789101112");
		
		assertEquals(builder.toString(), buildRetorno(util.mesesPeriodo(dataInicial, dataFinal)));
	}

	@Test
	public void testJaneiro2013Janeiro2014() {
		Calendar cal = Calendar.getInstance();
		cal.set(2013, 00, 01);
		Date dataInicial = cal.getTime();
		cal.set(2014, 00, 01);
		Date dataFinal = cal.getTime();
		
		DateUtil util = new DateUtil();
		StringBuilder builder = new StringBuilder();
		builder.append("12345678910111213");

		assertEquals(builder.toString(), buildRetorno(util.mesesPeriodo(dataInicial, dataFinal)));
	}
	
	@Test
	public void testJaneiro2013Abril2013() {
		Calendar cal = Calendar.getInstance();
		cal.set(2013, 00, 01);
		Date dataInicial = cal.getTime();
		cal.set(2013, 03, 01);
		Date dataFinal = cal.getTime();
		
		DateUtil util = new DateUtil();
		StringBuilder builder = new StringBuilder();
		builder.append("1234");
		
		assertEquals(builder.toString(), buildRetorno(util.mesesPeriodo(dataInicial, dataFinal)));
	}
	
	private String buildRetorno(List<Mes> meses){
		StringBuilder retorno = new StringBuilder();
		for (Mes mes : meses) {
			retorno.append(mes.getPosicao());
		}
		return retorno.toString();
	}
}
