package br.gov.pa.cosanpa.gopera.service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.pa.cosanpa.gopera.fachada.IRegistroConsumoETA;
import br.gov.pa.cosanpa.gopera.model.ETA;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.RegistroConsumoETA;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@Stateless
public class RegistroConsumoETAEJB implements IRegistroConsumoETA {

	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(RegistroConsumoETA obj) throws Exception {
		entity.persist(obj);
	}

	@Override
	public void atualizar(RegistroConsumoETA obj) throws Exception {
		entity.merge(obj);
	}

	@Override
	public void excluir(RegistroConsumoETA obj) throws Exception {
		RegistroConsumoETA objRemover = entity.find(RegistroConsumoETA.class, obj.getCodigo());
		entity.remove(objRemover);
	}

	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(RegistroConsumoETA.class, id);
	}
	
	@Override
	public List<RegistroConsumoETA> obterLista(Integer min, Integer max) throws Exception {
		TypedQuery<RegistroConsumoETA> query = entity.createQuery("select c1 from RegistroConsumoETA c1", RegistroConsumoETA.class);
		List<RegistroConsumoETA> lista = query.getResultList();
		/*		
		for (int i = 0; i < lista.size(); i++) {
			lista.get(i).setEta(iETA.obterETA(lista.get(i).getEta().getCodigo()));
			lista.get(i).setUnidadeNegocioProxy(iProxy.getUnidadeNegocio(lista.get(i).getUnidadeNegocioProxy().getCodigo()));
			lista.get(i).setLocalidadeProxy(iProxy.getLocalidade(lista.get(i).getLocalidadeProxy().getCodigo()));
			lista.get(i).setMunicipioProxy(iProxy.getMunicipio(lista.get(i).getMunicipioProxy().getCodigo()));
			lista.get(i).setRegionalProxy(iProxy.getRegional(lista.get(i).getRegionalProxy().getCodigo()));
		}
		*/
		return lista;
	}
	
	@Override
	public RegistroConsumoETA obterRegistroConsumo(Integer codigo) throws Exception {
		TypedQuery<RegistroConsumoETA> query = entity.createQuery("select c1 from RegistroConsumoETA c1 where rgcs_id = " + codigo, RegistroConsumoETA.class);
		RegistroConsumoETA registroConsumo = query.getSingleResult();

		/*
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
	
	@Override
	public List<RegistroConsumoETA> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception {  
        CriteriaBuilder cb = entity.getCriteriaBuilder();  
        CriteriaQuery<RegistroConsumoETA> q = cb.createQuery(RegistroConsumoETA.class);  
        Root<RegistroConsumoETA> c = q.from(RegistroConsumoETA.class);
        Join<RegistroConsumoETA, RegionalProxy> greg = c.join("regionalProxy");
        Join<RegistroConsumoETA, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
        Join<RegistroConsumoETA, MunicipioProxy> muni = c.join("municipioProxy");
        Join<RegistroConsumoETA, LocalidadeProxy> loca = c.join("localidadeProxy");
        Join<RegistroConsumoETA, ETA> eta = c.join("eta");
        q.select(c);  
        if (filters != null && !filters.isEmpty()) {  
            Predicate[] predicates = new Predicate[filters.size()];  
            int i = 0;  
            for (Map.Entry<String, String> entry : filters.entrySet()) {  
                String key = entry.getKey();  
                String val = entry.getValue();
                Expression<String> path;
                try {  
	                if (key.equals("regionalProxy.nome")) path = greg.get("nome"); 
	                else if (key.equals("unidadeNegocioProxy.nome")) path = uneg.get("nome");
	                else if (key.equals("municipioProxy.nome")) path = muni.get("nome");
	                else if (key.equals("localidadeProxy.nome")) path = loca.get("nome");
	                else if (key.equals("eta.descricao")) path = eta.get("descricao");
	                else path = c.get(key);
	                if (key.equals("dataConsumo")){
	            		SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
	                	Date dataConsumo = formataData.parse(val); 
	                	predicates[i] = cb.and(cb.equal(path, dataConsumo));
	                }
	                else{
//                    if (RegionalProxy.class.getDeclaredField(key).getType().equals(String.class)) {  
                        predicates[i] = cb.and(cb.like(cb.lower(path), "%" + val.toLowerCase() + "%"));
	                }
                } catch (SecurityException ex) {  
                	ex.printStackTrace();
                }  
                i++;  
            }  
            q.where(predicates);  
        }  
        q.orderBy(cb.asc(greg.get("nome")),cb.asc(uneg.get("nome")), cb.asc(muni.get("nome")), cb.asc(loca.get("nome")), cb.asc(eta.get("descricao")));
        /*
        if (sortField != null && !sortField.isEmpty()) {  
            if (sortOrder.equals(SortOrder.ASCENDING)) {  
                q.orderBy(cb.asc(c.get(sortField)));  
            } else if (sortOrder.equals(SortOrder.DESCENDING)) {  
                q.orderBy(cb.desc(c.get(sortField)));  
            }  
        } 
        */ 
		TypedQuery<RegistroConsumoETA> query = entity.createQuery(q);
        query.setMaxResults(maxPerPage);  
        query.setFirstResult(startingAt);
		List<RegistroConsumoETA> lista = query.getResultList();
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
        CriteriaQuery q = cb.createQuery(RegistroConsumoETA.class);  
        Root<RegistroConsumoETA> c = q.from(RegistroConsumoETA.class);
        Join<RegistroConsumoETA, RegionalProxy> greg = c.join("regionalProxy");
        Join<RegistroConsumoETA, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
        Join<RegistroConsumoETA, MunicipioProxy> muni = c.join("municipioProxy");
        Join<RegistroConsumoETA, LocalidadeProxy> loca = c.join("localidadeProxy");
        Join<RegistroConsumoETA, ETA> eta = c.join("eta");
        q.select(cb.count(c));  
        if (filters != null && !filters.isEmpty()) {  
            Predicate[] predicates = new Predicate[filters.size()];  
            int i = 0;  
            for (Map.Entry<String, String> entry : filters.entrySet()) {  
                String key = entry.getKey();  
                String val = entry.getValue();
                Expression<String> path;
                try {  
	                if (key.equals("regionalProxy.nome")) path = greg.get("nome"); 
	                else if (key.equals("unidadeNegocioProxy.nome")) path = uneg.get("nome");
	                else if (key.equals("municipioProxy.nome")) path = muni.get("nome");
	                else if (key.equals("localidadeProxy.nome")) path = loca.get("nome");
	                else if (key.equals("eta.descricao")) path = eta.get("descricao");
	                else path = c.get(key);
	                if (key.equals("dataConsumo")){
	            		SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
	                	Date dataConsumo = formataData.parse(val); 
	                	predicates[i] = cb.and(cb.equal(path, dataConsumo));
	                }
	                else{
                        predicates[i] = cb.and(cb.like(cb.lower(path), "%" + val.toLowerCase() + "%"));
	                }
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
