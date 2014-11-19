package br.gov.pa.cosanpa.gopera.exception;

import br.gov.model.exception.BaseRuntimeException;

public class UnidadeConsumidoraNaoRelacionada extends BaseRuntimeException {
	private static final long serialVersionUID = 5063625968006329888L;
	
	public UnidadeConsumidoraNaoRelacionada() {
		super("unidade_consumidora_nao_relacionada");
	}
}
