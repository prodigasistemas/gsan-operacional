package br.gov.pa.cosanpa.gopera.managedBean;

import static br.gov.model.util.Utilitarios.formataData;
import static br.gov.model.util.Utilitarios.formatarBigDecimalComVirgula;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.jboss.logging.Logger;

import br.gov.model.util.FormatoData;
import br.gov.pa.cosanpa.gopera.servicos.RelatorioConsumoProdutoBO;
import br.gov.pa.cosanpa.gopera.util.DadosExcel;
import br.gov.pa.cosanpa.gopera.util.GeradorExcel;
import br.gov.pa.cosanpa.gopera.util.TipoExibicao;
import br.gov.pa.cosanpa.gopera.util.WebUtil;
import br.gov.servicos.operacao.to.ConsultaConsumoProdutoTO;
import br.gov.servicos.operacao.to.IRelatorioConsumoTO;
import br.gov.servicos.operacao.to.RelatorioConsumoProdutoAnaliticoTO;
import br.gov.servicos.operacao.to.RelatorioConsumoProdutoMensalTO;
import br.gov.servicos.operacao.to.TipoRelatorioProdutoQuimico;

@ManagedBean
@ViewScoped
public class RelatorioConsumoProdutosQuimicosBean extends BaseMensagemBean{
	private static Logger logger = Logger.getLogger(RelatorioConsumoProdutosQuimicosBean.class);

	private TipoExibicao tipoExibicao = TipoExibicao.ANALITICO;
	
	private TipoRelatorioProdutoQuimico tipoRelatorio = TipoRelatorioProdutoQuimico.FISICO;

	private ConsultaConsumoProdutoTO to = new ConsultaConsumoProdutoTO();
    
	@EJB
	private RelatorioConsumoProdutoBO consumoBO;
	
	public RelatorioConsumoProdutosQuimicosBean() {
	}

	public String iniciar() {
	    return null;
	}

	public void exibir() {
        if (!to.intervaloValido()) {
            mostrarMensagemErro(bundle.getText("erro_intervalo_invalido"));
            return;
        }
	    
	    WebUtil util = new WebUtil();
	    if (util.mesesPeriodo(to.getDataInicial(), to.getDataFinal()).size() > 12) {
	        mostrarMensagemErro(bundle.getText("erro_periodo_superior_um_ano"));
	    }
	    
	    switch (tipoExibicao) {
	    case ANALITICO:
	        exibirRelatorioAnalitico();
	        break;
	    case SINTETICO:
	        exibirRelatorioSintetico();
	        break;
	    case MENSAL: 
	        exibirRelatorioMensal();
	        break;
	    }
	    to = new ConsultaConsumoProdutoTO();
	}
	
	public void exibirRelatorioMensal(){
        try {
            List<IRelatorioConsumoTO> relatorio = consumoBO.getConsumoProdutoMensal(to);
            
            if (relatorio.size() == 0){
                mostrarMensagemAviso(bundle.getText("erro_nao_existe_retorno_filtro"));
            }else{
                DadosExcel excel = new DadosExcel() {
                    public String periodo() {
                        return formataData(to.getDataInicial()) + " a " + formataData(to.getDataFinal());
                    }
                    
                    public String filtro(){
                        return to.filtroSelecionado();
                    }
                    
                    public String tituloRelatorio() {
                        return bundle.getText("consumo_produtos_quimicos_mensal");
                    }
                    
                    public String nomeArquivo() {
                        return "consumo_produtos_quimicos_mensal";
                    }
                    
                    public List<List<String>> dados() {
                        List<List<String>> linhas = new ArrayList<List<String>>();
                        for (IRelatorioConsumoTO item : relatorio) {
                            List<String> cols = item.converterDados(tipoRelatorio);
                            
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(to.getDataInicial());
                            while(!cal.getTime().after(to.getDataFinal())){
                                BigDecimal qtd = ((RelatorioConsumoProdutoMensalTO) item).getQuantidadesMes().get(formataData(cal.getTime(), FormatoData.ANO_MES));
                                if (tipoRelatorio == TipoRelatorioProdutoQuimico.FINANCEIRO && qtd != null){
                                    cols.add(formatarBigDecimalComVirgula(item.getValorUnitario().multiply(qtd)));
                                }else{
                                    cols.add(formatarBigDecimalComVirgula(qtd));
                                }
                                cal.add(Calendar.MONTH, 1);
                            }
                            
                            linhas.add(cols);
                        }
                        return linhas;
                    }
                    
                    public String[] cabecalho() {
                        List<String> lista = new ArrayList<String>();
                        lista.add(bundle.getText("produto"));
                        lista.add(bundle.getText("vigencia"));
                        if (tipoRelatorio == TipoRelatorioProdutoQuimico.FINANCEIRO){
                            lista.add(bundle.getText("valor_unitario"));
                        }
                        
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(to.getDataInicial());
                        while(!cal.getTime().after(to.getDataFinal())){
                            lista.add(formataData(cal.getTime(), FormatoData.MES_ANO));
                            cal.add(Calendar.MONTH, 1);
                        }
                        
                        String[] cabecalho = new String[lista.size()];
                        
                        for (int i = 0; i < lista.size(); i++) {
                            cabecalho[i] = lista.get(i);
                        }
                        
                        return cabecalho;
                    }
                };
                
                GeradorExcel gerador = new GeradorExcel(excel);
                gerador.geraPlanilha();
            }
        } catch (Exception e) {
            logger.error(bundle.getText("erro_gerar_relatorio"), e);
            mostrarMensagemErro(bundle.getText("erro_gerar_relatorio"));
        }
	}
	
	public void exibirRelatorioSintetico(){
	    try {
	        List<IRelatorioConsumoTO> relatorio = consumoBO.getConsumoProdutoSintetico(to);
	        
	        if (relatorio.size() == 0){
	            mostrarMensagemAviso(bundle.getText("erro_nao_existe_retorno_filtro"));
	        }else{
	            DadosExcel excel = new DadosExcel() {
                    public String periodo() {
                        return formataData(to.getDataInicial()) + " a " + formataData(to.getDataFinal());
                    }
	                
	                public String filtro(){
	                    return to.filtroSelecionado();
	                }
	                
	                public String tituloRelatorio() {
	                    return bundle.getText("consumo_produtos_quimicos_sintetico");
	                }
	                
	                public String nomeArquivo() {
	                    return "consumo_produtos_quimicos_sintetico";
	                }
	                
	                public List<List<String>> dados() {
	                    List<List<String>> linhas = new ArrayList<List<String>>();
	                    for (IRelatorioConsumoTO item : relatorio) {
	                        linhas.add(item.converterDados(tipoRelatorio));
	                    }
	                    return linhas;
	                }
	                
	                public String[] cabecalho() {
	                    List<String> lista = new ArrayList<String>();
	                    lista.add(bundle.getText("produto"));
	                    lista.add(bundle.getText("vigencia"));
	                    lista.add(bundle.getText("quantidade"));
	                    if (tipoRelatorio == TipoRelatorioProdutoQuimico.FINANCEIRO){
	                        lista.add(bundle.getText("valor_unitario"));
	                        lista.add(bundle.getText("valor_total"));
	                    }
	                    
	                    String[] cabecalho = new String[lista.size()];
	                    
	                    for (int i = 0; i < lista.size(); i++) {
	                        cabecalho[i] = lista.get(i);
	                    }
	                    
	                    return cabecalho;
	                }
	            };
	            
	            GeradorExcel gerador = new GeradorExcel(excel);
	            gerador.geraPlanilha();
	        }
	    } catch (Exception e) {
	        logger.error(bundle.getText("erro_gerar_relatorio"), e);
	        mostrarMensagemErro(bundle.getText("erro_gerar_relatorio"));
	    }
	}
	
	public void exibirRelatorioAnalitico(){
	    try {
	        List<RelatorioConsumoProdutoAnaliticoTO> relatorio = consumoBO.getConsumoProdutoAnalitico(to);
	        
	        if (relatorio.size() == 0){
	            mostrarMensagemAviso(bundle.getText("erro_nao_existe_retorno_filtro"));
	        }else{
	            DadosExcel excel = new DadosExcel() {
                    public String periodo() {
                        return formataData(to.getDataInicial()) + " a " + formataData(to.getDataFinal());
                    }
	                
	                public String filtro(){
	                    return to.filtroSelecionado();
	                }
	                
	                public String tituloRelatorio() {
	                    return bundle.getText("consumo_produtos_quimicos_analitico");
	                }
	                
	                public String nomeArquivo() {
	                    return "consumo_produtos_quimicos_analitico";
	                }
	                
	                public List<List<String>> dados() {
	                    List<List<String>> linhas = new ArrayList<List<String>>();
	                    for (RelatorioConsumoProdutoAnaliticoTO item : relatorio) {
	                        linhas.add(item.converterDados(tipoRelatorio));
	                    }
	                    return linhas;
	                }
	                
	                public String[] cabecalho() {
	                    List<String> lista = new ArrayList<String>();
	                    lista.add(bundle.getText("data_consumo"));
	                    lista.add(bundle.getText("regional"));
	                    lista.add(bundle.getText("unidade_negocio"));
	                    lista.add(bundle.getText("municipio"));
	                    lista.add(bundle.getText("localidade"));
	                    lista.add(bundle.getText("unidade_operacional"));
	                    lista.add(bundle.getText("produto"));
	                    lista.add(bundle.getText("quantidade"));
	                    if (tipoRelatorio == TipoRelatorioProdutoQuimico.FINANCEIRO){
	                        lista.add(bundle.getText("vigencia"));
	                        lista.add(bundle.getText("valor_unitario"));
	                        lista.add(bundle.getText("valor_total"));
	                    }
	                    
	                    String[] cabecalho = new String[lista.size()];
	                    
	                    for (int i = 0; i < lista.size(); i++) {
	                        cabecalho[i] = lista.get(i);
	                    }
	                    
	                    return cabecalho;
	                }
	            };
	            
	            GeradorExcel gerador = new GeradorExcel(excel);
	            gerador.geraPlanilha();
	        }
	    } catch (Exception e) {
	        logger.error(bundle.getText("erro_gerar_relatorio"), e);
	        mostrarMensagemErro(bundle.getText("erro_gerar_relatorio"));
	    }
	}
	
	public ConsultaConsumoProdutoTO getTo() {
        return to;
    }

    public void setTo(ConsultaConsumoProdutoTO to) {
        this.to = to;
    }

    public void setTipoExibicao(TipoExibicao tipoExibicao) {
        this.tipoExibicao = tipoExibicao;
    }

    public void setTipoRelatorio(TipoRelatorioProdutoQuimico tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public TipoExibicao getTipoExibicao() {
        return tipoExibicao;
    }

    public TipoRelatorioProdutoQuimico getTipoRelatorio() {
        return tipoRelatorio;
    }

    public TipoRelatorioProdutoQuimico[] getTiposRelatorio(){
	    return TipoRelatorioProdutoQuimico.values();
	}

	public TipoExibicao[] getTiposExibicao() {
		return TipoExibicao.values();
	}
}
