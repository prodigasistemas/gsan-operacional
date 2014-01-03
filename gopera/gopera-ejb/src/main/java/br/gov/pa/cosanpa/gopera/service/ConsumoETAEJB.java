package br.gov.pa.cosanpa.gopera.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
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

import br.gov.pa.cosanpa.gopera.fachada.IConsumoETA;
import br.gov.pa.cosanpa.gopera.fachada.IProduto;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.model.ConsumoETA;
import br.gov.pa.cosanpa.gopera.model.ConsumoETAProduto;
import br.gov.pa.cosanpa.gopera.model.ETA;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.Produto;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;
import br.gov.pa.cosanpa.gopera.model.UsuarioProxy;
import br.gov.pa.cosanpa.gopera.util.PerfilBeanEnum;

@Stateless
public class ConsumoETAEJB implements IConsumoETA {

	@PersistenceContext
	private EntityManager entity;
	@EJB
	private IProxy fachadaProxy;
	@EJB
	private IProduto fachadaProduto;	
	
	@Override
	public void salvar(ConsumoETA obj) throws Exception {
		entity.persist(obj);
	}
	
	@Override
	public void atualizar(ConsumoETA obj) throws Exception {
		entity.merge(obj);
	}

	@Override
	public void salvar(ConsumoETAProduto obj) throws Exception {
		entity.persist(obj);
	}
	
	@Override
	public void atualizarConsumoProduto(ConsumoETAProduto obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(ConsumoETA obj) throws Exception {
		ConsumoETA objRemover = entity.find(ConsumoETA.class, obj.getCodigo());
		entity.remove(objRemover);
	}

	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(ConsumoETA.class, id);
	}

	@Override
	public List<ConsumoETA> obterLista(Integer min, Integer max) throws Exception {
		TypedQuery<ConsumoETA> query = entity.createQuery("select c1 from ConsumoETA c1 order by cons_data desc", ConsumoETA.class);
		List<ConsumoETA> lista = query.getResultList();
		
		return lista;
	}

	@Override
	public ConsumoETA obterConsumo(Integer codigo) throws Exception {
		TypedQuery<ConsumoETA> query = entity.createQuery("select c1 from ConsumoETA c1 where cons_id = " + codigo, ConsumoETA.class);
		ConsumoETA consumoETA = query.getSingleResult();
		
//		consumoETA.setEta(fachadaETA.obterETA(consumoETA.getEta().getCodigo()));
		ConsumoETA ConsumoETA = consumoETA;
		for (int j = 0; j < ConsumoETA.getConsumoProduto().size(); j++) {
			consumoETA.getConsumoProduto().get(j);
			consumoETA.getConsumoProduto().get(j).setProduto(fachadaProduto.obterProduto(consumoETA.getConsumoProduto().get(j).getProduto().getCodigo()));
		}
		return consumoETA;
	}
	
	@Override
	public ConsumoETA obterConsumoLazy(Integer codigo) throws Exception {
		TypedQuery<ConsumoETA> query = entity.createQuery("select c1 from ConsumoETA c1 where cons_id = " + codigo, ConsumoETA.class);
		ConsumoETA consumoETA = query.getSingleResult();
		return consumoETA;
	}
	
	@Override
	public List<ConsumoETA> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception {  
        CriteriaBuilder cb = entity.getCriteriaBuilder();  
        CriteriaQuery<ConsumoETA> q = cb.createQuery(ConsumoETA.class);  
        Root<ConsumoETA> c = q.from(ConsumoETA.class);
        Join<ConsumoETA, RegionalProxy> greg = c.join("regionalProxy");
        Join<ConsumoETA, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
        Join<ConsumoETA, MunicipioProxy> muni = c.join("municipioProxy");
        Join<ConsumoETA, LocalidadeProxy> loca = c.join("localidadeProxy");
        Join<ConsumoETA, ETA> eta = c.join("eta");
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
        q.orderBy(cb.desc(c.get("dataConsumo")));
        /*
        if (sortField != null && !sortField.isEmpty()) {  
            if (sortOrder.equals(SortOrder.ASCENDING)) {  
                q.orderBy(cb.asc(c.get(sortField)));  
            } else if (sortOrder.equals(SortOrder.DESCENDING)) {  
                q.orderBy(cb.desc(c.get(sortField)));  
            }  
        } 
        */ 
		TypedQuery<ConsumoETA> query = entity.createQuery(q);
        query.setMaxResults(maxPerPage);  
        query.setFirstResult(startingAt);
		List<ConsumoETA> lista = query.getResultList();
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
        CriteriaQuery q = cb.createQuery(ConsumoETA.class);  
        Root<ConsumoETA> c = q.from(ConsumoETA.class);
        Join<ConsumoETA, RegionalProxy> greg = c.join("regionalProxy");
        Join<ConsumoETA, UnidadeNegocioProxy> uneg = c.join("unidadeNegocioProxy");
        Join<ConsumoETA, MunicipioProxy> muni = c.join("municipioProxy");
        Join<ConsumoETA, LocalidadeProxy> loca = c.join("localidadeProxy");
        Join<ConsumoETA, ETA> eta = c.join("eta");
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
	
	public List<Produto> listarProdutos() {
		TypedQuery<Produto> query= entity.createQuery("select c1 from Produto c1",Produto.class);
		return query.getResultList();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<RegionalProxy> getListaConsumoETARegional(UsuarioProxy usuario) throws Exception {

		String localidade = "";
		String query = "SELECT DISTINCT A.greg_id, A.greg_nmregional FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
				" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade, G.eta_id, G.eta_nome " +
				" FROM cadastro.gerencia_regional A " +
				"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
				"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
				"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
				"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id " +
				"INNER JOIN operacao.registroconsumoeta F ON A.greg_id = F.greg_id AND B.uneg_id = F.uneg_id AND C.loca_id = F.loca_id AND E.muni_id = F.muni_id " +
				"INNER JOIN operacao.eta G ON F.eta_id = G.eta_id) AS A "; 

		//Se Perfil de Gerente, acesso a gerência Regional
		if (usuario.getPerfil() == PerfilBeanEnum.GERENTE){
			query = query + " WHERE A.greg_id = " + usuario.getRegionalProxy().getCodigo();
		}
		else if (usuario.getPerfil() == PerfilBeanEnum.SUPERVISOR || usuario.getPerfil() == PerfilBeanEnum.COORDENADOR){
			for (LocalidadeProxy colunas : usuario.getLocalidadeProxy()) {
				localidade = localidade + colunas.getCodigo() + ",";
			}
			localidade = localidade.substring(0, localidade.length() - 1);
			query = query + " WHERE A.loca_id IN (" + localidade + ")";		
		}
		
		query = query + " ORDER BY A.greg_nmregional";
		
		List<List> valores = fachadaProxy.selectRegistros(query);
		List<RegionalProxy> lista = new ArrayList<RegionalProxy>();
		
		if (valores == null) {
			return lista;
		}

		for (List colunas : valores) {
			RegionalProxy consumo = new RegionalProxy(Integer.parseInt(colunas.get(0).toString()),colunas.get(1).toString());
			lista.add(consumo);
		}
		
		return lista;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<UnidadeNegocioProxy> getListaConsumoETAUnidadeNegocio(UsuarioProxy usuario, Integer codigoRegional) throws Exception {

		String localidade = "";
		String query = "SELECT DISTINCT A.uneg_id, A.uneg_nmunidadenegocio FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
				" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade, G.eta_id, G.eta_nome " +
				" FROM cadastro.gerencia_regional A " +
				"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
				"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
				"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
				"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id " +
				"INNER JOIN operacao.registroconsumoeta F ON A.greg_id = F.greg_id AND B.uneg_id = F.uneg_id AND C.loca_id = F.loca_id AND E.muni_id = F.muni_id " +
				"INNER JOIN operacao.eta G ON F.eta_id = G.eta_id) AS A "; 

		//Se Perfil de Gerente, acesso a gerência Regional
		if (usuario.getPerfil() == PerfilBeanEnum.GERENTE){
			query = query + " WHERE A.greg_id = " + usuario.getRegionalProxy().getCodigo()
					      + "   AND A.greg_id = " + codigoRegional;
		}
		else if (usuario.getPerfil() == PerfilBeanEnum.SUPERVISOR || usuario.getPerfil() == PerfilBeanEnum.COORDENADOR){
			for (LocalidadeProxy colunas : usuario.getLocalidadeProxy()) {
				localidade = localidade + colunas.getCodigo() + ",";
			}
			localidade = localidade.substring(0, localidade.length() - 1);
			query = query + " WHERE A.loca_id IN (" + localidade + ")"
						  + "   AND A.greg_id = " + codigoRegional;
		}
		else {
			query = query + " WHERE A.greg_id = " + codigoRegional;			
		}
		
		query = query + " ORDER BY A.uneg_nmunidadenegocio";
		
		List<List> valores = fachadaProxy.selectRegistros(query);
		List<UnidadeNegocioProxy> lista = new ArrayList<UnidadeNegocioProxy>();
		
		if (valores == null) {
			return lista;
		}

		for (List colunas : valores) {
			UnidadeNegocioProxy consumo = new UnidadeNegocioProxy(Integer.parseInt(colunas.get(0).toString()),colunas.get(1).toString());
			lista.add(consumo);
		}
		
		return lista;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<MunicipioProxy> getListaConsumoETAMunicipio(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio) throws Exception {

		String localidade = "";
		String query = "SELECT DISTINCT A.muni_id, A.muni_nmmunicipio FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
				" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade, G.eta_id, G.eta_nome " +
				" FROM cadastro.gerencia_regional A " +
				"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
				"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
				"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
				"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id " +
				"INNER JOIN operacao.registroconsumoeta F ON A.greg_id = F.greg_id AND B.uneg_id = F.uneg_id AND C.loca_id = F.loca_id AND E.muni_id = F.muni_id " +
				"INNER JOIN operacao.eta G ON F.eta_id = G.eta_id) AS A "; 

		//Se Perfil de Gerente, acesso a gerência Regional
		if (usuario.getPerfil() == PerfilBeanEnum.GERENTE){
			query = query + " WHERE A.greg_id = " + usuario.getRegionalProxy().getCodigo()
					      + "   AND A.greg_id = " + codigoRegional
						  + "   AND A.uneg_id = " + codigoUnidadeNegocio;
		}
		else if (usuario.getPerfil() == PerfilBeanEnum.SUPERVISOR || usuario.getPerfil() == PerfilBeanEnum.COORDENADOR){
			for (LocalidadeProxy colunas : usuario.getLocalidadeProxy()) {
				localidade = localidade + colunas.getCodigo() + ",";
			}
			localidade = localidade.substring(0, localidade.length() - 1);
			query = query + " WHERE A.loca_id IN (" + localidade + ")"
						  + "   AND A.greg_id = " + codigoRegional
						  + "   AND A.uneg_id = " + codigoUnidadeNegocio;
		}
		else {
			query = query + " WHERE A.greg_id = " + codigoRegional
						  + "   AND A.uneg_id = " + codigoUnidadeNegocio;
		}
		
		query = query + " ORDER BY A.muni_nmmunicipio";
		
		List<List> valores = fachadaProxy.selectRegistros(query);
		List<MunicipioProxy> lista = new ArrayList<MunicipioProxy>();
		
		if (valores == null) {
			return lista;
		}

		for (List colunas : valores) {
			MunicipioProxy consumo = new MunicipioProxy(Integer.parseInt(colunas.get(0).toString()),colunas.get(1).toString());
			lista.add(consumo);
		}
		
		return lista;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<LocalidadeProxy> getListaConsumoETALocalidade(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio) throws Exception {

		String localidade = "";
		String query = "SELECT DISTINCT A.loca_id, A.loca_nmlocalidade FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
				" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade, G.eta_id, G.eta_nome " +
				" FROM cadastro.gerencia_regional A " +
				"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
				"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
				"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
				"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id " +
				"INNER JOIN operacao.registroconsumoeta F ON A.greg_id = F.greg_id AND B.uneg_id = F.uneg_id AND C.loca_id = F.loca_id AND E.muni_id = F.muni_id " +
				"INNER JOIN operacao.eta G ON F.eta_id = G.eta_id) AS A "; 

		//Se Perfil de Gerente, acesso a gerência Regional
		if (usuario.getPerfil() == PerfilBeanEnum.GERENTE){
			query = query + " WHERE A.greg_id = " + usuario.getRegionalProxy().getCodigo()
					      + "   AND A.greg_id = " + codigoRegional
						  + "   AND A.uneg_id = " + codigoUnidadeNegocio
						  + "   AND A.muni_id = " + codigoMunicipio;
		}
		else if (usuario.getPerfil() == PerfilBeanEnum.SUPERVISOR || usuario.getPerfil() == PerfilBeanEnum.COORDENADOR){
			for (LocalidadeProxy colunas : usuario.getLocalidadeProxy()) {
				localidade = localidade + colunas.getCodigo() + ",";
			}
			localidade = localidade.substring(0, localidade.length() - 1);
			query = query + " WHERE A.loca_id IN (" + localidade + ")"
						  + "   AND A.greg_id = " + codigoRegional
						  + "   AND A.uneg_id = " + codigoUnidadeNegocio
						  + "   AND A.muni_id = " + codigoMunicipio;			
		}
		else {
			query = query + " WHERE A.greg_id = " + codigoRegional
						  + "   AND A.uneg_id = " + codigoUnidadeNegocio
			  			  + "   AND A.muni_id = " + codigoMunicipio;
		}
		query = query + " ORDER BY A.loca_nmlocalidade";
		
		List<List> valores = fachadaProxy.selectRegistros(query);
		List<LocalidadeProxy> lista = new ArrayList<LocalidadeProxy>();
		
		if (valores == null) {
			return lista;
		}

		for (List colunas : valores) {
			LocalidadeProxy consumo = new LocalidadeProxy(Integer.parseInt(colunas.get(0).toString()),colunas.get(1).toString());
			lista.add(consumo);
		}
		
		return lista;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public List<ETA> getListaConsumoETA(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception {

		String localidade = "";
		String query = "SELECT DISTINCT A.eta_id, A.eta_nome FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
				" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade, G.eta_id, G.eta_nome " +
				" FROM cadastro.gerencia_regional A " +
				"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
				"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
				"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
				"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id " +
				"INNER JOIN operacao.registroconsumoeta F ON A.greg_id = F.greg_id AND B.uneg_id = F.uneg_id AND C.loca_id = F.loca_id AND E.muni_id = F.muni_id " +
				"INNER JOIN operacao.eta G ON F.eta_id = G.eta_id) AS A "; 

		//Se Perfil de Gerente, acesso a gerência Regional
		if (usuario.getPerfil() == PerfilBeanEnum.GERENTE){
			query = query + " WHERE A.greg_id = " + usuario.getRegionalProxy().getCodigo()
					      + "   AND A.greg_id = " + codigoRegional
						  + "   AND A.uneg_id = " + codigoUnidadeNegocio
						  + "   AND A.muni_id = " + codigoMunicipio
						  + "   AND A.loca_id = " + codigoLocalidade;
		}
		else if (usuario.getPerfil() == PerfilBeanEnum.SUPERVISOR || usuario.getPerfil() == PerfilBeanEnum.COORDENADOR){
			for (LocalidadeProxy colunas : usuario.getLocalidadeProxy()) {
				localidade = localidade + colunas.getCodigo() + ",";
			}
			localidade = localidade.substring(0, localidade.length() - 1);
			query = query + " WHERE A.loca_id IN (" + localidade + ")"
						  + "   AND A.greg_id = " + codigoRegional
						  + "   AND A.uneg_id = " + codigoUnidadeNegocio
						  + "   AND A.muni_id = " + codigoMunicipio
						  + "   AND A.loca_id = " + codigoLocalidade;
		}
		else {
			query = query + " WHERE A.greg_id = " + codigoRegional
						  + "   AND A.uneg_id = " + codigoUnidadeNegocio
			  			  + "   AND A.muni_id = " + codigoMunicipio
						  + "   AND A.loca_id = " + codigoLocalidade;
		}
		query = query + " ORDER BY A.eta_nome";
		
		List<List> valores = fachadaProxy.selectRegistros(query);
		List<ETA> lista = new ArrayList<ETA>();
		
		if (valores == null) {
			return lista;
		}

		for (List colunas : valores) {
			ETA consumo = new ETA(Integer.parseInt(colunas.get(0).toString()),colunas.get(1).toString());
			lista.add(consumo);
		}
		
		return lista;
	}
}
