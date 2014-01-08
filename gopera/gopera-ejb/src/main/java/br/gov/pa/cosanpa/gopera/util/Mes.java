package br.gov.pa.cosanpa.gopera.util;

public class Mes {
	
	private int numeral;
	
	private String nome;
	
	private String nomeCurto;

	public int getNumeral() {
		return numeral;
	}

	public void setNumeral(int numeral) {
		this.numeral = numeral;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeCurto() {
		return nomeCurto;
	}

	public void setNomeCurto(String nomeCurto) {
		this.nomeCurto = nomeCurto;
	}

	public String toString() {
		return "Mes [numeral=" + numeral + ", nome=" + nome + ", nomeCurto=" + nomeCurto + "]";
	}
}
