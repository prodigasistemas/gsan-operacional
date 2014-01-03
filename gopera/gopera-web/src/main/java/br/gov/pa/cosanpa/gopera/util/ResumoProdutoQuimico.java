package br.gov.pa.cosanpa.gopera.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResumoProdutoQuimico{
	
	private String descricaoProduto;
	
	private Map<String, ValoresResumo> valores = new LinkedHashMap<String, ValoresResumo>();
	
	public ResumoProdutoQuimico(String descProduto) {
		this.descricaoProduto = descProduto;
	}
	
	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public Map<String, ValoresResumo> getValores() {
		return valores;
	}

	public String toString() {
		return "ResumoProdutoQuimico [descricaoProduto=" + descricaoProduto + ", valores=" + valores + "]";
	}
}
