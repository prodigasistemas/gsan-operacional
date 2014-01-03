package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import java.util.Date;

public class LancamentoPendente implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer codigo;
	private RegionalProxy regionalProxy;
	private UnidadeNegocioProxy unidadeNegocioProxy;
	private MunicipioProxy municipioProxy;
	private LocalidadeProxy localidadeProxy;
	private Integer tipoUnidadeOperacional;
	private Integer codigoUnidadeOperacional;
	private String unidadeOperacional;
	private Date dataConsumo;
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public RegionalProxy getRegionalProxy() {
		return regionalProxy;
	}
	public void setRegionalProxy(RegionalProxy regionalProxy) {
		this.regionalProxy = regionalProxy;
	}
	public UnidadeNegocioProxy getUnidadeNegocioProxy() {
		return unidadeNegocioProxy;
	}
	public void setUnidadeNegocioProxy(UnidadeNegocioProxy unidadeNegocioProxy) {
		this.unidadeNegocioProxy = unidadeNegocioProxy;
	}
	public MunicipioProxy getMunicipioProxy() {
		return municipioProxy;
	}
	public void setMunicipioProxy(MunicipioProxy municipioProxy) {
		this.municipioProxy = municipioProxy;
	}
	public LocalidadeProxy getLocalidadeProxy() {
		return localidadeProxy;
	}
	public void setLocalidadeProxy(LocalidadeProxy localidadeProxy) {
		this.localidadeProxy = localidadeProxy;
	}
	public Integer getTipoUnidadeOperacional() {
		return tipoUnidadeOperacional;
	}
	public void setTipoUnidadeOperacional(Integer tipoUnidadeOperacional) {
		this.tipoUnidadeOperacional = tipoUnidadeOperacional;
	}
	public Integer getCodigoUnidadeOperacional() {
		return codigoUnidadeOperacional;
	}
	public void setCodigoUnidadeOperacional(Integer codigoUnidadeOperacional) {
		this.codigoUnidadeOperacional = codigoUnidadeOperacional;
	}
	public String getUnidadeOperacional() {
		return unidadeOperacional;
	}
	public void setUnidadeOperacional(String unidadeOperacional) {
		this.unidadeOperacional = unidadeOperacional;
	}
	public Date getDataConsumo() {
		return dataConsumo;
	}
	public void setDataConsumo(Date dataConsumo) {
		this.dataConsumo = dataConsumo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime
				* result
				+ ((codigoUnidadeOperacional == null) ? 0
						: codigoUnidadeOperacional.hashCode());
		result = prime * result
				+ ((dataConsumo == null) ? 0 : dataConsumo.hashCode());
		result = prime * result
				+ ((localidadeProxy == null) ? 0 : localidadeProxy.hashCode());
		result = prime * result
				+ ((municipioProxy == null) ? 0 : municipioProxy.hashCode());
		result = prime * result
				+ ((regionalProxy == null) ? 0 : regionalProxy.hashCode());
		result = prime
				* result
				+ ((tipoUnidadeOperacional == null) ? 0
						: tipoUnidadeOperacional.hashCode());
		result = prime
				* result
				+ ((unidadeNegocioProxy == null) ? 0 : unidadeNegocioProxy
						.hashCode());
		result = prime
				* result
				+ ((unidadeOperacional == null) ? 0 : unidadeOperacional
						.hashCode());
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
		LancamentoPendente other = (LancamentoPendente) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (codigoUnidadeOperacional == null) {
			if (other.codigoUnidadeOperacional != null)
				return false;
		} else if (!codigoUnidadeOperacional
				.equals(other.codigoUnidadeOperacional))
			return false;
		if (dataConsumo == null) {
			if (other.dataConsumo != null)
				return false;
		} else if (!dataConsumo.equals(other.dataConsumo))
			return false;
		if (localidadeProxy == null) {
			if (other.localidadeProxy != null)
				return false;
		} else if (!localidadeProxy.equals(other.localidadeProxy))
			return false;
		if (municipioProxy == null) {
			if (other.municipioProxy != null)
				return false;
		} else if (!municipioProxy.equals(other.municipioProxy))
			return false;
		if (regionalProxy == null) {
			if (other.regionalProxy != null)
				return false;
		} else if (!regionalProxy.equals(other.regionalProxy))
			return false;
		if (tipoUnidadeOperacional == null) {
			if (other.tipoUnidadeOperacional != null)
				return false;
		} else if (!tipoUnidadeOperacional.equals(other.tipoUnidadeOperacional))
			return false;
		if (unidadeNegocioProxy == null) {
			if (other.unidadeNegocioProxy != null)
				return false;
		} else if (!unidadeNegocioProxy.equals(other.unidadeNegocioProxy))
			return false;
		if (unidadeOperacional == null) {
			if (other.unidadeOperacional != null)
				return false;
		} else if (!unidadeOperacional.equals(other.unidadeOperacional))
			return false;
		return true;
	}

}
