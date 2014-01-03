package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EnergiaAnalise implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private RegionalProxy regional;
	private UnidadeNegocioProxy unidadeNegocio;
	private MunicipioProxy municipio;
	private LocalidadeProxy localidade;
	private Integer codigoUC; 
	private String nomeUC;
	private Integer codigoDado;
	private String nomeDado;
	private List<EnergiaAnaliseDados> meses = new ArrayList<EnergiaAnaliseDados>();
	public RegionalProxy getRegional() {
		return regional;
	}
	public void setRegional(RegionalProxy regional) {
		this.regional = regional;
	}
	public UnidadeNegocioProxy getUnidadeNegocio() {
		return unidadeNegocio;
	}
	public void setUnidadeNegocio(UnidadeNegocioProxy unidadeNegocio) {
		this.unidadeNegocio = unidadeNegocio;
	}
	public MunicipioProxy getMunicipio() {
		return municipio;
	}
	public void setMunicipio(MunicipioProxy municipio) {
		this.municipio = municipio;
	}
	public LocalidadeProxy getLocalidade() {
		return localidade;
	}
	public void setLocalidade(LocalidadeProxy localidade) {
		this.localidade = localidade;
	}
	public Integer getCodigoUC() {
		return codigoUC;
	}
	public void setCodigoUC(Integer codigoUC) {
		this.codigoUC = codigoUC;
	}
	public String getNomeUC() {
		return nomeUC;
	}
	public void setNomeUC(String nomeUC) {
		this.nomeUC = nomeUC;
	}
	public Integer getCodigoDado() {
		return codigoDado;
	}
	public void setCodigoDado(Integer codigoDado) {
		this.codigoDado = codigoDado;
	}
	public String getNomeDado() {
		return nomeDado;
	}
	public void setNomeDado(String nomeDado) {
		this.nomeDado = nomeDado;
	}
	public List<EnergiaAnaliseDados> getMeses() {
		return meses;
	}
	public void setMeses(List<EnergiaAnaliseDados> meses) {
		this.meses = meses;
	}
}
