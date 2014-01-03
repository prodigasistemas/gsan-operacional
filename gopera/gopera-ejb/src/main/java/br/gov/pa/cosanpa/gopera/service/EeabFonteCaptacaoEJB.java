package br.gov.pa.cosanpa.gopera.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.pa.cosanpa.gopera.fachada.IEEABFonteCaptacao;
import br.gov.pa.cosanpa.gopera.model.EEABFonteCaptacao;

@Stateless
public class EeabFonteCaptacaoEJB implements IEEABFonteCaptacao{

	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(EEABFonteCaptacao obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(EEABFonteCaptacao obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(EEABFonteCaptacao obj) {
		EEABFonteCaptacao objRemover = entity.find(EEABFonteCaptacao.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(EEABFonteCaptacao.class, id);
	}

	@Override
	public List<EEABFonteCaptacao> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<EEABFonteCaptacao> query= entity.createQuery("select c1 from EEABFonteCaptacao c1",EEABFonteCaptacao.class);
		List<EEABFonteCaptacao> lista = query.getResultList();

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
	public List<EEABFonteCaptacao> obterEEABFonteCaptacaoPorEEAB(
			Integer codigo) throws Exception {
		TypedQuery<EEABFonteCaptacao> query= entity.createQuery("select c1 from EEABFonteCaptacao c1 where eeab_id = " + codigo,EEABFonteCaptacao.class);
		List<EEABFonteCaptacao> lista = query.getResultList();
		
		return lista;
	}
	
}
