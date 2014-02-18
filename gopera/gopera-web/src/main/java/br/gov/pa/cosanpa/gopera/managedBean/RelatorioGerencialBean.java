package br.gov.pa.cosanpa.gopera.managedBean;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.jboss.logging.Logger;

import br.gov.pa.cosanpa.gopera.fachada.IConsumoEAT;
import br.gov.pa.cosanpa.gopera.fachada.IConsumoETA;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.fachada.IRelatorioProdutoQuimico;
import br.gov.pa.cosanpa.gopera.model.EEAT;
import br.gov.pa.cosanpa.gopera.model.ETA;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.RelatorioGerencial;
import br.gov.pa.cosanpa.gopera.model.UnidadeConsumidoraOperacional;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;
import br.gov.pa.cosanpa.gopera.util.TipoExibicao;
import br.gov.pa.cosanpa.gopera.util.WebUtil;

@ManagedBean
@ViewScoped
public class RelatorioGerencialBean extends BaseRelatorioBean<RelatorioGerencial> {
	private static Logger logger = Logger.getLogger(RelatorioGerencialBean.class);

	@Resource(lookup = "java:/gopera")
	private DataSource dataSource;

	private RelatorioGerencial registro = new RelatorioGerencial();
	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private List<UnidadeConsumidoraOperacional> unidadesOperacional = new ArrayList<UnidadeConsumidoraOperacional>();
	private List<RelatorioGerencial> relatorioComFiltro = new ArrayList<RelatorioGerencial>();
	private String reportPath;
	private Date dataInicial;
	private Date dataFinal;
	private Integer tipoUnidadeOperacional;
	private TipoExibicao tipoExibicao;
	private Integer tipoRelatorio;
	private Integer tipoExportacao;
	private Integer tipoAgrupamento;
	private String filtroRelatorio = "";

	@EJB
	private IProxy fachadaProxy;
	@EJB
	private IRelatorioProdutoQuimico fachadaRel;
	@EJB
	private IConsumoETA fachadaConsumoETA;
	@EJB
	private IConsumoEAT fachadaConsumoEAT;

	public RelatorioGerencialBean() {
		this.registro = new RelatorioGerencial();
		this.tipoExibicao = TipoExibicao.ANALITICO;
		this.tipoExportacao = 1;
		this.tipoRelatorio = 1;
	}

	public String iniciar() {
		this.registro = new RelatorioGerencial();
		return "RelatorioGerencial.jsf";
	}

	JasperPrint jasperPrint;

	private void relatorioConsumoGerencialSintetico() throws Exception {
		reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/consumoProdutoQuimico.jasper");

		relatorioComFiltro = fachadaRel.getConsumoProdutoSintetico(this.registro.getCodigoRegional(), this.registro.getCodigoUnidadeNegocio(),
				this.registro.getCodigoMunicipio(), this.registro.getCodigoLocalidade(), this.tipoUnidadeOperacional,
				this.registro.getCodigoUnidadeOperacional(), this.dataInicial, this.dataFinal, tipoAgrupamento, tipoRelatorio);
	}

	private void relatorioConsumoGerencialMensal() throws Exception {
		reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/consumoProdutoQuimicoMensal.jasper");
		relatorioComFiltro = fachadaRel.getConsumoProdutoMensal(this.registro.getCodigoRegional(), this.registro.getCodigoUnidadeNegocio(),
				this.registro.getCodigoMunicipio(), this.registro.getCodigoLocalidade(), this.tipoUnidadeOperacional,
				this.registro.getCodigoUnidadeOperacional(), this.dataInicial, this.dataFinal, tipoAgrupamento, tipoRelatorio);
	}

	private void relatorioConsumoGerencialAnalitico() throws Exception {
		reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/consumoProdutoQuimico.jasper");
		relatorioComFiltro = fachadaRel.getConsumoProdutoAnalitico(this.registro.getCodigoRegional(), this.registro.getCodigoUnidadeNegocio(),
				this.registro.getCodigoMunicipio(), this.registro.getCodigoLocalidade(), this.tipoUnidadeOperacional,
				this.registro.getCodigoUnidadeOperacional(), this.dataInicial, this.dataFinal, tipoAgrupamento, tipoRelatorio);
	}

	public String exibir() {
		Connection con = null;
		try {
			WebUtil util = new WebUtil();
			if (util.mesesPeriodo(dataInicial, dataFinal).size() > 12) {
				mostrarMensagemErro(bundle.getText("erro_periodo_superior_um_ano"));
				return "";
			}

			con = dataSource.getConnection();
			String nomeExibicao = "";
			String nomeRelatorio = "";

			switch (tipoExibicao) {
			case ANALITICO: // ANALÍTICO
				relatorioConsumoGerencialAnalitico();
				nomeRelatorio = bundle.getText("consumo_produtos_quimicos_analitico");
				nomeExibicao = "ANALITICO";
				break;
			case SINTETICO: // SINTÉTICO
				relatorioConsumoGerencialSintetico();
				nomeRelatorio = bundle.getText("consumo_produtos_quimicos_sintetico");
				nomeExibicao = "SINTETICO";
				break;
			case MENSAL: // MENSAL
				relatorioConsumoGerencialMensal();
				nomeRelatorio = bundle.getText("consumo_produtos_quimicos_mensal");
				nomeExibicao = "MENSAL";
				break;
			}

			nomeExibicao = nomeExibicao + (tipoRelatorio == 1 ? "_FISICO" : "_FINANCEIRO");
			nomeRelatorio = nomeRelatorio + " " + (tipoRelatorio == 1 ? bundle.getText("fisico") : bundle.getText("financeiro"));

			if (relatorioComFiltro.size() == 0) {
				mostrarMensagemErro(bundle.getText("erro_nao_existe_retorno_filtro"));
				return "";
			}

			// DEFININDO FILTRO PARA EXIBIÇÃO
			filtroRelatorio = (this.registro.getCodigoRegional() != -1 ? bundle.getText("regional") + ": "
					+ fachadaProxy.getRegional(this.registro.getCodigoRegional()).getNome() : "");
			filtroRelatorio = filtroRelatorio
					+ (this.registro.getCodigoUnidadeNegocio() != -1 ? " " + bundle.getText("unid_negocio") + ": "
							+ fachadaProxy.getUnidadeNegocio(this.registro.getCodigoUnidadeNegocio()).getNome() : "");
			filtroRelatorio = filtroRelatorio
					+ (this.registro.getCodigoMunicipio() != -1 ? " " + bundle.getText("municipio") + ": "
							+ fachadaProxy.getMunicipio(this.registro.getCodigoMunicipio()).getNome() : "");
			if (this.registro.getCodigoLocalidade() != -1) {
				filtroRelatorio += "\n";
			}
			filtroRelatorio = filtroRelatorio
					+ (this.registro.getCodigoLocalidade() != -1 ? bundle.getText("localidade") + ": "
							+ fachadaProxy.getLocalidade(this.registro.getCodigoLocalidade()).getNome() : "");
			filtroRelatorio = filtroRelatorio
					+ (this.registro.getCodigoUnidadeOperacional() != -1 ? " " + bundle.getText("unid_operacional") + ": "
							+ fachadaProxy.getUnidadeOperacional(this.getTipoUnidadeOperacional(), this.registro.getCodigoUnidadeOperacional()) : "");

			HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			ServletOutputStream servletOutputStream;

			SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");

			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("dataInicial", dataInicial);
			parametros.put("dataFinal", dataFinal);
			parametros.put("logoRelatorio", "logoRelatorio.jpg");
			parametros.put("nomeUsuario", usuarioProxy.getNome());
			parametros.put("nomeRelatorio", nomeRelatorio);
			parametros.put("exibirRegional", (tipoAgrupamento >= 1 ? true : false));
			parametros.put("exibirUnidadeNegocio", (tipoAgrupamento >= 2 ? true : false));
			parametros.put("exibirMunicipio", (tipoAgrupamento >= 3 ? true : false));
			parametros.put("exibirLocalidade", (tipoAgrupamento >= 4 ? true : false));
			parametros.put("exibirUnidadeOperacional", (tipoAgrupamento >= 5 ? true : false));
			parametros.put("filtro", filtroRelatorio);
			parametros.put("REPORT_CONNECTION", con);
			parametros.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/") + "/");
			parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
			parametros.put("exibirFinanceiro", (tipoRelatorio == 2 ? true : false));
			parametros.put("exibirAnalitico", (tipoExibicao == TipoExibicao.ANALITICO ? true : false));

			JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(relatorioComFiltro);
			httpServletResponse.setCharacterEncoding("ISO-8859-1");
			
			switch (tipoExportacao) {
			case 1: // PDF
				parametros.put("exibirExcel", false);
				jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
				httpServletResponse.addHeader(
						"Content-disposition",
						"attachment; filename=RelatorioProdutoQuimico_" + nomeExibicao + "_" + formataData.format(dataInicial) + "_"
								+ formataData.format(dataFinal) + ".pdf");
				httpServletResponse.setContentType("application/pdf");
				servletOutputStream = httpServletResponse.getOutputStream();
				JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
				break;

			case 2: // EXCEL
				parametros.put("exibirExcel", true);
				jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
				httpServletResponse.addHeader(
						"Content-disposition",
						"attachment; filename=RelatorioProdutoQuimico_" + nomeExibicao + "_" + formataData.format(dataInicial) + "_"
								+ formataData.format(dataFinal) + ".xls");
				httpServletResponse.setContentType("application/vnd.ms-excel");
				servletOutputStream = httpServletResponse.getOutputStream();
				gerarExcel(servletOutputStream, jasperPrint);
				break;
			}
		} catch (Exception e) {
			logger.error(bundle.getText("erro_gerar_relatorio"), e);
			mostrarMensagemErro(bundle.getText("erro_gerar_relatorio"));
		}

		return null;
	}

	public RelatorioGerencial getRegistro() {
		return registro;
	}

	public void setRegistro(RelatorioGerencial registro) {
		this.registro = registro;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public TipoExibicao getTipoExibicao() {
		return tipoExibicao;
	}

	public void setTipoExibicao(TipoExibicao tipoExibicao) {
		this.tipoExibicao = tipoExibicao;
	}

	public Integer getTipoAgrupamento() {
		return tipoAgrupamento;
	}

	public void setTipoAgrupamento(Integer tipoAgrupamento) {
		this.tipoAgrupamento = tipoAgrupamento;
	}

	public Integer getTipoExportacao() {
		return tipoExportacao;
	}

	public void setTipoExportacao(Integer tipoExportacao) {
		this.tipoExportacao = tipoExportacao;
	}

	public Integer getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(Integer tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public Integer getTipoUnidadeOperacional() {
		return tipoUnidadeOperacional;
	}

	public void setTipoUnidadeOperacional(Integer tipoUnidadeOperacional) {
		this.tipoUnidadeOperacional = tipoUnidadeOperacional;
	}

	public TipoExibicao[] getTiposExibicao() {
		return TipoExibicao.values();
	}

	public List<RegionalProxy> getRegionais() {
		try {
			regionais = fachadaProxy.getListaRegional();
			regionais.add(0, new RegionalProxy(-1, "Selecione..."));
			return regionais;
		} catch (Exception e) {
			mostrarMensagemErro("Erro ao consultar sistema externo.");
		}
		return regionais;
	}

	public List<UnidadeNegocioProxy> getUnidadesNegocio() {
		if ((this.registro.getCodigoRegional() != null)) {
			try {
				this.unidadesNegocio = fachadaProxy.getListaUnidadeNegocio(this.registro.getCodigoRegional());
				this.unidadesNegocio.add(0, new UnidadeNegocioProxy(-1, "Selecione..."));
				return unidadesNegocio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
		unidadesNegocio.add(0, new UnidadeNegocioProxy(-1, "Selecione..."));
		return unidadesNegocio;
	}

	public List<MunicipioProxy> getMunicipios() {
		if (this.registro.getCodigoUnidadeNegocio() != null) {
			try {
				this.municipios = fachadaProxy.getListaMunicipio(this.registro.getCodigoRegional(), this.registro.getCodigoUnidadeNegocio());
				this.municipios.add(0, new MunicipioProxy(-1, "Selecione..."));
				return municipios;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		municipios = new ArrayList<MunicipioProxy>();
		this.municipios.add(0, new MunicipioProxy(-1, "Selecione..."));
		return municipios;
	}

	public List<LocalidadeProxy> getLocalidades() {
		if (this.registro.getCodigoMunicipio() != null) {
			try {
				this.localidades = fachadaProxy.getListaLocalidade(this.registro.getCodigoRegional(), this.registro.getCodigoUnidadeNegocio(),
						this.registro.getCodigoMunicipio());
				this.localidades.add(0, new LocalidadeProxy(-1, "Selecione..."));
				return this.localidades;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		localidades = new ArrayList<LocalidadeProxy>();
		localidades.add(0, new LocalidadeProxy(-1, "Selecione..."));
		return localidades;
	}

	public List<UnidadeConsumidoraOperacional> getUnidadesOperacional() {
		if (this.tipoUnidadeOperacional != null && this.tipoUnidadeOperacional != 0) {
			try {
				unidadesOperacional.clear();
				if (this.tipoUnidadeOperacional == 2) {// ETA
					List<ETA> listaETA = fachadaConsumoETA.getListaConsumoETA(usuarioProxy, this.registro.getCodigoRegional(),
							this.registro.getCodigoUnidadeNegocio(), this.registro.getCodigoMunicipio(), this.registro.getCodigoLocalidade());
					for (ETA eta : listaETA) {
						UnidadeConsumidoraOperacional unop = new UnidadeConsumidoraOperacional();
						unop.setCodigo(eta.getCodigo());
						unop.setCodigoUnidadeOperacional(eta.getCodigo());
						unop.setDescricao(eta.getDescricao());
						this.unidadesOperacional.add(unop);
					}
				} else {// EAT
					List<EEAT> listaEAT = fachadaConsumoEAT.getListaConsumoEAT(usuarioProxy, this.registro.getCodigoRegional(),
							this.registro.getCodigoUnidadeNegocio(), this.registro.getCodigoMunicipio(), this.registro.getCodigoLocalidade());
					for (EEAT eat : listaEAT) {
						UnidadeConsumidoraOperacional unop = new UnidadeConsumidoraOperacional();
						unop.setCodigo(eat.getCodigo());
						unop.setCodigoUnidadeOperacional(eat.getCodigo());
						unop.setDescricao(eat.getDescricao());
						this.unidadesOperacional.add(unop);
					}
				}
				this.unidadesOperacional.add(0, new UnidadeConsumidoraOperacional(-1, "Selecione..."));
				return this.unidadesOperacional;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		unidadesOperacional = new ArrayList<UnidadeConsumidoraOperacional>();
		unidadesOperacional.add(0, new UnidadeConsumidoraOperacional(-1, "Selecione..."));
		return unidadesOperacional;
	}
}
