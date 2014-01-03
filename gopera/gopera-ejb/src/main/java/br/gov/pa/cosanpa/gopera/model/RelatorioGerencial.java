package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.gov.pa.cosanpa.gopera.util.ElementoMensal;

public class RelatorioGerencial implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer codigoRegional;
	private String nomeRegional;
	private Integer codigoUnidadeNegocio;
	private String nomeUnidadeNegocio;
	private Integer codigoMunicipio;
	private String nomeMunicipio;
	private Integer codigoLocalidade;
	private String nomeLocalidade;
	private Integer tipoUnidadeOperacional;
	private Integer codigoUnidadeOperacional;
	private String nomeUnidadeOperacional;
	private String descricaoProduto;
	private String unidadeMedida;
	private Date dataConsumo;
	private double qtdConsumo;
	private double valorUnitario;
	private double valorTotal;
	private double qtdConsumoJAN;
	private double qtdConsumoFEV;
	private double qtdConsumoMAR;
	private double qtdConsumoABR;
	private double qtdConsumoMAI;
	private double qtdConsumoJUN;
	private double qtdConsumoJUL;
	private double qtdConsumoAGO;
	private double qtdConsumoSET;
	private double qtdConsumoOUT;
	private double qtdConsumoNOV;
	private double qtdConsumoDEZ;
	private List<ElementoMensal> quantidadesMensal = new LinkedList<ElementoMensal>();
	
	public Integer getCodigoRegional() {
		return codigoRegional;
	}

	public void setCodigoRegional(Integer codigoRegional) {
		this.codigoRegional = codigoRegional;
	}

	public String getNomeRegional() {
		return nomeRegional;
	}

	public void setNomeRegional(String nomeRegional) {
		this.nomeRegional = nomeRegional;
	}

	public Integer getCodigoUnidadeNegocio() {
		return codigoUnidadeNegocio;
	}

	public void setCodigoUnidadeNegocio(Integer codigoUnidadeNegocio) {
		this.codigoUnidadeNegocio = codigoUnidadeNegocio;
	}

	public String getNomeUnidadeNegocio() {
		return nomeUnidadeNegocio;
	}

	public void setNomeUnidadeNegocio(String nomeUnidadeNegocio) {
		this.nomeUnidadeNegocio = nomeUnidadeNegocio;
	}

	public Integer getCodigoMunicipio() {
		return codigoMunicipio;
	}

	public void setCodigoMunicipio(Integer codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public String getNomeMunicipio() {
		return nomeMunicipio;
	}

	public void setNomeMunicipio(String nomeMunicipio) {
		this.nomeMunicipio = nomeMunicipio;
	}

	public Integer getCodigoLocalidade() {
		return codigoLocalidade;
	}

	public void setCodigoLocalidade(Integer codigoLocalidade) {
		this.codigoLocalidade = codigoLocalidade;
	}

	public String getNomeLocalidade() {
		return nomeLocalidade;
	}

	public void setNomeLocalidade(String nomeLocalidade) {
		this.nomeLocalidade = nomeLocalidade;
	}

	public Integer getTipoUnidadeOperacional() {
		return tipoUnidadeOperacional;
	}

	public void setTipoUnidadeOperacional(Integer tipoUnidadeOperacional) {
		this.tipoUnidadeOperacional = tipoUnidadeOperacional;
	}

	public Integer getCodigoUnidadeOperacional() {
		return codigoUnidadeOperacional;
	}

	public void setCodigoUnidadeOperacional(Integer codigoUnidadeOperacional) {
		this.codigoUnidadeOperacional = codigoUnidadeOperacional;
	}

	public String getNomeUnidadeOperacional() {
		return nomeUnidadeOperacional;
	}

	public void setNomeUnidadeOperacional(String nomeUnidadeOperacional) {
		this.nomeUnidadeOperacional = nomeUnidadeOperacional;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public Date getDataConsumo() {
		return dataConsumo;
	}

	public void setDataConsumo(Date dataConsumo) {
		this.dataConsumo = dataConsumo;
	}

	public double getQtdConsumo() {
		return qtdConsumo;
	}

	public void setQtdConsumo(double qtdConsumo) {
		this.qtdConsumo = qtdConsumo;
	}

	public double getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public double getQtdConsumoJAN() {
		return qtdConsumoJAN;
	}

	public void setQtdConsumoJAN(double qtdConsumoJAN) {
		this.qtdConsumoJAN = qtdConsumoJAN;
	}

	public double getQtdConsumoFEV() {
		return qtdConsumoFEV;
	}

	public void setQtdConsumoFEV(double qtdConsumoFEV) {
		this.qtdConsumoFEV = qtdConsumoFEV;
	}

	public double getQtdConsumoMAR() {
		return qtdConsumoMAR;
	}

	public void setQtdConsumoMAR(double qtdConsumoMAR) {
		this.qtdConsumoMAR = qtdConsumoMAR;
	}

	public double getQtdConsumoABR() {
		return qtdConsumoABR;
	}

	public void setQtdConsumoABR(double qtdConsumoABR) {
		this.qtdConsumoABR = qtdConsumoABR;
	}

	public double getQtdConsumoMAI() {
		return qtdConsumoMAI;
	}

	public void setQtdConsumoMAI(double qtdConsumoMAI) {
		this.qtdConsumoMAI = qtdConsumoMAI;
	}

	public double getQtdConsumoJUN() {
		return qtdConsumoJUN;
	}

	public void setQtdConsumoJUN(double qtdConsumoJUN) {
		this.qtdConsumoJUN = qtdConsumoJUN;
	}

	public double getQtdConsumoJUL() {
		return qtdConsumoJUL;
	}

	public void setQtdConsumoJUL(double qtdConsumoJUL) {
		this.qtdConsumoJUL = qtdConsumoJUL;
	}

	public double getQtdConsumoAGO() {
		return qtdConsumoAGO;
	}

	public void setQtdConsumoAGO(double qtdConsumoAGO) {
		this.qtdConsumoAGO = qtdConsumoAGO;
	}

	public double getQtdConsumoSET() {
		return qtdConsumoSET;
	}

	public void setQtdConsumoSET(double qtdConsumoSET) {
		this.qtdConsumoSET = qtdConsumoSET;
	}

	public double getQtdConsumoOUT() {
		return qtdConsumoOUT;
	}

	public void setQtdConsumoOUT(double qtdConsumoOUT) {
		this.qtdConsumoOUT = qtdConsumoOUT;
	}

	public double getQtdConsumoNOV() {
		return qtdConsumoNOV;
	}

	public void setQtdConsumoNOV(double qtdConsumoNOV) {
		this.qtdConsumoNOV = qtdConsumoNOV;
	}

	public double getQtdConsumoDEZ() {
		return qtdConsumoDEZ;
	}

	public void setQtdConsumoDEZ(double qtdConsumoDEZ) {
		this.qtdConsumoDEZ = qtdConsumoDEZ;
	}

	public List<ElementoMensal> getQuantidadesMensal() {
		return quantidadesMensal;
	}
	
	public void addQuantidadeMensal(ElementoMensal item){
		this.quantidadesMensal.add(item);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoLocalidade == null) ? 0 : codigoLocalidade.hashCode());
		result = prime * result + ((codigoMunicipio == null) ? 0 : codigoMunicipio.hashCode());
		result = prime * result + ((codigoRegional == null) ? 0 : codigoRegional.hashCode());
		result = prime * result + ((codigoUnidadeNegocio == null) ? 0 : codigoUnidadeNegocio.hashCode());
		result = prime * result + ((codigoUnidadeOperacional == null) ? 0 : codigoUnidadeOperacional.hashCode());
		result = prime * result + ((descricaoProduto == null) ? 0 : descricaoProduto.hashCode());
		result = prime * result + ((tipoUnidadeOperacional == null) ? 0 : tipoUnidadeOperacional.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RelatorioGerencial other = (RelatorioGerencial) obj;
		if (codigoLocalidade == null) {
			if (other.codigoLocalidade != null)
				return false;
		} else if (!codigoLocalidade.equals(other.codigoLocalidade))
			return false;
		if (codigoMunicipio == null) {
			if (other.codigoMunicipio != null)
				return false;
		} else if (!codigoMunicipio.equals(other.codigoMunicipio))
			return false;
		if (codigoRegional == null) {
			if (other.codigoRegional != null)
				return false;
		} else if (!codigoRegional.equals(other.codigoRegional))
			return false;
		if (codigoUnidadeNegocio == null) {
			if (other.codigoUnidadeNegocio != null)
				return false;
		} else if (!codigoUnidadeNegocio.equals(other.codigoUnidadeNegocio))
			return false;
		if (codigoUnidadeOperacional == null) {
			if (other.codigoUnidadeOperacional != null)
				return false;
		} else if (!codigoUnidadeOperacional.equals(other.codigoUnidadeOperacional))
			return false;
		if (descricaoProduto == null) {
			if (other.descricaoProduto != null)
				return false;
		} else if (!descricaoProduto.equals(other.descricaoProduto))
			return false;
		if (tipoUnidadeOperacional == null) {
			if (other.tipoUnidadeOperacional != null)
				return false;
		} else if (!tipoUnidadeOperacional.equals(other.tipoUnidadeOperacional))
			return false;
		return true;
	}
}
