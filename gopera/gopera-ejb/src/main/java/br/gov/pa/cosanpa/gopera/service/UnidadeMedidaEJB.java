package br.gov.pa.cosanpa.gopera.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.pa.cosanpa.gopera.fachada.IUnidadeMedida;
import br.gov.pa.cosanpa.gopera.model.UnidadeMedida;

@Stateless
public class UnidadeMedidaEJB implements IUnidadeMedida{

	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(UnidadeMedida obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(UnidadeMedida obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(UnidadeMedida obj) {
		UnidadeMedida objRemover = entity.find(UnidadeMedida.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(UnidadeMedida.class, id);
	}

	@Override
	public List<UnidadeMedida> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<UnidadeMedida> query= entity.createQuery("select c1 from UnidadeMedida c1",UnidadeMedida.class);
		return query.getResultList();
	}
}
