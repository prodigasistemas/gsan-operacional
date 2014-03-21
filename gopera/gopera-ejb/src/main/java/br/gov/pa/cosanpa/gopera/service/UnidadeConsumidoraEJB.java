package br.gov.pa.cosanpa.gopera.service;

import java.util.ArrayList;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.fachada.IUnidadeConsumidora;
import br.gov.pa.cosanpa.gopera.model.EEAB;
import br.gov.pa.cosanpa.gopera.model.EEAT;
import br.gov.pa.cosanpa.gopera.model.ETA;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RSO;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.UnidadeConsumidora;
import br.gov.pa.cosanpa.gopera.model.UnidadeConsumidoraOperacional;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;
import br.gov.pa.cosanpa.gopera.model.UsuarioProxy;
import br.gov.pa.cosanpa.gopera.util.PerfilBeanEnum;

@Stateless
public class UnidadeConsumidoraEJB implements IUnidadeConsumidora{

	@PersistenceContext
	private EntityManager entity;
	
	@EJB
	private IProxy fachadaProxy;
	
	@Override
	public void salvar(UnidadeConsumidora obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(UnidadeConsumidora obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(UnidadeConsumidora obj) {
		UnidadeConsumidora objRemover = entity.find(UnidadeConsumidora.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(UnidadeConsumidora.class, id);
	}

	@Override
	public List<UnidadeConsumidora> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<UnidadeConsumidora> query= entity.createQuery("select c1 from UnidadeConsumidora c1 order by c1.descricao",UnidadeConsumidora.class);
		return query.getResultList();
	}
	
	@Override
	public List<UnidadeConsumidora> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters) throws Exception {  
        CriteriaBuilder cb = entity.getCriteriaBuilder();  
        CriteriaQuery<UnidadeConsumidora> q = cb.createQuery(UnidadeConsumidora.class);  
        Root<UnidadeConsumidora> c = q.from(UnidadeConsumidora.class);  
        q.select(c);  
        if (filters != null && !filters.isEmpty()) {  
            Predicate[] predicates = new Predicate[filters.size()];  
            int i = 0;  
            for (Map.Entry<String, String> entry : filters.entrySet()) {  
                String key = entry.getKey();  
                String val = entry.getValue();
                Expression<String> path;
                try {  
	                if (key.equals("uc")) {
	                	path = c.get(key); 
	                	predicates[i] = cb.and(cb.equal(path, val));
	                }
	                else{                	
		                path = c.get(key);
	                    predicates[i] = cb.and(cb.like(cb.lower(path), "%" + val.toLowerCase() + "%"));
	                }
                } catch (SecurityException ex) {  
                	ex.printStackTrace();
                }  
                i++;  
            }  
            q.where(predicates);  
        }  
        q.orderBy(cb.asc(c.get("descricao")));
		TypedQuery<UnidadeConsumidora> query = entity.createQuery(q);
        query.setMaxResults(maxPerPage);  
        query.setFirstResult(startingAt);
		List<UnidadeConsumidora> lista = query.getResultList();
        return lista;  
    }  
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override	
	public int obterQtdRegistros(Map<String, String> filters) throws Exception {
        CriteriaBuilder cb = entity.getCriteriaBuilder();  
        CriteriaQuery q = cb.createQuery(UnidadeConsumidora.class);
        Root<UnidadeConsumidora> c = q.from(UnidadeConsumidora.class);  
        q.select(cb.count(c));  
        if (filters != null && !filters.isEmpty()) {  
            Predicate[] predicates = new Predicate[filters.size()];  
            int i = 0;  
            for (Map.Entry<String, String> entry : filters.entrySet()) {  
                String key = entry.getKey();  
                String val = entry.getValue();
                Expression<String> path;
                try {  
	                if (key.equals("uc")) {
	                	path = c.get(key); 
	                	predicates[i] = cb.and(cb.equal(path, val));
	                }
	                else{
		                path = c.get(key);
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
	
	@Override
	public UnidadeConsumidora obterUnidadeConsumidora(Integer codigo) throws Exception {
		try{
			TypedQuery<UnidadeConsumidora> query= entity.createQuery("select c1 from UnidadeConsumidora c1 where c1.codigo = " + codigo,UnidadeConsumidora.class);
			UnidadeConsumidora uc = query.getSingleResult();
			
			UnidadeConsumidora UnidadeConsumidora = uc;
			for (int j = 0; j < UnidadeConsumidora.getOperacional().size(); j++) {
				uc.getOperacional().get(j);
				uc.getOperacional().get(j).setDescricao(fachadaProxy.getUnidadeOperacional(uc.getOperacional().get(j).getTipoUnidadeOperacional(), uc.getOperacional().get(j).getCodigoUnidadeOperacional()));
			}
			return uc;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public UnidadeConsumidora obterUnidadeConsumidoraUC(Integer codigoUC) throws Exception {
		try{
			TypedQuery<UnidadeConsumidora> query= entity.createQuery("select c1 from UnidadeConsumidora c1 where c1.uc = " + codigoUC,UnidadeConsumidora.class);
			UnidadeConsumidora uc = query.getSingleResult();
			return uc;
		} catch (Exception e) {
			return (null);
		}
	}
	@SuppressWarnings("rawtypes")
	@Override
	public List<RegionalProxy> getListaRegional(UsuarioProxy usuario) throws Exception {

		String localidade = "";
		String query = "SELECT DISTINCT A.greg_id, A.greg_nmregional FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
				" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade" +
				" FROM cadastro.gerencia_regional A " +
				"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
				"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
				"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
				"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A " +
				"INNER JOIN operacao.unidade_consumidora B ON A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id " +
				"INNER JOIN operacao.unidade_consumidora_operacional C ON B.ucon_id = C.ucon_id ";
		
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
		query += " ORDER BY A.greg_nmregional"; 
		
		List<List> valores = fachadaProxy.selectRegistros(query);
		List<RegionalProxy> lista = new ArrayList<RegionalProxy>();
		
		if (valores == null) {
			return lista;
		}

		for (List colunas : valores) {
			lista.add(new RegionalProxy(Integer.parseInt(colunas.get(0).toString()),colunas.get(1).toString()));
		}
		return lista;
	}	

	@SuppressWarnings("rawtypes")
	@Override
	public List<UnidadeNegocioProxy> getListaUnidadeNegocio(UsuarioProxy usuario, Integer codigoRegional) throws Exception {

		String localidade = "";
		String query = "SELECT DISTINCT A.uneg_id, A.uneg_nmunidadenegocio  FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
				" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade" +
				" FROM cadastro.gerencia_regional A " +
				"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
				"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
				"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
				"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A " +
				"INNER JOIN operacao.unidade_consumidora B ON A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id " +
				"INNER JOIN operacao.unidade_consumidora_operacional C ON B.ucon_id = C.ucon_id ";
		
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
		else
		{
			query = query + " WHERE A.greg_id = " + codigoRegional;
		}
		query += " ORDER BY A.uneg_nmunidadenegocio";
		
		List<List> valores = fachadaProxy.selectRegistros(query);
		List<UnidadeNegocioProxy> lista = new ArrayList<UnidadeNegocioProxy>();
		
		if (valores == null) {
			return lista;
		}

		for (List colunas : valores) {
			lista.add(new UnidadeNegocioProxy(Integer.parseInt(colunas.get(0).toString()),colunas.get(1).toString()));
		}
		return lista;
	}	

	@SuppressWarnings("rawtypes")
	@Override
	public List<MunicipioProxy> getListaMunicipio(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio) throws Exception {

		String localidade = "";
		String query = "SELECT DISTINCT A.muni_id, A.muni_nmmunicipio FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
				" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade" +
				" FROM cadastro.gerencia_regional A " +
				"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
				"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
				"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
				"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A " +
				"INNER JOIN operacao.unidade_consumidora B ON A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id " +
				"INNER JOIN operacao.unidade_consumidora_operacional C ON B.ucon_id = C.ucon_id ";
		
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
		else
		{
			query = query + " WHERE A.greg_id = " + codigoRegional
						  + "   AND A.uneg_id = " + codigoUnidadeNegocio;
		}
		query += " ORDER BY A.muni_nmmunicipio";
		
		List<List> valores = fachadaProxy.selectRegistros(query);
		List<MunicipioProxy> lista = new ArrayList<MunicipioProxy>();
		
		if (valores == null) {
			return lista;
		}

		for (List colunas : valores) {
			lista.add(new MunicipioProxy(Integer.parseInt(colunas.get(0).toString()),colunas.get(1).toString()));
		}
		return lista;
	}	

	@SuppressWarnings("rawtypes")
	@Override
	public List<LocalidadeProxy> getListaLocalidade(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio) throws Exception {

		String localidade = "";
		String query = "SELECT DISTINCT A.loca_id, A.loca_nmlocalidade FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
				" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade" +
				" FROM cadastro.gerencia_regional A " +
				"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
				"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
				"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
				"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A " +
				"INNER JOIN operacao.unidade_consumidora B ON A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id " +
				"INNER JOIN operacao.unidade_consumidora_operacional C ON B.ucon_id = C.ucon_id ";
		
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
		else
		{
			query = query + " WHERE A.greg_id = " + codigoRegional
						  + "   AND A.uneg_id = " + codigoUnidadeNegocio
						  + "   AND A.muni_id = " + codigoMunicipio;
		}
		query += " ORDER BY A.loca_nmlocalidade";
		
		List<List> valores = fachadaProxy.selectRegistros(query);
		List<LocalidadeProxy> lista = new ArrayList<LocalidadeProxy>();
		
		if (valores == null) {
			return lista;
		}

		for (List colunas : valores) {
			lista.add(new LocalidadeProxy(Integer.parseInt(colunas.get(0).toString()),colunas.get(1).toString()));
		}
		return lista;
	}	
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<EEAB> getListaEAB(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception {

		String localidade = "";
		String query = "SELECT DISTINCT D.eeab_id, D.eeab_nome FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
				" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade" +
				" FROM cadastro.gerencia_regional A " +
				"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
				"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
				"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
				"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A " +
				"INNER JOIN operacao.unidade_consumidora B ON A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id " +
				"INNER JOIN operacao.unidade_consumidora_operacional C ON B.ucon_id = C.ucon_id " +
				"INNER JOIN operacao.eeab D ON C.ucop_idoperacional = D.eeab_id " +
				"WHERE C.ucop_tipooperacional = 1";
		
		if (usuario == null){
			query = query + (codigoRegional !=0 ? " AND A.greg_id = " + codigoRegional : "");
			query = query + (codigoUnidadeNegocio !=0 ? " AND A.uneg_id = " + codigoUnidadeNegocio : "");
			query = query + (codigoMunicipio !=0 ? " AND A.muni_id = " + codigoMunicipio : "");
			query = query + (codigoLocalidade !=0 ? " AND A.loca_id = " + codigoLocalidade : "");
		}
		else{
			//Se Perfil de Gerente, acesso a gerência Regional
			if (usuario.getPerfil() == PerfilBeanEnum.GERENTE){
				query = query + "   AND A.greg_id = " + usuario.getRegionalProxy().getCodigo()
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
				query = query + "   AND A.loca_id IN (" + localidade + ")"
				  			  + "   AND A.greg_id = " + codigoRegional
							  + "   AND A.uneg_id = " + codigoUnidadeNegocio
							  + "   AND A.muni_id = " + codigoMunicipio
							  + "   AND A.loca_id = " + codigoLocalidade;
			}
			else
			{
				query = query + "   AND A.greg_id = " + codigoRegional
							  + "   AND A.uneg_id = " + codigoUnidadeNegocio
							  + "   AND A.muni_id = " + codigoMunicipio
							  + "   AND A.loca_id = " + codigoLocalidade;
			}
		}
		query += " ORDER BY D.eeab_nome";
		
		List<List> valores = fachadaProxy.selectRegistros(query);
		List<EEAB> lista = new ArrayList<EEAB>();
		
		if (valores == null) {
			return lista;
		}

		for (List colunas : valores) {
			lista.add(new EEAB(Integer.parseInt(colunas.get(0).toString()),colunas.get(1).toString()));
		}
		return lista;
	}	

	@SuppressWarnings("rawtypes")
	@Override
	public List<ETA> getListaETA(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception {

		String localidade = "";
		String query = "SELECT DISTINCT D.eta_id, D.eta_nome FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
				" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade" +
				" FROM cadastro.gerencia_regional A " +
				"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
				"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
				"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
				"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A " +
				"INNER JOIN operacao.unidade_consumidora B ON A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id " +
				"INNER JOIN operacao.unidade_consumidora_operacional C ON B.ucon_id = C.ucon_id " +
				"INNER JOIN operacao.eta D ON C.ucop_idoperacional = D.eta_id " +
				"WHERE C.ucop_tipooperacional = 2";

		if (usuario == null){
			query = query + (codigoRegional !=0 ? " AND A.greg_id = " + codigoRegional : "");
			query = query + (codigoUnidadeNegocio !=0 ? " AND A.uneg_id = " + codigoUnidadeNegocio : "");
			query = query + (codigoMunicipio !=0 ? " AND A.muni_id = " + codigoMunicipio : "");
			query = query + (codigoLocalidade !=0 ? " AND A.loca_id = " + codigoLocalidade : "");
		}
		else{
			//Se Perfil de Gerente, acesso a gerência Regional
			if (usuario.getPerfil() == PerfilBeanEnum.GERENTE){
				query = query + "   AND A.greg_id = " + usuario.getRegionalProxy().getCodigo()
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
				query = query + "   AND A.loca_id IN (" + localidade + ")"
				  			  + "   AND A.greg_id = " + codigoRegional
							  + "   AND A.uneg_id = " + codigoUnidadeNegocio
							  + "   AND A.muni_id = " + codigoMunicipio
							  + "   AND A.loca_id = " + codigoLocalidade;
			}
			else
			{
				query = query + "   AND A.greg_id = " + codigoRegional
							  + "   AND A.uneg_id = " + codigoUnidadeNegocio
							  + "   AND A.muni_id = " + codigoMunicipio
							  + "   AND A.loca_id = " + codigoLocalidade;
			}
		}
		query += " ORDER BY D.eta_nome";
		
		List<List> valores = fachadaProxy.selectRegistros(query);
		List<ETA> lista = new ArrayList<ETA>();
		
		if (valores == null) {
			return lista;
		}

		for (List colunas : valores) {
			lista.add(new ETA(Integer.parseInt(colunas.get(0).toString()),colunas.get(1).toString()));
		}
		return lista;
	}	

	@SuppressWarnings("rawtypes")
	@Override
	public List<EEAT> getListaEAT(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception {

		String localidade = "";
		String query = "SELECT DISTINCT D.eeat_id, D.eeat_nome FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
				" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade" +
				" FROM cadastro.gerencia_regional A " +
				"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
				"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
				"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
				"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A " +
				"INNER JOIN operacao.unidade_consumidora B ON A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id " +
				"INNER JOIN operacao.unidade_consumidora_operacional C ON B.ucon_id = C.ucon_id " +
				"INNER JOIN operacao.eeat D ON C.ucop_idoperacional = D.eeat_id " +
				"WHERE C.ucop_tipooperacional = 3";
		
		if (usuario == null){
			query = query + (codigoRegional !=0 ? " AND A.greg_id = " + codigoRegional : "");
			query = query + (codigoUnidadeNegocio !=0 ? " AND A.uneg_id = " + codigoUnidadeNegocio : "");
			query = query + (codigoMunicipio !=0 ? " AND A.muni_id = " + codigoMunicipio : "");
			query = query + (codigoLocalidade !=0 ? " AND A.loca_id = " + codigoLocalidade : "");
		}
		else{
			//Se Perfil de Gerente, acesso a gerência Regional
			if (usuario.getPerfil() == PerfilBeanEnum.GERENTE){
				query = query + "   AND A.greg_id = " + usuario.getRegionalProxy().getCodigo()
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
				query = query + "   AND A.loca_id IN (" + localidade + ")"
				  			  + "   AND A.greg_id = " + codigoRegional
							  + "   AND A.uneg_id = " + codigoUnidadeNegocio
							  + "   AND A.muni_id = " + codigoMunicipio
							  + "   AND A.loca_id = " + codigoLocalidade;
			}
			else
			{
				query = query + "   AND A.greg_id = " + codigoRegional
							  + "   AND A.uneg_id = " + codigoUnidadeNegocio
							  + "   AND A.muni_id = " + codigoMunicipio
							  + "   AND A.loca_id = " + codigoLocalidade;
			}
		}	
		query += " ORDER BY D.eeat_nome";
		
		List<List> valores = fachadaProxy.selectRegistros(query);
		List<EEAT> lista = new ArrayList<EEAT>();
		
		if (valores == null) {
			return lista;
		}

		for (List colunas : valores) {
			lista.add(new EEAT(Integer.parseInt(colunas.get(0).toString()),colunas.get(1).toString()));
		}
		return lista;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<RSO> getListaRSO(UsuarioProxy usuario, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception {

		String localidade = "";
		String query = "SELECT DISTINCT D.rso_id, D.rso_nome FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," + 
				" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade" +
				" FROM cadastro.gerencia_regional A " +
				"INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id " +
				"INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID " +
				"INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id " +
				"INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A " +
				"INNER JOIN operacao.unidade_consumidora B ON A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id " +
				"INNER JOIN operacao.unidade_consumidora_operacional C ON B.ucon_id = C.ucon_id " +
				"INNER JOIN operacao.rso D ON C.ucop_idoperacional = D.rso_id " +
				"WHERE C.ucop_tipooperacional = 4";
		
		if (usuario == null){
			query = query + (codigoRegional !=0 ? " AND A.greg_id = " + codigoRegional : "");
			query = query + (codigoUnidadeNegocio !=0 ? " AND A.uneg_id = " + codigoUnidadeNegocio : "");
			query = query + (codigoMunicipio !=0 ? " AND A.muni_id = " + codigoMunicipio : "");
			query = query + (codigoLocalidade !=0 ? " AND A.loca_id = " + codigoLocalidade : "");
		}
		else{
			//Se Perfil de Gerente, acesso a gerência Regional
			if (usuario.getPerfil() == PerfilBeanEnum.GERENTE){
				query = query + "   AND A.greg_id = " + usuario.getRegionalProxy().getCodigo()
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
				query = query + "   AND A.loca_id IN (" + localidade + ")"
				  			  + "   AND A.greg_id = " + codigoRegional
							  + "   AND A.uneg_id = " + codigoUnidadeNegocio
							  + "   AND A.muni_id = " + codigoMunicipio
							  + "   AND A.loca_id = " + codigoLocalidade;
			}
			else
			{
				query = query + "   AND A.greg_id = " + codigoRegional
							  + "   AND A.uneg_id = " + codigoUnidadeNegocio
							  + "   AND A.muni_id = " + codigoMunicipio
							  + "   AND A.loca_id = " + codigoLocalidade;
			}
		}	
		query += " ORDER BY D.rso_nome";
			
		List<List> valores = fachadaProxy.selectRegistros(query);
		List<RSO> lista = new ArrayList<RSO>();
		
		if (valores == null) {
			return lista;
		}

		for (List colunas : valores) {
			lista.add(new RSO(Integer.parseInt(colunas.get(0).toString()),colunas.get(1).toString()));
		}
		return lista;
	}	
	
	@Override
	public List<UnidadeConsumidoraOperacional> getListaUnidadeOperacional(Integer tipoUnidade, Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception {

		List<UnidadeConsumidoraOperacional> lista = new ArrayList<UnidadeConsumidoraOperacional>();
		UnidadeConsumidoraOperacional undOpe;

		List<EEAB> listaEAB = new ArrayList<EEAB>();
		List<ETA> listaETA = new ArrayList<ETA>();
		List<EEAT> listaEAT = new ArrayList<EEAT>();
		List<RSO> listaRSO = new ArrayList<RSO>();		
		if (tipoUnidade == 1){//EAB
			listaEAB = getListaEAB(null, codigoRegional, codigoUnidadeNegocio, codigoMunicipio, codigoLocalidade) ;
			for (EEAB eeab : listaEAB) {
				undOpe = new UnidadeConsumidoraOperacional();
				undOpe.setCodigo(eeab.getCodigo());
				undOpe.setCodigoUnidadeOperacional(eeab.getCodigo());
				undOpe.setDescricao(eeab.getDescricao() );
				lista.add(undOpe);
			}
		}else if (tipoUnidade == 2){//ETA
			listaETA = getListaETA(null, codigoRegional, codigoUnidadeNegocio, codigoMunicipio, codigoLocalidade) ;
			for (ETA eta : listaETA) {
				undOpe = new UnidadeConsumidoraOperacional();
				undOpe.setCodigo(eta.getCodigo());
				undOpe.setCodigoUnidadeOperacional(eta.getCodigo());
				undOpe.setDescricao(eta.getDescricao() );
				lista.add(undOpe);
			}
		}else if (tipoUnidade == 3){//EAT
			listaEAT = getListaEAT(null, codigoRegional, codigoUnidadeNegocio, codigoMunicipio, codigoLocalidade) ;
			for (EEAT eat : listaEAT) {
				undOpe = new UnidadeConsumidoraOperacional();
				undOpe.setCodigo(eat.getCodigo());
				undOpe.setCodigoUnidadeOperacional(eat.getCodigo());
				undOpe.setDescricao(eat.getDescricao() );
				lista.add(undOpe);
			}			
		}else if (tipoUnidade == 4){//RSO
			listaRSO = getListaRSO(null, codigoRegional, codigoUnidadeNegocio, codigoMunicipio, codigoLocalidade) ;
			for (RSO rso : listaRSO) {
				undOpe = new UnidadeConsumidoraOperacional();
				undOpe.setCodigo(rso.getCodigo());
				undOpe.setCodigoUnidadeOperacional(rso.getCodigo());
				undOpe.setDescricao(rso.getDescricao() );
				lista.add(undOpe);
			}			
		}
		return lista;
	}
	
	public List<UnidadeConsumidora> unidadesConsumidoras(Integer codigoRegional, Integer codigoUnidadeNegocio, Integer codigoMunicipio, Integer codigoLocalidade)
			throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select uc from UnidadeConsumidora uc")
		.append(" where uc.regionalProxy.codigo = :codigoRegional")
		.append(" and uc.unidadeNegocioProxy.codigo = :codigoUnidadeNegocio")
		.append(" and uc.municipioProxy.codigo = :codigoMunicipio")
		.append(" and uc.localidadeProxy.codigo = :codigoLocalidade");

		TypedQuery<UnidadeConsumidora> query = entity.createQuery(sql.toString(), UnidadeConsumidora.class)
				.setParameter("codigoRegional", codigoRegional)
				.setParameter("codigoUnidadeNegocio", codigoUnidadeNegocio)
				.setParameter("codigoMunicipio", codigoMunicipio)
				.setParameter("codigoLocalidade", codigoLocalidade);

		return query.getResultList();
	}
	
}
