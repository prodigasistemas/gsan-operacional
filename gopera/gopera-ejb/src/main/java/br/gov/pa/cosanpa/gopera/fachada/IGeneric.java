package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;

public interface IGeneric <T> {
	 
	public void salvar(T obj) throws Exception;
	public void atualizar(T obj) throws Exception;
	public void excluir(T obj) throws Exception;
	public void obterPorID(Integer id) throws Exception;
	public List<T> obterLista(Integer min, Integer max) throws Exception;
}
