package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SequenceGenerator(name="sequence_RSO", sequenceName="sequence_RSO", schema="operacao", initialValue=1, allocationSize=1)
@Table(name="rso",schema="operacao")
public class RSO implements BaseEntidade, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_RSO")
    @Column(name="rso_id", unique=true, nullable=false, precision=3, scale=0)
	private Integer codigo;
	
	@Column(name="rso_nome", nullable=false, length=50)
	private String descricao;

    @OneToOne
    @JoinColumn(name="eeat_id", nullable=false)
	private EEAT eeat = new EEAT();    
	
	@OneToOne
	@JoinColumn(name="mmed_identrada", nullable=false)
	private MacroMedidor medidorEntrada = new MacroMedidor();

	@Column(name="mmed_dtinstalacao")
	@Temporal(TemporalType.DATE)
	private Date dataInstalacao;

	@Column(name="mmed_tag", length=50)
	private String tag;

	@Column(name="rso_cmb")
	private Integer cmbQuantidade;

	@Column(name="rso_cmbmodelo", length=50)
	private String cmbModelo;
	
	@Column(name="rso_cmbvazao")
	private Double cmbVazao;

	@Column(name="rso_cmbpotencia")
	private Integer cmbPotencia;
	
	@Column(name="rso_cmbmca") //Metro por Coluna D´água
	private Double cmbMca;

	@Column(name="rso_volumeutil") 
	private Double volumeUtil;

	@Column(name="rso_alturautil") 
	private Double alturaUtil;
	
	@Column(name="rso_capacidade") 
	private Double capacidade;

    @Column(name="usur_id", nullable=false)
    private UsuarioProxy usuario = new UsuarioProxy();
    
    @Column(name="rso_tmultimaalteracao", nullable=false, insertable=false, columnDefinition="timestamp default current_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;

	public RSO() {
		super();
	}

	public RSO(Integer codigo, String descricao) {
		super();
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public EEAT getEeat() {
		return eeat;
	}

	public void setEeat(EEAT eeat) {
		this.eeat = eeat;
	}

	public MacroMedidor getMedidorEntrada() {
		return medidorEntrada;
	}

	public void setMedidorEntrada(MacroMedidor medidorEntrada) {
		this.medidorEntrada = medidorEntrada;
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

	public Integer getCmbQuantidade() {
		return cmbQuantidade;
	}

	public void setCmbQuantidade(Integer cmbQuantidade) {
		this.cmbQuantidade = cmbQuantidade;
	}

	public String getCmbModelo() {
		return cmbModelo;
	}

	public void setCmbModelo(String cmbModelo) {
		this.cmbModelo = cmbModelo;
	}

	public Double getCmbVazao() {
		return cmbVazao;
	}

	public void setCmbVazao(Double cmbVazao) {
		this.cmbVazao = cmbVazao;
	}

	public Integer getCmbPotencia() {
		return cmbPotencia;
	}

	public void setCmbPotencia(Integer cmbPotencia) {
		this.cmbPotencia = cmbPotencia;
	}

	public Double getCmbMca() {
		return cmbMca;
	}

	public void setCmbMca(Double cmbMca) {
		this.cmbMca = cmbMca;
	}

	public Double getVolumeUtil() {
		return volumeUtil;
	}

	public void setVolumeUtil(Double volumeUtil) {
		this.volumeUtil = volumeUtil;
	}

	public Double getAlturaUtil() {
		return alturaUtil;
	}

	public void setAlturaUtil(Double alturaUtil) {
		this.alturaUtil = alturaUtil;
	}

	public Double getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(Double capacidade) {
		this.capacidade = capacidade;
	}

	public UsuarioProxy getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioProxy usuario) {
		this.usuario = usuario;
	}

	public Date getUltimaAlteracao() {
		return ultimaAlteracao;
	}

	public void setUltimaAlteracao(Date ultimaAlteracao) {
		this.ultimaAlteracao = ultimaAlteracao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((alturaUtil == null) ? 0 : alturaUtil.hashCode());
		result = prime * result
				+ ((capacidade == null) ? 0 : capacidade.hashCode());
		result = prime * result + ((cmbMca == null) ? 0 : cmbMca.hashCode());
		result = prime * result
				+ ((cmbModelo == null) ? 0 : cmbModelo.hashCode());
		result = prime * result
				+ ((cmbPotencia == null) ? 0 : cmbPotencia.hashCode());
		result = prime * result
				+ ((cmbQuantidade == null) ? 0 : cmbQuantidade.hashCode());
		result = prime * result
				+ ((cmbVazao == null) ? 0 : cmbVazao.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((dataInstalacao == null) ? 0 : dataInstalacao.hashCode());
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((eeat == null) ? 0 : eeat.hashCode());
		result = prime * result
				+ ((medidorEntrada == null) ? 0 : medidorEntrada.hashCode());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result
				+ ((ultimaAlteracao == null) ? 0 : ultimaAlteracao.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result
				+ ((volumeUtil == null) ? 0 : volumeUtil.hashCode());
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
		RSO other = (RSO) obj;
		if (alturaUtil == null) {
			if (other.alturaUtil != null)
				return false;
		} else if (!alturaUtil.equals(other.alturaUtil))
			return false;
		if (capacidade == null) {
			if (other.capacidade != null)
				return false;
		} else if (!capacidade.equals(other.capacidade))
			return false;
		if (cmbMca == null) {
			if (other.cmbMca != null)
				return false;
		} else if (!cmbMca.equals(other.cmbMca))
			return false;
		if (cmbModelo == null) {
			if (other.cmbModelo != null)
				return false;
		} else if (!cmbModelo.equals(other.cmbModelo))
			return false;
		if (cmbPotencia == null) {
			if (other.cmbPotencia != null)
				return false;
		} else if (!cmbPotencia.equals(other.cmbPotencia))
			return false;
		if (cmbQuantidade == null) {
			if (other.cmbQuantidade != null)
				return false;
		} else if (!cmbQuantidade.equals(other.cmbQuantidade))
			return false;
		if (cmbVazao == null) {
			if (other.cmbVazao != null)
				return false;
		} else if (!cmbVazao.equals(other.cmbVazao))
			return false;
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
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (eeat == null) {
			if (other.eeat != null)
				return false;
		} else if (!eeat.equals(other.eeat))
			return false;
		if (medidorEntrada == null) {
			if (other.medidorEntrada != null)
				return false;
		} else if (!medidorEntrada.equals(other.medidorEntrada))
			return false;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		if (ultimaAlteracao == null) {
			if (other.ultimaAlteracao != null)
				return false;
		} else if (!ultimaAlteracao.equals(other.ultimaAlteracao))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		if (volumeUtil == null) {
			if (other.volumeUtil != null)
				return false;
		} else if (!volumeUtil.equals(other.volumeUtil))
			return false;
		return true;
	}
}
