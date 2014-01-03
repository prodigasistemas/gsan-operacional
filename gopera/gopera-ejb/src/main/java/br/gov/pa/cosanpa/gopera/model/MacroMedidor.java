package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.OneToMany;

@Entity
@SequenceGenerator(name="sequence_macro_medidor", sequenceName="sequence_macro_medidor", schema="operacao", initialValue=1, allocationSize=1)
@Table(name="macro_medidor",schema="operacao")
public class MacroMedidor implements BaseEntidade, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_macro_medidor")
    @Column(name="mmed_id", unique=true, nullable=false, precision=3, scale=0)
	private Integer codigo;
	
	@Column(name="mmed_tipo", nullable=false)
	private Integer tipoMedidor;

    @Column(name="mmed_idleitura", nullable=false, length=50)
	private String identificadorLeitura;

    @Column(name="mmed_tipomedicao")
	private Integer tipoMedicao;

    @Column(name="mmed_principio", length=50)
	private String principio;

    @Column(name="mmed_tiposensor", length=50)
	private String tipoSensor;

    @Column(name="mmed_fabricante", length=50)
	private String fabricante;

    @Column(name="mmed_modelo", length=50)
	private String modelo;
    
    @Column(name="mmed_numeroSerie", length=50)
	private String numeroSerie;
    
    @Column(name="mmed_tombamento", length=50)
	private String tombamento;
    
    @Column(name="mmed_range", length=50)
	private String range;
    
    @Column(name="mmed_grauprotecao", length=50)
	private String grauProtecao;
    
    @Column(name="mmed_alimentacao", length=50)
	private String Alimentacao;
    
    @Column(name="mmed_sinalsaida", length=50)
	private String sinalSaida;
    
    @Column(name="mmed_protocolo", length=50)
	private String protocolo;
    
    @OneToMany(mappedBy="medidor", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<MacroMedidorAfericao> afericao;
    
    @Column(name="usur_id", nullable=false)
    private UsuarioProxy usuario = new UsuarioProxy();
    
    @Column(name="mmed_tmultimaalteracao", nullable=false, insertable=false, columnDefinition="timestamp default current_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;

	public MacroMedidor() {
		super();
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getTipoMedidor() {
		return tipoMedidor;
	}

	public void setTipoMedidor(Integer tipoMedidor) {
		this.tipoMedidor = tipoMedidor;
	}

	public String getIdentificadorLeitura() {
		return identificadorLeitura;
	}

	public void setIdentificadorLeitura(String identificadorLeitura) {
		this.identificadorLeitura = identificadorLeitura;
	}

	public Integer getTipoMedicao() {
		return tipoMedicao;
	}

	public void setTipoMedicao(Integer tipoMedicao) {
		this.tipoMedicao = tipoMedicao;
	}

	public String getPrincipio() {
		return principio;
	}

	public void setPrincipio(String principio) {
		this.principio = principio;
	}

	public String getTipoSensor() {
		return tipoSensor;
	}

	public void setTipoSensor(String tipoSensor) {
		this.tipoSensor = tipoSensor;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public String getTombamento() {
		return tombamento;
	}

	public void setTombamento(String tombamento) {
		this.tombamento = tombamento;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getGrauProtecao() {
		return grauProtecao;
	}

	public void setGrauProtecao(String grauProtecao) {
		this.grauProtecao = grauProtecao;
	}

	public String getAlimentacao() {
		return Alimentacao;
	}

	public void setAlimentacao(String alimentacao) {
		Alimentacao = alimentacao;
	}

	public String getSinalSaida() {
		return sinalSaida;
	}

	public void setSinalSaida(String sinalSaida) {
		this.sinalSaida = sinalSaida;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public List<MacroMedidorAfericao> getAfericao() {
		return afericao;
	}

	public void setAfericao(List<MacroMedidorAfericao> afericao) {
		this.afericao = afericao;
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
				+ ((Alimentacao == null) ? 0 : Alimentacao.hashCode());
		result = prime * result
				+ ((afericao == null) ? 0 : afericao.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((fabricante == null) ? 0 : fabricante.hashCode());
		result = prime * result
				+ ((grauProtecao == null) ? 0 : grauProtecao.hashCode());
		result = prime
				* result
				+ ((identificadorLeitura == null) ? 0 : identificadorLeitura
						.hashCode());
		result = prime * result + ((modelo == null) ? 0 : modelo.hashCode());
		result = prime * result
				+ ((numeroSerie == null) ? 0 : numeroSerie.hashCode());
		result = prime * result
				+ ((principio == null) ? 0 : principio.hashCode());
		result = prime * result
				+ ((protocolo == null) ? 0 : protocolo.hashCode());
		result = prime * result + ((range == null) ? 0 : range.hashCode());
		result = prime * result
				+ ((sinalSaida == null) ? 0 : sinalSaida.hashCode());
		result = prime * result
				+ ((tipoMedicao == null) ? 0 : tipoMedicao.hashCode());
		result = prime * result
				+ ((tipoMedidor == null) ? 0 : tipoMedidor.hashCode());
		result = prime * result
				+ ((tipoSensor == null) ? 0 : tipoSensor.hashCode());
		result = prime * result
				+ ((tombamento == null) ? 0 : tombamento.hashCode());
		result = prime * result
				+ ((ultimaAlteracao == null) ? 0 : ultimaAlteracao.hashCode());
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
		MacroMedidor other = (MacroMedidor) obj;
		if (Alimentacao == null) {
			if (other.Alimentacao != null)
				return false;
		} else if (!Alimentacao.equals(other.Alimentacao))
			return false;
		if (afericao == null) {
			if (other.afericao != null)
				return false;
		} else if (!afericao.equals(other.afericao))
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (fabricante == null) {
			if (other.fabricante != null)
				return false;
		} else if (!fabricante.equals(other.fabricante))
			return false;
		if (grauProtecao == null) {
			if (other.grauProtecao != null)
				return false;
		} else if (!grauProtecao.equals(other.grauProtecao))
			return false;
		if (identificadorLeitura == null) {
			if (other.identificadorLeitura != null)
				return false;
		} else if (!identificadorLeitura.equals(other.identificadorLeitura))
			return false;
		if (modelo == null) {
			if (other.modelo != null)
				return false;
		} else if (!modelo.equals(other.modelo))
			return false;
		if (numeroSerie == null) {
			if (other.numeroSerie != null)
				return false;
		} else if (!numeroSerie.equals(other.numeroSerie))
			return false;
		if (principio == null) {
			if (other.principio != null)
				return false;
		} else if (!principio.equals(other.principio))
			return false;
		if (protocolo == null) {
			if (other.protocolo != null)
				return false;
		} else if (!protocolo.equals(other.protocolo))
			return false;
		if (range == null) {
			if (other.range != null)
				return false;
		} else if (!range.equals(other.range))
			return false;
		if (sinalSaida == null) {
			if (other.sinalSaida != null)
				return false;
		} else if (!sinalSaida.equals(other.sinalSaida))
			return false;
		if (tipoMedicao == null) {
			if (other.tipoMedicao != null)
				return false;
		} else if (!tipoMedicao.equals(other.tipoMedicao))
			return false;
		if (tipoMedidor == null) {
			if (other.tipoMedidor != null)
				return false;
		} else if (!tipoMedidor.equals(other.tipoMedidor))
			return false;
		if (tipoSensor == null) {
			if (other.tipoSensor != null)
				return false;
		} else if (!tipoSensor.equals(other.tipoSensor))
			return false;
		if (tombamento == null) {
			if (other.tombamento != null)
				return false;
		} else if (!tombamento.equals(other.tombamento))
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
		return true;
	}

}
