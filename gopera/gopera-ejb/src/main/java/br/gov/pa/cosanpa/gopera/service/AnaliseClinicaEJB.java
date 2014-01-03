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

import br.gov.pa.cosanpa.gopera.fachada.IAnaliseClinica;
import br.gov.pa.cosanpa.gopera.model.AnaliseClinica;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@Stateless
public class AnaliseClinicaEJB implements IAnaliseClinica{

	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(AnaliseClinica obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(AnaliseClinica obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(AnaliseClinica obj) {
		AnaliseClinica objRemover = entity.find(AnaliseClinica.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(AnaliseClinica.class, id);
	}

	@Override
	public List<AnaliseClinica> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<AnaliseClinica> query= entity.createQuery("select c1 from AnaliseClinica c1 order by c1.referencia DESC",AnaliseClinica.class);
		return query.getResultList();
	}

	@Override
	public AnaliseClinica obterAnaliseClinica(Integer codigo) throws Exception {
		TypedQuery<AnaliseClinica> query= entity.createQuery("select c1 from AnaliseClinica c1 where etev_id = " + codigo,AnaliseClinica.class);
		AnaliseClinica etevolume = query.getSingleResult();
		return etevolume;		
	}
	
	@Override
	public boolean verificaMesReferencia(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade, String mesReferencia) throws Exception {
		TypedQuery<AnaliseClinica> query= entity.createQuery("select c1 from AnaliseClinica c1 where regionalProxy = " + codigoRegional + 
				" and unidadeNegocioProxy = " + codigoUnidadeNegocio + 
				" and municipioProxy = " + codigoMunicipio + 
				" and localidadeProxy = " + codigoLocalidade + 
				" and referencia = '" + mesReferencia + "'",AnaliseClinica.class);
		
		List<AnaliseClinica> analiseClinica = query.getResultList();
		if(analiseClinica.size() > 0) { //Se houver mes cadastrado
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public List<AnaliseClinica> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception {  
        CriteriaBuilder cb = entity.getCriteriaBuilder();  
        CriteriaQuery<AnaliseClinica> q = cb.createQuery(AnaliseClinica.class);  
        Root<AnaliseClinica> c = q.from(AnaliseClinica.class);
        Join<AnaliseClinica, RegionalProxy> greg = c.join("regionalProxy");
        Join<AnaliseClinica, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
        Join<AnaliseClinica, MunicipioProxy> muni = c.join("municipioProxy");
        Join<AnaliseClinica, LocalidadeProxy> loca = c.join("localidadeProxy");
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
		TypedQuery<AnaliseClinica> query = entity.createQuery(q);
        query.setMaxResults(maxPerPage);  
        query.setFirstResult(startingAt);
		List<AnaliseClinica> lista = query.getResultList();
/*
		for (int i = 0; i < lista.size(); i++) {
			lista.get(i).setEta(fachadaETE.obterETELazy(lista.get(i).getEta().getCodigo()));
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
        CriteriaQuery q = cb.createQuery(AnaliseClinica.class);  
        Root<AnaliseClinica> c = q.from(AnaliseClinica.class);
        Join<AnaliseClinica, RegionalProxy> greg = c.join("regionalProxy");
        Join<AnaliseClinica, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
        Join<AnaliseClinica, MunicipioProxy> muni = c.join("municipioProxy");
        Join<AnaliseClinica, LocalidadeProxy> loca = c.join("localidadeProxy");
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
