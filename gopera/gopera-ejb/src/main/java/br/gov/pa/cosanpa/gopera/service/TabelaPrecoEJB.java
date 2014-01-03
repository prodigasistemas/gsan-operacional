package br.gov.pa.cosanpa.gopera.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.pa.cosanpa.gopera.fachada.ITabelaPreco;
import br.gov.pa.cosanpa.gopera.model.TabelaPreco;

@Stateless
public class TabelaPrecoEJB implements ITabelaPreco {

	@PersistenceContext
	private EntityManager entity;

	@Override
	public void salvar(TabelaPreco obj) throws Exception {
		entity.persist(obj);
	}
	
	@Override
	public void atualizar(TabelaPreco obj) throws Exception {
		entity.merge(obj);

	}

	@Override
	public void excluir(TabelaPreco obj) throws Exception {
		TabelaPreco objRemover = entity.find(TabelaPreco.class, obj.getCodigo());
		entity.remove(objRemover);
	}

	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(TabelaPreco.class, id);

	}

	@Override
	public List<TabelaPreco> obterLista(Integer min, Integer max) throws Exception {
		TypedQuery<TabelaPreco> query = entity.createQuery("select c1 from TabelaPreco c1 order by tabp_vigencia DESC", TabelaPreco.class);
		List<TabelaPreco> lista = query.getResultList();

		for (int i = 0; i < lista.size(); i++) {
			TabelaPreco tabelaPreco = lista.get(i);
			for (int j = 0; j < tabelaPreco.getTabelaPrecoProduto().size(); j++) {
				lista.get(i).getTabelaPrecoProduto().get(j);
			}
		}
		
		return lista;
	}
}
