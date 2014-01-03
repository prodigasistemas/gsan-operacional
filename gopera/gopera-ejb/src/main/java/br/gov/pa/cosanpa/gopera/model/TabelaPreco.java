package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@SequenceGenerator(name="sequence_tabelapreco", sequenceName="sequence_tabelapreco", schema="operacao", initialValue=1, allocationSize=1)
@Table(name="tabelapreco",schema="operacao")
public class TabelaPreco implements BaseEntidade, Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_tabelapreco")
	@Column(name="tabp_id", unique=true, nullable=false, precision=3, scale=0)	
	private Integer codigo;

	@Column(name="tabp_vigencia")
	private Date dataVigencia;

	@OneToMany(mappedBy="tabelaPreco", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<TabelaPrecoProduto> tabelaPrecoProduto = new ArrayList<TabelaPrecoProduto>();
    
    @Column(name="usur_id", nullable=false)
    private UsuarioProxy usuario = new UsuarioProxy();
    
    @Column(name="tabp_tmultimaalteracao", nullable=false, insertable=false, columnDefinition="timestamp default current_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;
    
	public TabelaPreco() {
	}

	public TabelaPreco(Integer codigo, Date dataVigencia,
			List<TabelaPrecoProduto> tabelaPrecoProduto) {
		this.codigo = codigo;
		this.dataVigencia = dataVigencia;
		this.tabelaPrecoProduto = tabelaPrecoProduto;
	}
	
	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

		public Date getDataVigencia() {
		return dataVigencia;
	}

	public void setDataVigencia(Date dataVigencia) {
		this.dataVigencia = dataVigencia;
	}

	public List<TabelaPrecoProduto> getTabelaPrecoProduto() {
		return tabelaPrecoProduto;
	}

	public void setTabelaPrecoProduto(List<TabelaPrecoProduto> tabelaPrecoProduto) {
		this.tabelaPrecoProduto = tabelaPrecoProduto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
				+ ((dataVigencia == null) ? 0 : dataVigencia.hashCode());
		result = prime
				* result
				+ ((tabelaPrecoProduto == null) ? 0 : tabelaPrecoProduto
						.hashCode());
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
		TabelaPreco other = (TabelaPreco) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (dataVigencia == null) {
			if (other.dataVigencia != null)
				return false;
		} else if (!dataVigencia.equals(other.dataVigencia))
			return false;
		if (tabelaPrecoProduto == null) {
			if (other.tabelaPrecoProduto != null)
				return false;
		} else if (!tabelaPrecoProduto.equals(other.tabelaPrecoProduto))
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
