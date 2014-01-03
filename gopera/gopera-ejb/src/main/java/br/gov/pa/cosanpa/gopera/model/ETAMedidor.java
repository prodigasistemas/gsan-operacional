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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SequenceGenerator(name="sequence_eta_medidor", sequenceName="sequence_eta_medidor", schema="operacao", initialValue=1, allocationSize=1)
@Table(name="eta_medidor",schema="operacao")
public class ETAMedidor implements BaseEntidade, Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_eta_medidor")
    @Column(name="etam_id", unique=true, nullable=false, precision=3, scale=0)
	private Integer codigo;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="eta_id")
    private ETA eta;

	@OneToOne
	@JoinColumn(name="mmed_idsaida", nullable=false)
	private MacroMedidor medidorSaida = new MacroMedidor();

	@Column(name="mmed_dtinstalacao")
	@Temporal(TemporalType.DATE)
	private Date dataInstalacao;

	@Column(name="mmed_tag", length=50)
	private String tag;
	
	public ETAMedidor() {
		super();
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ETA getEta() {
		return eta;
	}

	public void setEta(ETA eta) {
		this.eta = eta;
	}

	public MacroMedidor getMedidorSaida() {
		return medidorSaida;
	}

	public void setMedidorSaida(MacroMedidor medidorSaida) {
		this.medidorSaida = medidorSaida;
	}

	public Date getDataInstalacao() {
		return dataInstalacao;
	}

	public void setDataInstalacao(Date dataInstalacao) {
		this.dataInstalacao = dataInstalacao;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((dataInstalacao == null) ? 0 : dataInstalacao.hashCode());
		result = prime * result + ((eta == null) ? 0 : eta.hashCode());
		result = prime * result
				+ ((medidorSaida == null) ? 0 : medidorSaida.hashCode());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
		ETAMedidor other = (ETAMedidor) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (dataInstalacao == null) {
			if (other.dataInstalacao != null)
				return false;
		} else if (!dataInstalacao.equals(other.dataInstalacao))
			return false;
		if (eta == null) {
			if (other.eta != null)
				return false;
		} else if (!eta.equals(other.eta))
			return false;
		if (medidorSaida == null) {
			if (other.medidorSaida != null)
				return false;
		} else if (!medidorSaida.equals(other.medidorSaida))
			return false;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}
}
