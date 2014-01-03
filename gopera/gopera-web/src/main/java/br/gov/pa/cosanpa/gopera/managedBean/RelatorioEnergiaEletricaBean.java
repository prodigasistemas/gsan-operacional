package br.gov.pa.cosanpa.gopera.managedBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import jxl.JXLException;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

import org.jboss.logging.Logger;

import br.gov.pa.cosanpa.gopera.command.GeraPlanilhaAnaliseEnergiaEletricaCommand;
import br.gov.pa.cosanpa.gopera.command.GeraPlanilhaFaturamentoMensalCommand;
import br.gov.pa.cosanpa.gopera.command.GeraPlanilhaUCNaoCadastradaCommand;
import br.gov.pa.cosanpa.gopera.command.GeraPlanilhaUCsNaoFaturadasCommand;
import br.gov.pa.cosanpa.gopera.command.InformacoesParaRelatorio;
import br.gov.pa.cosanpa.gopera.exception.BaseRuntimeException;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.fachada.IRelatorioEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.EnergiaEletricaDados;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.RelatorioEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.RelatorioExcel;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;
import br.gov.pa.cosanpa.gopera.util.DadoRelatorio;
import br.gov.pa.cosanpa.gopera.util.WebBundle;

@ManagedBean
@SessionScoped
@SuppressWarnings({ "rawtypes" })
public class RelatorioEnergiaEletricaBean extends BaseBean<RelatorioEnergiaEletrica> {

	private static Logger logger = Logger.getLogger(RelatorioEnergiaEletricaBean.class);

	private RelatorioEnergiaEletrica registro = new RelatorioEnergiaEletrica();
	private List<RelatorioEnergiaEletrica> relatorio = new ArrayList<RelatorioEnergiaEletrica>();
	private List<EnergiaEletricaDados> energiaEletricaDados = new ArrayList<EnergiaEletricaDados>();
	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private Integer codigoRegional;
	private Integer codigoUnidadeNegocio;
	private Integer codigoMunicipio;
	private Integer codigoLocalidade;
	private String reportPath;
	private String referencia;
	private String referenciaInicial;
	private String referenciaFinal;
	private Integer tipoRelatorio;
	private Integer tipoExportacao;
	private List<SelectItem> listaRelatorio = new ArrayList<SelectItem>();
	private List<SelectItem> listaTipoExportacao = new ArrayList<SelectItem>();
	private SortedMap<String, DadoRelatorio> mapDados;
	private List<String> dadosSelecionados;
	private Boolean exibirTipoExportacao;

	private Map<String, Object> parametros = new HashMap<String, Object>();

	@EJB
	private IRelatorioEnergiaEletrica fachadaRel;
	@EJB
	private IProxy fachadaProxy;

	public Boolean getExibirTipoExportacao() {
		return exibirTipoExportacao;
	}

	public void setExibirTipoExportacao(Boolean exibirTipoExportacao) {
		this.exibirTipoExportacao = exibirTipoExportacao;
	}

	public RelatorioEnergiaEletrica getRegistro() {
		return registro;
	}

	public void setRegistro(RelatorioEnergiaEletrica registro) {
		this.registro = registro;
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

	public void setRegionais(List<RegionalProxy> regionais) {
		this.regionais = regionais;
	}

	public List<UnidadeNegocioProxy> getUnidadesNegocio() {
		if ((this.getCodigoRegional() != null)) {
			try {
				this.unidadesNegocio = fachadaProxy.getListaUnidadeNegocio(this.getCodigoRegional());
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

	public void setUnidadesNegocio(List<UnidadeNegocioProxy> unidadesNegocio) {
		this.unidadesNegocio = unidadesNegocio;
	}

	public List<MunicipioProxy> getMunicipios() {
		if (this.getCodigoUnidadeNegocio() != null) {
			try {
				this.municipios = fachadaProxy.getListaMunicipio(this.getCodigoRegional(), this.getCodigoUnidadeNegocio());
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

	public void setMunicipios(List<MunicipioProxy> municipios) {
		this.municipios = municipios;
	}

	public List<LocalidadeProxy> getLocalidades() {
		if (this.getCodigoMunicipio() != null) {
			try {
				this.localidades = fachadaProxy.getListaLocalidade(this.getCodigoRegional(), this.getCodigoUnidadeNegocio(), this.getCodigoMunicipio());
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

	public Integer getCodigoRegional() {
		return codigoRegional;
	}

	public void setCodigoRegional(Integer codigoRegional) {
		this.codigoRegional = codigoRegional;
	}

	public Integer getCodigoUnidadeNegocio() {
		return codigoUnidadeNegocio;
	}

	public void setCodigoUnidadeNegocio(Integer codigoUnidadeNegocio) {
		this.codigoUnidadeNegocio = codigoUnidadeNegocio;
	}

	public Integer getCodigoMunicipio() {
		return codigoMunicipio;
	}

	public void setCodigoMunicipio(Integer codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public Integer getCodigoLocalidade() {
		return codigoLocalidade;
	}

	public void setCodigoLocalidade(Integer codigoLocalidade) {
		this.codigoLocalidade = codigoLocalidade;
	}

	public void setLocalidades(List<LocalidadeProxy> localidades) {
		this.localidades = localidades;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getReferenciaInicial() {
		return referenciaInicial;
	}

	public void setReferenciaInicial(String referenciaInicial) {
		this.referenciaInicial = referenciaInicial;
	}

	public String getReferenciaFinal() {
		return referenciaFinal;
	}

	public void setReferenciaFinal(String referenciaFinal) {
		this.referenciaFinal = referenciaFinal;
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

	public List<SelectItem> getListaRelatorio() {
		return listaRelatorio;
	}

	public void setListaRelatorio(List<SelectItem> listaRelatorio) {
		this.listaRelatorio = listaRelatorio;
	}

	public List<SelectItem> getListaTipoExportacao() {
		return listaTipoExportacao;
	}

	public void setListaTipoExportacao(List<SelectItem> listaTipoExportacao) {
		this.listaTipoExportacao = listaTipoExportacao;
	}

	public SortedMap<String, DadoRelatorio> getMapDados() {
		return mapDados;
	}

	public void setMapDados(SortedMap<String, DadoRelatorio> mapDados) {
		this.mapDados = mapDados;
	}

	public List<String> getDadosSelecionados() {
		return dadosSelecionados;
	}

	public void setDadosSelecionados(List<String> dadosSelecionados) {
		this.dadosSelecionados = dadosSelecionados;
	}

	public RelatorioEnergiaEletricaBean() {
		super();
		this.registro = new RelatorioEnergiaEletrica();
		this.tipoExportacao = 1;
		this.tipoRelatorio = 1;
		this.exibirTipoExportacao = true;
		SelectItem tipoRel = new SelectItem();
		tipoRel.setValue(1);
		if (bundle == null) {
			bundle = new WebBundle();
		}
		tipoRel.setLabel(bundle.getText("ucs_nao_cadastradas"));
		listaRelatorio.add(tipoRel);
		tipoRel = new SelectItem();
		tipoRel.setValue(2);
		tipoRel.setLabel(bundle.getText("ucs_nao_faturadas"));
		listaRelatorio.add(tipoRel);
		tipoRel = new SelectItem();
		tipoRel.setValue(3);
		tipoRel.setLabel(bundle.getText("faturamento_mensal"));
		listaRelatorio.add(tipoRel);
		tipoRel = new SelectItem();
		tipoRel.setValue(4);
		tipoRel.setLabel(bundle.getText("analise_energia_eletrica"));
		listaRelatorio.add(tipoRel);
		mapDados = new TreeMap<String, DadoRelatorio>();
		ResourceBundle dadosRelatorio = ResourceBundle.getBundle("dados_relatorio_energia");
		for (String key : dadosRelatorio.keySet()) {
			String valor = dadosRelatorio.getString(key);
			String[] labels = valor.split(";");
			mapDados.put(labels[1].trim(), new DadoRelatorio(labels[1].trim(), labels[0].trim()));
		}
		VerificaTipoRelatorio();
	}

	public void VerificaTipoRelatorio() {
		SelectItem tipoExp;
		listaTipoExportacao.clear();
		if (tipoRelatorio == 1 || tipoRelatorio == 2) {
			tipoExp = new SelectItem();
			tipoExp.setValue(1);
			tipoExp.setLabel("PDF");
			listaTipoExportacao.add(tipoExp);
			exibirTipoExportacao = true;
		} else {
			exibirTipoExportacao = false;
		}
		tipoExp = new SelectItem();
		tipoExp.setValue(2);
		tipoExp.setLabel("XLS");
		listaTipoExportacao.add(tipoExp);
	}

	public String iniciar() {
		this.registro = new RelatorioEnergiaEletrica();

		return "RelatorioEnergiaEletrica.jsf";
	}

	JasperPrint jasperPrint;

	private void relatorioEnergiaEletrica() throws Exception {
		try {
			switch (tipoRelatorio) {
			case 1: // UC´s NÃO CADASTRADAS
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/energiaEletricaUC.jasper");
				relatorio = fachadaRel.getEnergiaEletricaUC(primeiroDiaMes(referencia), this.tipoRelatorio, 0, 0, 0, 0);
				break;
			case 2: // UC´s NÃO FATURADAS
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/energiaEletricaUC.jasper");
				relatorio = fachadaRel.getEnergiaEletricaUC(primeiroDiaMes(referencia), this.tipoRelatorio, 0, 0, 0, 0);
				break;
			case 3: // FATURAMENTO MENSAL
				energiaEletricaDados.clear();
				energiaEletricaDados = fachadaRel.getEnergiaEletricaDados(primeiroDiaMes(referencia));
				break;
			case 4: // ANALISE ENERGIA ELETRICA
				this.setReferencia(referenciaFinal);
				break;
			}

		} catch (Exception e) {
			mostrarMensagemErro("Erro ao gerar Relatório: " + e.getMessage());
		}
	}

	public void exibir() {
		try {
			switch (tipoRelatorio) {
			case 1: // UC´s NÃO CADASTRADAS
				relatorioEnergiaEletrica();
				parametros.put("nomeRelatorio", bundle.getText("ucs_nao_cadastradas"));
				parametros.put("exibeDetalhe", true);
				if (relatorio.size() == 0) {
					mostrarMensagemErro(bundle.getText("erro_nao_existe_retorno_filtro"));
					return;
				}
				break;
			case 2: // UC´S NÃO FATURADAS
				relatorioEnergiaEletrica();
				parametros.put("nomeRelatorio", bundle.getText("ucs_nao_faturadas"));
				parametros.put("exibeDetalhe", false);
				if (relatorio.size() == 0) {
					mostrarMensagemErro(bundle.getText("erro_nao_existe_retorno_filtro"));
					return;
				}
				break;
			case 3: // FATURAMENTO MENSAL
				relatorioEnergiaEletrica();
				parametros.put("nomeRelatorio", bundle.getText("faturamento_mensal"));
				parametros.put("exibeDetalhe", false);
				tipoExportacao = 2;
				if (energiaEletricaDados.size() == 0) {
					mostrarMensagemErro(bundle.getText("erro_nao_existe_retorno_filtro"));
					return;
				}
				break;
			case 4: // ANALISE ENERGIA ELETRICA
				relatorioEnergiaEletrica();
				parametros.put("nomeRelatorio", bundle.getText("analise_energia_eletrica"));
				parametros.put("exibeDetalhe", false);
				tipoExportacao = 2;
				break;
			}

			// CONFIGURANDO PARAMETROS
			parametros.put("dataReferencia", primeiroDiaMes(referencia));
			parametros.put("logoRelatorio", "logoRelatorio.jpg");
			parametros.put("nomeUsuario", usuarioProxy.getNome());

			if (tipoExportacao != 2) {
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(relatorio);
				jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
			}

			HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			ServletOutputStream servletOutputStream;

			switch (tipoExportacao) {
			case 1: // PDF
				httpServletResponse.addHeader("Content-disposition", "attachment; filename=RelatorioEnergiaEletrica.pdf");
				servletOutputStream = httpServletResponse.getOutputStream();
				JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
				break;

			case 2: // EXCEL
				RelatorioExcel relatorioExcel = new RelatorioExcel();
				relatorioExcel.setCaminho(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports"));

				switch (tipoRelatorio) {
				case 1: // UC´s NÃO CADASTRADAS
					relatorioExcel.setNomeArquivoExistente("AnaliseEnergiaEletrica.xls");
					relatorioExcel.setRelatorioEnergiaEletrica(this.relatorio);
					break;
				case 2: // UC´S NÃO FATURADAS
					relatorioExcel.setNomeArquivoExistente("AnaliseEnergiaEletrica.xls");
					relatorioExcel.setRelatorioEnergiaEletrica(this.relatorio);
					break;
				case 3: // FATURAMENTO MENSAL
					relatorioExcel.setNomeArquivoExistente("FaturamentoMensalEnergiaEletrica.xls");
					relatorioExcel.setEnergiaEletricaDados(this.energiaEletricaDados);
					break;
				case 4: // ANALISE COMPLETA ENERGIA ELETRICA
					relatorioExcel.setNomeArquivoExistente("AnaliseCompletaEnergiaEletrica.xls");
					relatorioExcel.setDataReferenciaInicial(primeiroDiaMes(referenciaInicial));
					relatorioExcel.setDataReferenciaFinal(primeiroDiaMes(referenciaFinal));
					relatorioExcel.setCodigoMunicipio(codigoMunicipio);
					relatorioExcel.setCodigoLocalidade(codigoLocalidade);
					relatorioExcel.setDadoSelecionado(dadosSelecionados);
					break;
				}
				relatorioExcel.setNomeArquivoNovo("Energia Eletrica" + "-" + this.parametros.get("nomeRelatorio") + "-"
						+ this.filtroData(primeiroDiaMes(referencia), "MM-yyyy") + ".xls");

				relatorioExcel.setNomeRelatorio(this.parametros.get("nomeRelatorio").toString());
				relatorioExcel.setDataReferencia(this.filtroData(primeiroDiaMes(referencia), "MM/yyyy"));
				relatorioExcel.setTipoRelatorio(tipoRelatorio);
				if (geraPlanilha(relatorioExcel))
					downloadFile(relatorioExcel.getNomeArquivoNovo(), relatorioExcel.getArquivoNovo().getAbsolutePath());
				break;
			case 3: // WORD
				httpServletResponse.addHeader("Content-disposition", "attachment; filename=RelatorioEnergiaEletrica.docx");
				ServletOutputStream servletOutputStream1 = httpServletResponse.getOutputStream();
				JRDocxExporter docxExporter = new JRDocxExporter();
				docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream1);
				docxExporter.exportReport();
				break;
			}
			FacesContext.getCurrentInstance().responseComplete();
		}
		catch(BaseRuntimeException e){
			mostrarMensagemErro(bundle.getText(e.getMessage()));
			logger.error("Erro ao exibir Relatorio", e);
		}
		catch (Exception e) {
			mostrarMensagemErro(bundle.getText("erro_exibir_relatorio"));
			logger.error("Erro ao exibir Relatorio", e);
		}
	}

	// Método responsável por fazer a escrita, a inserção dos dados na planilha
	public boolean geraPlanilha(RelatorioExcel relatorioExcel) throws IOException, WriteException, JXLException, Exception {
		// carrega planilha pre existente
		relatorioExcel.setArquivo(new File(relatorioExcel.getCaminho() + "/" + relatorioExcel.getNomeArquivoExistente()));
		String diretorio = fachadaProxy.getParametroSistema(9);
		new File(diretorio).mkdirs();
		relatorioExcel.setArquivoNovo(new File(diretorio + relatorioExcel.getNomeArquivoNovo()));
		WorkbookSettings ws = new WorkbookSettings();
		ws.setLocale(new Locale("pt", "BR"));
		ws.setEncoding("ISO-8859-1");
		Workbook workbook = Workbook.getWorkbook(relatorioExcel.getArquivo(), ws);
		WritableWorkbook copy = Workbook.createWorkbook(relatorioExcel.getArquivoNovo(), workbook);
		WritableSheet sheet = copy.getSheet(0);
		boolean retCode = defineConteudo(relatorioExcel, sheet);
		copy.write();
		copy.close();
		return retCode;
	}

	private boolean defineConteudo(RelatorioExcel relatorioExcel, WritableSheet sheet) throws Exception {
		InformacoesParaRelatorio infos = new InformacoesParaRelatorio();
		infos.setReferencia(referencia);
		infos.setPrimeiroDiaReferencia(primeiroDiaMes(referencia));

		switch (this.tipoRelatorio) {

		case 1: // UC's NÃO CADASTRADAS
			new GeraPlanilhaUCNaoCadastradaCommand(relatorioExcel, sheet).execute(infos, fachadaRel);
			break;
		case 2: // UC´S NÃO FATURADAS
			new GeraPlanilhaUCsNaoFaturadasCommand(relatorioExcel, sheet).execute(infos, fachadaRel);
			break;
		case 3: // FATURAMENTO MENSAL
			new GeraPlanilhaFaturamentoMensalCommand(relatorioExcel, sheet).execute(infos, fachadaRel);
			break;
		case 4: // ANALISE ENERGIA ELETRICA
			infos.setPrimeiroDiaReferenciaInicial(primeiroDiaMes(referenciaInicial));
			infos.setPrimeiroDiaReferenciaFinal(primeiroDiaMes(referenciaFinal));
			infos.setCodigoRegional(codigoRegional);
			infos.setCodigoUnidadeNegocio(codigoUnidadeNegocio);
			infos.setCodigoMunicipio(codigoMunicipio);
			infos.setCodigoLocalidade(codigoLocalidade);
			infos.setMapDados(mapDados);
			infos.setDadosSelecionados(dadosSelecionados);

			new GeraPlanilhaAnaliseEnergiaEletricaCommand(relatorioExcel, sheet).execute(infos, fachadaRel);
			break;
		}

		return true;
	}

	public String downloadFile(String nomeDoArquivoGeradoParaDownload, String caminhoRelativoComNomeEextensao) {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		// Obtem o caminho para o arquivo e efetua a leitura
		byte[] arquivo = readFile(new File(caminhoRelativoComNomeEextensao));
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		// configura o arquivo que vai voltar para o usuario.
		response.setHeader("Content-Disposition", "attachment;filename=\"" + nomeDoArquivoGeradoParaDownload + "\"");
		response.setContentLength(arquivo.length);
		// isso faz abrir a janelinha de download
		response.setContentType("application/download");
		response.setCharacterEncoding("ISO-8859-1");
		// envia o arquivo de volta
		try {
			OutputStream out = response.getOutputStream();
			out.write(arquivo);
			out.flush();
			out.close();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException e) {
			System.out.print("Erro no envio do arquivo");
			e.printStackTrace();
		}
		return "";
	}

	// efetua a leitura do arquivo
	public static byte[] readFile(File file) {
		int len = (int) file.length();
		byte[] sendBuf = new byte[len];
		FileInputStream inFile = null;
		try {
			inFile = new FileInputStream(file);
			inFile.read(sendBuf, 0, len);

		} catch (FileNotFoundException e) {
			System.out.print("Arquivo não encontrado");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.print("Erro na leitura do arquivo");
			e.printStackTrace();
		}
		return sendBuf;
	}
}
