package br.gov.pa.cosanpa.gopera.util;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;

import org.junit.Test;

public class TestePrimeiroUltimoMes {

	@Test
	public void testPrimeiroDiaJan() {
		String mesAno = "01/2013";
		WebUtil webUtil = new WebUtil();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		assertEquals("01/01/2013", format.format(webUtil.primeiroDiaMes(mesAno)));
	}

	@Test
	public void testUltimoDiaJan() {
		String mesAno = "01/2013";
		WebUtil webUtil = new WebUtil();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		assertEquals("31/01/2013", format.format(webUtil.ultimoDiaMes(mesAno)));
	}

	@Test
	public void testUltimoDiaFev() {
		String mesAno = "02/2013";
		WebUtil webUtil = new WebUtil();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		assertEquals("28/02/2013", format.format(webUtil.ultimoDiaMes(mesAno)));
	}

	@Test
	public void testUltimoDiaJun() {
		String mesAno = "06/2013";
		WebUtil webUtil = new WebUtil();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		assertEquals("30/06/2013", format.format(webUtil.ultimoDiaMes(mesAno)));
	}
}