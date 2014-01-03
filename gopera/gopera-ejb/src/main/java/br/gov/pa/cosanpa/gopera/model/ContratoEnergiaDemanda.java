package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SequenceGenerator(name="sequence_contrato_energia_demanda", sequenceName="sequence_contrato_energia_demanda", schema="operacao", initialValue=1, allocationSize=1)
@Table(name="contrato_energia_demanda",schema="operacao")
public class ContratoEnergiaDemanda implements BaseEntidade, Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_contrato_energia_demanda")
    @Column(name="cend_id", unique=true, nullable=false, precision=3, scale=0)
	private Integer codigo;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cene_id")
    private ContratoEnergia contrato;

	@Column(name="cend_dataini")
	@Temporal(TemporalType.DATE)
	private Date dataInicial; 

	@Column(name="cend_datafim")
	@Temporal(TemporalType.DATE)
	private Date dataFinal; 
	
	@Column(name="cend_demandasecoponta")
	private Integer demandaSecoPonta;
	
	@Column(name="cend_demandasecoforaponta")
	private Integer demandaSecoForaPonta;

	@Column(name="cend_demandaumidoponta")
	private Integer demandaUmidoPonta;

	@Column(name="cend_demandaumidoforaponta")
	private Integer demandaUmidoForaPonta;

	@Column(name="cend_convencionalverde")
	private Integer convencionalVerde;
	
	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ContratoEnergia getContrato() {
		return contrato;
	}

	public void setContrato(ContratoEnergia contrato) {
		this.contrato = contrato;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Integer getDemandaSecoPonta() {
		return demandaSecoPonta;
	}

	public void setDemandaSecoPonta(Integer demandaSecoPonta) {
		this.demandaSecoPonta = demandaSecoPonta;
	}

	public Integer getDemandaSecoForaPonta() {
		return demandaSecoForaPonta;
	}

	public void setDemandaSecoForaPonta(Integer demandaSecoForaPonta) {
		this.demandaSecoForaPonta = demandaSecoForaPonta;
	}

	public Integer getDemandaUmidoPonta() {
		return demandaUmidoPonta;
	}

	public void setDemandaUmidoPonta(Integer demandaUmidoPonta) {
		this.demandaUmidoPonta = demandaUmidoPonta;
	}

	public Integer getDemandaUmidoForaPonta() {
		return demandaUmidoForaPonta;
	}

	public void setDemandaUmidoForaPonta(Integer demandaUmidoForaPonta) {
		this.demandaUmidoForaPonta = demandaUmidoForaPonta;
	}
	
	public Integer getConvencionalVerde() {
		return convencionalVerde;
	}

	public void setConvencionalVerde(Integer convencionalVerde) {
		this.convencionalVerde = convencionalVerde;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((contrato == null) ? 0 : contrato.hashCode());
		result = prime * result + ((dataFinal == null) ? 0 : dataFinal.hashCode());
		result = prime * result + ((dataInicial == null) ? 0 : dataInicial.hashCode());
		result = prime
				* result
				+ ((demandaSecoForaPonta == null) ? 0 : demandaSecoForaPonta
						.hashCode());
		result = prime
				* result
				+ ((demandaSecoPonta == null) ? 0 : demandaSecoPonta.hashCode());
		result = prime
				* result
				+ ((demandaUmidoForaPonta == null) ? 0 : demandaUmidoForaPonta
						.hashCode());
		result = prime
				* result
				+ ((demandaUmidoPonta == null) ? 0 : demandaUmidoPonta
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
		ContratoEnergiaDemanda other = (ContratoEnergiaDemanda) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (contrato == null) {
			if (other.contrato != null)
				return false;
		} else if (!contrato.equals(other.contrato))
			return false;
		if (dataFinal == null) {
			if (other.dataFinal != null)
				return false;
		} else if (!dataFinal.equals(other.dataFinal))
			return false;
		if (dataInicial == null) {
			if (other.dataInicial != null)
				return false;
		} else if (!dataInicial.equals(other.dataInicial))
			return false;
		if (demandaSecoForaPonta == null) {
			if (other.demandaSecoForaPonta != null)
				return false;
		} else if (!demandaSecoForaPonta.equals(other.demandaSecoForaPonta))
			return false;
		if (demandaSecoPonta == null) {
			if (other.demandaSecoPonta != null)
				return false;
		} else if (!demandaSecoPonta.equals(other.demandaSecoPonta))
			return false;
		if (demandaUmidoForaPonta == null) {
			if (other.demandaUmidoForaPonta != null)
				return false;
		} else if (!demandaUmidoForaPonta.equals(other.demandaUmidoForaPonta))
			return false;
		if (demandaUmidoPonta == null) {
			if (other.demandaUmidoPonta != null)
				return false;
		} else if (!demandaUmidoPonta.equals(other.demandaUmidoPonta))
			return false;
		return true;
	}

}
