package br.gov.pa.cosanpa.gopera.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.pa.cosanpa.gopera.fachada.IRegistroConsumoSistemaAbastecimento;
import br.gov.pa.cosanpa.gopera.model.RegistroConsumoSistemaAbastecimento;

@Stateless
public class RegistroConsumoSistemaAbastecimentoEJB implements IRegistroConsumoSistemaAbastecimento {

	@PersistenceContext
	private EntityManager entity;

	@Override
	public void salvar(RegistroConsumoSistemaAbastecimento obj) throws Exception {
		entity.persist(obj);
	}

	@Override
	public void atualizar(RegistroConsumoSistemaAbastecimento obj) throws Exception {
		entity.merge(obj);
	}

	@Override
	public void excluir(RegistroConsumoSistemaAbastecimento obj) throws Exception {
		RegistroConsumoSistemaAbastecimento objRemover = entity.find(RegistroConsumoSistemaAbastecimento.class, obj.getCodigo());
		entity.remove(objRemover);
	}

	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(RegistroConsumoSistemaAbastecimento.class, id);
	}

	@Override
	public List<RegistroConsumoSistemaAbastecimento> obterLista(Integer min, Integer max) throws Exception {
		TypedQuery<RegistroConsumoSistemaAbastecimento> query = entity.createQuery("select c1 from RegistroConsumoSistemaAbastecimento c1", RegistroConsumoSistemaAbastecimento.class);
		List<RegistroConsumoSistemaAbastecimento> lista = query.getResultList();
/*		
		for (int i = 0; i < lista.size(); i++) {
			lista.get(i).setSistemaAbastecimentoProxy(iProxy.getSistemaAbastecimento(lista.get(i).getSistemaAbastecimentoProxy().getCodigo()));
			lista.get(i).setUnidadeNegocioProxy(iProxy.getUnidadeNegocio(lista.get(i).getUnidadeNegocioProxy().getCodigo()));
			lista.get(i).setLocalidadeProxy(iProxy.getLocalidade(lista.get(i).getLocalidadeProxy().getCodigo()));
			lista.get(i).setMunicipioProxy(iProxy.getMunicipio(lista.get(i).getMunicipioProxy().getCodigo()));
			lista.get(i).setRegionalProxy(iProxy.getRegional(lista.get(i).getRegionalProxy().getCodigo()));
		}
*/		
		return lista;
	}

	@Override
	public RegistroConsumoSistemaAbastecimento obterRegistroConsumo(Integer codigo) throws Exception {
		TypedQuery<RegistroConsumoSistemaAbastecimento> query = entity.createQuery("select c1 from RegistroConsumoSistemaAbastecimento c1 where rgcs_id = " + codigo, RegistroConsumoSistemaAbastecimento.class);
		RegistroConsumoSistemaAbastecimento registroConsumo = query.getSingleResult();
/*
		registroConsumo.setSistemaAbastecimentoProxy(iProxy.getSistemaAbastecimento(registroConsumo.getSistemaAbastecimentoProxy().getCodigo()));
		registroConsumo.setUnidadeNegocioProxy(iProxy.getUnidadeNegocio(registroConsumo.getUnidadeNegocioProxy().getCodigo()));
		registroConsumo.setLocalidadeProxy(iProxy.getLocalidade(registroConsumo.getLocalidadeProxy().getCodigo()));
		registroConsumo.setMunicipioProxy(iProxy.getMunicipio(registroConsumo.getMunicipioProxy().getCodigo()));
		registroConsumo.setRegionalProxy(iProxy.getRegional(registroConsumo.getRegionalProxy().getCodigo()));
*/
		for (int j = 0; j < registroConsumo.getRegistrosConsumo().size(); j++) {
			registroConsumo.getRegistrosConsumo().get(j);
		}
		return registroConsumo;
	}
}
