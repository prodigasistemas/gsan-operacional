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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SequenceGenerator(name="sequence_eta_volume", sequenceName="sequence_eta_volume", schema="operacao", initialValue=1, allocationSize=1)
@Table(name="eta_volume",schema="operacao")
public class ETAVolume implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_eta_volume")
	@Column(name="etav_id", unique=true, nullable=false, precision=3, scale=0)		
	private Integer codigo;

	@Column(name="etav_referencia")
	@Temporal(TemporalType.DATE)
	private Date referencia;
	
	@Column(name="etav_tmmedicao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataHoraMedicao;

	@Column(name="etav_estimado")
	private Boolean estimado;
	
	@Column(name="etav_volume")
	private Double volume;
	
	@OneToOne//(cascade={CascadeType.DETACH}, fetch=FetchType.LAZY)
	@JoinColumn(name="greg_id")
	private RegionalProxy regionalProxy = new RegionalProxy();;
	
	@OneToOne//(cascade={CascadeType.DETACH}, fetch=FetchType.LAZY)
	@JoinColumn(name="uneg_id")
	private UnidadeNegocioProxy unidadeNegocioProxy = new UnidadeNegocioProxy();
	
	@OneToOne//(cascade={CascadeType.DETACH}, fetch=FetchType.LAZY)
	@JoinColumn(name="muni_id")
	private MunicipioProxy municipioProxy = new MunicipioProxy();
	
	@OneToOne//(cascade={CascadeType.DETACH}, fetch=FetchType.LAZY)
	@JoinColumn(name="loca_id")
	private LocalidadeProxy localidadeProxy = new LocalidadeProxy();
	
	@OneToOne
	@JoinColumn(name="eta_id", nullable=false)
	private ETA eta;
	
	@OneToMany(mappedBy="etaVolume", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<ETAVolumeEntrada> volumeEntrada = new ArrayList<ETAVolumeEntrada>();    

	@OneToMany(mappedBy="etaVolume", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<ETAVolumeSaida> volumeSaida = new ArrayList<ETAVolumeSaida>();  
	
    @Column(name="usur_id", nullable=false)
    private UsuarioProxy usuario = new UsuarioProxy();
    
    @Column(name="etav_tmultimaalteracao", nullable=false, insertable=false, columnDefinition="timestamp default current_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;
    
    @Column
    private String observacoes;
	
	public ETAVolume(){
		eta = new ETA();
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getReferencia() {
		return referencia;
	}

	public void setReferencia(Date referencia) {
		this.referencia = referencia;
	}

	public Date getDataHoraMedicao() {
		return dataHoraMedicao;
	}

	public void setDataHoraMedicao(Date dataHoraMedicao) {
		this.dataHoraMedicao = dataHoraMedicao;
	}

	public Boolean getEstimado() {
		return estimado;
	}

	public void setEstimado(Boolean estimado) {
		this.estimado = estimado;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public RegionalProxy getRegionalProxy() {
		return regionalProxy;
	}

	public void setRegionalProxy(RegionalProxy regionalProxy) {
		this.regionalProxy = regionalProxy;
	}

	public UnidadeNegocioProxy getUnidadeNegocioProxy() {
		return unidadeNegocioProxy;
	}

	public void setUnidadeNegocioProxy(UnidadeNegocioProxy unidadeNegocioProxy) {
		this.unidadeNegocioProxy = unidadeNegocioProxy;
	}

	public MunicipioProxy getMunicipioProxy() {
		return municipioProxy;
	}

	public void setMunicipioProxy(MunicipioProxy municipioProxy) {
		this.municipioProxy = municipioProxy;
	}

	public LocalidadeProxy getLocalidadeProxy() {
		return localidadeProxy;
	}

	public void setLocalidadeProxy(LocalidadeProxy localidadeProxy) {
		this.localidadeProxy = localidadeProxy;
	}

	public ETA getEta() {
		return eta;
	}

	public void setEta(ETA eta) {
		this.eta = eta;
	}

	public List<ETAVolumeEntrada> getVolumeEntrada() {
		return volumeEntrada;
	}

	public void setVolumeEntrada(List<ETAVolumeEntrada> volumeEntrada) {
		this.volumeEntrada = volumeEntrada;
	}

	public List<ETAVolumeSaida> getVolumeSaida() {
		return volumeSaida;
	}

	public void setVolumeSaida(List<ETAVolumeSaida> volumeSaida) {
		this.volumeSaida = volumeSaida;
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
	
	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((dataHoraMedicao == null) ? 0 : dataHoraMedicao.hashCode());
		result = prime * result
				+ ((estimado == null) ? 0 : estimado.hashCode());
		result = prime * result + ((eta == null) ? 0 : eta.hashCode());
		result = prime * result
				+ ((localidadeProxy == null) ? 0 : localidadeProxy.hashCode());
		result = prime * result
				+ ((municipioProxy == null) ? 0 : municipioProxy.hashCode());
		result = prime * result
				+ ((referencia == null) ? 0 : referencia.hashCode());
		result = prime * result
				+ ((regionalProxy == null) ? 0 : regionalProxy.hashCode());
		result = prime * result
				+ ((ultimaAlteracao == null) ? 0 : ultimaAlteracao.hashCode());
		result = prime
				* result
				+ ((unidadeNegocioProxy == null) ? 0 : unidadeNegocioProxy
						.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + ((volume == null) ? 0 : volume.hashCode());
		result = prime * result
				+ ((volumeEntrada == null) ? 0 : volumeEntrada.hashCode());
		result = prime * result
				+ ((volumeSaida == null) ? 0 : volumeSaida.hashCode());
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
		ETAVolume other = (ETAVolume) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (dataHoraMedicao == null) {
			if (other.dataHoraMedicao != null)
				return false;
		} else if (!dataHoraMedicao.equals(other.dataHoraMedicao))
			return false;
		if (estimado == null) {
			if (other.estimado != null)
				return false;
		} else if (!estimado.equals(other.estimado))
			return false;
		if (eta == null) {
			if (other.eta != null)
				return false;
		} else if (!eta.equals(other.eta))
			return false;
		if (localidadeProxy == null) {
			if (other.localidadeProxy != null)
				return false;
		} else if (!localidadeProxy.equals(other.localidadeProxy))
			return false;
		if (municipioProxy == null) {
			if (other.municipioProxy != null)
				return false;
		} else if (!municipioProxy.equals(other.municipioProxy))
			return false;
		if (referencia == null) {
			if (other.referencia != null)
				return false;
		} else if (!referencia.equals(other.referencia))
			return false;
		if (regionalProxy == null) {
			if (other.regionalProxy != null)
				return false;
		} else if (!regionalProxy.equals(other.regionalProxy))
			return false;
		if (ultimaAlteracao == null) {
			if (other.ultimaAlteracao != null)
				return false;
		} else if (!ultimaAlteracao.equals(other.ultimaAlteracao))
			return false;
		if (unidadeNegocioProxy == null) {
			if (other.unidadeNegocioProxy != null)
				return false;
		} else if (!unidadeNegocioProxy.equals(other.unidadeNegocioProxy))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		if (volume == null) {
			if (other.volume != null)
				return false;
		} else if (!volume.equals(other.volume))
			return false;
		if (volumeEntrada == null) {
			if (other.volumeEntrada != null)
				return false;
		} else if (!volumeEntrada.equals(other.volumeEntrada))
			return false;
		if (volumeSaida == null) {
			if (other.volumeSaida != null)
				return false;
		} else if (!volumeSaida.equals(other.volumeSaida))
			return false;
		return true;
	}

	public String toString() {
		return "ETAVolume [codigo=" + codigo + ", eta=" + eta + "]";
	}
}
