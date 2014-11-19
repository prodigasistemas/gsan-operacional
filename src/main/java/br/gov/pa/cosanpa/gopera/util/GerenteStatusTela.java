package br.gov.pa.cosanpa.gopera.util;

import br.gov.pa.cosanpa.gopera.enums.StatusTela;

public class GerenteStatusTela {
	
	private StatusTela estadoAtual = StatusTela.VISUALIZANDO;

	public StatusTela getEstado() {
		return estadoAtual;
	}

	private void setEstado(StatusTela estado) {
		this.estadoAtual = estado;
	}
	
	public boolean getVisualizando() {
		return this.estadoAtual.equals(StatusTela.VISUALIZANDO);
	}
	
	public boolean getConfirmando() {
		return this.estadoAtual.equals(StatusTela.CONFIRMANDO);
	}

	public boolean getFinalizado() {
		return this.estadoAtual.equals(StatusTela.FINALIZADO);
	}

	public boolean getCadastrando() {
		return getEstado().equals(StatusTela.CADASTRANDO);
	}

	public boolean getEditando() {
		return getEstado().equals(StatusTela.EDITANDO);
	}

	public boolean getConsultando() {
		return getEstado().equals(StatusTela.CONSULTANDO);
	}
	
	public void visualizar() {
		this.setEstado(StatusTela.VISUALIZANDO);
	}

	public void finalizar() {
		this.setEstado(StatusTela.FINALIZADO);
	}

}
