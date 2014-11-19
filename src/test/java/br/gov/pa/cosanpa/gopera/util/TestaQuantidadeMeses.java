package br.gov.pa.cosanpa.gopera.util;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class TestaQuantidadeMeses {
	@Test
	public void testNovembro2012Novembro2013_13_meses() {
	    Calendar cal = Calendar.getInstance();
	    cal.set(2012, 10, 01);
	    Date dataInicial = cal.getTime();
	    cal.set(2013, 10, 01);
	    Date dataFinal = cal.getTime();

		WebUtil util = new WebUtil();
		assertEquals(13, util.mesesPeriodo(dataInicial, dataFinal).size());
	}

	@Test
	public void testNovembro2012_outubro2013_12_meses() {
		Calendar cal = Calendar.getInstance();
		cal.set(2012, 10, 01);
		Date dataInicial = cal.getTime();
		cal.set(2013, 9, 30);
		Date dataFinal = cal.getTime();
		
		WebUtil util = new WebUtil();
		assertEquals(12, util.mesesPeriodo(dataInicial, dataFinal).size());
	}
}
