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

import br.gov.pa.cosanpa.gopera.fachada.IETAHoras;
import br.gov.pa.cosanpa.gopera.model.ETA;
import br.gov.pa.cosanpa.gopera.model.ETAHoras;

@Stateless
public class EtaHorasEJB implements IETAHoras{
	
	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(ETAHoras obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(ETAHoras obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(ETAHoras obj) {
		ETAHoras objRemover = entity.find(ETAHoras.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(ETAHoras.class, id);
	}

	@Override
	public List<ETAHoras> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<ETAHoras> query= entity.createQuery("select c1 from ETAHoras c1",ETAHoras.class);
		List<ETAHoras> lista = query.getResultList();
		
		return lista;
	}
	
	@Override
	public ETAHoras obterETAHoras(Integer codigo) throws Exception {
		TypedQuery<ETAHoras> query= entity.createQuery("select c1 from ETAHoras c1 where etah_id = " + codigo,ETAHoras.class);
		ETAHoras etahoras = query.getSingleResult();
		
		ETAHoras ETAHoras = etahoras;
		for (int j = 0; j < ETAHoras.getCmb().size(); j++) {
			etahoras.getCmb().get(j);
		}
		
		return etahoras;	
	}

	@Override
	public boolean verificaMesReferencia(Integer codigo, String mesReferencia) throws Exception {
		TypedQuery<ETAHoras> query= entity.createQuery("select c1 from ETAHoras c1 where eta_id = " + codigo + " AND etah_referencia = '" + mesReferencia + "'",ETAHoras.class);
		List<ETAHoras> etahoras = query.getResultList();
		if(etahoras.size() > 0) { //Se houver mes cadastrado
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public List<ETAHoras> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception {  
        CriteriaBuilder cb = entity.getCriteriaBuilder();  
        CriteriaQuery<ETAHoras> q = cb.createQuery(ETAHoras.class);  
        Root<ETAHoras> c = q.from(ETAHoras.class);
//        Join<ConsumoETA, RegionalProxy> greg = c.join("regionalProxy");
//        Join<ConsumoETA, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
//        Join<ConsumoETA, MunicipioProxy> muni = c.join("municipioProxy");
//        Join<ConsumoETA, LocalidadeProxy> loca = c.join("localidadeProxy");
        Join<ETAHoras, ETA> eta = c.join("eta");
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
	                if (key.equals("eta.descricao")) path = eta.get("descricao");
	                else path = c.get(key);
	                if (key.equals("referencia")){
	            		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
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
        q.orderBy(cb.desc(c.get("referencia")));
        /*
        if (sortField != null && !sortField.isEmpty()) {  
            if (sortOrder.equals(SortOrder.ASCENDING)) {  
                q.orderBy(cb.asc(c.get(sortField)));  
            } else if (sortOrder.equals(SortOrder.DESCENDING)) {  
                q.orderBy(cb.desc(c.get(sortField)));  
            }  
        } 
        */ 
		TypedQuery<ETAHoras> query = entity.createQuery(q);
        query.setMaxResults(maxPerPage);  
        query.setFirstResult(startingAt);
		List<ETAHoras> lista = query.getResultList();
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
        CriteriaQuery q = cb.createQuery(ETAHoras.class);  
        Root<ETAHoras> c = q.from(ETAHoras.class);
/*        
        Join<EEABHoras, RegionalProxy> greg = c.join("regionalProxy");
        Join<EEABHoras, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
        Join<EEABHoras, MunicipioProxy> muni = c.join("municipioProxy");
        Join<EEABHoras, LocalidadeProxy> loca = c.join("localidadeProxy");
*/        
        Join<ETAHoras, ETA> eta = c.join("eta");
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
	                if (key.equals("eta.descricao")) path = eta.get("descricao");
	                else path = c.get(key);
	                if (key.equals("referencia")){
	            		SimpleDateFormat formataData = new SimpleDateFormat("MM/yyyy");
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
