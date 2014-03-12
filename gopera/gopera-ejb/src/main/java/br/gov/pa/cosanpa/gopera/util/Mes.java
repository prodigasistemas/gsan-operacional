package br.gov.pa.cosanpa.gopera.util;

public class Mes {
	
	private int numeral;
	
	private int posicao;
	
	private String nome;
	
	private String nomeCurto;
	
	private String mesAno;

	public int getNumeral() {
		return numeral;
	}

	public void setNumeral(int numeral) {
		this.numeral = numeral;
	}
	
	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
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

	public String getMesAno() {
		return mesAno;
	}

	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
	}

	public String toString() {
		return "Mes [numeral=" + numeral + ", nome=" + nome + ", nomeCurto=" + nomeCurto + "]";
	}
}
