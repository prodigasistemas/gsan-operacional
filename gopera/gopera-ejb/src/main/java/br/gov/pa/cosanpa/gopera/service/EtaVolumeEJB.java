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

import br.gov.pa.cosanpa.gopera.fachada.IETAVolume;
import br.gov.pa.cosanpa.gopera.model.ETA;
import br.gov.pa.cosanpa.gopera.model.ETAVolume;

@Stateless
public class EtaVolumeEJB implements IETAVolume{

	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(ETAVolume obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(ETAVolume obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(ETAVolume obj) {
		ETAVolume objRemover = entity.find(ETAVolume.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(ETAVolume.class, id);
	}

	@Override
	public List<ETAVolume> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<ETAVolume> query= entity.createQuery("select c1 from ETAVolume c1 order by c1.referencia DESC",ETAVolume.class);
		return query.getResultList();
	}

	@Override
	public ETAVolume obterETAVolume(Integer codigo) throws Exception {
		TypedQuery<ETAVolume> query= entity.createQuery("select c1 from ETAVolume c1 where etav_id = " + codigo,ETAVolume.class);
		ETAVolume etavolume = query.getSingleResult();
		
		ETAVolume ETAVolume = etavolume;
		for (int j = 0; j < ETAVolume.getVolumeEntrada().size(); j++) {
			etavolume.getVolumeEntrada().get(j);
		}
		
		for (int j = 0; j < ETAVolume.getVolumeSaida().size(); j++) {
			etavolume.getVolumeSaida().get(j);
		}
		
		return etavolume;		
	}
	
	@Override
	public boolean verificaMesReferencia(Integer codigo, String mesReferencia) throws Exception {
		TypedQuery<ETAVolume> query= entity.createQuery("select c1 from ETAVolume c1 where eta_id = " + codigo + " AND etav_referencia = '" + mesReferencia + "'",ETAVolume.class);
		List<ETAVolume> etavolume = query.getResultList();
		if(etavolume.size() > 0) { //Se houver mes cadastrado
			return false;
		} else {
			return true;
		}
	}
	@Override
	public List<ETAVolume> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception {  
        CriteriaBuilder cb = entity.getCriteriaBuilder();  
        CriteriaQuery<ETAVolume> q = cb.createQuery(ETAVolume.class);  
        Root<ETAVolume> c = q.from(ETAVolume.class);
//        Join<ConsumoETA, RegionalProxy> greg = c.join("regionalProxy");
//        Join<ConsumoETA, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
//        Join<ConsumoETA, MunicipioProxy> muni = c.join("municipioProxy");
//        Join<ConsumoETA, LocalidadeProxy> loca = c.join("localidadeProxy");
        Join<ETAVolume, ETA> eta = c.join("eta");
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
		TypedQuery<ETAVolume> query = entity.createQuery(q);
        query.setMaxResults(maxPerPage);  
        query.setFirstResult(startingAt);
		List<ETAVolume> lista = query.getResultList();
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
        CriteriaQuery q = cb.createQuery(ETAVolume.class);  
        Root<ETAVolume> c = q.from(ETAVolume.class);
/*        
        Join<ETAVolume, RegionalProxy> greg = c.join("regionalProxy");
        Join<ETAVolume, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
        Join<ETAVolume, MunicipioProxy> muni = c.join("municipioProxy");
        Join<ETAVolume, LocalidadeProxy> loca = c.join("localidadeProxy");
*/        
        Join<ETAVolume, ETA> eta = c.join("eta");
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
