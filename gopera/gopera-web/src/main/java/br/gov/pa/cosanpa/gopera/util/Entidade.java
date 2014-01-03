package br.gov.pa.cosanpa.gopera.util;

import java.lang.reflect.Method;

class Entidade {

	private String descricao;
	private Method metodo;

	public Entidade(String descricao, Method metodo) {
		this.descricao = descricao;
		this.metodo = metodo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Method getMetodo() {
		return metodo;
	}
}
