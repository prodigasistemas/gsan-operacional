package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class DadosRelatorioEnergiaEletricaPK implements Serializable{
	private static final long serialVersionUID = -8506207406381574085L;

	private Integer idMunicipio;

	private Integer idLocalidade;
	
	private Integer uc;
	
	private String mes;
	
	public DadosRelatorioEnergiaEletricaPK() {
	}

	public Integer getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public Integer getIdLocalidade() {
		return idLocalidade;
	}

	public void setIdLocalidade(Integer idLocalidade) {
		this.idLocalidade = idLocalidade;
	}

	public Integer getUc() {
		return uc;
	}

	public void setUc(Integer uc) {
		this.uc = uc;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idLocalidade == null) ? 0 : idLocalidade.hashCode());
		result = prime * result
				+ ((idMunicipio == null) ? 0 : idMunicipio.hashCode());
		result = prime * result + ((mes == null) ? 0 : mes.hashCode());
		result = prime * result + ((uc == null) ? 0 : uc.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DadosRelatorioEnergiaEletricaPK other = (DadosRelatorioEnergiaEletricaPK) obj;
		if (idLocalidade == null) {
			if (other.idLocalidade != null)
				return false;
		} else if (!idLocalidade.equals(other.idLocalidade))
			return false;
		if (idMunicipio == null) {
			if (other.idMunicipio != null)
				return false;
		} else if (!idMunicipio.equals(other.idMunicipio))
			return false;
		if (mes == null) {
			if (other.mes != null)
				return false;
		} else if (!mes.equals(other.mes))
			return false;
		if (uc == null) {
			if (other.uc != null)
				return false;
		} else if (!uc.equals(other.uc))
			return false;
		return true;
	}
}
