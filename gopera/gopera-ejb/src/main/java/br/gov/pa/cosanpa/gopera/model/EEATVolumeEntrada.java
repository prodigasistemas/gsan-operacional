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
@SequenceGenerator(name="sequence_eeat_volume_entrada", sequenceName="sequence_eeat_volume_entrada", schema="operacao", initialValue=1, allocationSize=1)
@Table(name="eeat_volume_entrada",schema="operacao")
public class EEATVolumeEntrada implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_eeat_volume_entrada")
	@Column(name="eate_id", unique=true, nullable=false, precision=3, scale=0)		
	private Integer codigo;

	@OneToOne
	@JoinColumn(name="eatv_id", nullable=false)
	private EEATVolume eeatVolume;

	@Column(name="eate_volume")
	private Double valorMedicaoEntrada;

	@Transient	
	private String valorMedicaoEntradaAux;
	
	@Column(name="mmed_tipomedicao")
	private Integer tipomedicao;
	
	@OneToOne
	@JoinColumn(name="mmed_identrada", nullable=false)
	private MacroMedidor medidorEntrada;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public EEATVolume getEeatVolume() {
		return eeatVolume;
	}

	public void setEeatVolume(EEATVolume eeatVolume) {
		this.eeatVolume = eeatVolume;
	}

	public Double getValorMedicaoEntrada() {
		return valorMedicaoEntrada;
	}

	public void setValorMedicaoEntrada(Double valorMedicaoEntrada) {
		this.valorMedicaoEntrada = valorMedicaoEntrada;
	}

	public String getValorMedicaoEntradaAux() {
		return valorMedicaoEntradaAux;
	}

	public void setValorMedicaoEntradaAux(String valorMedicaoEntradaAux) {
		this.valorMedicaoEntradaAux = valorMedicaoEntradaAux;
	}

	public Integer getTipomedicao() {
		return tipomedicao;
	}

	public void setTipomedicao(Integer tipomedicao) {
		this.tipomedicao = tipomedicao;
	}

	public MacroMedidor getMedidorEntrada() {
		return medidorEntrada;
	}

	public void setMedidorEntrada(MacroMedidor medidorEntrada) {
		this.medidorEntrada = medidorEntrada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((eeatVolume == null) ? 0 : eeatVolume.hashCode());
		result = prime * result
				+ ((medidorEntrada == null) ? 0 : medidorEntrada.hashCode());
		result = prime * result
				+ ((tipomedicao == null) ? 0 : tipomedicao.hashCode());
		result = prime
				* result
				+ ((valorMedicaoEntrada == null) ? 0 : valorMedicaoEntrada
						.hashCode());
		result = prime
				* result
				+ ((valorMedicaoEntradaAux == null) ? 0
						: valorMedicaoEntradaAux.hashCode());
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
		EEATVolumeEntrada other = (EEATVolumeEntrada) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (eeatVolume == null) {
			if (other.eeatVolume != null)
				return false;
		} else if (!eeatVolume.equals(other.eeatVolume))
			return false;
		if (medidorEntrada == null) {
			if (other.medidorEntrada != null)
				return false;
		} else if (!medidorEntrada.equals(other.medidorEntrada))
			return false;
		if (tipomedicao == null) {
			if (other.tipomedicao != null)
				return false;
		} else if (!tipomedicao.equals(other.tipomedicao))
			return false;
		if (valorMedicaoEntrada == null) {
			if (other.valorMedicaoEntrada != null)
				return false;
		} else if (!valorMedicaoEntrada.equals(other.valorMedicaoEntrada))
			return false;
		if (valorMedicaoEntradaAux == null) {
			if (other.valorMedicaoEntradaAux != null)
				return false;
		} else if (!valorMedicaoEntradaAux.equals(other.valorMedicaoEntradaAux))
			return false;
		return true;
	}

}
