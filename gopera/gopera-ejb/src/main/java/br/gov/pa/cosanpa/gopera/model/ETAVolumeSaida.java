package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@SequenceGenerator(name="sequence_eta_volume_saida", sequenceName="sequence_eta_volume_saida", schema="operacao", initialValue=1, allocationSize=1)
@Table(name="eta_volume_saida",schema="operacao")
public class ETAVolumeSaida implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_eta_volume_saida")
	@Column(name="etas_id", unique=true, nullable=false, precision=3, scale=0)		
	private Integer codigo;

	@OneToOne
	@JoinColumn(name="etav_id", nullable=false)
	private ETAVolume etaVolume;

	@Column(name="etas_volume")
	private Double valorMedicaoSaida;

	@Transient	
	private String valorMedicaoSaidaAux;
	
	@Column(name="mmed_tipomedicao")
	private Integer tipomedicao;
	
	@OneToOne
	@JoinColumn(name="mmed_idsaida", nullable=false)
	private MacroMedidor medidorSaida;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ETAVolume getEtaVolume() {
		return etaVolume;
	}

	public void setEtaVolume(ETAVolume etaVolume) {
		this.etaVolume = etaVolume;
	}

	public Double getValorMedicaoSaida() {
		return valorMedicaoSaida;
	}

	public void setValorMedicaoSaida(Double valorMedicaoSaida) {
		this.valorMedicaoSaida = valorMedicaoSaida;
	}

	public String getValorMedicaoSaidaAux() {
		return valorMedicaoSaidaAux;
	}

	public void setValorMedicaoSaidaAux(String valorMedicaoSaidaAux) {
		this.valorMedicaoSaidaAux = valorMedicaoSaidaAux;
	}

	public Integer getTipomedicao() {
		return tipomedicao;
	}

	public void setTipomedicao(Integer tipomedicao) {
		this.tipomedicao = tipomedicao;
	}

	public MacroMedidor getMedidorSaida() {
		return medidorSaida;
	}

	public void setMedidorSaida(MacroMedidor medidorSaida) {
		this.medidorSaida = medidorSaida;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((etaVolume == null) ? 0 : etaVolume.hashCode());
		result = prime * result
				+ ((medidorSaida == null) ? 0 : medidorSaida.hashCode());
		result = prime * result
				+ ((tipomedicao == null) ? 0 : tipomedicao.hashCode());
		result = prime
				* result
				+ ((valorMedicaoSaida == null) ? 0 : valorMedicaoSaida
						.hashCode());
		result = prime
				* result
				+ ((valorMedicaoSaidaAux == null) ? 0 : valorMedicaoSaidaAux
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
		ETAVolumeSaida other = (ETAVolumeSaida) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (etaVolume == null) {
			if (other.etaVolume != null)
				return false;
		} else if (!etaVolume.equals(other.etaVolume))
			return false;
		if (medidorSaida == null) {
			if (other.medidorSaida != null)
				return false;
		} else if (!medidorSaida.equals(other.medidorSaida))
			return false;
		if (tipomedicao == null) {
			if (other.tipomedicao != null)
				return false;
		} else if (!tipomedicao.equals(other.tipomedicao))
			return false;
		if (valorMedicaoSaida == null) {
			if (other.valorMedicaoSaida != null)
				return false;
		} else if (!valorMedicaoSaida.equals(other.valorMedicaoSaida))
			return false;
		if (valorMedicaoSaidaAux == null) {
			if (other.valorMedicaoSaidaAux != null)
				return false;
		} else if (!valorMedicaoSaidaAux.equals(other.valorMedicaoSaidaAux))
			return false;
		return true;
	}
}
