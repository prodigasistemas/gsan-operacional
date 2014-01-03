package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class NodeSigma implements Serializable {
	private static final long serialVersionUID = 5998420653286142717L;

	@Id
	private String id;

	@Column
	private String label;

	@Column
	private String type;

	private transient Boolean relacionado = false; 
	
	private static String[][] color = new String[][]{
		{"EAB", "116", "188", "20"},//verde #74bc14 
		{"ETA", "159", "193", "219"},//blue #9fc1db
		{"EAT", "229", "57", "40"}//red #e53928
		};

	public NodeSigma() {
	}
	
	public NodeSigma(String id){
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String nodeType) {
		this.type = nodeType;
	}

	public Boolean isRelacionado() {
		return relacionado;
	}

	public void setRelacionado(Boolean relacionado) {
		this.relacionado = relacionado;
	}

	public String getXml() {
		return String.format("<node id=\"%s\" label=\"%s\">  %s </node>", this.getId(), this.getLabel(), this.getColor());
	}

	public String toString() {
		return "NodeSigma [id=" + id + ", label=" + label + "]";
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeSigma other = (NodeSigma) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getColor() {
		for(int i=0;i<3;i++){
			if (color[i][0].equals(type)){
				return "<color r=\"" + color[i][1] + "\" g=\"" + color[i][2] + "\" b=\"" + color[i][3] + "\"></color>";
			}
		}
		return "<color r=\"20\" g=\"20\" b=\"20\"></color>";
	}
}
