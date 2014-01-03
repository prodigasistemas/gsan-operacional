package br.gov.pa.cosanpa.gopera.util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class TotalProdutoQuimicoMensal implements Serializable{
	private static final long serialVersionUID = -1355094309644488195L;

	private String produto;
	
	private String medida;
	
	private List<TotalMensal> meses = new LinkedList<TotalMensal>();

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}
	
	public String getMedida() {
		return medida;
	}

	public void setMedida(String medida) {
		this.medida = medida;
	}

	public List<TotalMensal> getMeses() {
		return meses;
	}

	public void addResumoMensal(TotalMensal resumo) {
		int indice = this.meses.indexOf(resumo);
		if (indice > -1){
			TotalMensal element = this.meses.get(indice);
			element.setValor(resumo.getValor());
		}else{
			this.meses.add(resumo);
		}
	}
}
