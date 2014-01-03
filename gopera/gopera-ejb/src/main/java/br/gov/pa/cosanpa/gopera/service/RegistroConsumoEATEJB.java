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

import br.gov.pa.cosanpa.gopera.fachada.IRegistroConsumoEAT;
import br.gov.pa.cosanpa.gopera.model.EEAT;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.RegistroConsumoEAT;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@Stateless
public class RegistroConsumoEATEJB implements IRegistroConsumoEAT {

	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(RegistroConsumoEAT obj) throws Exception {
		entity.persist(obj);
	}

	@Override
	public void atualizar(RegistroConsumoEAT obj) throws Exception {
		entity.merge(obj);
	}

	@Override
	public void excluir(RegistroConsumoEAT obj) throws Exception {
		RegistroConsumoEAT objRemover = entity.find(RegistroConsumoEAT.class, obj.getCodigo());
		entity.remove(objRemover);
	}

	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(RegistroConsumoEAT.class, id);
	}

	@Override
	public List<RegistroConsumoEAT> obterLista(Integer min, Integer max) throws Exception {
		TypedQuery<RegistroConsumoEAT> query = entity.createQuery("select c1 from RegistroConsumoEAT c1", RegistroConsumoEAT.class);
		List<RegistroConsumoEAT> lista = query.getResultList();
		/*
		for (int i = 0; i < lista.size(); i++) {
			lista.get(i).setEat(iEAT.obterEEAT(lista.get(i).getEat().getCodigo()));
			lista.get(i).setUnidadeNegocioProxy(iProxy.getUnidadeNegocio(lista.get(i).getUnidadeNegocioProxy().getCodigo()));
			lista.get(i).setLocalidadeProxy(iProxy.getLocalidade(lista.get(i).getLocalidadeProxy().getCodigo()));
			lista.get(i).setMunicipioProxy(iProxy.getMunicipio(lista.get(i).getMunicipioProxy().getCodigo()));
			lista.get(i).setRegionalProxy(iProxy.getRegional(lista.get(i).getRegionalProxy().getCodigo()));
		}
		*/
		return lista;
	}

	@Override
	public RegistroConsumoEAT obterRegistroConsumo(Integer codigo) throws Exception {
		TypedQuery<RegistroConsumoEAT> query = entity.createQuery("select c1 from RegistroConsumoEAT c1 where rgcs_id = " + codigo, RegistroConsumoEAT.class);
		RegistroConsumoEAT registroConsumo = query.getSingleResult();

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
	public List<RegistroConsumoEAT> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception {  
        CriteriaBuilder cb = entity.getCriteriaBuilder();  
        CriteriaQuery<RegistroConsumoEAT> q = cb.createQuery(RegistroConsumoEAT.class);  
        Root<RegistroConsumoEAT> c = q.from(RegistroConsumoEAT.class);
        Join<RegistroConsumoEAT, RegionalProxy> greg = c.join("regionalProxy");
        Join<RegistroConsumoEAT, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
        Join<RegistroConsumoEAT, MunicipioProxy> muni = c.join("municipioProxy");
        Join<RegistroConsumoEAT, LocalidadeProxy> loca = c.join("localidadeProxy");
        Join<RegistroConsumoEAT, EEAT> eta = c.join("eat");
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
	                else if (key.equals("eat.descricao")) path = eta.get("descricao");
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
		TypedQuery<RegistroConsumoEAT> query = entity.createQuery(q);
        query.setMaxResults(maxPerPage);  
        query.setFirstResult(startingAt);
		List<RegistroConsumoEAT> lista = query.getResultList();
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
        CriteriaQuery q = cb.createQuery(RegistroConsumoEAT.class);  
        Root<RegistroConsumoEAT> c = q.from(RegistroConsumoEAT.class);
        Join<RegistroConsumoEAT, RegionalProxy> greg = c.join("regionalProxy");
        Join<RegistroConsumoEAT, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
        Join<RegistroConsumoEAT, MunicipioProxy> muni = c.join("municipioProxy");
        Join<RegistroConsumoEAT, LocalidadeProxy> loca = c.join("localidadeProxy");
        Join<RegistroConsumoEAT, EEAT> eta = c.join("eat");
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
	                else if (key.equals("eat.descricao")) path = eta.get("descricao");
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
