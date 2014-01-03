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

import br.gov.pa.cosanpa.gopera.fachada.IEEABHoras;
import br.gov.pa.cosanpa.gopera.model.EEAB;
import br.gov.pa.cosanpa.gopera.model.EEABHoras;

@Stateless
public class EeabHorasEJB implements IEEABHoras{

	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(EEABHoras obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(EEABHoras obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(EEABHoras obj) {
		EEABHoras objRemover = entity.find(EEABHoras.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(EEABHoras.class, id);
	}

	@Override
	public List<EEABHoras> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<EEABHoras> query= entity.createQuery("select c1 from EEABHoras c1",EEABHoras.class);
		return query.getResultList();
	}
	
	@Override
	public EEABHoras obterEEABHoras(Integer codigo) throws Exception {
		TypedQuery<EEABHoras> query= entity.createQuery("select c1 from EEABHoras c1 where eabh_id = " + codigo,EEABHoras.class);
		EEABHoras eeabhoras = query.getSingleResult();
		
		EEABHoras EEABHoras = eeabhoras;
		for (int j = 0; j < EEABHoras.getCmb().size(); j++) {
			eeabhoras.getCmb().get(j);
		}
		
		return eeabhoras;		
	}
	
	@Override
	public boolean verificaMesReferencia(Integer codigo, String mesReferencia) throws Exception {
		TypedQuery<EEABHoras> query= entity.createQuery("select c1 from EEABHoras c1 where eeab_id = " + codigo + " AND eabh_referencia = '" + mesReferencia + "'",EEABHoras.class);
		List<EEABHoras> eeabhoras = query.getResultList();
		if(eeabhoras.size() > 0) { //Se houver mes cadastrado
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public List<EEABHoras> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception {  
        CriteriaBuilder cb = entity.getCriteriaBuilder();  
        CriteriaQuery<EEABHoras> q = cb.createQuery(EEABHoras.class);  
        Root<EEABHoras> c = q.from(EEABHoras.class);
//        Join<ConsumoETA, RegionalProxy> greg = c.join("regionalProxy");
//        Join<ConsumoETA, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
//        Join<ConsumoETA, MunicipioProxy> muni = c.join("municipioProxy");
//        Join<ConsumoETA, LocalidadeProxy> loca = c.join("localidadeProxy");
        Join<EEABHoras, EEAB> eab = c.join("eeab");
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
	                if (key.equals("eeab.descricao")) path = eab.get("descricao");
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
		TypedQuery<EEABHoras> query = entity.createQuery(q);
        query.setMaxResults(maxPerPage);  
        query.setFirstResult(startingAt);
		List<EEABHoras> lista = query.getResultList();
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
        CriteriaQuery q = cb.createQuery(EEABHoras.class);  
        Root<EEABHoras> c = q.from(EEABHoras.class);
/*        
        Join<EEABHoras, RegionalProxy> greg = c.join("regionalProxy");
        Join<EEABHoras, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
        Join<EEABHoras, MunicipioProxy> muni = c.join("municipioProxy");
        Join<EEABHoras, LocalidadeProxy> loca = c.join("localidadeProxy");
*/        
        Join<EEABHoras, EEAB> eab = c.join("eeab");
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
	                if (key.equals("eeab.descricao")) path = eab.get("descricao");
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
