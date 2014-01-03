package br.gov.pa.cosanpa.gopera.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;

public class DateUtil {
	public List<Mes> numeralMesesPeriodo(Date dataInicial, Date dataFinal) {
		List<Mes> meses = new LinkedList<Mes>();
		
		Locale locale = new Locale("pt", "BR");
		DateFormat format = new SimpleDateFormat("MMMMM", locale);
		DateFormat formatCurto = new SimpleDateFormat("MMM", locale);
		
		Date inicio = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		Date fim = DateUtils.truncate(dataFinal, Calendar.DAY_OF_MONTH);
		while (!inicio.after(fim)) {
			Mes mes = new Mes();
			mes.setNumeral(DateUtils.toCalendar(inicio).get(Calendar.MONTH) + 1);
			mes.setNome(format.format(inicio));
			mes.setNomeCurto(formatCurto.format(inicio).toUpperCase());
			meses.add(mes);
			inicio = DateUtils.addMonths(inicio, 1);
		}
		return meses;
	}
}
