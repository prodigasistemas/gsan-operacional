package br.gov.pa.cosanpa.gopera.exception;

public class MesReferenciaJaCadastradoException extends BaseRuntimeException {
	private static final long serialVersionUID = 5063625968006329888L;
	
	public MesReferenciaJaCadastradoException() {
		super("erro_mes_referencia_ja_cadastrado");
	}
}
