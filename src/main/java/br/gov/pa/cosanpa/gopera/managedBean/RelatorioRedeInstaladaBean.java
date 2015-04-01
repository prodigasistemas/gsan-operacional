package br.gov.pa.cosanpa.gopera.managedBean;

import static br.gov.model.util.Utilitarios.converteAnoMesParaMesAno;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.jboss.logging.Logger;

import br.gov.pa.cosanpa.gopera.util.GeradorRelatorio;
import br.gov.pa.cosanpa.gopera.util.TipoExportacao;
import br.gov.servicos.operacao.RelatorioRedeInstaladaRepositorio;
import br.gov.servicos.operacao.to.FiltroOperacionalTO;
import br.gov.servicos.operacao.to.RedeInstaladaRelatorioTO;

@ManagedBean
@ViewScoped
public class RelatorioRedeInstaladaBean extends BaseMensagemBean {
	private static Logger logger = Logger.getLogger(RelatorioRedeInstaladaBean.class);

	@EJB
	private RelatorioRedeInstaladaRepositorio relatorioRedeInstalada;
	
	private FiltroOperacionalTO to = new FiltroOperacionalTO();

	public String iniciar(){
		return null;
	}	

	public void exibir() {
    	try{
    		if (!to.intervaloValido()) {
				mostrarMensagemErro(bundle.getText("erro_intervalo_invalido"));
				return;
			}
    		
    		List<RedeInstaladaRelatorioTO> relatorio = relatorioRedeInstalada.consultarRedesInstaladas(to);
    		
    		if (relatorio.size() == 0){
			    mostrarMensagemAviso(bundle.getText("erro_nao_existe_retorno_filtro"));
			}else{
			    
			    GeradorRelatorio gerador = new GeradorRelatorio() {
                    
                    public String getTituloRelatorio() {
                        return bundle.getText("adutoras_rede_agua");
                    }
                    
                    public TipoExportacao getTipoExportacao() {
                        return TipoExportacao.EXCEL;
                    }
                    
                    public Map<String, Object> getParametros() {
                        
                        Map<String, Object> parametros = new HashMap<String, Object>();
                        parametros.put("dataInicial", converteAnoMesParaMesAno(to.getReferenciaInicial()));
                        parametros.put("dataFinal", converteAnoMesParaMesAno(to.getReferenciaFinal()));
                        parametros.put("filtro", to.filtroSelecionado());
                        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
                        
                        return parametros;
                    }
                    
                    public String getNomeArquivo() {
                        return "redeInstalada";
                    }
                    
                    public JRDataSource getDados() {
                        return new JRBeanCollectionDataSource(relatorio);
                    }
                };
                
                gerador.geraRelatorio();
			}
		}
		catch (Exception e){
			logger.error(bundle.getText("erro_gerar_relatorio"), e);
			mostrarMensagemErro(bundle.getText("erro_gerar_relatorio"));
		}
    }

	public RelatorioRedeInstaladaRepositorio getRelatorioRedeInstalada() {
		return relatorioRedeInstalada;
	}

	public void setRelatorioRedeInstalada(RelatorioRedeInstaladaRepositorio relatorioRedeInstalada) {
		this.relatorioRedeInstalada = relatorioRedeInstalada;
	}

	public FiltroOperacionalTO getTo() {
		return to;
	}

	public void setTo(FiltroOperacionalTO to) {
		this.to = to;
	}
}

