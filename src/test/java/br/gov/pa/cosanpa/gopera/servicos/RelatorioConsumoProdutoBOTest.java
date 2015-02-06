package br.gov.pa.cosanpa.gopera.servicos;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.gov.model.operacao.Consumo;
import br.gov.model.operacao.ConsumoProduto;
import br.gov.model.operacao.PrecoProduto;
import br.gov.model.operacao.Produto;
import br.gov.model.operacao.UnidadeMedida;
import br.gov.servicos.operacao.ConsumoProdutoRepositorio;
import br.gov.servicos.operacao.to.ConsultaConsumoProdutoTO;
import br.gov.servicos.operacao.to.IRelatorioConsumoTO;
import br.gov.servicos.operacao.to.RelatorioConsumoProdutoMensalTO;


@RunWith(EasyMockRunner.class)
public class RelatorioConsumoProdutoBOTest {
	
	@TestSubject
	private RelatorioConsumoProdutoBO bo;
	
	@Mock
	private ConsumoProdutoRepositorio repositorio;

	ConsultaConsumoProdutoTO to = new ConsultaConsumoProdutoTO();
	
	Produto cloro = new Produto();
	Produto fluor = new Produto();
	
	PrecoProduto preco01FEV2015 = new PrecoProduto();
	PrecoProduto preco15SET2015 = new PrecoProduto();
	PrecoProduto preco01OUT2015 = new PrecoProduto();
	
	UnidadeMedida kg = new UnidadeMedida();
	
    Date _01Fev2015 = null;
    Date _10Fev2015 = null;
    Date _15Set2015 = null;
    Date _01Out2015 = null;
    Date _18Out2015 = null;
    Date _01Mai2016 = null;
    
    
    DateFormat format;
	
	@Before
	public void setup(){
        format = new SimpleDateFormat("yyyy-MM-dd");
        
        Calendar cal = Calendar.getInstance();
        cal.set(2015, 1, 1);
        _01Fev2015 = cal.getTime();
        cal.set(2015, 1, 10);
        _10Fev2015 = cal.getTime();
        cal.set(2015, 8, 15);
        _15Set2015 = cal.getTime();
        cal.set(2015, 9, 1);
        _01Out2015 = cal.getTime();
        cal.set(2015, 9, 18);
        _18Out2015 = cal.getTime();
        cal.set(2016, 4, 1);
        _01Mai2016 = cal.getTime();
	    
		bo = new RelatorioConsumoProdutoBO();
		
		preco01FEV2015 = new PrecoProduto();
		preco01FEV2015.setPreco(new BigDecimal(12.50));
		preco01FEV2015.setVigencia(_01Fev2015);
		
		preco15SET2015 = new PrecoProduto();
		preco15SET2015.setPreco(new BigDecimal(18.00));
		preco15SET2015.setVigencia(_15Set2015);
		
		preco01OUT2015 = new PrecoProduto();
		preco01OUT2015.setPreco(new BigDecimal(3.00));
		preco01OUT2015.setVigencia(_01Out2015);
		
		kg.setDescricao("Kg");
		
		cloro.setDescricao("Cloro");
		cloro.setUnidadeMedida(kg);
		cloro.addPreco(preco01FEV2015);
		cloro.addPreco(preco15SET2015);
		
		fluor.setDescricao("Fluor");
		fluor.setUnidadeMedida(kg);
		fluor.addPreco(preco01OUT2015);
	}
	
    @Test
    public void umConsumo(){
        mockConsumo(listaComUmConsumo());
        
        
        List<IRelatorioConsumoTO>  rel = bo.getConsumoProdutoMensal(to);
        
        IRelatorioConsumoTO to = rel.get(0);
        assertEquals("Cloro", to.getNomeProduto());
        assertEquals(12.50, to.getValorUnitario().doubleValue(), 1);
        assertEquals("2015-02-01", format.format(to.getVigencia()));
    }
    
    @Test
    public void variosConsumosDoMesmoProduto(){
        mockConsumo(consumosDoCloro());
        
        List<IRelatorioConsumoTO>  rel = bo.getConsumoProdutoMensal(to);
        
        rel.sort(Comparator.comparing(IRelatorioConsumoTO::getNomeProduto)
                .thenComparing(Comparator.comparing(IRelatorioConsumoTO::getVigencia).reversed())
                );
        
        RelatorioConsumoProdutoMensalTO to = (RelatorioConsumoProdutoMensalTO) rel.get(0);
        assertEquals("Cloro", to.getNomeProduto());
        assertEquals(18.00, to.getValorUnitario().doubleValue(), 1);
        assertEquals("2015-09-15", format.format(to.getVigencia()));
        assertEquals(12, to.getQuantidadesMes().get("201510").intValue());
        
        to = (RelatorioConsumoProdutoMensalTO) rel.get(1);
        assertEquals("Cloro", to.getNomeProduto());
        assertEquals(12.50, to.getValorUnitario().doubleValue(), 1);
        assertEquals("2015-02-01", format.format(to.getVigencia()));
        assertEquals(4, to.getQuantidadesMes().get("201502").intValue());
        
        to = (RelatorioConsumoProdutoMensalTO) rel.get(2);
        assertEquals("Fluor", to.getNomeProduto());
        assertEquals(3.00, to.getValorUnitario().doubleValue(), 1);
        assertEquals("2015-10-01", format.format(to.getVigencia()));
        assertEquals(43.50, to.getQuantidadesMes().get("201510").doubleValue(), 1);
    }

    private List<ConsumoProduto> listaComUmConsumo(){
        List<ConsumoProduto> lista = new ArrayList<ConsumoProduto>();
        
        ConsumoProduto cp = new ConsumoProduto();
        cp.setQuantidade(new BigDecimal(4));
        cp.setProduto(cloro);
        
        Consumo c = new Consumo();
        c.setData(_10Fev2015);
        c.addConsumoProduto(cp);
        
        cp.setConsumo(c);
        
        lista.add(cp);
        
        return lista;
    }
    
    private List<ConsumoProduto> consumosDoCloro(){
        List<ConsumoProduto> lista = new ArrayList<ConsumoProduto>();
        
        ConsumoProduto cp = new ConsumoProduto();
        cp.setQuantidade(new BigDecimal(4));
        cp.setProduto(cloro);
        Consumo c = new Consumo();
        c.setData(_10Fev2015);
        c.addConsumoProduto(cp);
        cp.setConsumo(c);
        lista.add(cp);
        
        cp = new ConsumoProduto();
        cp.setQuantidade(new BigDecimal(12));
        cp.setProduto(cloro);
        c = new Consumo();
        c.setData(_18Out2015);
        c.addConsumoProduto(cp);
        cp.setConsumo(c);
        lista.add(cp);
        
        cp = new ConsumoProduto();
        cp.setQuantidade(new BigDecimal(40.50));
        cp.setProduto(fluor);
        c = new Consumo();
        c.setData(_01Out2015);
        c.addConsumoProduto(cp);
        cp.setConsumo(c);
        lista.add(cp);
        
        cp = new ConsumoProduto();
        cp.setQuantidade(new BigDecimal(3.00));
        cp.setProduto(fluor);
        c = new Consumo();
        c.setData(_18Out2015);
        c.addConsumoProduto(cp);
        cp.setConsumo(c);
        lista.add(cp);
        
        return lista;
    }
    
    private void mockConsumo(List<ConsumoProduto> consumos) {
        expect(repositorio.listarConsumos(to)).andReturn(consumos);
        replay(repositorio);
    }	
}
