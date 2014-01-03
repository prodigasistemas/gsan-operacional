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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="contrato_energia",schema="operacao")
public class ContratoEnergia implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="cene_id", unique=true, nullable=false, precision=3, scale=0)
	private Integer codigo;
	
	@Column(name="cene_nmcontrato", nullable=false, length=50)
	private String numeroContrato;

	@ManyToOne//(fetch=FetchType.LAZY)
	@JoinColumn(name="ucon_id", nullable=false)
	private UnidadeConsumidora unidadeConsumidora = new UnidadeConsumidora();
	
	@Column(name="cene_tensaonominal")
	private Double tensaoNominal;

	@Column(name="cene_tensaocontratada")
	private Double tensaoContratada;

	@Column(name="cene_subgrupotarifario", length=20)
	private String subGrupoTarifario;

	@Column(name="cene_frequencia")
	private Double frequencia;

	@Column(name="cene_perdastransformacao")
	private Double perdasTransformacao;

	@Column(name="cene_potenciainstalada")
	private Double potenciaInstalada;

	@Column(name="cene_horariopontaini")
	@Temporal(TemporalType.TIME)
	private Date horarioPontaInicial;

	@Column(name="cene_horariopontafim")
	@Temporal(TemporalType.TIME)
	private Date horarioPontaFinal;
	
	@Column(name="cene_horarioreservadoini")
	@Temporal(TemporalType.TIME)
	private Date horarioReservadoInicial;

	@Column(name="cene_horarioreservadofim")
	@Temporal(TemporalType.TIME)
	private Date horarioReservadoFinal;
	
	@Column(name="cene_dataini")
	private Date dataInicial;
	
	@Column(name="cene_datafim")
	private Date dataFinal;

	@Column(name="cene_dataassinatura")
	private Date dataAssinatura;

	@Column(name="cene_periodoteste")
	private String periodoTeste;

	@Column(name="cene_periodoajuste")
	private String periodoAjuste;
	
	@Column(name="cene_opcaofaturamento", length=20)
	private String opcaoFaturamento;

	@Column(name="cene_modalidadetarifaria", length=20)
	private String modalidadeTarifaria;

	@Column(name="cene_agrupadorfatura", length=20)
	private String agrupadorFatura;

    @OneToMany(mappedBy="contrato", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<ContratoEnergiaDemanda> demanda = new ArrayList<ContratoEnergiaDemanda>();    
	
    @Column(name="usur_id", nullable=false)
    private UsuarioProxy usuario = new UsuarioProxy();
    
    @Column(name="cene_tmultimaalteracao", nullable=false, insertable=false, columnDefinition="timestamp default current_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public UnidadeConsumidora getUnidadeConsumidora() {
		return unidadeConsumidora;
	}

	public void setUnidadeConsumidora(UnidadeConsumidora unidadeConsumidora) {
		this.unidadeConsumidora = unidadeConsumidora;
	}

	public Double getTensaoNominal() {
		return tensaoNominal;
	}

	public void setTensaoNominal(Double tensaoNominal) {
		this.tensaoNominal = tensaoNominal;
	}

	public Double getTensaoContratada() {
		return tensaoContratada;
	}

	public void setTensaoContratada(Double tensaoContratada) {
		this.tensaoContratada = tensaoContratada;
	}

	public String getSubGrupoTarifario() {
		return subGrupoTarifario;
	}

	public void setSubGrupoTarifario(String subGrupoTarifario) {
		this.subGrupoTarifario = subGrupoTarifario;
	}

	public Double getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(Double frequencia) {
		this.frequencia = frequencia;
	}

	public Double getPerdasTransformacao() {
		return perdasTransformacao;
	}

	public void setPerdasTransformacao(Double perdasTransformacao) {
		this.perdasTransformacao = perdasTransformacao;
	}

	public Double getPotenciaInstalada() {
		return potenciaInstalada;
	}

	public void setPotenciaInstalada(Double potenciaInstalada) {
		this.potenciaInstalada = potenciaInstalada;
	}

	public Date getHorarioPontaInicial() {
		return horarioPontaInicial;
	}

	public void setHorarioPontaInicial(Date horarioPontaInicial) {
		this.horarioPontaInicial = horarioPontaInicial;
	}

	public Date getHorarioPontaFinal() {
		return horarioPontaFinal;
	}

	public void setHorarioPontaFinal(Date horarioPontaFinal) {
		this.horarioPontaFinal = horarioPontaFinal;
	}

	public Date getHorarioReservadoInicial() {
		return horarioReservadoInicial;
	}

	public void setHorarioReservadoInicial(Date horarioReservadoInicial) {
		this.horarioReservadoInicial = horarioReservadoInicial;
	}

	public Date getHorarioReservadoFinal() {
		return horarioReservadoFinal;
	}

	public void setHorarioReservadoFinal(Date horarioReservadoFinal) {
		this.horarioReservadoFinal = horarioReservadoFinal;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Date getDataAssinatura() {
		return dataAssinatura;
	}

	public void setDataAssinatura(Date dataAssinatura) {
		this.dataAssinatura = dataAssinatura;
	}

	public String getPeriodoTeste() {
		return periodoTeste;
	}

	public void setPeriodoTeste(String periodoTeste) {
		this.periodoTeste = periodoTeste;
	}

	public String getPeriodoAjuste() {
		return periodoAjuste;
	}

	public void setPeriodoAjuste(String periodoAjuste) {
		this.periodoAjuste = periodoAjuste;
	}

	public String getOpcaoFaturamento() {
		return opcaoFaturamento;
	}

	public void setOpcaoFaturamento(String opcaoFaturamento) {
		this.opcaoFaturamento = opcaoFaturamento;
	}

	public String getModalidadeTarifaria() {
		return modalidadeTarifaria;
	}

	public void setModalidadeTarifaria(String modalidadeTarifaria) {
		this.modalidadeTarifaria = modalidadeTarifaria;
	}

	public String getAgrupadorFatura() {
		return agrupadorFatura;
	}

	public void setAgrupadorFatura(String agrupadorFatura) {
		this.agrupadorFatura = agrupadorFatura;
	}

	public List<ContratoEnergiaDemanda> getDemanda() {
		return demanda;
	}

	public void setDemanda(List<ContratoEnergiaDemanda> demanda) {
		this.demanda = demanda;
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
				+ ((agrupadorFatura == null) ? 0 : agrupadorFatura.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((dataAssinatura == null) ? 0 : dataAssinatura.hashCode());
		result = prime * result
				+ ((dataFinal == null) ? 0 : dataFinal.hashCode());
		result = prime * result
				+ ((dataInicial == null) ? 0 : dataInicial.hashCode());
		result = prime * result + ((demanda == null) ? 0 : demanda.hashCode());
		result = prime * result
				+ ((frequencia == null) ? 0 : frequencia.hashCode());
		result = prime
				* result
				+ ((horarioPontaFinal == null) ? 0 : horarioPontaFinal
						.hashCode());
		result = prime
				* result
				+ ((horarioPontaInicial == null) ? 0 : horarioPontaInicial
						.hashCode());
		result = prime
				* result
				+ ((horarioReservadoFinal == null) ? 0 : horarioReservadoFinal
						.hashCode());
		result = prime
				* result
				+ ((horarioReservadoInicial == null) ? 0
						: horarioReservadoInicial.hashCode());
		result = prime
				* result
				+ ((modalidadeTarifaria == null) ? 0 : modalidadeTarifaria
						.hashCode());
		result = prime * result
				+ ((numeroContrato == null) ? 0 : numeroContrato.hashCode());
		result = prime
				* result
				+ ((opcaoFaturamento == null) ? 0 : opcaoFaturamento.hashCode());
		result = prime
				* result
				+ ((perdasTransformacao == null) ? 0 : perdasTransformacao
						.hashCode());
		result = prime * result
				+ ((periodoAjuste == null) ? 0 : periodoAjuste.hashCode());
		result = prime * result
				+ ((periodoTeste == null) ? 0 : periodoTeste.hashCode());
		result = prime
				* result
				+ ((potenciaInstalada == null) ? 0 : potenciaInstalada
						.hashCode());
		result = prime
				* result
				+ ((subGrupoTarifario == null) ? 0 : subGrupoTarifario
						.hashCode());
		result = prime
				* result
				+ ((tensaoContratada == null) ? 0 : tensaoContratada.hashCode());
		result = prime * result
				+ ((tensaoNominal == null) ? 0 : tensaoNominal.hashCode());
		result = prime * result
				+ ((ultimaAlteracao == null) ? 0 : ultimaAlteracao.hashCode());
		result = prime
				* result
				+ ((unidadeConsumidora == null) ? 0 : unidadeConsumidora
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
		ContratoEnergia other = (ContratoEnergia) obj;
		if (agrupadorFatura == null) {
			if (other.agrupadorFatura != null)
				return false;
		} else if (!agrupadorFatura.equals(other.agrupadorFatura))
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (dataAssinatura == null) {
			if (other.dataAssinatura != null)
				return false;
		} else if (!dataAssinatura.equals(other.dataAssinatura))
			return false;
		if (dataFinal == null) {
			if (other.dataFinal != null)
				return false;
		} else if (!dataFinal.equals(other.dataFinal))
			return false;
		if (dataInicial == null) {
			if (other.dataInicial != null)
				return false;
		} else if (!dataInicial.equals(other.dataInicial))
			return false;
		if (demanda == null) {
			if (other.demanda != null)
				return false;
		} else if (!demanda.equals(other.demanda))
			return false;
		if (frequencia == null) {
			if (other.frequencia != null)
				return false;
		} else if (!frequencia.equals(other.frequencia))
			return false;
		if (horarioPontaFinal == null) {
			if (other.horarioPontaFinal != null)
				return false;
		} else if (!horarioPontaFinal.equals(other.horarioPontaFinal))
			return false;
		if (horarioPontaInicial == null) {
			if (other.horarioPontaInicial != null)
				return false;
		} else if (!horarioPontaInicial.equals(other.horarioPontaInicial))
			return false;
		if (horarioReservadoFinal == null) {
			if (other.horarioReservadoFinal != null)
				return false;
		} else if (!horarioReservadoFinal.equals(other.horarioReservadoFinal))
			return false;
		if (horarioReservadoInicial == null) {
			if (other.horarioReservadoInicial != null)
				return false;
		} else if (!horarioReservadoInicial
				.equals(other.horarioReservadoInicial))
			return false;
		if (modalidadeTarifaria == null) {
			if (other.modalidadeTarifaria != null)
				return false;
		} else if (!modalidadeTarifaria.equals(other.modalidadeTarifaria))
			return false;
		if (numeroContrato == null) {
			if (other.numeroContrato != null)
				return false;
		} else if (!numeroContrato.equals(other.numeroContrato))
			return false;
		if (opcaoFaturamento == null) {
			if (other.opcaoFaturamento != null)
				return false;
		} else if (!opcaoFaturamento.equals(other.opcaoFaturamento))
			return false;
		if (perdasTransformacao == null) {
			if (other.perdasTransformacao != null)
				return false;
		} else if (!perdasTransformacao.equals(other.perdasTransformacao))
			return false;
		if (periodoAjuste == null) {
			if (other.periodoAjuste != null)
				return false;
		} else if (!periodoAjuste.equals(other.periodoAjuste))
			return false;
		if (periodoTeste == null) {
			if (other.periodoTeste != null)
				return false;
		} else if (!periodoTeste.equals(other.periodoTeste))
			return false;
		if (potenciaInstalada == null) {
			if (other.potenciaInstalada != null)
				return false;
		} else if (!potenciaInstalada.equals(other.potenciaInstalada))
			return false;
		if (subGrupoTarifario == null) {
			if (other.subGrupoTarifario != null)
				return false;
		} else if (!subGrupoTarifario.equals(other.subGrupoTarifario))
			return false;
		if (tensaoContratada == null) {
			if (other.tensaoContratada != null)
				return false;
		} else if (!tensaoContratada.equals(other.tensaoContratada))
			return false;
		if (tensaoNominal == null) {
			if (other.tensaoNominal != null)
				return false;
		} else if (!tensaoNominal.equals(other.tensaoNominal))
			return false;
		if (ultimaAlteracao == null) {
			if (other.ultimaAlteracao != null)
				return false;
		} else if (!ultimaAlteracao.equals(other.ultimaAlteracao))
			return false;
		if (unidadeConsumidora == null) {
			if (other.unidadeConsumidora != null)
				return false;
		} else if (!unidadeConsumidora.equals(other.unidadeConsumidora))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
    
}
