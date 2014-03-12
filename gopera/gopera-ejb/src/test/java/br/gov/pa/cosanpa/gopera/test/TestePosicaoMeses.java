package br.gov.pa.cosanpa.gopera.test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

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
		builder.append("[");
		builder.append("Mes [numeral=11, nome=Novembro, nomeCurto=NOV]");
		builder.append(", ");
		builder.append("Mes [numeral=12, nome=Dezembro, nomeCurto=DEZ]");
		builder.append(", ");
		builder.append("Mes [numeral=1, nome=Janeiro, nomeCurto=JAN]");
		builder.append(", ");
		builder.append("Mes [numeral=2, nome=Fevereiro, nomeCurto=FEV]");
		builder.append(", ");
		builder.append("Mes [numeral=3, nome=Março, nomeCurto=MAR]");
		builder.append(", ");
		builder.append("Mes [numeral=4, nome=Abril, nomeCurto=ABR]");
		builder.append("]");
		assertEquals(builder.toString(), util.mesesPeriodo(dataInicial, dataFinal).toString());
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
		builder.append("[");
		builder.append("Mes [numeral=1, nome=Janeiro, nomeCurto=JAN]");
		builder.append(", ");
		builder.append("Mes [numeral=2, nome=Fevereiro, nomeCurto=FEV]");
		builder.append(", ");
		builder.append("Mes [numeral=3, nome=Março, nomeCurto=MAR]");
		builder.append(", ");
		builder.append("Mes [numeral=4, nome=Abril, nomeCurto=ABR]");
		builder.append(", ");
		builder.append("Mes [numeral=5, nome=Maio, nomeCurto=MAI]");
		builder.append(", ");
		builder.append("Mes [numeral=6, nome=Junho, nomeCurto=JUN]");
		builder.append(", ");
		builder.append("Mes [numeral=7, nome=Julho, nomeCurto=JUL]");
		builder.append(", ");
		builder.append("Mes [numeral=8, nome=Agosto, nomeCurto=AGO]");
		builder.append(", ");
		builder.append("Mes [numeral=9, nome=Setembro, nomeCurto=SET]");
		builder.append(", ");
		builder.append("Mes [numeral=10, nome=Outubro, nomeCurto=OUT]");
		builder.append(", ");
		builder.append("Mes [numeral=11, nome=Novembro, nomeCurto=NOV]");
		builder.append(", ");
		builder.append("Mes [numeral=12, nome=Dezembro, nomeCurto=DEZ]");
		builder.append("]");
		assertEquals(builder.toString(), util.mesesPeriodo(dataInicial, dataFinal).toString());
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
		builder.append("[");
		builder.append("Mes [numeral=1, nome=Janeiro, nomeCurto=JAN]");
		builder.append(", ");
		builder.append("Mes [numeral=2, nome=Fevereiro, nomeCurto=FEV]");
		builder.append(", ");
		builder.append("Mes [numeral=3, nome=Março, nomeCurto=MAR]");
		builder.append(", ");
		builder.append("Mes [numeral=4, nome=Abril, nomeCurto=ABR]");
		builder.append(", ");
		builder.append("Mes [numeral=5, nome=Maio, nomeCurto=MAI]");
		builder.append(", ");
		builder.append("Mes [numeral=6, nome=Junho, nomeCurto=JUN]");
		builder.append(", ");
		builder.append("Mes [numeral=7, nome=Julho, nomeCurto=JUL]");
		builder.append(", ");
		builder.append("Mes [numeral=8, nome=Agosto, nomeCurto=AGO]");
		builder.append(", ");
		builder.append("Mes [numeral=9, nome=Setembro, nomeCurto=SET]");
		builder.append(", ");
		builder.append("Mes [numeral=10, nome=Outubro, nomeCurto=OUT]");
		builder.append(", ");
		builder.append("Mes [numeral=11, nome=Novembro, nomeCurto=NOV]");
		builder.append(", ");
		builder.append("Mes [numeral=12, nome=Dezembro, nomeCurto=DEZ]");
		builder.append(", ");
		builder.append("Mes [numeral=1, nome=Janeiro, nomeCurto=JAN]");
		builder.append("]");
		
		assertEquals(builder.toString(), util.mesesPeriodo(dataInicial, dataFinal).toString());
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
		builder.append("01/2013");
		builder.append("02/2013");
		builder.append("03/2013");
		builder.append("04/2013");
		
		StringBuilder meses = new StringBuilder();
		for (Mes mes: util.mesesPeriodo(dataInicial, dataFinal)){
			meses.append(mes.getMesAno());
		}
		
		assertEquals(builder.toString(), meses.toString());
	}
}
