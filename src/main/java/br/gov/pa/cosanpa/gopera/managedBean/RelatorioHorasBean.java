package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.jboss.logging.Logger;

import br.gov.pa.cosanpa.gopera.util.ReportGenerator;
import br.gov.pa.cosanpa.gopera.util.TipoExportacao;
import br.gov.servicos.operacao.ConsultasCadastroRepositorio;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;
import br.gov.servicos.operacao.RelatorioHorasRepositorio;
import br.gov.servicos.operacao.to.ConsultaHorasTO;
import br.gov.servicos.operacao.to.HorasRelatorioTO;

@ManagedBean
@ViewScoped
public class RelatorioHorasBean extends BaseMensagemBean{

	private Logger logger = Logger.getLogger(RelatorioHorasBean.class);
	
	@EJB
	private ConsultasCadastroRepositorio cadastros;
	
	@EJB
	private RelatorioHorasRepositorio relatorioHoras;
	
	private ConsultaHorasTO to = new ConsultaHorasTO();
	
	private TipoExportacao tipoExportacao = TipoExportacao.PDF;
	
	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;

	public String iniciar(){
		return "RelatorioHoras.jsf";
	}
	
	public String filtro() throws Exception{
		StringBuilder filtroRelatorio = new StringBuilder();
		if (to.getCodigoRegional() != null && to.getCodigoRegional() != -1) {
    		filtroRelatorio.append(bundle.getText("regional"))
    		.append(": ")
    		.append(fachadaProxy.getRegional(to.getCodigoRegional()).getNome());
		}
		if (to.getCodigoUnidadeNegocio() != null && to.getCodigoUnidadeNegocio() != -1) {
    		filtroRelatorio.append("  ")
    		.append(bundle.getText("unid_negocio"))
    		.append(": ")
    		.append(fachadaProxy.getUnidadeNegocio(this.to.getCodigoUnidadeNegocio()).getNome());
		}
		if (to.getCodigoMunicipio() != null && to.getCodigoMunicipio() != -1) {
    		filtroRelatorio.append("  ")
    		.append(bundle.getText("municipio"))
    		.append(": ")
    		.append(fachadaProxy.getMunicipio(this.to.getCodigoMunicipio()).getNome());
		}
		if (to.getCodigoLocalidade() != null && to.getCodigoLocalidade() != -1) {
    		filtroRelatorio.append("  ")
    		.append(bundle.getText("localidade"))
    		.append(": ")
    		.append(fachadaProxy.getLocalidade(this.to.getCodigoLocalidade()).getNome());
		}
		return filtroRelatorio.toString();
	}
	
	public String exibir() {
		try {
			if (!to.intervaloValido()) {
				mostrarMensagemErro(bundle.getText("erro_intervalo_invalido"));
				return "";
			}
			
			List<HorasRelatorioTO> relatorio = relatorioHoras.consultaHoras(to);
			
			final Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("dataInicial", to.getReferenciaInicial());
			parametros.put("dataFinal", to.getReferenciaFinal());
			parametros.put("filtro", filtro());
//			parametros.put("exibirRegional", (tipoAgrupamento >= 1 ? true : false));  
//			parametros.put("exibirUnidadeNegocio", (tipoAgrupamento >= 2 ? true : false));
//			parametros.put("exibirMunicipio", (tipoAgrupamento >= 3 ? true : false));
//			parametros.put("exibirLocalidade", (tipoAgrupamento >= 4 ? true : false));
//			parametros.put("exibirUnidadeOperacional", (tipoAgrupamento >= 5 ? true : false));
			parametros.put("exibirRegional", true);  
			parametros.put("exibirUnidadeNegocio", true);
			parametros.put("exibirMunicipio", false);
			parametros.put("exibirLocalidade", false);
			parametros.put("exibirUnidadeOperacional", false);
			
			final TipoExportacao tipoExportacao = TipoExportacao.EXCEL;
			final JRBeanCollectionDataSource reportDataSource = new JRBeanCollectionDataSource(relatorio);
			ReportGenerator generator = new ReportGenerator() {
				public String getTituloRelatorio() {
					return "Relatório de Horas Trabalhadas";
				}

				public TipoExportacao getTipoExportacao() {
					return tipoExportacao;
				}
				
				public JRDataSource getReportDataSource() {
					return reportDataSource;
				}
				
				public Map<String, Object> getParametros() {
					return parametros;
				}
				
				public String getNomeArquivo() {
					return "horasTrabalhadas";
				}				
			};
			
			generator.geraRelatorio();
			
		} catch (Exception e) {
			logger.error(bundle.getText("erro_gerar_relatorio"), e);
			mostrarMensagemErro(bundle.getText("erro_gerar_relatorio"));
		}
		

		return null;
	}

	public TipoExportacao getTipoExportacao() {
		return tipoExportacao;
	}

	public void setTipoExportacao(TipoExportacao tipoExportacao) {
		this.tipoExportacao = tipoExportacao;
	}

	public ConsultaHorasTO getTo() {
		return to;
	}

	public void setTo(ConsultaHorasTO to) {
		this.to = to;
	}

	public ConsultasCadastroRepositorio getCadastros() {
		return cadastros;
	}
	
	public List<TipoExportacao> getTiposExportacao(){
		return Arrays.asList(TipoExportacao.values());
	}
}
