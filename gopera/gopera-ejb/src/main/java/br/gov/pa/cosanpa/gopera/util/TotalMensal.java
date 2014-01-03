package br.gov.pa.cosanpa.gopera.util;

public class TotalMensal {
	private Integer mes;
	
	private Double valor;

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mes == null) ? 0 : mes.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TotalMensal other = (TotalMensal) obj;
		if (mes == null) {
			if (other.mes != null)
				return false;
		} else if (!mes.equals(other.mes))
			return false;
		return true;
	}

	public String toString() {
		return "TotalMensal [mes=" + mes + ", valor=" + valor + "]";
	}
}
