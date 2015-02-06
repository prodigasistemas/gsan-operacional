package br.gov.pa.cosanpa.gopera.servicos;

import static br.gov.model.util.Utilitarios.formataData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.model.operacao.ConsumoProduto;
import br.gov.model.operacao.PrecoProduto;
import br.gov.model.util.FormatoData;
import br.gov.servicos.operacao.ConsumoProdutoRepositorio;
import br.gov.servicos.operacao.to.ConsultaConsumoProdutoTO;
import br.gov.servicos.operacao.to.IRelatorioConsumoTO;
import br.gov.servicos.operacao.to.RelatorioConsumoProdutoAnaliticoTO;
import br.gov.servicos.operacao.to.RelatorioConsumoProdutoMensalTO;
import br.gov.servicos.operacao.to.RelatorioConsumoProdutoSinteticoTO;

@Stateless
public class RelatorioConsumoProdutoBO{

    @PersistenceContext
    private EntityManager entity;
    
    @Inject ConsumoProdutoRepositorio repositorio;
    
	public List<RelatorioConsumoProdutoAnaliticoTO> getConsumoProdutoAnalitico(ConsultaConsumoProdutoTO to) {

        List<ConsumoProduto> valores = repositorio.listarConsumos(to);
        
        List<RelatorioConsumoProdutoAnaliticoTO> lista = new ArrayList<RelatorioConsumoProdutoAnaliticoTO>();

        for (ConsumoProduto consumo : valores) {
            PrecoProduto preco = precoComVigenciaDoConsumo(consumo);
            
            RelatorioConsumoProdutoAnaliticoTO item = new RelatorioConsumoProdutoAnaliticoTO();
            
            item.setNomeRegional(consumo.getConsumo().getUnidadeConsumidoraOperacional().getUC().getRegionalProxy().getNome());
            item.setNomeUnidadeNegocio(consumo.getConsumo().getUnidadeConsumidoraOperacional().getUC().getUnidadeNegocioProxy().getNome());
            item.setNomeMunicipio(consumo.getConsumo().getUnidadeConsumidoraOperacional().getUC().getMunicipioProxy().getNome());
            item.setNomeLocalidade(consumo.getConsumo().getUnidadeConsumidoraOperacional().getUC().getLocalidadeProxy().getNome());
            item.setNomeUnidadeOperacional(consumo.getConsumo().getEstacao().getNome());
            item.setNomeProduto(consumo.getProduto().getDescricao());
            item.setQuantidade(consumo.getQuantidade());
            item.setVigencia(preco.getVigencia());
            item.setValorUnitario(preco.getPreco());
            item.setDataConsumo(consumo.getConsumo().getData());
            
            lista.add(item);
        }
        
        return lista;
	}
	
	public List<IRelatorioConsumoTO> getConsumoProdutoMensal(ConsultaConsumoProdutoTO to) {
	    
	    List<ConsumoProduto> valores = repositorio.listarConsumos(to);
	    
	    List<IRelatorioConsumoTO> lista = new ArrayList<IRelatorioConsumoTO>();
	    
	    for (ConsumoProduto consumo : valores) {
	        
	        PrecoProduto preco = precoComVigenciaDoConsumo(consumo);
	        
	        IRelatorioConsumoTO item = recuperaProdutoComNomeEVigencia(lista, preco, consumo);
	        
	        if (item == null){
	            item = new RelatorioConsumoProdutoMensalTO();
	            
	            item.setNomeProduto(consumo.getProduto().getDescricao());
	            
	            item.setVigencia(preco.getVigencia());
	            
	            item.setValorUnitario(preco.getPreco());
	            
	            lista.add(item);
	        }
	        
	        Date dataConsumo = consumo.getConsumo().getData();
	        
	        String anoMes = formataData(dataConsumo, FormatoData.ANO_MES);

	        ((RelatorioConsumoProdutoMensalTO) item).addMensal(anoMes, consumo.getQuantidade());
	    }
	    
	    return lista;
	}

	public List<IRelatorioConsumoTO> getConsumoProdutoSintetico(ConsultaConsumoProdutoTO to) {
	    
	    List<ConsumoProduto> valores = repositorio.listarConsumos(to);
	    
	    List<IRelatorioConsumoTO> lista = new ArrayList<IRelatorioConsumoTO>();
	    
	    for (ConsumoProduto consumo : valores) {
	        PrecoProduto preco = precoComVigenciaDoConsumo(consumo);
	        
	        IRelatorioConsumoTO item = recuperaProdutoComNomeEVigencia(lista, preco, consumo);
	        
	        if (item == null){
	            item = new RelatorioConsumoProdutoSinteticoTO();
	            
	            item.setNomeProduto(consumo.getProduto().getDescricao());
	            
	            item.setVigencia(preco.getVigencia());
	            
	            item.setValorUnitario(preco.getPreco());
	            
	            lista.add(item);
	        }
	        
	        ((RelatorioConsumoProdutoSinteticoTO) item).incrementaQuantidade(consumo.getQuantidade());
	    }
	    
	    return lista;
	}
	
    private PrecoProduto precoComVigenciaDoConsumo(ConsumoProduto consumo) {
        consumo.getProduto().getPrecos()
            .sort(Comparator.comparing(PrecoProduto::getVigencia).reversed());
        
        PrecoProduto preco = consumo.getProduto().getPrecos()
                .stream().filter(e -> !e.getVigencia().after(consumo.getConsumo().getData()))
                .limit(1).findFirst().get();

        return preco;
    }

    private IRelatorioConsumoTO recuperaProdutoComNomeEVigencia(List<IRelatorioConsumoTO> lista, PrecoProduto preco, ConsumoProduto consumo) {
        Iterator<IRelatorioConsumoTO> iterator = lista.stream()
                .filter(e -> e.getNomeProduto() == consumo.getProduto().getDescricao())
                .filter(e -> !e.getVigencia().before(preco.getVigencia())).limit(1).iterator();

        IRelatorioConsumoTO stream = null;
        
        if (iterator.hasNext()){
            stream = iterator.next();
        }
        
        return stream;
    }
}