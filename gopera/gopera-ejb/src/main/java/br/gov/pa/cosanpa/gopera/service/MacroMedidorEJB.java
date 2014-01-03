package br.gov.pa.cosanpa.gopera.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.pa.cosanpa.gopera.fachada.IMacroMedidor;
import br.gov.pa.cosanpa.gopera.model.MacroMedidor;

@Stateless
public class MacroMedidorEJB implements IMacroMedidor{

	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(MacroMedidor obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(MacroMedidor obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(MacroMedidor obj) {
		MacroMedidor objRemover = entity.find(MacroMedidor.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(MacroMedidor.class, id);
	}

	@Override
	public List<MacroMedidor> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<MacroMedidor> query= entity.createQuery("select c1 from MacroMedidor c1",MacroMedidor.class);
		List<MacroMedidor> lista = query.getResultList();

		/*
		for (int i = 0; i < lista.size(); i++) {
			lista.get(i).getAfericao();
		}
		*/
		
		return lista;
	}
	
	@Override
	public List<MacroMedidor> obterLista2(Integer codigo)
			throws Exception {
		TypedQuery<MacroMedidor> query= entity.createQuery("select c1 from MacroMedidor c1 where mmed_id = " + codigo,MacroMedidor.class);
		List<MacroMedidor> lista = query.getResultList();

		/*
		for (int i = 0; i < lista.size(); i++) {
			lista.get(i).getAfericao();
		}
		*/
		
		return lista;
	}
	
	
	@Override
	public MacroMedidor obterMacroMedidor(Integer codigo) throws Exception {
		TypedQuery<MacroMedidor> query= entity.createQuery("select c1 from MacroMedidor c1 where mmed_id = " + codigo,MacroMedidor.class);
		MacroMedidor medidor = query.getSingleResult();
		MacroMedidor MacroMedidor = medidor;
		for (int j = 0; j < MacroMedidor.getAfericao().size(); j++) {
			medidor.getAfericao().get(j);
		}
		return medidor;		
	}

}
