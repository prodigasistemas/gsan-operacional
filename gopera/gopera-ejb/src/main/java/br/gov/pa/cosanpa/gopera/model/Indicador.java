package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Indicador implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer codigoRegional = 0;
	private String nomeRegional;
	private Integer codigoUnidadeNegocio = 0;
	private String nomeUnidadeNegocio;
	private Integer codigoMunicipio = 0;
	private String nomeMunicipio;
	private Integer codigoLocalidade = 0;
	private String nomeLocalidade;
	private Integer tipoUnidadeOperacional = 0;
	private Integer codigoUnidadeOperacional = 0;
	private String nomeUnidadeOperacional;	
	private Integer codigo; //CÒDIGO DO INDICADOR
	private Integer sequencial; //SEQUENCIAL DE IMPRESSÃO
	private String nomeIndicador;
	private String unidadeIndicador;
	private String formulaIndicador;
	private String responsavelIndicador;
	private List<IndicadorValor> valor = new ArrayList<IndicadorValor>();
	
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
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public Integer getSequencial() {
		return sequencial;
	}
	public void setSequencial(Integer sequencial) {
		this.sequencial = sequencial;
	}
	public String getNomeIndicador() {
		return nomeIndicador;
	}
	public void setNomeIndicador(String nomeIndicador) {
		this.nomeIndicador = nomeIndicador;
	}
	public String getUnidadeIndicador() {
		return unidadeIndicador;
	}
	public void setUnidadeIndicador(String unidadeIndicador) {
		this.unidadeIndicador = unidadeIndicador;
	}
	public String getFormulaIndicador() {
		return formulaIndicador;
	}
	public void setFormulaIndicador(String formulaIndicador) {
		this.formulaIndicador = formulaIndicador;
	}
	public String getResponsavelIndicador() {
		return responsavelIndicador;
	}
	public void setResponsavelIndicador(String responsavelIndicador) {
		this.responsavelIndicador = responsavelIndicador;
	}
	public List<IndicadorValor> getValor() {
		return valor;
	}
	public void setValor(List<IndicadorValor> valor) {
		this.valor = valor;
	}
}
