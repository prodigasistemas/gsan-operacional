package br.gov.pa.cosanpa.gopera.command;

import java.util.List;
import java.util.SortedMap;

import br.gov.pa.cosanpa.gopera.util.DadoRelatorio;

public class InformacoesParaRelatorio {
	
	private Integer referencia;
	
	private Integer referenciaInicial;

	private Integer referenciaFinal;
	
	private Integer codigoRegional;
	
	private Integer codigoUnidadeNegocio;
	
	private Integer codigoMunicipio;
	
	private Integer codigoLocalidade;
	
	private SortedMap<String, DadoRelatorio> mapDados;
	
	private List<String> dadosSelecionados;

    public Integer getReferencia() {
        return referencia;
    }

    public void setReferencia(Integer referencia) {
        this.referencia = referencia;
    }

    public Integer getReferenciaInicial() {
        return referenciaInicial;
    }

    public void setReferenciaInicial(Integer referenciaInicial) {
        this.referenciaInicial = referenciaInicial;
    }

    public Integer getReferenciaFinal() {
        return referenciaFinal;
    }

    public void setReferenciaFinal(Integer referenciaFinal) {
        this.referenciaFinal = referenciaFinal;
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
