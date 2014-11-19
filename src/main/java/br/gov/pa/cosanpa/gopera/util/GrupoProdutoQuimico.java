package br.gov.pa.cosanpa.gopera.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class GrupoProdutoQuimico {
	
	private String descricao;
	
	private String id;
	
	private Map<String, ResumoProdutoQuimico> resumos = new LinkedHashMap<String, ResumoProdutoQuimico>();
	
	private int leftPad = 0;

	public GrupoProdutoQuimico(String descricao, String id, int leftPad) {
		this.descricao = descricao;
		this.id = id;
		this.leftPad = leftPad;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getId() {
		return id;
	}
	
	public Map<String, ResumoProdutoQuimico> getResumos() {
		return resumos;
	}

	public void addResumo(ResumoProdutoQuimico resumo) {
		this.resumos.put(resumo.getDescricaoProduto(), resumo);
	}
	
	public int getLeftPad(){
		return leftPad;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GrupoProdutoQuimico other = (GrupoProdutoQuimico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String toString() {
		return "GrupoProdutoQuimico [descricao=" + descricao + ", id=" + id + ", resumos=" + resumos + "]";
	}
}
