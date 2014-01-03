package br.gov.pa.cosanpa.gopera.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.pa.cosanpa.gopera.fachada.IEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.EnergiaEletrica;

@Stateless
public class EnergiaEletricaEJB implements IEnergiaEletrica{

//	@EJB
//	private IEnergiaEletricaDados fachadaEDados;

//	@EJB
//	private IUnidadeConsumidora fachadaUC;
	
	@PersistenceContext
	private EntityManager entity;
	
	@Override
	public void salvar(EnergiaEletrica obj) {
		entity.persist(obj);
	}

	@Override
	public void atualizar(EnergiaEletrica obj) throws Exception {
		entity.merge(obj);
	}
	
	@Override
	public void excluir(EnergiaEletrica obj) {
		EnergiaEletrica objRemover = entity.find(EnergiaEletrica.class, obj.getCodigo());
		entity.remove(objRemover);		
	}
	
	@Override
	public void obterPorID(Integer id) throws Exception {
		entity.find(EnergiaEletrica.class, id);
	}

	@Override
	public List<EnergiaEletrica> obterLista(Integer min, Integer max)
			throws Exception {
		TypedQuery<EnergiaEletrica> query= entity.createQuery("select c1 from EnergiaEletrica c1 order by enel_referencia desc",EnergiaEletrica.class);
		List<EnergiaEletrica> lista = query.getResultList();
		return lista;
	}
	
	@Override
	public EnergiaEletrica obterEnergia(Integer codigo) throws Exception {
		TypedQuery<EnergiaEletrica> query= entity.createQuery("select c1 from EnergiaEletrica c1 where enel_id = " + codigo,EnergiaEletrica.class);
		EnergiaEletrica energiaEletrica = query.getSingleResult();
		
		for (int j = 0; j < energiaEletrica.getDados().size(); j++) {
			energiaEletrica.getDados().get(j);
		}
		return energiaEletrica;
	}
	
	@Override
	public EnergiaEletrica obterEnergiaPorData(Date dataReferencia) throws Exception {
		try {
		SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");
		String dataAux = formataData.format(dataReferencia);
		
		TypedQuery<EnergiaEletrica> query = entity.createQuery("select c1 from EnergiaEletrica c1 where enel_referencia = '" + dataAux + "'", EnergiaEletrica.class);
		EnergiaEletrica energiaEletrica = query.getSingleResult();
		
		for (int j = 0; j < energiaEletrica.getDados().size(); j++) {
			energiaEletrica.getDados().get(j);
		}
		return energiaEletrica;
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}

/*	
	@Override
	public List<EnergiaEletrica> obterEnergiaRelatorio(Date dataReferenciaInicial, Date dataReferenciaFinal, Integer codigoMunicipio, Integer codigoLocalidade) throws Exception {
		SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");
		String dataAux1 = formataData.format(dataReferenciaInicial);
		String dataAux2 = formataData.format(dataReferenciaFinal);
		String qLocalidade = "";
		if(codigoLocalidade != -1) {
			qLocalidade = " AND E.loca_id = " + codigoLocalidade;
		}
		
		TypedQuery<EnergiaEletrica> query = entity.createQuery("select A from EnergiaEletrica A JOIN A.dados B JOIN B.UC C JOIN C.municipioProxy D JOIN C.localidadeProxy E" + 
		
		" where enel_referencia BETWEEN '" + dataAux1 + "' AND '" + dataAux2 + "' " , EnergiaEletrica.class);
		List<EnergiaEletrica> energiaEletrica = query.getResultList();
		
		for (int i=0; i < energiaEletrica.size(); i++ ) {
			for (int j = 0; j < energiaEletrica.get(i).getDados().size(); j++) {
				energiaEletrica.get(i).getDados().get(j);
			}
		}
		return energiaEletrica;
	}
*/	
}
