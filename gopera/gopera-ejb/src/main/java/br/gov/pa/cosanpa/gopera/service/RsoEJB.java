package br.gov.pa.cosanpa.gopera.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.pa.cosanpa.gopera.fachada.IRSO;
import br.gov.pa.cosanpa.gopera.model.RSO;

@Stateless
public class RsoEJB implements IRSO{

	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(RSO obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(RSO obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(RSO obj) {
		RSO objRemover = entity.find(RSO.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(RSO.class, id);
	}

	@Override
	public List<RSO> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<RSO> query= entity.createQuery("select c1 from RSO c1",RSO.class);
		return query.getResultList();
	}

	@Override
	public RSO obterRSO(Integer codigo) throws Exception {
		TypedQuery<RSO> query= entity.createQuery("select c1 from RSO c1 where rso_id = " + codigo,RSO.class);
		RSO rso = query.getSingleResult();
		return rso;
	}
	
	@Override
	public List<RSO> obterListaComCMB(Integer min, Integer max)
			throws Exception {
		TypedQuery<RSO> query= entity.createQuery("select c1 from RSO c1 where rso_cmb > 0",RSO.class);
		return query.getResultList();
	}
}
