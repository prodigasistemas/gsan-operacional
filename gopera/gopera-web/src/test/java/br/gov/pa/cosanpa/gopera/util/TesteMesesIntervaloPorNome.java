package br.gov.pa.cosanpa.gopera.util;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class TesteMesesIntervaloPorNome {

	@Test
	public void testNovembro2012Abril2013() {
	    Calendar cal = Calendar.getInstance();
	    cal.set(2012, 10, 01);
	    Date dataInicial = cal.getTime();
	    cal.set(2013, 03, 01);
	    Date dataFinal = cal.getTime();

		WebUtil util = new WebUtil();
		assertEquals("[NOV, DEZ, JAN, FEV, MAR, ABR]", util.mesesPeriodoEmNome(dataInicial, dataFinal).toString());
	}

	@Test
	public void testJaneiro2013Dezembro2013() {
		Calendar cal = Calendar.getInstance();
		cal.set(2013, 00, 01);
		Date dataInicial = cal.getTime();
		cal.set(2013, 11, 01);
		Date dataFinal = cal.getTime();
		
		WebUtil util = new WebUtil();
		assertEquals("[JAN, FEV, MAR, ABR, MAI, JUN, JUL, AGO, SET, OUT, NOV, DEZ]", util.mesesPeriodoEmNome(dataInicial, dataFinal).toString());
	}

	@Test
	public void testJaneiro2013Janeiro2014() {
		Calendar cal = Calendar.getInstance();
		cal.set(2013, 00, 01);
		Date dataInicial = cal.getTime();
		cal.set(2014, 00, 01);
		Date dataFinal = cal.getTime();
		
		WebUtil util = new WebUtil();
		assertEquals("[JAN, FEV, MAR, ABR, MAI, JUN, JUL, AGO, SET, OUT, NOV, DEZ, JAN]", util.mesesPeriodoEmNome(dataInicial, dataFinal).toString());
	}
}
