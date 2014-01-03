package br.gov.pa.cosanpa.gopera.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.pa.cosanpa.gopera.fachada.IConsultaLigacaoUnidadesOperacionais;
import br.gov.pa.cosanpa.gopera.model.LigacaoUnidadeOperacional;
import br.gov.pa.cosanpa.gopera.model.NodeSigma;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@Stateless
public class ConsultaLigacaoUnidadesOperacionaisEJB implements IConsultaLigacaoUnidadesOperacionais{

	@PersistenceContext
	private EntityManager entity;
	
	private String trechoQueryUnidadeConsumidora(){
		StringBuilder builder = new StringBuilder();
		builder
		.append(" select uco.ucop_idoperacional")
		.append(" from operacao.unidade_consumidora_operacional uco")
		.append("   inner join operacao.unidade_consumidora uc on")
		.append("     uco.ucon_id = uc.ucon_id")
		.append(" where uc.uneg_id = :unidade_negocio ");
		return builder.toString();
	}
	
	public List<NodeSigma> eabs(int unidadeNegocio) {
		StringBuilder builder = new StringBuilder();

		builder.append("select 'EAB' || cast(eeab_id as varchar) as id, eeab_nome  as label,")
		.append(" cast('EAB' as varchar) as type ")
		.append(" from operacao.eeab where eeab_id in ")
		.append("  (")
		.append(   trechoQueryUnidadeConsumidora())
		.append("  and uco.ucop_tipooperacional = 1")
		.append(" )");		
		Query query = entity.createNativeQuery(builder.toString(), NodeSigma.class);
		query.setParameter("unidade_negocio", unidadeNegocio);
		return query.getResultList();
	}

	public List<NodeSigma> etas(int unidadeNegocio) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("select 'ETA' || cast(eta_id as varchar) as id, eta_nome  as label,")
		.append(" cast('ETA' as varchar) as type ")
		.append(" from operacao.eta where eta_id in ")
		.append("  (")
		.append(   trechoQueryUnidadeConsumidora())
		.append("  and uco.ucop_tipooperacional = 2")
		.append(" )");		
		Query query = entity.createNativeQuery(builder.toString(), NodeSigma.class);
		query.setParameter("unidade_negocio", unidadeNegocio);
		return query.getResultList();
	}
	
	public List<NodeSigma> eats(int unidadeNegocio) {
		StringBuilder builder = new StringBuilder();

		builder.append("select 'EAT' || cast(eeat_id as varchar) as id, eeat_nome  as label,")
		.append(" cast('EAT' as varchar) as type ")
		.append(" from operacao.eeat where eeat_id in ")
		.append("  (")
		.append(   trechoQueryUnidadeConsumidora())
		.append("  and uco.ucop_tipooperacional = 3")
		.append(" )");		
		Query query = entity.createNativeQuery(builder.toString(), NodeSigma.class);
		query.setParameter("unidade_negocio", unidadeNegocio);
		return query.getResultList();
	}

	public List<LigacaoUnidadeOperacional> montaLigacaoEabEab(int unidadeNegocio) {
		StringBuilder builder = new StringBuilder();

		builder.append("select ")
		.append(" 'EAB' || cast(d.eeab_id as varchar) as source_id,")
		.append(" d.eeab_nome as source_name,")
		.append(" 'EAB' || cast(o.eeab_id as varchar) as target_id,")
		.append(" o.eeab_nome as target_name")
		.append(" from operacao.eeab d")
		.append(" inner join operacao.eeab_fontecaptacao f on")
		.append("   f.eeab_id = d.eeab_id")
		.append(" inner join operacao.eeab o on")
		.append("   f.eabf_fonte = o.eeab_id")
		.append(" where f.eabf_tipofonte = 1")
		.append(" and d.eeab_id in (")
		.append(    trechoQueryUnidadeConsumidora())
		.append("   and uco.ucop_tipooperacional = 1")
		.append(" )")
		.append(" and o.eeab_id in (")
		.append(    trechoQueryUnidadeConsumidora())
		.append("   and uco.ucop_tipooperacional = 1")
		.append(" )")
		.append(" and d.eeab_id <> o.eeab_id");
		
		
		Query query = entity.createNativeQuery(builder.toString(), LigacaoUnidadeOperacional.class);
		query.setParameter("unidade_negocio", unidadeNegocio);
		List<LigacaoUnidadeOperacional> list = query.getResultList();
		return list;
	}

	public List<LigacaoUnidadeOperacional> montaLigacaoEabEta(int unidadeNegocio) {
		
		StringBuilder builder = new StringBuilder();
		builder.append("select ")
		.append(" 'ETA' || cast(eta.eta_id as varchar) as source_id,")
		.append(" eta.eta_nome as source_name,")
		.append(" 'EAB' || cast(eab.eeab_id as varchar) as target_id,")
		.append(" eab.eeab_nome as target_name")
		.append(" from operacao.eta")
		.append(" inner join operacao.eta_fontecaptacao fon on")
		.append("   fon.eta_id = eta.eta_id")
		.append(" inner join operacao.eeab eab on")
		.append("   eab.eeab_id = fon.eeab_id")
		.append(" where eta.eta_id in (")
		.append(    trechoQueryUnidadeConsumidora())
		.append("   and uco.ucop_tipooperacional = 2")
		.append(" )")
		.append(" and eab.eeab_id in (")
		.append(    trechoQueryUnidadeConsumidora())
		.append("   and uco.ucop_tipooperacional = 1")
		.append(" )");
		
		
		Query query = entity.createNativeQuery(builder.toString(), LigacaoUnidadeOperacional.class);
		query.setParameter("unidade_negocio", unidadeNegocio);
		List<LigacaoUnidadeOperacional> list = query.getResultList();
		return list;
	}

	public List<LigacaoUnidadeOperacional> montaLigacaoEatEta(int unidadeNegocio) {
		StringBuilder builder = new StringBuilder();
		builder.append("select ")
		.append(" 'EAT' || cast(d.eeat_id as varchar) as target_id,")
		.append(" d.eeat_nome as target_name,")
		.append(" 'ETA' || cast(o.eta_id as varchar) as source_id,")
		.append(" o.eta_nome as source_name")
		.append(" from operacao.eeat d")
		.append(" inner join operacao.eeat_fontecaptacao f on")
		.append("   f.eeat_id = d.eeat_id")
		.append(" inner join operacao.eta o on")
		.append("   o.eta_id = f.etft_fonte")
		.append(" where o.eta_id in (")
		.append(    trechoQueryUnidadeConsumidora())
		.append("   and uco.ucop_tipooperacional = 2")
		.append(" )")
		.append(" and d.eeat_id in (")
		.append(    trechoQueryUnidadeConsumidora())
		.append("   and uco.ucop_tipooperacional = 3")
		.append(" )")
		.append(" and f.etft_tipofonte = 2");
		
		
		Query query = entity.createNativeQuery(builder.toString(), LigacaoUnidadeOperacional.class);
		query.setParameter("unidade_negocio", unidadeNegocio);
		List<LigacaoUnidadeOperacional> list = query.getResultList();
		return list;
	}

	public List<LigacaoUnidadeOperacional> montaLigacaoEatEat(int unidadeNegocio) {
		StringBuilder builder = new StringBuilder();
		builder.append("select ")
		.append(" 'EAT' || cast(o.eeat_id as varchar) as source_id,")
		.append(" o.eeat_nome as source_name,")
		.append(" 'EAT' || cast(d.eeat_id as varchar) as target_id,")
		.append(" d.eeat_nome as target_name")
		.append(" from operacao.eeat d")
		.append(" inner join operacao.eeat_fontecaptacao f on")
		.append("   f.eeat_id = d.eeat_id")
		.append(" inner join operacao.eeat o on")
		.append("   o.eeat_id = f.etft_fonte")
		.append(" where o.eeat_id in (")
		.append(    trechoQueryUnidadeConsumidora())
		.append("   and uco.ucop_tipooperacional = 3")
		.append(" )")
		.append(" and d.eeat_id in (")
		.append(    trechoQueryUnidadeConsumidora())
		.append("   and uco.ucop_tipooperacional = 3")
		.append(" )")
		.append(" and f.etft_tipofonte = 1");
		
		
		Query query = entity.createNativeQuery(builder.toString(), LigacaoUnidadeOperacional.class);
		query.setParameter("unidade_negocio", unidadeNegocio);
		List<LigacaoUnidadeOperacional> list = query.getResultList();

		return list;
	}

	public List<UnidadeNegocioProxy> todasUnidadesNegocio() {
		Query query = entity.createQuery(" from UnidadeNegocioProxy order by nome",UnidadeNegocioProxy.class);
		return query.getResultList();
	}
}
