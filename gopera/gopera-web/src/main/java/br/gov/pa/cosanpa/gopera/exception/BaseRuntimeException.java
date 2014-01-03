package br.gov.pa.cosanpa.gopera.exception;

public abstract class BaseRuntimeException extends RuntimeException{
	private static final long serialVersionUID = -5713243194968714704L;
	
	public BaseRuntimeException(String message) {
		super(message);
	}
}
