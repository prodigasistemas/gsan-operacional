package br.gov.pa.cosanpa.gopera.util;

public enum TipoExibicao {
	ANALITICO(1, "analitico"), SINTETICO(2, "sintetico"), MENSAL(3, "mensal");
	
	private int tipo;
	
	private String descricao; 
	
	TipoExibicao(int cod, String desc){
		this.tipo = cod;
		this.descricao = desc;
	}

	public int getTipo() {
		return tipo;
	}
	
	public String getDescricao(){
		return descricao;
	}
}
