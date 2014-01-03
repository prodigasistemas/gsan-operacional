package br.gov.pa.cosanpa.gopera.service;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.pa.cosanpa.gopera.fachada.IProduto;
import br.gov.pa.cosanpa.gopera.model.Produto;
import br.gov.pa.cosanpa.gopera.model.UnidadeMedida;


@Stateless
public class ProdutoEJB implements IProduto{

	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(Produto obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(Produto obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(Produto obj) {
		Produto objRemover = entity.find(Produto.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(Produto.class, id);
	}

	@Override
	public List<Produto> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<Produto> query= entity.createQuery("select c1 from Produto c1",Produto.class);
		return query.getResultList();
	}

	@Override
	public Produto obterProduto(Integer codigo) throws Exception {
		TypedQuery<Produto> query = entity.createQuery("select c1 from Produto c1 where prod_id = " + codigo, Produto.class);
		Produto produto = query.getSingleResult();
		return produto;
	}
	
	@Override
	public List<UnidadeMedida> listarUnidadeMedidas(){
		TypedQuery<UnidadeMedida> query = entity.createQuery("select c1 from UnidadeMedida c1",UnidadeMedida.class);
		return query.getResultList();
	}
}
