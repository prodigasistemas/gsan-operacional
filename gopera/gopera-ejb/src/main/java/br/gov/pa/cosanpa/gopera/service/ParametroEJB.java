package br.gov.pa.cosanpa.gopera.service;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.pa.cosanpa.gopera.fachada.IParametro;
import br.gov.pa.cosanpa.gopera.model.Parametro;


@Stateless
public class ParametroEJB implements IParametro {

	@PersistenceContext
	private EntityManager entity;

	@Override
	public void salvar(Parametro obj) throws Exception {
		entity.persist(obj);
	}
	
	@Override
	public void atualizar(Parametro obj) throws Exception {
		entity.merge(obj);

	}

	@Override
	public void excluir(Parametro obj) throws Exception {
		Parametro objRemover = entity.find(Parametro.class, obj.getCodigo());
		entity.remove(objRemover);
	}

	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(Parametro.class, id);

	}
	
	@Override
	public List<Parametro> obterLista(Integer min, Integer max) throws Exception {
		TypedQuery<Parametro> query = entity.createQuery("select c1 from Parametro c1", Parametro.class);
		List<Parametro> lista = query.getResultList();
		return lista;
	}
}
