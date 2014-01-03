package br.gov.pa.cosanpa.gopera.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.pa.cosanpa.gopera.fachada.IEEAT;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.model.EEAT;

@Stateless
public class EeatEJB implements IEEAT{

	@EJB
	private IProxy iProxy;
	
	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(EEAT obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(EEAT obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(EEAT obj) {
		EEAT objRemover = entity.find(EEAT.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(EEAT.class, id);
	}

	@Override
	public List<EEAT> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<EEAT> query= entity.createQuery("select c1 from EEAT c1",EEAT.class);
		return query.getResultList();
	}
	
	@Override
	public List<EEAT> obterListaComCMB(Integer min, Integer max)
			throws Exception {
		TypedQuery<EEAT> query= entity.createQuery("select c1 from EEAT c1 where eeat_cmb > 0",EEAT.class);
		return query.getResultList();
	}
	
	@Override
	public EEAT obterEEAT(Integer codigo) throws Exception {
		TypedQuery<EEAT> query= entity.createQuery("select c1 from EEAT c1 where eeat_id = " + codigo,EEAT.class);
		EEAT eeat = query.getSingleResult();
		
		EEAT EEAT = eeat;
		for (int j = 0; j < EEAT.getFonteCaptacao().size(); j++) {
			eeat.getFonteCaptacao().get(j);
			eeat.getFonteCaptacao().get(j).setNomeFonte(iProxy.getFonteCaptacaoEEAT(eeat.getFonteCaptacao().get(j).getTipoFonte(), eeat.getFonteCaptacao().get(j).getCodigoFonte()));			
		}
		
		for (int j = 0; j < EEAT.getMedidorSaida().size(); j++) {
			eeat.getMedidorSaida().get(j);
		}
		
		return eeat;		
	}
}
