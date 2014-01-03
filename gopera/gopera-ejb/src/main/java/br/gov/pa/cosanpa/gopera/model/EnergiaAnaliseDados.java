package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import java.util.Date;

public class EnergiaAnaliseDados implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Date mesReferencia;
	private double valor;
	
	public Date getMesReferencia() {
		return mesReferencia;
	}
	public void setMesReferencia(Date mesReferencia) {
		this.mesReferencia = mesReferencia;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
}
