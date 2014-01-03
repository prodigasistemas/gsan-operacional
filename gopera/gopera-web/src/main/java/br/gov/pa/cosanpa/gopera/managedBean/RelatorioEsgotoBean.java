package br.gov.pa.cosanpa.gopera.managedBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.RelatorioGerencial;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@ManagedBean
@SessionScoped
public class RelatorioEsgotoBean extends BaseRelatorioBean<RelatorioGerencial> {

	@Resource(lookup="java:/gopera")
	private DataSource	dataSource;

	@EJB
	private IProxy fachadaProxy;
	
	private List<RegionalProxy> listaRegional = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> listaUnidadeNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> listaMunicipio = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> listaLocalidade = new ArrayList<LocalidadeProxy>();
	private Integer tipoExportacao;
	private Integer tipoAgrupamento;
	private JasperPrint jasperPrint;
	private String nomeRelatorio;
	private String nomeArquivo;
	private String reportPath; 
	private String filtroRelatorio;
	private String referenciaInicial;
	private String referenciaFinal;

	public List<RegionalProxy> getListaRegional() {
		try {
				this.listaRegional =  fachadaProxy.getListaRegional();
				this.listaRegional.add(0, new RegionalProxy(-1, "Selecione..."));
				return listaRegional;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		listaRegional = new ArrayList<RegionalProxy>();
		this.listaRegional.add(0, new RegionalProxy(-1, "Selecione..."));
		return listaRegional;
	}
	
	public List<UnidadeNegocioProxy> getListaUnidadeNegocio() {
		if (this.registro.getCodigoRegional() != null) {
			try {
					this.listaUnidadeNegocio =  fachadaProxy.getListaUnidadeNegocio(this.registro.getCodigoRegional());
					this.listaUnidadeNegocio.add(0, new UnidadeNegocioProxy(-1, "Selecione..."));
					return listaUnidadeNegocio;
				} catch (Exception e) {
					mostrarMensagemErro("Erro ao consultar sistema externo.");
				}
		} 	
		listaUnidadeNegocio = new ArrayList<UnidadeNegocioProxy>();
		this.listaUnidadeNegocio.add(0, new UnidadeNegocioProxy(-1, "Selecione..."));
		return listaUnidadeNegocio;
	}

	public List<MunicipioProxy> getListaMunicipio() {
		if (this.registro.getCodigoUnidadeNegocio() != null) {
			try {
				this.listaMunicipio =  fachadaProxy.getListaMunicipio(this.registro.getCodigoRegional(),
																	  this.registro.getCodigoUnidadeNegocio());
				this.listaMunicipio.add(0, new MunicipioProxy(-1, "Selecione..."));				
				return this.listaMunicipio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.listaMunicipio = new ArrayList<MunicipioProxy>();
		this.listaMunicipio.add(0, new MunicipioProxy(-1, "Selecione..."));
		return this.listaMunicipio;
	}

	public List<LocalidadeProxy> getListaLocalidade() {
		if (this.registro.getCodigoMunicipio() != null) {
			try {
				this.listaLocalidade =  fachadaProxy.getListaLocalidade(this.registro.getCodigoRegional(), 
																	    this.registro.getCodigoUnidadeNegocio(),
																	    this.registro.getCodigoMunicipio());
				this.listaLocalidade.add(0, new LocalidadeProxy(-1, "Selecione..."));
				return this.listaLocalidade;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.listaLocalidade = new ArrayList<LocalidadeProxy>();
		this.listaLocalidade.add(0, new LocalidadeProxy(-1, "Selecione..."));
		return this.listaLocalidade;
	}
	
	public Integer getTipoExportacao() {
		return tipoExportacao;
	}

	public void setTipoExportacao(Integer tipoExportacao) {
		this.tipoExportacao = tipoExportacao;
	}
	
    public Integer getTipoAgrupamento() {
		return tipoAgrupamento;
	}

	public void setTipoAgrupamento(Integer tipoAgrupamento) {
		this.tipoAgrupamento = tipoAgrupamento;
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

	@Override
	public String iniciar(){
		tipoExportacao = 1;	
		tipoAgrupamento = 5;
		this.registro = new RelatorioGerencial();
		return "RelatorioEsgoto.jsf";
	}	

	public void exibir() throws SQLException{
    	String sql = "", sqlFiltro = "";
    	Connection con = null; 
    	try{
    		con =  dataSource.getConnection();
    		Statement stm = con.createStatement();
    		JRResultSetDataSource jrsds = null;
    		
    		//VERIFICA FILTRO
    		filtroRelatorio = "";
			if (registro.getCodigoRegional() != -1) {
				sqlFiltro = sqlFiltro + " AND A.greg_id = " + registro.getCodigoRegional();
	    		filtroRelatorio = filtroRelatorio + " Regional: " + fachadaProxy.getRegional(this.registro.getCodigoRegional()).getNome();
			}
			if (registro.getCodigoUnidadeNegocio() != -1) {
				sqlFiltro = sqlFiltro + " AND A.uneg_id = " + registro.getCodigoUnidadeNegocio();
	    		filtroRelatorio = filtroRelatorio + " Und Negócio: " + fachadaProxy.getUnidadeNegocio(this.registro.getCodigoUnidadeNegocio()).getNome();
			}
			if (registro.getCodigoMunicipio() != -1) {
				sqlFiltro = sqlFiltro + " AND A.muni_id = " + registro.getCodigoMunicipio();
	    		filtroRelatorio = filtroRelatorio + " Município: " + fachadaProxy.getMunicipio(this.registro.getCodigoMunicipio()).getNome();
			}
			if (registro.getCodigoLocalidade() != -1) {
				sqlFiltro = sqlFiltro + " AND A.loca_id = " + registro.getCodigoLocalidade();
	    		filtroRelatorio = filtroRelatorio + " Localidade: " + fachadaProxy.getLocalidade(this.registro.getCodigoLocalidade()).getNome();
			} 
    		
			SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");	
			GregorianCalendar gc = new GregorianCalendar();
			//Formata Data Inicial
			gc.setTime(primeiroDiaMes(referenciaInicial));
			String dataIni = formataData.format(gc.getTime());
			//Formata Data Final
			gc.setTime(primeiroDiaMes(referenciaFinal));
			String dataFim = formataData.format(gc.getTime());
			
			nomeRelatorio = "Tratamento de Esgoto";
			nomeArquivo = "tratamentoEsgoto";
			reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/" + nomeArquivo + ".jasper");
			sql = "SELECT A.greg_id, greg_nmregional, A.uneg_id, uneg_nmunidadenegocio, A.muni_id, muni_nmmunicipio, A.loca_id, loca_nmlocalidade, A.ete_id, ete_nome,"
			    + "		  etev_referencia, etev_volumecoletado, etev_volumetratado"
			    + "  FROM operacao.ete_volume A"
			    + " INNER JOIN cadastro.gerencia_regional B ON A.greg_id = B.greg_id"
			    + " INNER JOIN cadastro.unidade_negocio C ON A.uneg_id = C.uneg_id"
			    + " INNER JOIN cadastro.municipio D ON A.muni_id = D.muni_id"
			    + " INNER JOIN cadastro.localidade E ON A.loca_id = E.loca_id"
			    + " INNER JOIN operacao.ete F ON A.ete_id = F.ete_id"
			    + " WHERE etev_referencia BETWEEN '" + dataIni + "' AND '" + dataFim + "'" + sqlFiltro
			    + " ORDER BY greg_nmregional, uneg_nmunidadenegocio, muni_nmmunicipio, loca_nmlocalidade, ete_nome, etev_referencia";

			//Monta ResultSet	
  			ResultSet rsAux = stm.executeQuery(sql);
    		if (!rsAux.next()){
    			mostrarMensagemErro(bundle.getText("erro_nao_existe_retorno_filtro"));
    			return;
    		}
			ResultSet rs = stm.executeQuery(sql);
			switch (tipoExportacao) {
				case 1: //PDF
					jrsds = new JRResultSetDataSource(rs);
					
					//Preenche Parâmetros	
					Map<String, Object> parametros = new HashMap<String, Object>();
		 	        parametros.put("logoRelatorio", "logoRelatorio.jpg");
		 	        parametros.put("nomeRelatorio", nomeRelatorio);
			        parametros.put("nomeUsuario", usuarioProxy.getNome());
			        parametros.put("dataInicial", primeiroDiaMes(referenciaInicial));
			        parametros.put("dataFinal", primeiroDiaMes(referenciaFinal));
			        parametros.put("filtro", filtroRelatorio);
			        parametros.put("filtro", filtroRelatorio);
			        parametros.put("exibirRegional", (tipoAgrupamento >= 1 ? true : false));  
			        parametros.put("exibirUnidadeNegocio", (tipoAgrupamento >= 2 ? true : false));
			        parametros.put("exibirMunicipio", (tipoAgrupamento >= 3 ? true : false));
			        parametros.put("exibirLocalidade", (tipoAgrupamento >= 4 ? true : false));
			        parametros.put("exibirUnidadeOperacional", (tipoAgrupamento >= 5 ? true : false));
			        parametros.put("REPORT_CONNECTION", con);
			        parametros.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/") + "/");
			        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
			        
		 	        jasperPrint = JasperFillManager.fillReport(reportPath, parametros, jrsds);

			 	    HttpServletResponse httpServletResponse=(HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
					httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + nomeArquivo + ".pdf");
					ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
				    JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
				    break;
			 
				case 2: //EXCEL
					ExportaExcel(nomeArquivo, rs);
				    break;
			 }	
			FacesContext.getCurrentInstance().responseComplete();  
		}
		catch (Exception e){
			e.printStackTrace();
			mostrarMensagemErro("Erro ao exibir Relatório: " + e.getMessage());
		}
		finally {
			con.close();
		}    	
    }

	private void ExportaExcel(String nomeArquivo, ResultSet rs) throws Exception{
    	Integer linha = 4;
		SimpleDateFormat formataDataSQL = new SimpleDateFormat("yyyy-MM-dd");;
		SimpleDateFormat formataDataPadrao = new SimpleDateFormat("dd/MM/yyyy");;
    	//GERA PLANILHA 
 		super.nomeArquivo = nomeArquivo; 
 		super.iniciar();
 		WritableWorkbook wb = geraPlanilha();
 		WritableSheet sheet = wb.getSheet(0);
 		sheet.setName(nomeRelatorio);
 		
		//Titulo			
		addLabel(sheet, 0, 0, "TRATAMENTO DE ESGOTO", wcfLabelBold);
		sheet.mergeCells(0, 0, 6, 0);
		//Filtro
		addLabel(sheet, 0, 1, filtroRelatorio, wcfLabelLeft);
		sheet.mergeCells(0, 1, 6, 1);

		//Referência
		addLabel(sheet, 0, 2, "Referência: " + referenciaInicial + " a " + referenciaFinal, wcfLabelBold);
		sheet.mergeCells(0, 2, 6, 2);
		
		//Dados
		String greg_nm = "", uneg_nm = "", muni_nm = "", loca_nm = "";
		Boolean greg_qb = false, uneg_qb = false, muni_qb = false, loca_qb = false;
		Double greg_total1 = 0.0, greg_total2 = 0.0;
		Double uneg_total1 = 0.0, uneg_total2 = 0.0;
		Double muni_total1 = 0.0, muni_total2 = 0.0;
		Double loca_total1 = 0.0, loca_total2 = 0.0;
		Double total1 = 0.0, total2 = 0.0;
		Double valor1 = 0.0, valor2 = 0.0;
		
		//Cabeçalho
		addLabel(sheet, 0, linha, "REGIONAL", wcfLabelHeader);
		addLabel(sheet, 1, linha, "UNIDADE NEGÓCIO", wcfLabelHeader);
		addLabel(sheet, 2, linha, "MUNICÍPIO", wcfLabelHeader);
		addLabel(sheet, 3, linha, "LOCALIDADE", wcfLabelHeader);
		addLabel(sheet, 4, linha, "REFERÊNCIA", wcfLabelHeader);
		addLabel(sheet, 5, linha, "VOLUME COLETADO", wcfLabelHeader);
		addLabel(sheet, 6, linha, "VOLUME TRATADO", wcfLabelHeader);
		linha++;
		while (rs.next()) {
			//TOTALIZADORES	
			if (loca_qb){
				loca_qb = false;
				addLabel(sheet, 3, linha, "TOTAL " + loca_nm,  wcfLabelBoldLeft);
				addNumero(sheet, 5, linha, loca_total1, wcfNumeroBold);
				addNumero(sheet, 6, linha, loca_total2, wcfNumeroBold);
				loca_total1 = 0.0;
				loca_total2 = 0.0;
				linha++;
			}
			if (muni_qb){
				muni_qb = false;
				addLabel(sheet, 2, linha, "TOTAL " + muni_nm,  wcfLabelBoldLeft);
				addNumero(sheet, 5, linha, muni_total1, wcfNumeroBold);
				addNumero(sheet, 6, linha, muni_total2, wcfNumeroBold);
				muni_total1 = 0.0;
				muni_total2 = 0.0;
				linha++;
			}
			if (uneg_qb){
				uneg_qb = false;
				addLabel(sheet, 1, linha, "TOTAL " + uneg_nm,  wcfLabelBoldLeft);
				addNumero(sheet, 5, linha, uneg_total1, wcfNumeroBold);
				addNumero(sheet, 6, linha, uneg_total2, wcfNumeroBold);
				uneg_total1 = 0.0;
				uneg_total2 = 0.0;
				linha++;
			}			
			if (greg_qb){
				greg_qb = false;
				addLabel(sheet, 0, linha, "TOTAL " + greg_nm,  wcfLabelBoldLeft);
				addNumero(sheet, 5, linha, greg_total1, wcfNumeroBold);
				addNumero(sheet, 6, linha, greg_total2, wcfNumeroBold);
				greg_total1 = 0.0;
				greg_total2 = 0.0;
				linha++;
			}

			if (!greg_nm.equals(rs.getString("greg_nmregional"))){
				if (!greg_nm.equals("")) {
					greg_qb = true; uneg_qb = true; muni_qb = true; loca_qb = true;
				}
				addLabel(sheet, 0, linha, rs.getString("greg_nmregional"), wcfLabelBoldLeft);
				linha++;
			}

			if (!uneg_nm.equals(rs.getString("uneg_nmunidadenegocio"))){
				if (!uneg_qb.equals("")){
					uneg_qb = true; muni_qb = true; loca_qb = true;
				}
				addLabel(sheet, 1, linha, rs.getString("uneg_nmunidadenegocio"), wcfLabelBoldLeft);
				linha++;
			}

			if (!muni_nm.equals(rs.getString("muni_nmmunicipio"))){
				if (!muni_qb.equals("")){
					muni_qb = true;  loca_qb = true;
				}
				addLabel(sheet, 2, linha, rs.getString("muni_nmmunicipio"), wcfLabelBoldLeft);
				linha++;
			}

			if (!loca_nm.equals(rs.getString("loca_nmlocalidade"))){
				if (!loca_qb.equals("")){
					loca_qb = true;
				}
				addLabel(sheet, 3, linha, rs.getString("loca_nmlocalidade"), wcfLabelBoldLeft);
				linha++;
			}
			
			addLabel(sheet, 4, linha, formataDataPadrao.format(formataDataSQL.parse(rs.getString("etev_referencia"))) , wcfLabel);
			valor1 = Double.parseDouble(rs.getString("etev_volumecoletado"));
			valor2 = Double.parseDouble(rs.getString("etev_volumetratado"));
			addNumero(sheet, 5, linha, valor1, wcfNumero);
			addNumero(sheet, 6, linha, valor2, wcfNumero);
			total1 += valor1; total2 += valor2;
			greg_total1 += valor1; greg_total2 += valor2; 
			uneg_total1 += valor1; uneg_total2 += valor2;
			muni_total1 += valor1; muni_total2 += valor2;
			loca_total1 += valor1; loca_total2 += valor2;
			greg_nm = rs.getString("greg_nmregional");
			uneg_nm = rs.getString("uneg_nmunidadenegocio");
			muni_nm = rs.getString("muni_nmmunicipio");
			loca_nm = rs.getString("loca_nmlocalidade");
			linha++;			
		}
		//TOTALIZADOR GERAL
		addLabel(sheet, 3, linha, "TOTAL " + loca_nm,  wcfLabelBoldLeft);
		addNumero(sheet, 5, linha, loca_total1, wcfNumeroBold);
		addNumero(sheet, 6, linha, loca_total2, wcfNumeroBold);
		linha++;
		addLabel(sheet, 2, linha, "TOTAL " + muni_nm,  wcfLabelBoldLeft);
		addNumero(sheet, 5, linha, muni_total1, wcfNumeroBold);
		addNumero(sheet, 6, linha, muni_total2, wcfNumeroBold);
		linha++;
		addLabel(sheet, 1, linha, "TOTAL " + uneg_nm,  wcfLabelBoldLeft);
		addNumero(sheet, 5, linha, uneg_total1, wcfNumeroBold);
		addNumero(sheet, 6, linha, uneg_total2, wcfNumeroBold);
		linha++;
		addLabel(sheet, 0, linha, "TOTAL " + greg_nm,  wcfLabelBoldLeft);
		addNumero(sheet, 5, linha, greg_total1, wcfNumeroBold);
		addNumero(sheet, 6, linha, greg_total2, wcfNumeroBold);
		linha++;		
		linha++;
		addLabel(sheet, 0, linha, "TOTAL GERAL",  wcfLabelBoldLeft);
		addNumero(sheet, 5, linha, total1, wcfNumeroBold);
		addNumero(sheet, 6, linha, total2, wcfNumeroBold);
		//REALIZA DOWNLOAD DA PLANILHA
    	downloadFile(wb);
    }
}


