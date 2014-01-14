package tests;

import java.util.Calendar;

public class DifMillis {

	public static void main(String[] args) {
		Calendar iniRel = Calendar.getInstance();
		iniRel.setTimeInMillis(1389017380850L);
		Calendar fimRel = Calendar.getInstance();
		fimRel.setTimeInMillis(1389017707952L);
		
		Calendar iniMesesUcs = Calendar.getInstance();
		iniMesesUcs.setTimeInMillis(1389017478007L);
		
		double dif = (1389017707952L - 1389017380850L) / (double) 1000 / 60;
		System.out.println("dif " + dif);

		double dif2 = (1389017707952L - 1389017478007L) / (double) 1000 / 60;
		System.out.println("dif " + dif2);
	}
}
