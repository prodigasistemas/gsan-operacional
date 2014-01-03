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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.pa.cosanpa.gopera.fachada.IEnergiaEletricaDados;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.model.ContratoEnergia;
import br.gov.pa.cosanpa.gopera.model.EnergiaEletricaDados;
import br.gov.pa.cosanpa.gopera.model.UnidadeConsumidora;

@Stateless
public class EnergiaEletricaDadosEJB implements IEnergiaEletricaDados{

	@PersistenceContext
	private EntityManager entity;

	@EJB
	private IProxy fachadaProxy;
	
	@Override
	public void salvar(EnergiaEletricaDados obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(EnergiaEletricaDados obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(EnergiaEletricaDados obj) {
		EnergiaEletricaDados objRemover = entity.find(EnergiaEletricaDados.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(EnergiaEletricaDados.class, id);
	}

	@Override
	public List<EnergiaEletricaDados> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<EnergiaEletricaDados> query= entity.createQuery("select c1 from EnergiaEletricaDados c1 order by enld_id",EnergiaEletricaDados.class);
		List<EnergiaEletricaDados> lista = query.getResultList();
		return lista;
	}
	
	@Override
	public EnergiaEletricaDados obterDados(Integer codigo) throws Exception {
		TypedQuery<EnergiaEletricaDados> query= entity.createQuery("select c1 from EnergiaEletricaDados c1 where enld_id = " + codigo,EnergiaEletricaDados.class);
		EnergiaEletricaDados dados = query.getSingleResult();
		return dados;
	}
/*	
	@Override
	public EnergiaEletricaDados CalculaDados(EnergiaEletricaDados dados) throws Exception {
		try{
			dados.setCvlr_Consumo(dados.getC_Kwh_Cv() + dados.getC_Kwh_FS() + dados.getC_Kwh_FU() + dados.getC_Kwh_PS() + dados.getC_Kwh_PU());
    	} catch (Exception e) {
    		dados.setCvlr_Consumo(0.00);
    	}		
		return dados;
	}
*/	
	
	@Override
	public List<EnergiaEletricaDados> obterListaLazy(int startingAt, int maxPerPage, Map<String, String> filters, int codigoEnergia) throws Exception {  
        CriteriaBuilder cb = entity.getCriteriaBuilder();  
        CriteriaQuery<EnergiaEletricaDados> q = cb.createQuery(EnergiaEletricaDados.class);  
        Root<EnergiaEletricaDados> c = q.from(EnergiaEletricaDados.class);
//        Join<EnergiaEletricaDados, UnidadeConsumidora> uc = c.join("uc");
        q.select(c);
        Expression<String> path;  
        Predicate[] predicates = new Predicate[1];
        int i = 0;        
        if (filters != null && !filters.isEmpty()) {  
            predicates = new Predicate[filters.size() + 1];  
 
            for (Map.Entry<String, String> entry : filters.entrySet()) {  
                String key = entry.getKey();  
                String val = entry.getValue();
                try {  
                	path = c.get(key);
	                if (key.equals("dataLeitura")){
	            		SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
	                	Date dataConsumo = formataData.parse(val); 
	                	predicates[i] = cb.and(cb.equal(path, dataConsumo));
	                }
	                else if (key.equals("uc")){
	                	predicates[i] = cb.and(cb.equal(path, val));
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
        }
        path = c.get("energiaEletrica");
        predicates[i] = cb.and(cb.equal(path, codigoEnergia));
        q.where(predicates);  
        q.orderBy(cb.desc(c.get("codigo")));
        /*
        if (sortField != null && !sortField.isEmpty()) {  
            if (sortOrder.equals(SortOrder.ASCENDING)) {  
                q.orderBy(cb.asc(c.get(sortField)));  
            } else if (sortOrder.equals(SortOrder.DESCENDING)) {  
                q.orderBy(cb.desc(c.get(sortField)));  
            }  
        } 
        */ 
		TypedQuery<EnergiaEletricaDados> query = entity.createQuery(q);
        query.setMaxResults(maxPerPage);  
        query.setFirstResult(startingAt);
		List<EnergiaEletricaDados> lista = query.getResultList();
        return lista;  
    }  
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int obterQtdRegistros(Map<String, String> filters, int codigoEnergia) throws Exception {
        CriteriaBuilder cb = entity.getCriteriaBuilder();  
        CriteriaQuery q = cb.createQuery(EnergiaEletricaDados.class);  
        Root<EnergiaEletricaDados> c = q.from(EnergiaEletricaDados.class);
        q.select(cb.count(c));
        Expression<String> path;  
        Predicate[] predicates = new Predicate[1];
        int i = 0;        
        if (filters != null && !filters.isEmpty()) {  
            predicates = new Predicate[filters.size() + 1];  
            for (Map.Entry<String, String> entry : filters.entrySet()) {  
                String key = entry.getKey();  
                String val = entry.getValue();
                try {  
	                path = c.get(key);
	                if (key.equals("dataLeitura")){
	            		SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
	                	Date dataConsumo = formataData.parse(val); 
	                	predicates[i] = cb.and(cb.equal(path, dataConsumo));
	                }
	                else if (key.equals("uc")){
	                	predicates[i] = cb.and(cb.equal(path, val));
	                }	                
	                else{
                        predicates[i] = cb.and(cb.like(cb.lower(path), "%" + val.toLowerCase() + "%"));
	                }
                } catch (SecurityException ex) {  
                	ex.printStackTrace();
                }  
                i++;  
            }   
        }
        path = c.get("energiaEletrica");
        predicates[i] = cb.and(cb.equal(path, codigoEnergia));
        q.where(predicates);  
        Query query = entity.createQuery(q);
        return ((Long) query.getSingleResult()).intValue();
	}

	@SuppressWarnings("rawtypes")
	private Date getDataLeituraAnterior(Integer UC, Date dataLeituraAtual){

		try{
			SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");
			String dataLeitura = formataData.format(dataLeituraAtual);
			
			String query = "select enld_dataleitura from operacao.energiaeletrica_dados" +
						   " where enld_uc = " + UC +
						   "   and enld_dataleitura < '" + dataLeitura + "'" +
						   " order by enld_dataleitura DESC LIMIT 1";
			List<List> valores = fachadaProxy.selectRegistros(query);
			
			if (valores.size() == 0) {
				return (dataLeituraAtual);
			}
			
			for (List colunas : valores) {
				formataData = new SimpleDateFormat("yyyy-MM-dd");
				Date datLeituraAnt = formataData.parse(colunas.get(0).toString());
				return(datLeituraAnt);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	@Override
	public List<EnergiaEletricaDados> CalculaDados(List<EnergiaEletricaDados> listaDados){
		List<EnergiaEletricaDados> listaAux = new ArrayList<EnergiaEletricaDados>();
		try{
			for (EnergiaEletricaDados dados: listaDados){
				dados.setCvlr_Consumo(dados.getC_Kwh_Cv() + dados.getC_Kwh_FS() + dados.getC_Kwh_FU() + dados.getC_Kwh_PS() + dados.getC_Kwh_PU());
				//Unidade Consumidora
				if (dados.getUnidadeConsumidora() == null) dados.setUnidadeConsumidora(new UnidadeConsumidora()); 				
				//dados.setUC(fachadaUC.obterUnidadeConsumidoraUC(dados.getUc()));
				//Contrato Vigente
				if (dados.getContrato() == null) dados.setContrato(new ContratoEnergia());
				//if(dados.getUC().getCodigo() != null)
				//	dados.setContrato(fachadaContrato.obterContratoVigente(dados.getUC().getCodigo()));
				//else 
				//	dados.setContrato(new ContratoEnergia());
				
				dados.setCaj_fat_Pt(dados.getVlr_DRe_Cv() + dados.getVlr_DRe_FP() + dados.getVlr_DRe_Pt() + dados.getVlr_ERe_Cv() + dados.getVlr_ERe_FP() + dados.getVlr_ERe_Pt());
			    dados.setCult_Demanda(dados.getVlr_Ult_Pt() + dados.getVlr_Ult_FP() + dados.getVlr_Ult_Cv()); //Ultra Demanda
			    
			    //Fator de Potência Mínimo
			    Double fatorPotenciaMinimo = Double.parseDouble(fachadaProxy.getParametroSistema(2));
			    //Fator de Carga na Ponta
			    Double fatorCargaPt = Double.parseDouble(fachadaProxy.getParametroSistema(3));
			    //Fator de Carga Fora da Ponta
			    Double fatorCargaFP = Double.parseDouble(fachadaProxy.getParametroSistema(4));
			    //Fator de Carga Fora da Ponta
			    Double fatorCapacitivo = Double.parseDouble(fachadaProxy.getParametroSistema(5));			    
			    //Fator de Potência Fora de Ponta Úmida
			    dados.setCfat_pot_FP_Umida(dados.getVlr_Kwh_FU() != 0 ? dados.getC_Kwh_FU() * fatorPotenciaMinimo / (dados.getVlr_ERe_FP() * dados.getC_Kwh_FU() / dados.getVlr_Kwh_FU() + dados.getC_Kwh_FU()) : 0);
			    //Fator de Potência na Ponta Úmida
			    dados.setCfat_pot_Pt_Umida(dados.getVlr_Kwh_PU() != 0 ? dados.getC_Kwh_PU() * fatorPotenciaMinimo / (dados.getVlr_ERe_Pt() * dados.getC_Kwh_PU() / dados.getVlr_Kwh_PU() + dados.getC_Kwh_PU()) : 0);
			    //Fator de Potência Fora de Ponta Seca
			    dados.setCfat_pot_FP_Seca(dados.getVlr_Kwh_FS() != 0 ? dados.getC_Kwh_FS() * fatorPotenciaMinimo / (dados.getVlr_ERe_FP() * dados.getC_Kwh_FS() / dados.getVlr_Kwh_FS() + dados.getC_Kwh_FS()) : 0); 
			    //Fator de Potência na Ponta Seca
			    dados.setCfat_pot_Pt_Seca(dados.getVlr_Kwh_PS() != 0 ? dados.getC_Kwh_PS() * fatorPotenciaMinimo / (dados.getVlr_ERe_Pt() * dados.getC_Kwh_PS() / dados.getVlr_Kwh_PS() + dados.getC_Kwh_PS()) : 0);
			    //Fator de Potência Convencional
			    dados.setCfat_pot_Cv(dados.getVlr_Kwh_Cv() != 0 ? dados.getC_Kwh_Cv() * fatorPotenciaMinimo / (dados.getVlr_ERe_Cv() * dados.getC_Kwh_Cv() / dados.getVlr_Kwh_Cv() + dados.getC_Kwh_Cv()) : 0);
			    if (Double.isInfinite(dados.getCfat_pot_Cv()) || Double.isNaN(dados.getCfat_pot_Cv())) dados.setCfat_pot_Cv(0.00);
			    //Data Leitura Anterior
			    dados.setCdataLeituraAnterior(getDataLeituraAnterior(dados.getCodigoUC(), dados.getDataLeitura()));
			    //Nº Dias Faturado
			    dados.setCdias_fat((dados.getDataLeitura().getTime() - dados.getCdataLeituraAnterior().getTime()) /1000/60/60/24);
			    //Consumo KWh 30 dias
			    dados.setCcons_KW(dados.getCvlr_Consumo() / dados.getCdias_fat() * 30);
			    if (Double.isInfinite(dados.getCcons_KW()) || Double.isNaN(dados.getCcons_KW())) dados.setCcons_KW(0.00);
			    //Total R$ 30 dias
			    dados.setCcons_total(dados.getVlr_Total() / dados.getCdias_fat() * 30); 
			    if (Double.isInfinite(dados.getCcons_total()) || Double.isNaN(dados.getCcons_total())) dados.setCcons_total(0.00);
			    //Tarifa Média R$/KWh
			    dados.setCtarifa_media((dados.getVlr_Kwh_FS() + dados.getVlr_Kwh_FU() + dados.getVlr_Kwh_PS() + dados.getVlr_Kwh_PU() + dados.getVlr_Kwh_Cv()) / dados.getCvlr_Consumo());
			    if (Double.isInfinite(dados.getCtarifa_media()) || Double.isNaN(dados.getCtarifa_media())) dados.setCtarifa_media(0.00);
			    //Variação de Consumo
			    dados.setCvariacao_consumo((dados.getCcons_KW() * 100) / (dados.getCcons_KW() - 100));
			    if (Double.isInfinite(dados.getCvariacao_consumo()) || Double.isNaN(dados.getCvariacao_consumo())) dados.setCvariacao_consumo(0.00);
			    //Fator de Carga
			    dados.setCfat_carga(dados.getC_Kwh_Cv() / (dados.getDem_Fat_Cv() * (fatorCargaPt + fatorCargaFP)));
			    if (Double.isInfinite(dados.getCfat_carga()) || Double.isNaN(dados.getCfat_carga())) dados.setCfat_carga(0.00);
			    //Fator de Carga na Ponta
			    dados.setCfat_carga_Pt(dados.getC_Kwh_PU() / (dados.getDem_Med_Pt() * fatorCargaPt)); 
			    if (Double.isInfinite(dados.getCfat_carga_Pt()) || Double.isNaN(dados.getCfat_carga_Pt())) dados.setCfat_carga_Pt(0.00);
			    //Fator de Carga Fora da Ponta
			    dados.setCfat_carga_FP(dados.getC_Kwh_FU() / (dados.getDem_Med_FP() * fatorCargaFP)); 
			    if (Double.isInfinite(dados.getCfat_carga_FP()) || Double.isNaN(dados.getCfat_carga_FP())) dados.setCfat_carga_FP(0.00);
			    //Fator de Carga Fora da Ponta Umida Capacitivo
			    dados.setCcons_FP_Umida_Cap(dados.getC_Kwh_FU() * fatorCapacitivo);
			    if (Double.isInfinite(dados.getCcons_FP_Umida_Cap()) || Double.isNaN(dados.getCcons_FP_Umida_Cap())) dados.setCcons_FP_Umida_Cap(0.00);
			    //Fator de Carga Fora da Ponta Umida Indutivo
			    dados.setCcons_FP_Umida_Ind(dados.getC_Kwh_FU() - dados.getCcons_FP_Umida_Cap());
			    //Fator de Carga Fora da Ponta Seca Capacitivo
			    dados.setCcons_FP_Seca_Cap(dados.getC_Kwh_FU() == 0 ? dados.getC_Kwh_FS() * fatorCapacitivo : 0);
			    //Fator de Carga Fora da Ponta Seca Indutivo
			    dados.setCcons_FP_Seca_Ind(dados.getC_Kwh_FS() - dados.getCcons_FP_Seca_Cap()); 
			    //Consumo KWh Fora da Ponta Seca
			    dados.setCcons_KW_FP_Seca((dados.getC_Kwh_FS() / dados.getCdias_fat()) * 30);
			    if (Double.isInfinite(dados.getCcons_KW_FP_Seca()) || Double.isNaN(dados.getCcons_KW_FP_Seca())) dados.setCcons_KW_FP_Seca(0.00);
			    //Consumo KWh na Ponta Seca
			    dados.setCcons_KW_Pt_Seca((dados.getC_Kwh_PS() / dados.getCdias_fat()) * 30);
			    if (Double.isInfinite(dados.getCcons_KW_Pt_Seca()) || Double.isNaN(dados.getCcons_KW_Pt_Seca())) dados.setCcons_KW_Pt_Seca(0.00);
			    //Consumo KWh Fora da Ponta Umida
			    dados.setCcons_KW_FP_Umida((dados.getC_Kwh_FU() / dados.getCdias_fat()) * 30);
			    if (Double.isInfinite(dados.getCcons_KW_FP_Umida()) || Double.isNaN(dados.getCcons_KW_FP_Umida())) dados.setCcons_KW_FP_Umida(0.00);
			    //Consumo KWh na Ponta Umida
			    dados.setCcons_KW_Pt_Umida((dados.getC_Kwh_PU() / dados.getCdias_fat()) * 30);
			    if (Double.isInfinite(dados.getCcons_KW_Pt_Umida()) || Double.isNaN(dados.getCcons_KW_Pt_Umida())) dados.setCcons_KW_Pt_Umida(0.00);
			    //Valor Faturado Consumo Fora da Ponta Seca
			    dados.setCvlr_Consumo_FP_Seca((dados.getVlr_Kwh_FS() / dados.getCdias_fat()) * 30);
			    if (Double.isInfinite(dados.getCvlr_Consumo_FP_Seca()) || Double.isNaN(dados.getCvlr_Consumo_FP_Seca())) dados.setCvlr_Consumo_FP_Seca(0.00);
			    //Valor Faturado Consumo na Ponta Seca
			    dados.setCvlr_Consumo_Pt_Seca((dados.getVlr_Kwh_PS() / dados.getCdias_fat()) * 30);
			    if (Double.isInfinite(dados.getCvlr_Consumo_Pt_Seca()) || Double.isNaN(dados.getCvlr_Consumo_Pt_Seca())) dados.setCvlr_Consumo_Pt_Seca(0.00);
			    //Valor Faturado Consumo Fora da Ponta Umida
			    dados.setCvlr_Consumo_FP_Umida((dados.getVlr_Kwh_FU() / dados.getCdias_fat()) * 30);
			    if (Double.isInfinite(dados.getCvlr_Consumo_FP_Umida()) || Double.isNaN(dados.getCvlr_Consumo_FP_Umida())) dados.setCvlr_Consumo_FP_Umida(0.00);
			    //Valor Faturado Consumo na Ponta Umida
			    dados.setCvlr_Consumo_Pt_Umida((dados.getVlr_Kwh_PU() / dados.getCdias_fat()) * 30);
			    if (Double.isInfinite(dados.getCvlr_Consumo_Pt_Umida()) || Double.isNaN(dados.getCvlr_Consumo_Pt_Umida())) dados.setCvlr_Consumo_Pt_Umida(0.00);
			    //Consumo KWh Convencional			    
			    dados.setCcons_KW_Cv((dados.getC_Kwh_Cv() / dados.getCdias_fat()) * 30);
			    if (Double.isInfinite(dados.getCcons_KW_Cv()) || Double.isNaN(dados.getCcons_KW_Cv())) dados.setCcons_KW_Cv(0.00);
			    //Valor Faturado Consumo Convencional			    
			    dados.setCvlr_Consumo_Cv((dados.getVlr_Kwh_Cv() / dados.getCdias_fat()) * 30);
			    if (Double.isInfinite(dados.getCvlr_Consumo_Cv()) || Double.isNaN(dados.getCvlr_Consumo_Cv())) dados.setCvlr_Consumo_Cv(0.00);
				listaAux.add(dados);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}			
		return listaAux;
	}

}
