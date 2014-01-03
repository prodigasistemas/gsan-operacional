package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//@Embeddable
@Entity
@Table(name="sistema_abastecimento",schema="operacional")
public class SistemaAbastecimentoProxy implements Serializable {
	
	private static final long serialVersionUID = 8999806559105931785L;
	
	@Id
	@Column(name="sabs_id", nullable=false)
	private Integer codigo;
	
	//@Transient
	@Column(name="sabs_dssistemaabastecimento")
	private String nome;
	
	
	public SistemaAbastecimentoProxy() {
	}
	
	public SistemaAbastecimentoProxy(Integer codigo, String nome) {
		super();
		this.codigo = codigo;
		this.nome = nome;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		SistemaAbastecimentoProxy other = (SistemaAbastecimentoProxy) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

}
