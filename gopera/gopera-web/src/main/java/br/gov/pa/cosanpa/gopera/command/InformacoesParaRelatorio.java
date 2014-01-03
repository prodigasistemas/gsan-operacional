package br.gov.pa.cosanpa.gopera.command;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import br.gov.pa.cosanpa.gopera.util.DadoRelatorio;

public class InformacoesParaRelatorio {
	
	private String referencia;
	
	private Date primeiroDiaReferencia;
	
	private Date primeiroDiaReferenciaInicial;

	private Date primeiroDiaReferenciaFinal;
	
	private Integer codigoRegional;
	
	private Integer codigoUnidadeNegocio;
	
	private Integer codigoMunicipio;
	
	private Integer codigoLocalidade;
	
	private SortedMap<String, DadoRelatorio> mapDados;
	
	private List<String> dadosSelecionados;

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Date getPrimeiroDiaReferencia() {
		return primeiroDiaReferencia;
	}

	public void setPrimeiroDiaReferencia(Date primeiroDiaReferencia) {
		this.primeiroDiaReferencia = primeiroDiaReferencia;
	}

	public Date getPrimeiroDiaReferenciaInicial() {
		return primeiroDiaReferenciaInicial;
	}

	public void setPrimeiroDiaReferenciaInicial(Date primeiroDiaReferenciaInicial) {
		this.primeiroDiaReferenciaInicial = primeiroDiaReferenciaInicial;
	}

	public Date getPrimeiroDiaReferenciaFinal() {
		return primeiroDiaReferenciaFinal;
	}

	public void setPrimeiroDiaReferenciaFinal(Date primeiroDiaReferenciaFinal) {
		this.primeiroDiaReferenciaFinal = primeiroDiaReferenciaFinal;
	}

	public Integer getCodigoRegional() {
		return codigoRegional;
	}

	public void setCodigoRegional(Integer codigoRegional) {
		this.codigoRegional = codigoRegional;
	}

	public Integer getCodigoUnidadeNegocio() {
		return codigoUnidadeNegocio;
	}

	public void setCodigoUnidadeNegocio(Integer codigoUnidadeNegocio) {
		this.codigoUnidadeNegocio = codigoUnidadeNegocio;
	}

	public Integer getCodigoMunicipio() {
		return codigoMunicipio;
	}

	public void setCodigoMunicipio(Integer codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public Integer getCodigoLocalidade() {
		return codigoLocalidade;
	}

	public void setCodigoLocalidade(Integer codigoLocalidade) {
		this.codigoLocalidade = codigoLocalidade;
	}

	public SortedMap<String, DadoRelatorio> getMapDados() {
		return mapDados;
	}

	public void setMapDados(SortedMap<String, DadoRelatorio> mapDados) {
		this.mapDados = mapDados;
	}

	public List<String> getDadosSelecionados() {
		return dadosSelecionados;
	}

	public void setDadosSelecionados(List<String> dadosSelecionados) {
		this.dadosSelecionados = dadosSelecionados;
	}
}
