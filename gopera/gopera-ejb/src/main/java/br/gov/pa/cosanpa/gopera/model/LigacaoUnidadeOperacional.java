package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class LigacaoUnidadeOperacional implements Serializable{
	private static final long serialVersionUID = 6336080620478827242L;
	
	@EmbeddedId
	private RelacionamentoPK key;
	
	@Column(name="source_name")
	private String sourceName;
	
	@Column(name="target_name")
	private String targetName;
	
	public LigacaoUnidadeOperacional() {
	}

	public RelacionamentoPK getKey() {
		return key;
	}

	public void setKey(RelacionamentoPK key) {
		this.key = key;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String etaNome) {
		this.sourceName = etaNome;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String eabNome) {
		this.targetName = eabNome;
	}

	@Override
	public String toString() {
		return "RelacionamentoUnidadeOperacional [sourceName=" + sourceName + ", targetName=" + targetName + "]";
	}
}
