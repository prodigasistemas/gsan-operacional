package br.gov.pa.cosanpa.gopera.util;

import br.gov.pa.cosanpa.gopera.model.GraphElement;

public class EdgeSigma implements GraphElement, Comparable<EdgeSigma>{
	private String sourceId;
	
	private String targetId;
	
	public EdgeSigma(String sourceId, String targetId) {
		this.sourceId = sourceId;
		this.targetId = targetId;
	}

	public String getXml(){
		return String.format("<edge id=\"%s\" source=\"%s\" target=\"%s\"/> <color r=\"20\" g=\"20\" b=\"20\"></color>"
				, sourceId + targetId
				, sourceId
				, targetId);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceId == null) ? 0 : sourceId.hashCode());
		result = prime * result + ((targetId == null) ? 0 : targetId.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EdgeSigma other = (EdgeSigma) obj;
		if (sourceId == null) {
			if (other.sourceId != null)
				return false;
		} else if (!sourceId.equals(other.sourceId))
			return false;
		if (targetId == null) {
			if (other.targetId != null)
				return false;
		} else if (!targetId.equals(other.targetId))
			return false;
		return true;
	}

	public int compareTo(EdgeSigma o) {
		return sourceId.concat(targetId).compareTo(o.sourceId.concat(o.targetId));
	}

	public String toString() {
		return "EdgeSigma [sourceId=" + sourceId + ", targetId=" + targetId + "]";
	}

	public String getSourceId() {
		return sourceId;
	}

	public String getTargetId() {
		return targetId;
	}
}
