package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="consumoeta_produto",schema="operacao")
public class ConsumoETAProduto implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cons_id")
    private ConsumoETA consumo;
	
	@Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="prod_id")
	private Produto produto;
	
	@Column(name="conp_quantidade")
	private Double qtdConsumo;
	
	public ConsumoETAProduto(){
		
	}

	public ConsumoETAProduto(ConsumoETA consumo, Produto produto, Double qtdConsumo) {
		super();
		this.consumo = consumo;
		this.produto = produto;
		this.qtdConsumo = qtdConsumo;
	}
	
	public Double getQtdConsumo() {
		return qtdConsumo;
	}
	public void setQtdConsumo(Double qtdConsumo) {
		this.qtdConsumo = qtdConsumo;
	}
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public ConsumoETA getConsumo() {
		return consumo;
	}
	public void setConsumo(ConsumoETA consumo) {
		this.consumo = consumo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((consumo == null) ? 0 : consumo.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		result = prime * result
				+ ((qtdConsumo == null) ? 0 : qtdConsumo.hashCode());
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
		ConsumoETAProduto other = (ConsumoETAProduto) obj;
		if (consumo == null) {
			if (other.consumo != null)
				return false;
		} else if (!consumo.equals(other.consumo))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		if (qtdConsumo == null) {
			if (other.qtdConsumo != null)
				return false;
		} else if (!qtdConsumo.equals(other.qtdConsumo))
			return false;
		return true;
	}
}
