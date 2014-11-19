package br.gov.pa.cosanpa.gopera.util;

public class ValoresResumo {
	private double qtdConsumo;

	private double vlrUnitario;
	
	private double vlrTotal;

	public ValoresResumo(double qtdConsumo, double vlrUnitario, double vlrTotal) {
		this.qtdConsumo = qtdConsumo;
		this.vlrUnitario = vlrUnitario;
		this.vlrTotal = vlrTotal;
	}
	
	public double getQtdConsumo() {
		return qtdConsumo;
	}

	public double getVlrUnitario() {
		return vlrUnitario;
	}

	public double getVlrTotal() {
		return vlrTotal;
	}

	public void addConsumo(double qtd){
		this.qtdConsumo += qtd;
	}
	
	public void incrementaTotal(double vlr){
		this.vlrTotal += vlr;
	}

	public String toString() {
		return "ValoresResumo [qtdConsumo=" + qtdConsumo + ", vlrUnitario=" + vlrUnitario + ", vlrTotal=" + vlrTotal + "]";
	}
}
