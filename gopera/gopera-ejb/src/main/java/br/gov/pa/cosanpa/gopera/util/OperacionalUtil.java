package br.gov.pa.cosanpa.gopera.util;


public class OperacionalUtil {
	public String ordenarCampos(int tipoAgrupamento) {
		
		String [] campos = {"A.greg_id", "A.uneg_id", "A.muni_id", "A.loca_id", "B.unop_tipo, B.unop_id"};
		String ordem ="";
		for(int i = 0; i < tipoAgrupamento; i++) {
			ordem += campos[i] + ", ";
		}
		 
		ordem += "B.cons_data";
		return ordem;
	}
}
