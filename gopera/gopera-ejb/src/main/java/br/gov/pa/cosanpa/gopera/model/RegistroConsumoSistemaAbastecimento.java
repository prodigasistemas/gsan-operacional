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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="registroconsumosistemaabastecimento",schema="operacao")
public class RegistroConsumoSistemaAbastecimento implements Serializable {
	
	private static final long serialVersionUID = 2033942816477757171L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
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
	@JoinColumn(name="sabs_id", nullable=false)
	private SistemaAbastecimentoProxy sistemaAbastecimentoProxy;	
	@ManyToMany(fetch=FetchType.LAZY,cascade=CascadeType.MERGE)
	@JoinTable(name="registroconsumosistemaabastecimento_registroconsumo", schema="operacao", joinColumns=@JoinColumn(name="rgcs_id"), inverseJoinColumns= @JoinColumn(name="regc_id"))
	private List<RegistroConsumo> registrosConsumo = new ArrayList<RegistroConsumo>();

    @Column(name="usur_id", nullable=false)
    private UsuarioProxy usuario = new UsuarioProxy();
    
    @Column(name="rgcs_tmultimaalteracao", nullable=false, insertable=false, columnDefinition="timestamp default current_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;
	
	public RegistroConsumoSistemaAbastecimento() {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<RegistroConsumo> getRegistrosConsumo() {
		return registrosConsumo;
	}

	public void setRegistrosConsumo(List<RegistroConsumo> registrosConsumo) {
		this.registrosConsumo = registrosConsumo;
	}

	public SistemaAbastecimentoProxy getSistemaAbastecimentoProxy() {
		return sistemaAbastecimentoProxy;
	}

	public void setSistemaAbastecimentoProxy(SistemaAbastecimentoProxy sistemaAbastecimentoProxy) {
		this.sistemaAbastecimentoProxy = sistemaAbastecimentoProxy;
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
}
