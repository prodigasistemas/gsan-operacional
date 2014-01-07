package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class DadosRelatorioEnergiaEletrica implements Serializable{
	private static final long serialVersionUID = -3571976936845251980L;
	
	@EmbeddedId
	private DadosRelatorioEnergiaEletricaPK id;
	
	private String nomeMunicipio;
	private String nomeLocalidade;
	private BigDecimal consumo;
	private BigDecimal ultrapassagemKwh;
	private BigDecimal ultrapassagemValor;
	private BigDecimal totalFatorPotencia;
	private BigDecimal valorTotal;
	
	public DadosRelatorioEnergiaEletrica() {
	}
	
	public DadosRelatorioEnergiaEletricaPK getId() {
		return id;
	}
	public void setId(DadosRelatorioEnergiaEletricaPK id) {
		this.id = id;
	}
	public String getNomeMunicipio() {
		return nomeMunicipio;
	}
	public void setNomeMunicipio(String nomeMunicipio) {
		this.nomeMunicipio = nomeMunicipio;
	}
	public String getNomeLocalidade() {
		return nomeLocalidade;
	}
	public void setNomeLocalidade(String nomeLocalidade) {
		this.nomeLocalidade = nomeLocalidade;
	}
	public BigDecimal getConsumo() {
		return consumo;
	}
	public void setConsumo(BigDecimal consumo) {
		this.consumo = consumo;
	}
	public BigDecimal getUltrapassagemKwh() {
		return ultrapassagemKwh;
	}
	public void setUltrapassagemKwh(BigDecimal ultrapassagemKwh) {
		this.ultrapassagemKwh = ultrapassagemKwh;
	}
	public BigDecimal getUltrapassagemValor() {
		return ultrapassagemValor;
	}
	public void setUltrapassagemValor(BigDecimal ultrapassagemValor) {
		this.ultrapassagemValor = ultrapassagemValor;
	}
	public BigDecimal getTotalFatorPotencia() {
		return totalFatorPotencia;
	}
	public void setTotalFatorPotencia(BigDecimal totalFatorPotencia) {
		this.totalFatorPotencia = totalFatorPotencia;
	}
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
}
