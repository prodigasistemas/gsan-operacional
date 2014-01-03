package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import java.util.Date;

public class IndicadorValor implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Date mesReferencia;
	private double indicador1;
	private double indicador2;
	private double total;
	
	public Date getMesReferencia() {
		return mesReferencia;
	}
	public void setMesReferencia(Date mesReferencia) {
		this.mesReferencia = mesReferencia;
	}
	public double getIndicador1() {
		return indicador1;
	}
	public void setIndicador1(double indicador1) {
		this.indicador1 = indicador1;
	}
	public double getIndicador2() {
		return indicador2;
	}
	public void setIndicador2(double indicador2) {
		this.indicador2 = indicador2;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	
}
