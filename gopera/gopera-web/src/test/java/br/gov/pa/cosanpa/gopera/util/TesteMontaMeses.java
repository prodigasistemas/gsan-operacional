package br.gov.pa.cosanpa.gopera.util;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class TesteMontaMeses {

	@Test
	public void testNovembro2012Abril2013() {
	    Calendar cal = Calendar.getInstance();
	    cal.set(2012, 10, 01);
	    Date dataInicial = cal.getTime();
	    cal.set(2013, 03, 01);
	    Date dataFinal = cal.getTime();

		WebUtil util = new WebUtil();
		assertEquals("[11, 12, 01, 02, 03, 04]", util.mesesPeriodo(dataInicial, dataFinal).toString());
	}

	@Test
	public void testJaneiro2013Dezembro2013() {
		Calendar cal = Calendar.getInstance();
		cal.set(2013, 00, 01);
		Date dataInicial = cal.getTime();
		cal.set(2013, 11, 01);
		Date dataFinal = cal.getTime();
		
		WebUtil util = new WebUtil();
		assertEquals("[01, 02, 03, 04, 05, 06, 07, 08, 09, 10, 11, 12]", util.mesesPeriodo(dataInicial, dataFinal).toString());
	}

	@Test
	public void testJaneiro2013Janeiro2014() {
		Calendar cal = Calendar.getInstance();
		cal.set(2013, 00, 01);
		Date dataInicial = cal.getTime();
		cal.set(2014, 00, 01);
		Date dataFinal = cal.getTime();
		
		WebUtil util = new WebUtil();
		assertEquals("[01, 02, 03, 04, 05, 06, 07, 08, 09, 10, 11, 12, 01]", util.mesesPeriodo(dataInicial, dataFinal).toString());
	}
}
