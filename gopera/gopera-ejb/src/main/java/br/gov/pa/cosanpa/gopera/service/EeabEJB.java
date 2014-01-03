package br.gov.pa.cosanpa.gopera.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.pa.cosanpa.gopera.fachada.IEEAB;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.model.EEAB;

@Stateless
public class EeabEJB implements IEEAB{

	@EJB
	private IProxy iProxy;
	
	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(EEAB obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(EEAB obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(EEAB obj) {
		EEAB objRemover = entity.find(EEAB.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(EEAB.class, id);
	}

	@Override
	public List<EEAB> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<EEAB> query= entity.createQuery("select c1 from EEAB c1 order by eeab_nome",EEAB.class);
		List<EEAB> lista = query.getResultList();

/*			
		for (int i = 0; i < lista.size(); i++) {
			lista.get(i).setSistemaAbastecimentoProxy(iProxy.getSistemaAbastecimento(lista.get(i).getSistemaAbastecimentoProxy().getCodigo()));
			EEAB EEAB = lista.get(i);
			for (int j = 0; j < EEAB.getFonteCaptacao().size(); j++) {
				lista.get(i).getFonteCaptacao().get(j);
				lista.get(i).getFonteCaptacao().get(j).setNomeFonte(iProxy.getFonteCaptacaoEEAB(lista.get(i).getFonteCaptacao().get(j).getTipoFonte(), lista.get(i).getFonteCaptacao().get(j).getCodigoFonte()));
			}
		}
*/						
		
		return lista;
	}
	
	@Override
	public EEAB obterEEAB(Integer codigo) throws Exception {
		TypedQuery<EEAB> query= entity.createQuery("select c1 from EEAB c1 where eeab_id = " + codigo,EEAB.class);
		EEAB eeab = query.getSingleResult();
		
//		eeab.setSistemaAbastecimentoProxy(iProxy.getSistemaAbastecimento(eeab.getSistemaAbastecimentoProxy().getCodigo()));

		EEAB EEAB = eeab;
		for (int j = 0; j < EEAB.getFonteCaptacao().size(); j++) {
			eeab.getFonteCaptacao().get(j);
			eeab.getFonteCaptacao().get(j).setNomeFonte(iProxy.getFonteCaptacaoEEAB(eeab.getFonteCaptacao().get(j).getTipoFonte(), eeab.getFonteCaptacao().get(j).getCodigoFonte()));			
		}

		for (int j = 0; j < EEAB.getMedidorSaida().size(); j++) {
			eeab.getMedidorSaida().get(j);
		}
		
		return eeab;		
	}
}
