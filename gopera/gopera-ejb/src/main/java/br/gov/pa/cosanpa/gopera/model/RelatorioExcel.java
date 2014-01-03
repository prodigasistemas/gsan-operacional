package br.gov.pa.cosanpa.gopera.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RelatorioExcel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String planilha;
	private String caminho;
	private String nomeArquivoExistente;
	private String nomeArquivoNovo;
	private String nomeRelatorio;
	private String dataReferencia;
	private Date dataReferenciaInicial;
	private Date dataReferenciaFinal;
	private Integer codigoMunicipio;
	private Integer codigoLocalidade;
	private Integer tipoRelatorio;
	private File arquivo;
	private File arquivoNovo;
	private List<RelatorioEnergiaEletrica> relatorioEnergiaEletrica = new ArrayList<RelatorioEnergiaEletrica>();
	private List<EnergiaEletricaDados> energiaEletricaDados = new ArrayList<EnergiaEletricaDados>();
	private List<String> dadoSelecionado;
	
	public String getPlanilha() {
		return planilha;
	}
	public void setPlanilha(String planilha) {
		this.planilha = planilha;
	}
	public String getCaminho() {
		return caminho;
	}
	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
	public String getNomeArquivoExistente() {
		return nomeArquivoExistente;
	}
	public void setNomeArquivoExistente(String nomeArquivoExistente) {
		this.nomeArquivoExistente = nomeArquivoExistente;
	}
	public String getNomeArquivoNovo() {
		return nomeArquivoNovo;
	}
	public void setNomeArquivoNovo(String nomeArquivoNovo) {
		this.nomeArquivoNovo = nomeArquivoNovo;
	}
	public String getNomeRelatorio() {
		return nomeRelatorio;
	}
	public void setNomeRelatorio(String nomeRelatorio) {
		this.nomeRelatorio = nomeRelatorio;
	}
	public String getDataReferencia() {
		return dataReferencia;
	}
	public void setDataReferencia(String dataReferencia) {
		this.dataReferencia = dataReferencia;
	}
	public Date getDataReferenciaInicial() {
		return dataReferenciaInicial;
	}
	public void setDataReferenciaInicial(Date dataReferenciaInicial) {
		this.dataReferenciaInicial = dataReferenciaInicial;
	}
	public Date getDataReferenciaFinal() {
		return dataReferenciaFinal;
	}
	public void setDataReferenciaFinal(Date dataReferenciaFinal) {
		this.dataReferenciaFinal = dataReferenciaFinal;
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
	public Integer getTipoRelatorio() {
		return tipoRelatorio;
	}
	public void setTipoRelatorio(Integer tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}
	public File getArquivo() {
		return arquivo;
	}
	public void setArquivo(File arquivo) {
		this.arquivo = arquivo;
	}
	public File getArquivoNovo() {
		return arquivoNovo;
	}
	public void setArquivoNovo(File arquivoNovo) {
		this.arquivoNovo = arquivoNovo;
	}
	public List<RelatorioEnergiaEletrica> getRelatorioEnergiaEletrica() {
		return relatorioEnergiaEletrica;
	}
	public void setRelatorioEnergiaEletrica(
			List<RelatorioEnergiaEletrica> relatorioEnergiaEletrica) {
		this.relatorioEnergiaEletrica = relatorioEnergiaEletrica;
	}
	public List<EnergiaEletricaDados> getEnergiaEletricaDados() {
		return energiaEletricaDados;
	}
	public void setEnergiaEletricaDados(
			List<EnergiaEletricaDados> energiaEletricaDados) {
		this.energiaEletricaDados = energiaEletricaDados;
	}
	public List<String> getDadoSelecionado() {
		return dadoSelecionado;
	}
	public void setDadoSelecionado(List<String> dadoSelecionado) {
		this.dadoSelecionado = dadoSelecionado;
	}
}
