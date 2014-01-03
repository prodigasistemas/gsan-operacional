package br.gov.pa.cosanpa.gopera.exception;

public class ArquivoDeRelatorioNaoExiste extends BaseRuntimeException {
	private static final long serialVersionUID = 5063625968006329888L;
	
	public ArquivoDeRelatorioNaoExiste() {
		super("erro_arquivo_relatorio_nao_existe");
	}
}
