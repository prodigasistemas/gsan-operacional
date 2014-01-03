package br.gov.pa.cosanpa.gopera.service;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.pa.cosanpa.gopera.fachada.IETE;
import br.gov.pa.cosanpa.gopera.model.ETE;

@Stateless
public class EteEJB implements IETE{

	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(ETE obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(ETE obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(ETE obj) {
		ETE objRemover = entity.find(ETE.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(ETE.class, id);
	}

	@Override
	public List<ETE> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<ETE> query= entity.createQuery("select c1 from ETE c1 order by c1.descricao",ETE.class);
		return query.getResultList();
	}

	@Override
	public ETE obterETELazy(Integer codigo) throws Exception {
		TypedQuery<ETE> query= entity.createQuery("select c1 from ETE c1 where ete_id = " + codigo,ETE.class);
		ETE eta = query.getSingleResult();
		return eta;
	}

	@Override
	public List<ETE> getListaETE(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade)
			throws Exception {
		TypedQuery<ETE> query= entity.createQuery("select c1 from ETE c1 where regionalProxy = " + codigoRegional + 
														" and unidadeNegocioProxy = " + codigoUnidadeNegocio + 
														" and municipioProxy = " + codigoMunicipio + 
														" and localidadeProxy = " + codigoLocalidade + 
														" order by descricao DESC",ETE.class);
		return query.getResultList();
	}
	
	@Override
	public List<ETE> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception {  
        CriteriaBuilder cb = entity.getCriteriaBuilder();  
        CriteriaQuery<ETE> q = cb.createQuery(ETE.class);  
        Root<ETE> c = q.from(ETE.class);
//        Join<ConsumoETA, RegionalProxy> greg = c.join("regionalProxy");
//        Join<ConsumoETA, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
//        Join<ConsumoETA, MunicipioProxy> muni = c.join("municipioProxy");
//        Join<ConsumoETA, LocalidadeProxy> loca = c.join("localidadeProxy");
        q.select(c);  
        if (filters != null && !filters.isEmpty()) {  
            Predicate[] predicates = new Predicate[filters.size()];  
            int i = 0;  
            for (Map.Entry<String, String> entry : filters.entrySet()) {  
                String key = entry.getKey();  
                String val = entry.getValue();
                Expression<String> path;
                try {  
/*                	
	                if (key.equals("regionalProxy.nome")) path = greg.get("nome"); 
	                else if (key.equals("unidadeNegocioProxy.nome")) path = uneg.get("nome");
	                else if (key.equals("municipioProxy.nome")) path = muni.get("nome");
	                else if (key.equals("localidadeProxy.nome")) path = loca.get("nome");
*/	                
	                path = c.get(key);
                    predicates[i] = cb.and(cb.like(cb.lower(path), "%" + val.toLowerCase() + "%"));
                } catch (SecurityException ex) {  
                	ex.printStackTrace();
                }  
                i++;  
            }  
            q.where(predicates);  
        }  
        q.orderBy(cb.asc(c.get("descricao")));
        /*
        if (sortField != null && !sortField.isEmpty()) {  
            if (sortOrder.equals(SortOrder.ASCENDING)) {  
                q.orderBy(cb.asc(c.get(sortField)));  
            } else if (sortOrder.equals(SortOrder.DESCENDING)) {  
                q.orderBy(cb.desc(c.get(sortField)));  
            }  
        } 
        */ 
		TypedQuery<ETE> query = entity.createQuery(q);
        query.setMaxResults(maxPerPage);  
        query.setFirstResult(startingAt);
		List<ETE> lista = query.getResultList();
/*
		for (int i = 0; i < lista.size(); i++) {
			lista.get(i).setEta(fachadaETA.obterETALazy(lista.get(i).getEta().getCodigo()));
			lista.get(i).setRegionalProxy(fachadaProxy.getRegional(lista.get(i).getRegionalProxy().getCodigo()));
			lista.get(i).setUnidadeNegocioProxy(fachadaProxy.getUnidadeNegocio(lista.get(i).getUnidadeNegocioProxy().getCodigo()));
			lista.get(i).setMunicipioProxy(fachadaProxy.getMunicipio(lista.get(i).getMunicipioProxy().getCodigo()));
			lista.get(i).setLocalidadeProxy(fachadaProxy.getLocalidade(lista.get(i).getLocalidadeProxy().getCodigo()));
		}
				*/
        return lista;  
    }  
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int obterQtdRegistros(Map<String, String> filters) throws Exception {
        CriteriaBuilder cb = entity.getCriteriaBuilder();  
        CriteriaQuery q = cb.createQuery(ETE.class);  
        Root<ETE> c = q.from(ETE.class);
/*        
        Join<EEABHoras, RegionalProxy> greg = c.join("regionalProxy");
        Join<EEABHoras, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
        Join<EEABHoras, MunicipioProxy> muni = c.join("municipioProxy");
        Join<EEABHoras, LocalidadeProxy> loca = c.join("localidadeProxy");
*/        
        q.select(cb.count(c));  
        if (filters != null && !filters.isEmpty()) {  
            Predicate[] predicates = new Predicate[filters.size()];  
            int i = 0;  
            for (Map.Entry<String, String> entry : filters.entrySet()) {  
                String key = entry.getKey();  
                String val = entry.getValue();
                Expression<String> path;
                try {  
/*                	
	                if (key.equals("regionalProxy.nome")) path = greg.get("nome"); 
	                else if (key.equals("unidadeNegocioProxy.nome")) path = uneg.get("nome");
	                else if (key.equals("municipioProxy.nome")) path = muni.get("nome");
	                else if (key.equals("localidadeProxy.nome")) path = loca.get("nome");
*/	                
	                path = c.get(key);
                    predicates[i] = cb.and(cb.like(cb.lower(path), "%" + val.toLowerCase() + "%"));
                } catch (SecurityException ex) {  
                	ex.printStackTrace();
                }  
                i++;  
            }   
            q.where(predicates);  
        }  
        Query query = entity.createQuery(q);
        return ((Long) query.getSingleResult()).intValue();
	}
}
