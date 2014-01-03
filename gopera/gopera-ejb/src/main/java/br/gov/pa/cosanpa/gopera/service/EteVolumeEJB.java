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

import br.gov.pa.cosanpa.gopera.fachada.IETEVolume;
import br.gov.pa.cosanpa.gopera.model.ETE;
import br.gov.pa.cosanpa.gopera.model.ETEVolume;

@Stateless
public class EteVolumeEJB implements IETEVolume{

	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(ETEVolume obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(ETEVolume obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(ETEVolume obj) {
		ETEVolume objRemover = entity.find(ETEVolume.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(ETEVolume.class, id);
	}

	@Override
	public List<ETEVolume> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<ETEVolume> query= entity.createQuery("select c1 from ETEVolume c1 order by c1.referencia DESC",ETEVolume.class);
		return query.getResultList();
	}

	@Override
	public ETEVolume obterETEVolume(Integer codigo) throws Exception {
		TypedQuery<ETEVolume> query= entity.createQuery("select c1 from ETEVolume c1 where etev_id = " + codigo,ETEVolume.class);
		ETEVolume etevolume = query.getSingleResult();
		return etevolume;		
	}
	
	@Override
	public boolean verificaMesReferencia(Integer codigo, String mesReferencia) throws Exception {
		TypedQuery<ETEVolume> query= entity.createQuery("select c1 from ETEVolume c1 where ete_id = " + codigo + " AND etev_referencia = '" + mesReferencia + "'",ETEVolume.class);
		List<ETEVolume> etevolume = query.getResultList();
		if(etevolume.size() > 0) { //Se houver mes cadastrado
			return false;
		} else {
			return true;
		}
	}
	@Override
	public List<ETEVolume> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception {  
        CriteriaBuilder cb = entity.getCriteriaBuilder();  
        CriteriaQuery<ETEVolume> q = cb.createQuery(ETEVolume.class);  
        Root<ETEVolume> c = q.from(ETEVolume.class);
//        Join<ConsumoETE, RegionalProxy> greg = c.join("regionalProxy");
//        Join<ConsumoETE, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
//        Join<ConsumoETE, MunicipioProxy> muni = c.join("municipioProxy");
//        Join<ConsumoETE, LocalidadeProxy> loca = c.join("localidadeProxy");
        Join<ETEVolume, ETE> ete = c.join("ete");
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
	                if (key.equals("ete.descricao")) path = ete.get("descricao");
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
		TypedQuery<ETEVolume> query = entity.createQuery(q);
        query.setMaxResults(maxPerPage);  
        query.setFirstResult(startingAt);
		List<ETEVolume> lista = query.getResultList();
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
        CriteriaQuery q = cb.createQuery(ETEVolume.class);  
        Root<ETEVolume> c = q.from(ETEVolume.class);
/*        
        Join<ETEVolume, RegionalProxy> greg = c.join("regionalProxy");
        Join<ETEVolume, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
        Join<ETEVolume, MunicipioProxy> muni = c.join("municipioProxy");
        Join<ETEVolume, LocalidadeProxy> loca = c.join("localidadeProxy");
*/        
        Join<ETEVolume, ETE> ete = c.join("ete");
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
	                if (key.equals("ete.descricao")) path = ete.get("descricao");
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
