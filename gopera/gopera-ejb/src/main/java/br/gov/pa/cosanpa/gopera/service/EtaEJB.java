package br.gov.pa.cosanpa.gopera.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.pa.cosanpa.gopera.fachada.IEEAB;
import br.gov.pa.cosanpa.gopera.fachada.IETA;
import br.gov.pa.cosanpa.gopera.model.ETA;

@Stateless
public class EtaEJB implements IETA{

	@PersistenceContext
	private EntityManager entity;
	
	@EJB
	private IEEAB fachadaEAB;
	
	@Override
	public void salvar(ETA obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(ETA obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(ETA obj) {
		ETA objRemover = entity.find(ETA.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(ETA.class, id);
	}

	@Override
	public List<ETA> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<ETA> query= entity.createQuery("select c1 from ETA c1 order by c1.descricao",ETA.class);
		return query.getResultList();
	}

	@Override
	public ETA obterETA(Integer codigo) throws Exception {
		TypedQuery<ETA> query= entity.createQuery("select c1 from ETA c1 where eta_id = " + codigo,ETA.class);
		ETA eta = query.getSingleResult();

		ETA ETA = eta;
		for (int j = 0; j < ETA.getFonteCaptacao().size(); j++) {
			eta.getFonteCaptacao().get(j);
			eta.getFonteCaptacao().get(j).setEeab(fachadaEAB.obterEEAB(eta.getFonteCaptacao().get(j).getEeab().getCodigo()));
		}
		
		for (int j = 0; j < ETA.getMedidorSaida().size(); j++) {
			eta.getMedidorSaida().get(j);
		}
		
		return eta;
	}

	@Override
	public ETA obterETALazy(Integer codigo) throws Exception {
		TypedQuery<ETA> query= entity.createQuery("select c1 from ETA c1 where eta_id = " + codigo,ETA.class);
		ETA eta = query.getSingleResult();
		return eta;
	}
	
	@Override
	public List<ETA> obterListaComCMB(Integer min, Integer max)
			throws Exception {
		TypedQuery<ETA> query= entity.createQuery("select c1 from ETA c1 where eta_cmb > 0",ETA.class);
		return query.getResultList();
	}

}
