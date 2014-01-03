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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SequenceGenerator(name="sequence_registroconsumoeat", sequenceName="sequence_registroconsumoeat", schema="operacao", initialValue=1, allocationSize=1) 
@Table(name="registroconsumoeat",schema="operacao")
public class RegistroConsumoEAT implements Serializable {
	
	private static final long serialVersionUID = 2033942816477757171L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_registroconsumoeat")
	@Column(name="rgcs_id")	
	private Integer codigo;
	@OneToOne
	@JoinColumn(name="greg_id", nullable=false)
	private RegionalProxy regionalProxy = new RegionalProxy();
	@OneToOne
	@JoinColumn(name="uneg_id", nullable=false)
	private UnidadeNegocioProxy unidadeNegocioProxy = new UnidadeNegocioProxy();
	@OneToOne
	@JoinColumn(name="muni_id", nullable=false)
	private MunicipioProxy municipioProxy = new MunicipioProxy();
	@OneToOne
	@JoinColumn(name="loca_id", nullable=false)
	private LocalidadeProxy localidadeProxy = new LocalidadeProxy();
	@OneToOne
	@JoinColumn(name="eat_id", nullable=false)
	private EEAT eat = new EEAT();
	
	@ManyToMany(fetch=FetchType.EAGER,cascade={CascadeType.MERGE})
	@JoinTable(name="registroconsumoeat_registroconsumo", schema="operacao", joinColumns=@JoinColumn(name="rgcs_id"), inverseJoinColumns= @JoinColumn(name="regc_id"))
	private List<RegistroConsumo> registrosConsumo = new ArrayList<RegistroConsumo>();

    @Column(name="usur_id", nullable=false)
    private UsuarioProxy usuario = new UsuarioProxy();
    
    @Column(name="rgcs_tmultimaalteracao", nullable=false, insertable=false, columnDefinition="timestamp default current_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;
	
	public RegistroConsumoEAT() {
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public UnidadeNegocioProxy getUnidadeNegocioProxy() {
		return unidadeNegocioProxy;
	}

	public void setUnidadeNegocioProxy(UnidadeNegocioProxy unidadeNegocioProxy) {
		this.unidadeNegocioProxy = unidadeNegocioProxy;
	}

	public RegionalProxy getRegionalProxy() {
		return regionalProxy;
	}

	public void setRegionalProxy(RegionalProxy regionalProxy) {
		this.regionalProxy = regionalProxy;
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

	public EEAT getEat() {
		return eat;
	}

	public void setEat(EEAT eat) {
		this.eat = eat;
	}

	public List<RegistroConsumo> getRegistrosConsumo() {
		return registrosConsumo;
	}

	public void setRegistrosConsumo(List<RegistroConsumo> registrosConsumo) {
		this.registrosConsumo = registrosConsumo;
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
		result = prime * result + ((eat == null) ? 0 : eat.hashCode());
		result = prime * result
				+ ((localidadeProxy == null) ? 0 : localidadeProxy.hashCode());
		result = prime * result
				+ ((municipioProxy == null) ? 0 : municipioProxy.hashCode());
		result = prime * result
				+ ((regionalProxy == null) ? 0 : regionalProxy.hashCode());
		result = prime
				* result
				+ ((registrosConsumo == null) ? 0 : registrosConsumo.hashCode());
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
		RegistroConsumoEAT other = (RegistroConsumoEAT) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (eat == null) {
			if (other.eat != null)
				return false;
		} else if (!eat.equals(other.eat))
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
		if (regionalProxy == null) {
			if (other.regionalProxy != null)
				return false;
		} else if (!regionalProxy.equals(other.regionalProxy))
			return false;
		if (registrosConsumo == null) {
			if (other.registrosConsumo != null)
				return false;
		} else if (!registrosConsumo.equals(other.registrosConsumo))
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
