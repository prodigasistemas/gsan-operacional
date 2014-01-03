package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@SequenceGenerator(name="sequence_unidade_consumidora_operacional", sequenceName="sequence_unidade_consumidora_operacional", schema="operacao", initialValue=1, allocationSize=1)
@Table(name="unidade_consumidora_operacional",schema="operacao")
public class UnidadeConsumidoraOperacional implements BaseEntidade, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_unidade_consumidora_operacional")
    @Column(name="ucop_id", unique=true, nullable=false, precision=3, scale=0)
	private Integer codigo;
	
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ucon_id")
    private UnidadeConsumidora UC;

	@Column(name="ucop_tipooperacional")
	private Integer tipoUnidadeOperacional;

	@Column(name="ucop_idoperacional")
	private Integer codigoUnidadeOperacional;

	@Transient
	private String descricao;
	
	@Column(name="ucop_percentual")
	private Double percentual;

	public UnidadeConsumidoraOperacional() {
		super();
	}

	public UnidadeConsumidoraOperacional(Integer codigo, String nome) {
		super();
		this.codigo = codigo;
		this.descricao = nome;
	}
	
	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public UnidadeConsumidora getUC() {
		return UC;
	}

	public void setUC(UnidadeConsumidora uC) {
		UC = uC;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getPercentual() {
		return percentual;
	}

	public void setPercentual(Double percentual) {
		this.percentual = percentual;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((UC == null) ? 0 : UC.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime
				* result
				+ ((codigoUnidadeOperacional == null) ? 0
						: codigoUnidadeOperacional.hashCode());
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result
				+ ((percentual == null) ? 0 : percentual.hashCode());
		result = prime
				* result
				+ ((tipoUnidadeOperacional == null) ? 0
						: tipoUnidadeOperacional.hashCode());
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
		UnidadeConsumidoraOperacional other = (UnidadeConsumidoraOperacional) obj;
		if (UC == null) {
			if (other.UC != null)
				return false;
		} else if (!UC.equals(other.UC))
			return false;
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
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (percentual == null) {
			if (other.percentual != null)
				return false;
		} else if (!percentual.equals(other.percentual))
			return false;
		if (tipoUnidadeOperacional == null) {
			if (other.tipoUnidadeOperacional != null)
				return false;
		} else if (!tipoUnidadeOperacional.equals(other.tipoUnidadeOperacional))
			return false;
		return true;
	}
}
