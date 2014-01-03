package br.gov.pa.cosanpa.gopera.util;

import java.io.Serializable;

public class ElementoMensal implements Serializable{
	private static final long serialVersionUID = -8783130951715911441L;

	private boolean isVisible = false;
	
	private Double quantidade = null;
	
	private String mes = null;

	public ElementoMensal() {
	}

	public boolean isVisible() {
		return isVisible;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public String getMes() {
		return mes;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}
}