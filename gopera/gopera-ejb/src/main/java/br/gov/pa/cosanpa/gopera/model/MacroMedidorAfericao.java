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
@SequenceGenerator(name="sequence_macro_medidor_afericao", sequenceName="sequence_macro_medidor_afericao", schema="operacao", initialValue=1, allocationSize=1)
@Table(name="macro_medidor_afericao",schema="operacao")
public class MacroMedidorAfericao implements BaseEntidade, Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_macro_medidor_afericao")
    @Column(name="meda_id", unique=true, nullable=false, precision=3, scale=0)
	private Integer codigo;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="mmed_id")
    private MacroMedidor medidor;

	@Column(name="meda_dtafericao")
	@Temporal(TemporalType.DATE)
	private Date dataAfericao;

	public MacroMedidorAfericao() {
		super();
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public MacroMedidor getMedidor() {
		return medidor;
	}

	public void setMedidor(MacroMedidor medidor) {
		this.medidor = medidor;
	}

	public Date getDataAfericao() {
		return dataAfericao;
	}

	public void setDataAfericao(Date dataAfericao) {
		this.dataAfericao = dataAfericao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((dataAfericao == null) ? 0 : dataAfericao.hashCode());
		result = prime * result + ((medidor == null) ? 0 : medidor.hashCode());
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
		MacroMedidorAfericao other = (MacroMedidorAfericao) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (dataAfericao == null) {
			if (other.dataAfericao != null)
				return false;
		} else if (!dataAfericao.equals(other.dataAfericao))
			return false;
		if (medidor == null) {
			if (other.medidor != null)
				return false;
		} else if (!medidor.equals(other.medidor))
			return false;
		return true;
	}
}
