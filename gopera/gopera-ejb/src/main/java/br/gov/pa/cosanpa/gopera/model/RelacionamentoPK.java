package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RelacionamentoPK implements Serializable {
	private static final long serialVersionUID = -933506545397778773L;

	@Column(name = "source_id")
	private String sourceId;

	@Column(name = "target_id")
	private String targetId;

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String etaId) {
		this.sourceId = etaId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String eabId) {
		this.targetId = eabId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((targetId == null) ? 0 : targetId.hashCode());
		result = prime * result + ((sourceId == null) ? 0 : sourceId.hashCode());
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
		RelacionamentoPK other = (RelacionamentoPK) obj;
		if (targetId == null) {
			if (other.targetId != null)
				return false;
		} else if (!targetId.equals(other.targetId))
			return false;
		if (sourceId == null) {
			if (other.sourceId != null)
				return false;
		} else if (!sourceId.equals(other.sourceId))
			return false;
		return true;
	}
}
