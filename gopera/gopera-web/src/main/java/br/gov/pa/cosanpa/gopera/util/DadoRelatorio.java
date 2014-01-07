package br.gov.pa.cosanpa.gopera.util;

import java.io.Serializable;

public class DadoRelatorio implements Comparable<DadoRelatorio>, Serializable{
	private static final long serialVersionUID = 6785874933205212275L;

	private String indice;
	
	private String label;
	
	private String nome;
	
	public DadoRelatorio() {
	}
	
	public DadoRelatorio(String indice, String label, String nome) {
		this.indice = indice;
		this.label = label;
		this.nome = nome;
	}

	public String getIndice() {
		return indice;
	}

	public void setIndice(String indice) {
		this.indice = indice;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int compareTo(DadoRelatorio o) {
		return this.indice.compareTo(o.indice);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((indice == null) ? 0 : indice.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DadoRelatorio other = (DadoRelatorio) obj;
		if (indice == null) {
			if (other.indice != null)
				return false;
		} else if (!indice.equals(other.indice))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return indice + " - " + label;
	}
}
