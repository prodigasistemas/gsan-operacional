package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SequenceGenerator(name="sequence_eeat", sequenceName="sequence_eeat", schema="operacao", initialValue=1, allocationSize=1)
@Table(name="eeat",schema="operacao")
public class EEAT implements BaseEntidade, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_eeat")
    @Column(name="eeat_id", unique=true, nullable=false, precision=3, scale=0)
	private Integer codigo;
	
	@Column(name="eeat_nome", nullable=false, length=50)
	private String descricao;

	@Column(name="eeat_tipo", nullable=false)
	private Integer tipo;
	
    @OneToMany(mappedBy="eeat", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<EEATFonteCaptacao> fonteCaptacao = new ArrayList<EEATFonteCaptacao>();    

    @OneToMany(mappedBy="eeat", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<EEATMedidor> medidorSaida = new ArrayList<EEATMedidor>();        
    
	@Column(name="eeat_cmb")
	private Integer cmbQuantidade;

	@Column(name="eeat_cmbmodelo", length=50)
	private String cmbModelo;
	
	@Column(name="eeat_cmbvazao")
	private Double cmbVazao;

	@Column(name="eeat_cmbpotencia")
	private Integer cmbPotencia;
	
	@Column(name="eeat_cmbmca") //Metro por Coluna D´água
	private Double cmbMca;

	@Column(name="eeat_volumeutil") 
	private Double volumeUtil;

	@Column(name="eeat_alturautil") 
	private Double alturaUtil;
	
	@Column(name="eeat_capacidade") 
	private Double capacidade;

    @Column(name="usur_id", nullable=false)
    private UsuarioProxy usuario = new UsuarioProxy();
    
    @Column(name="eeat_tmultimaalteracao", nullable=false, insertable=false, columnDefinition="timestamp default current_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;

	public EEAT() {
		super();
	}

	public EEAT(Integer codigo, String descricao) {
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

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public List<EEATFonteCaptacao> getFonteCaptacao() {
		return fonteCaptacao;
	}

	public void setFonteCaptacao(List<EEATFonteCaptacao> fonteCaptacao) {
		this.fonteCaptacao = fonteCaptacao;
	}

	public List<EEATMedidor> getMedidorSaida() {
		return medidorSaida;
	}

	public void setMedidorSaida(List<EEATMedidor> medidorSaida) {
		this.medidorSaida = medidorSaida;
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
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result
				+ ((fonteCaptacao == null) ? 0 : fonteCaptacao.hashCode());
		result = prime * result
				+ ((medidorSaida == null) ? 0 : medidorSaida.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		EEAT other = (EEAT) obj;
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
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (fonteCaptacao == null) {
			if (other.fonteCaptacao != null)
				return false;
		} else if (!fonteCaptacao.equals(other.fonteCaptacao))
			return false;
		if (medidorSaida == null) {
			if (other.medidorSaida != null)
				return false;
		} else if (!medidorSaida.equals(other.medidorSaida))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
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

	public String toString() {
		return "EEAT [codigo=" + codigo + ", descricao=" + descricao + "]";
	}
}
