package br.gov.pa.cosanpa.gopera.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.pa.cosanpa.gopera.fachada.IRegistroConsumo;
import br.gov.pa.cosanpa.gopera.model.Produto;
import br.gov.pa.cosanpa.gopera.model.RegistroConsumo;


@Stateless
public class RegistroConsumoEJB implements IRegistroConsumo{

	@PersistenceContext
	private EntityManager entity;

	@Override
	public void salvar(RegistroConsumo obj) throws Exception {
		entity.persist(obj);
	}
	
	@Override
	public void atualizar(RegistroConsumo obj) throws Exception {
		entity.merge(obj);
	}

	@Override
	public void excluir(RegistroConsumo obj) throws Exception {
		RegistroConsumo objRemover = entity.find(RegistroConsumo.class, obj.getCodigo());
		entity.remove(objRemover);
	}

	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(RegistroConsumo.class, id);

	}
	
	@Override
	public List<RegistroConsumo> obterLista(Integer min, Integer max) throws Exception {
		TypedQuery<RegistroConsumo> query = entity.createQuery("select c1 from RegistroConsumo c1",RegistroConsumo.class);
		List<RegistroConsumo> lista = query.getResultList();
		for (int i = 0; i < lista.size(); i++) {
			for (int j = 0; j < lista.get(i).getProdutos().size(); j++) {
				lista.get(i).getProdutos().get(j).getCodigo();	
			}
		}
		return lista;
	}

	@Override
	public List<Produto> listarProdutos() {
		TypedQuery<Produto> query= entity.createQuery("select c1 from Produto c1",Produto.class);
		return query.getResultList();
	}
}
