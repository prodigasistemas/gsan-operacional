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
@SequenceGenerator(name="sequence_rede_instalada", sequenceName="sequence_rede_instalada", schema="operacao", initialValue=1, allocationSize=1)
@Table(name="rede_instalada",schema="operacao")
public class RedeInstalada implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_rede_instalada")
	@Column(name="rdin_id", unique=true, nullable=false, precision=3, scale=0)		
	private Integer codigo;

	@Column(name="rdin_referencia", nullable=false)
	@Temporal(TemporalType.DATE)
	private Date referencia;

	@OneToOne//(cascade={CascadeType.DETACH}, fetch=FetchType.LAZY)
	@JoinColumn(name="greg_id", nullable=false)	
	private RegionalProxy regionalProxy = new RegionalProxy();
	
	@OneToOne//(cascade={CascadeType.DETECH}, fetch=FetchType.LAZY)
	@JoinColumn(name="uneg_id", nullable=false)
	private UnidadeNegocioProxy unidadeNegocioProxy = new UnidadeNegocioProxy();
	
	@OneToOne//(cascade={CascadeType.DETECH}, fetch=FetchType.LAZY)
	@JoinColumn(name="muni_id", nullable=false)
	private MunicipioProxy municipioProxy = new MunicipioProxy();
	
	@OneToOne//(cascade={CascadeType.DETECH}, fetch=FetchType.LAZY)
	@JoinColumn(name="loca_id", nullable=false)
	private LocalidadeProxy localidadeProxy = new LocalidadeProxy();
	
	@Column(name="rdin_cadastrada", nullable=false)
	private Double redeCadastrada;

	@Column(name="rdin_existente", nullable=false)
	private Double redeExistente;
	
    @Column(name="usur_id", nullable=false)
    private UsuarioProxy usuario = new UsuarioProxy();
    
    @Column(name="rdin_tmultimaalteracao", nullable=false, insertable=false, columnDefinition="timestamp default current_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;

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

	public Double getRedeCadastrada() {
		return redeCadastrada;
	}

	public void setRedeCadastrada(Double redeCadastrada) {
		this.redeCadastrada = redeCadastrada;
	}

	public Double getRedeExistente() {
		return redeExistente;
	}

	public void setRedeExistente(Double redeExistente) {
		this.redeExistente = redeExistente;
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
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((localidadeProxy == null) ? 0 : localidadeProxy.hashCode());
		result = prime * result
				+ ((municipioProxy == null) ? 0 : municipioProxy.hashCode());
		result = prime * result
				+ ((redeCadastrada == null) ? 0 : redeCadastrada.hashCode());
		result = prime * result
				+ ((redeExistente == null) ? 0 : redeExistente.hashCode());
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
		RedeInstalada other = (RedeInstalada) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
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
		if (redeCadastrada == null) {
			if (other.redeCadastrada != null)
				return false;
		} else if (!redeCadastrada.equals(other.redeCadastrada))
			return false;
		if (redeExistente == null) {
			if (other.redeExistente != null)
				return false;
		} else if (!redeExistente.equals(other.redeExistente))
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
		return true;
	}
}
