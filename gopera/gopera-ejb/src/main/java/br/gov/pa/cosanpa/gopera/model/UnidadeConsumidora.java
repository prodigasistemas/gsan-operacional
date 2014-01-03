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
@SequenceGenerator(name="sequence_unidade_consumidora", sequenceName="sequence_unidade_consumidora", schema="operacao", initialValue=1, allocationSize=1)
@Table(name="unidade_consumidora",schema="operacao")
public class UnidadeConsumidora implements BaseEntidade, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_unidade_consumidora")
    @Column(name="ucon_id", unique=true, nullable=false, precision=3, scale=0)
	private Integer codigo;
	
	@Column(name="ucon_nmconsumidora", nullable=false, length=50)
	private String descricao;

    @Column(name="ucon_uc", nullable=false, precision=12, scale=0)
	private Integer uc;

	@OneToOne
	@JoinColumn(name="greg_id")
	private RegionalProxy regionalProxy = new RegionalProxy();
    
	@OneToOne
	@JoinColumn(name="uneg_id", nullable=false)
	private UnidadeNegocioProxy unidadeNegocioProxy = new UnidadeNegocioProxy();
	
	//@OneToOne(fetch=FetchType.LAZY)
	@OneToOne
	@JoinColumn(name="muni_id", nullable=false)
	private MunicipioProxy municipioProxy = new MunicipioProxy();
	  
	//@OneToOne(fetch=FetchType.LAZY)
	@OneToOne
	@JoinColumn(name="loca_id", nullable=false)
	private LocalidadeProxy localidadeProxy = new LocalidadeProxy();

	@Column(name="ucon_undoperacional", nullable=false, length=100)
	private String unidadeOperacional;

	@Column(name="ucon_natureza", nullable=false, length=100)
	private String naturezaAtividade;
	
	@Column(name="ucon_endereco", nullable=false, length=100)
	private String endereco;

	@Column(name="ucon_endereconumero", nullable=false, length=10)
	private String numero;

	@Column(name="ucon_enderecocomplemento", nullable=false, length=100)
	private String complemento;
	
	@Column(name="ucon_bairro", nullable=false, length=50)
	private String bairro;

	@Column(name="ucon_cep", nullable=false, length=9)
	private String cep;	
	
	@Column(name="ucon_ativo")
	private Boolean ativo;	
	
	@Column(name="ucon_equipamento")
	private String equipamento;	
	
	@Column(name="ucon_dtinstalacao")
    @Temporal(TemporalType.DATE)	
	private Date dataInstalacao;	

	@Column(name="ucon_potencia")
	private String potencia;	

	@Column(name="ucon_alimentador")
	private String alimentador;	
	                                         
    @OneToMany(mappedBy="UC", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<UnidadeConsumidoraOperacional> operacional = new ArrayList<UnidadeConsumidoraOperacional>(); 
	
    @Column(name="usur_id", nullable=false)
    private UsuarioProxy usuario = new UsuarioProxy();
    
    @Column(name="ucon_tmultimaalteracao", nullable=false, insertable=false, columnDefinition="timestamp default current_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;

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

	public Integer getUc() {
		return uc;
	}

	public void setUc(Integer uc) {
		this.uc = uc;
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

	public String getUnidadeOperacional() {
		return unidadeOperacional;
	}

	public void setUnidadeOperacional(String unidadeOperacional) {
		this.unidadeOperacional = unidadeOperacional;
	}

	public String getNaturezaAtividade() {
		return naturezaAtividade;
	}

	public void setNaturezaAtividade(String naturezaAtividade) {
		this.naturezaAtividade = naturezaAtividade;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public String getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(String equipamento) {
		this.equipamento = equipamento;
	}

	public Date getDataInstalacao() {
		return dataInstalacao;
	}

	public void setDataInstalacao(Date dataInstalacao) {
		this.dataInstalacao = dataInstalacao;
	}

	public String getPotencia() {
		return potencia;
	}

	public void setPotencia(String potencia) {
		this.potencia = potencia;
	}

	public String getAlimentador() {
		return alimentador;
	}

	public void setAlimentador(String alimentador) {
		this.alimentador = alimentador;
	}

	public List<UnidadeConsumidoraOperacional> getOperacional() {
		return operacional;
	}

	public void setOperacional(List<UnidadeConsumidoraOperacional> operacional) {
		this.operacional = operacional;
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
				+ ((alimentador == null) ? 0 : alimentador.hashCode());
		result = prime * result + ((ativo == null) ? 0 : ativo.hashCode());
		result = prime * result + ((bairro == null) ? 0 : bairro.hashCode());
		result = prime * result + ((cep == null) ? 0 : cep.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((complemento == null) ? 0 : complemento.hashCode());
		result = prime * result
				+ ((dataInstalacao == null) ? 0 : dataInstalacao.hashCode());
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result
				+ ((equipamento == null) ? 0 : equipamento.hashCode());
		result = prime * result
				+ ((localidadeProxy == null) ? 0 : localidadeProxy.hashCode());
		result = prime * result
				+ ((municipioProxy == null) ? 0 : municipioProxy.hashCode());
		result = prime
				* result
				+ ((naturezaAtividade == null) ? 0 : naturezaAtividade
						.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result
				+ ((operacional == null) ? 0 : operacional.hashCode());
		result = prime * result
				+ ((potencia == null) ? 0 : potencia.hashCode());
		result = prime * result
				+ ((regionalProxy == null) ? 0 : regionalProxy.hashCode());
		result = prime * result + ((uc == null) ? 0 : uc.hashCode());
		result = prime * result
				+ ((ultimaAlteracao == null) ? 0 : ultimaAlteracao.hashCode());
		result = prime
				* result
				+ ((unidadeNegocioProxy == null) ? 0 : unidadeNegocioProxy
						.hashCode());
		result = prime
				* result
				+ ((unidadeOperacional == null) ? 0 : unidadeOperacional
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
		UnidadeConsumidora other = (UnidadeConsumidora) obj;
		if (alimentador == null) {
			if (other.alimentador != null)
				return false;
		} else if (!alimentador.equals(other.alimentador))
			return false;
		if (ativo == null) {
			if (other.ativo != null)
				return false;
		} else if (!ativo.equals(other.ativo))
			return false;
		if (bairro == null) {
			if (other.bairro != null)
				return false;
		} else if (!bairro.equals(other.bairro))
			return false;
		if (cep == null) {
			if (other.cep != null)
				return false;
		} else if (!cep.equals(other.cep))
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (complemento == null) {
			if (other.complemento != null)
				return false;
		} else if (!complemento.equals(other.complemento))
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
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		if (equipamento == null) {
			if (other.equipamento != null)
				return false;
		} else if (!equipamento.equals(other.equipamento))
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
		if (naturezaAtividade == null) {
			if (other.naturezaAtividade != null)
				return false;
		} else if (!naturezaAtividade.equals(other.naturezaAtividade))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (operacional == null) {
			if (other.operacional != null)
				return false;
		} else if (!operacional.equals(other.operacional))
			return false;
		if (potencia == null) {
			if (other.potencia != null)
				return false;
		} else if (!potencia.equals(other.potencia))
			return false;
		if (regionalProxy == null) {
			if (other.regionalProxy != null)
				return false;
		} else if (!regionalProxy.equals(other.regionalProxy))
			return false;
		if (uc == null) {
			if (other.uc != null)
				return false;
		} else if (!uc.equals(other.uc))
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
		if (unidadeOperacional == null) {
			if (other.unidadeOperacional != null)
				return false;
		} else if (!unidadeOperacional.equals(other.unidadeOperacional))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UnidadeConsumidora [descricao=" + descricao + ", uc=" + uc + "]";
	}
}
