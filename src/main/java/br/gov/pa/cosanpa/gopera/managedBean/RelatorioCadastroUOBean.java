package br.gov.pa.cosanpa.gopera.managedBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.RelatorioGerencial;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;

@ManagedBean
@SessionScoped
public class RelatorioCadastroUOBean extends BaseRelatorioBean<RelatorioGerencial> {

	@Resource(lookup="java:/jboss/datasources/GsanDS")
	private DataSource	dataSource;

	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;
	
	private List<UnidadeNegocioProxy> listaUnidadeNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> listaMunicipio = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> listaLocalidade = new ArrayList<LocalidadeProxy>();
	
	private Integer tipoRelatorio;
	private Integer tipoExportacao;
	private List<SelectItem> listaRelatorio = new ArrayList<SelectItem>();
	private JasperPrint jasperPrint;
	private String nomeRelatorio;
	private String nomeArquivo;
	private String reportPath; 
	private String filtroRelatorio;
	private UnidadeNegocioProxy unidadeNegocioProxy = new UnidadeNegocioProxy();
	private MunicipioProxy municipioProxy = new MunicipioProxy();
	private LocalidadeProxy localidadeProxy = new LocalidadeProxy();

	public List<UnidadeNegocioProxy> getListaUnidadeNegocio() {
		try {
				this.listaUnidadeNegocio =  fachadaProxy.getListaUnidadeNegocio(0);
				return listaUnidadeNegocio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		return listaUnidadeNegocio = new ArrayList<UnidadeNegocioProxy>();
	}

	public List<MunicipioProxy> getListaMunicipio() {
		if (this.getUnidadeNegocioProxy().getCodigo() != null) {
			try {
				this.listaMunicipio =  fachadaProxy.getListaMunicipio(0, this.getUnidadeNegocioProxy().getCodigo());
				return this.listaMunicipio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		return this.listaMunicipio = new ArrayList<MunicipioProxy>();
	}

	public List<LocalidadeProxy> getListaLocalidade() {
		if (this.getMunicipioProxy().getCodigo() != null) {
			try {
				this.listaLocalidade =  fachadaProxy.getListaLocalidade(0, this.getUnidadeNegocioProxy().getCodigo(), this.getMunicipioProxy().getCodigo());
				return this.listaLocalidade;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		return this.listaLocalidade= new ArrayList<LocalidadeProxy>();
	}
	
	public Integer getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(Integer tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public Integer getTipoExportacao() {
		return tipoExportacao;
	}

	public void setTipoExportacao(Integer tipoExportacao) {
		this.tipoExportacao = tipoExportacao;
	}

	public List<SelectItem> getListaRelatorio() {
		return listaRelatorio;
	}

	public void setListaRelatorio(List<SelectItem> listaRelatorio) {
		this.listaRelatorio = listaRelatorio;
	}


	public UnidadeNegocioProxy getUnidadeNegocioProxy() {
		return unidadeNegocioProxy;
	}

	public void setUnidadeNegocioProxy(UnidadeNegocioProxy unidadeNegocioProxy) {
		this.unidadeNegocioProxy = unidadeNegocioProxy;
	}

	public MunicipioProxy getMunicipioProxy() {
		return municipioProxy;
	}

	public void setMunicipioProxy(MunicipioProxy municipioProxy) {
		this.municipioProxy = municipioProxy;
	}

	public LocalidadeProxy getLocalidadeProxy() {
		return localidadeProxy;
	}

	public void setLocalidadeProxy(LocalidadeProxy localidadeProxy) {
		this.localidadeProxy = localidadeProxy;
	}

	
	public String iniciar(){
		//UNIDADES OPERACIONAIS
		tipoRelatorio = 1;
		tipoExportacao = 1;
		listaRelatorio.clear();
		SelectItem tipoRel = new SelectItem();  
		tipoRel.setValue(1);  
		tipoRel.setLabel("EAB");  
		listaRelatorio.add(tipoRel);
		tipoRel = new SelectItem();
		tipoRel.setValue(2);  
		tipoRel.setLabel("ETA");  
		listaRelatorio.add(tipoRel);
		tipoRel = new SelectItem();
		tipoRel.setValue(3);  
		tipoRel.setLabel("EAT");  
		listaRelatorio.add(tipoRel);		
		tipoRel = new SelectItem();
		tipoRel.setValue(4);  
		tipoRel.setLabel("RSO");  
		listaRelatorio.add(tipoRel);
		tipoRel = new SelectItem();
		tipoRel.setValue(5);  
		tipoRel.setLabel("ETE");  
		listaRelatorio.add(tipoRel);		
		return "RelatorioCadastroUO.jsf";
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
			if (unidadeNegocioProxy.getCodigo() != 0) {
				sqlFiltro = sqlFiltro + " AND A.uneg_id = " + unidadeNegocioProxy.getCodigo();
	    		filtroRelatorio = filtroRelatorio + " Und Negócio: " + fachadaProxy.getUnidadeNegocio(this.unidadeNegocioProxy.getCodigo()).getNome();
			}
			if (municipioProxy.getCodigo() != 0) {
				sqlFiltro = sqlFiltro + " AND A.muni_id = " + municipioProxy.getCodigo();
	    		filtroRelatorio = filtroRelatorio + " Município: " + fachadaProxy.getMunicipio(this.municipioProxy.getCodigo()).getNome();
			}
			if (localidadeProxy.getCodigo() != 0) {
				sqlFiltro = sqlFiltro + " AND A.loca_id = " + localidadeProxy.getCodigo();
	    		filtroRelatorio = filtroRelatorio + " Localidade: " + fachadaProxy.getLocalidade(this.localidadeProxy.getCodigo()).getNome();
			}    		
    		
			switch (tipoRelatorio) {
			case 1: //EAB
				nomeRelatorio = "Cadastro de EAB";
				nomeArquivo = "cadastroEAB";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/" + nomeArquivo + ".jasper");
				sql = "SELECT DISTINCT eeab_id, eeab_nome, eeab_sabs, eeab_volumeutil, eeab_alturautil, eeab_capacidade,"
					+ "			       eeab_cmb, eeab_cmbmodelo, eeab_cmbvazao, eeab_cmbpotencia, eeab_cmbmca"
					+ "  FROM operacao.eeab D"
					+ "  LEFT JOIN operacao.unidade_consumidora_operacional C ON C.ucop_idoperacional = D.eeab_id AND C.ucop_tipooperacional = 1"
					+ "  LEFT JOIN operacao.unidade_consumidora B ON B.ucon_id = C.ucon_id "
					+ "  LEFT JOIN (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," 
					+ "				   		        E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade"
					+ "	  	          FROM cadastro.gerencia_regional A "
					+ "			     INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id" 
					+ "			     INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID" 
					+ "				 INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id "
					+ "				 INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A ON A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id"
					+ " WHERE 1 = 1" + sqlFiltro
				    + " ORDER BY eeab_nome ";
				break;
			case 2: //ETA
				nomeRelatorio = "Cadastro de ETA";
				nomeArquivo = "cadastroETA";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/" + nomeArquivo + ".jasper");
				sql = "SELECT DISTINCT eta_id, eta_nome, eta_volumeutil, eta_alturautil, eta_capacidade,"
					+ "       eta_cmb, eta_cmbmodelo, eta_cmbvazao, eta_cmbpotencia, eta_cmbmca"
					+ "  FROM operacao.eta D"
					+ "  LEFT JOIN operacao.unidade_consumidora_operacional C ON C.ucop_idoperacional = D.eta_id AND C.ucop_tipooperacional = 2"
					+ "  LEFT JOIN operacao.unidade_consumidora B ON B.ucon_id = C.ucon_id "
					+ "  LEFT JOIN (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," 
					+ "				   		        E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade"
					+ "	  	          FROM cadastro.gerencia_regional A "
					+ "			     INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id" 
					+ "			     INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID" 
					+ "				 INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id "
					+ "				 INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A ON A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id"
					+ " WHERE 1 = 1" + sqlFiltro
					+ " ORDER BY eta_nome";
				break;
			case 3: //EAT
				nomeRelatorio = "Cadastro de EAT";
				nomeArquivo = "cadastroEAT";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/" + nomeArquivo + ".jasper");
				sql = "SELECT DISTINCT eeat_id, eeat_nome, CASE eeat_tipo WHEN 1 THEN 'Elevada' WHEN 2 THEN 'Enterrada' WHEN 3 THEN 'Apoiada' END AS eeat_tipo," 
				    + "       eeat_volumeutil, eeat_alturautil, eeat_capacidade,"
				    + "       eeat_cmb, eeat_cmbmodelo, eeat_cmbvazao, eeat_cmbpotencia, eeat_cmbmca"
				    + "  FROM operacao.eeat D"
					+ "  LEFT JOIN operacao.unidade_consumidora_operacional C ON C.ucop_idoperacional = D.eeat_id AND C.ucop_tipooperacional = 3"
					+ "  LEFT JOIN operacao.unidade_consumidora B ON B.ucon_id = C.ucon_id "
					+ "  LEFT JOIN (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," 
					+ "				   		        E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade"
					+ "	  	          FROM cadastro.gerencia_regional A "
					+ "			     INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id" 
					+ "			     INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID" 
					+ "				 INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id "
					+ "				 INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A ON A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id"
					+ " WHERE 1 = 1" + sqlFiltro
				    + " ORDER BY eeat_nome";
				break;
			case 4: //RSO
				nomeRelatorio = "Cadastro de RSO";
				nomeArquivo = "cadastroRSO";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/" + nomeArquivo + ".jasper");
				sql = "SELECT DISTINCT rso_id, rso_nome, eeat_nome,"
			        + "	      rso_volumeutil, rso_alturautil, rso_capacidade,"
			        + "		  rso_cmb, rso_cmbmodelo, rso_cmbvazao, rso_cmbpotencia, rso_cmbmca,"
			        + "	      mmed_idleitura, mmed_dtinstalacao, mmed_tag"
					+ "  FROM operacao.rso D"
					+ " INNER JOIN operacao.eeat E ON D.eeat_id = E.eeat_id" 
					+ " INNER JOIN operacao.macro_medidor F ON D.mmed_identrada = F.mmed_id"
					+ "  LEFT JOIN operacao.unidade_consumidora_operacional C ON C.ucop_idoperacional = D.rso_id AND C.ucop_tipooperacional = 4"
					+ "  LEFT JOIN operacao.unidade_consumidora B ON B.ucon_id = C.ucon_id "
					+ "  LEFT JOIN (SELECT DISTINCT A.greg_id, A.greg_nmregional, B.uneg_id, B.uneg_nmunidadenegocio," 
					+ "				   		        E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade"
					+ "	  	          FROM cadastro.gerencia_regional A "
					+ "			     INNER JOIN cadastro.unidade_negocio B ON A.greg_id = B.greg_id" 
					+ "			     INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND B.uneg_id = C.uneg_ID" 
					+ "				 INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id "
					+ "				 INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A ON A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id"
					+ " WHERE 1 = 1" + sqlFiltro					
					+ " ORDER BY rso_nome";
				break;
			case 5: //ETE
				nomeRelatorio = "Cadastro de ETE";
				nomeArquivo = "cadastroETE";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/" + nomeArquivo + ".jasper");
				sql = "SELECT ete_id, ete_nome"
					+ "  FROM operacao.ete A"
					+ " WHERE 1 = 1" + sqlFiltro
					+ " ORDER BY ete_nome";
				break;				
			}
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
			        parametros.put("filtro", filtroRelatorio);
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
    	Integer linha = 3;
    	String sql = "";
    	Connection con =  dataSource.getConnection();
		Statement stm = con.createStatement();
		SimpleDateFormat formataDataSQL = new SimpleDateFormat("yyyy-MM-dd");;
		SimpleDateFormat formataDataPadrao = new SimpleDateFormat("dd/MM/yyyy");;
		Boolean headerLinha = false;
    	//GERA PLANILHA 
 		super.nomeArquivo = nomeArquivo; 
 		super.iniciar();
 		WritableWorkbook wb = geraPlanilha();
 		WritableSheet sheet = wb.getSheet(0);
	 	
		switch (tipoRelatorio) {
		case 1: //EAB
			//Nome Planilha
			sheet.setName("Cadastro EAB");
			//Titulo			
			addLabel(sheet, 0, 0, "CADASTRO DE EAB", wcfLabelBold);
			sheet.mergeCells(0, 0, 5, 0);
			//Filtro
			addLabel(sheet, 0, 1, filtroRelatorio, wcfLabelLeft);
			sheet.mergeCells(0, 1, 5, 1);
			//Dados
			while (rs.next()) {
				addLabel(sheet, 0, linha, "EAB:", wcfLabelHeader);
				addLabel(sheet, 1, linha, rs.getString("eeab_nome") , wcfLabelHeader);
				addLabel(sheet, 2, linha, "Sistema de Abastecimento:", wcfLabelHeader);
				addLabel(sheet, 3, linha, rs.getString("eeab_sabs") , wcfLabelHeader);
				linha++;				
				addLabel(sheet, 0, linha, bundle.getText("volume_util"), wcfLabel);
				addLabel(sheet, 1, linha, bundle.getText("altura_util"), wcfLabel);
				addLabel(sheet, 2, linha, bundle.getText("capacidade"), wcfLabel);
				linha++;
				addNumero(sheet, 0, linha, Double.parseDouble(rs.getString("eeab_volumeutil")) , wcfNumero);
				addNumero(sheet, 1, linha, Double.parseDouble(rs.getString("eeab_alturautil")) , wcfNumero);
				addNumero(sheet, 2, linha, Double.parseDouble(rs.getString("eeab_capacidade")) , wcfNumero);
				linha++;
				addLabel(sheet, 0, linha, "CMB - Conjunto Motor-Bomba", wcfLabelBold);
				sheet.mergeCells(0, linha, 5, linha);
				linha++;
				addLabel(sheet, 0, linha, bundle.getText("qtd"), wcfLabel);
				addLabel(sheet, 1, linha, bundle.getText("modelo"), wcfLabel);
				addLabel(sheet, 2, linha, bundle.getText("vazao"), wcfLabel);
				addLabel(sheet, 3, linha, bundle.getText("potencia"), wcfLabel);
				addLabel(sheet, 4, linha, "MCA", wcfLabel);
				linha++;
				addNumero(sheet, 0, linha, Double.parseDouble(rs.getString("eeab_cmb")) , wcfInteiro);
				addLabel(sheet, 1, linha, rs.getString("eeab_cmbmodelo") , wcfLabel);
				addNumero(sheet, 2, linha, Double.parseDouble(rs.getString("eeab_cmbvazao")) , wcfNumero);
				addNumero(sheet, 3, linha, Double.parseDouble(rs.getString("eeab_cmbpotencia")) , wcfInteiro);
				addNumero(sheet, 4, linha, Double.parseDouble(rs.getString("eeab_cmbmca")) , wcfNumero);
				linha++;
				//Medidor de Saída
				sql = "SELECT mmed_idleitura, mmed_dtinstalacao, mmed_tag"
					+ "  FROM operacao.eeab_medidor A"
					+ " INNER JOIN operacao.macro_medidor B ON A.mmed_idsaida = B.mmed_id"
					+ " WHERE A.eeab_id = " + rs.getString("eeab_id")
					+ " ORDER BY mmed_idleitura";
				
				ResultSet rsMed = stm.executeQuery(sql);
				headerLinha = false;
				while (rsMed.next()) {
					if (!headerLinha){
						addLabel(sheet, 1, linha, bundle.getText("medidor_saida"), wcfLabelBold);
						sheet.mergeCells(1, linha, 3, linha);
						linha++;
						addLabel(sheet, 1, linha, "Medidor", wcfLabel);
						addLabel(sheet, 2, linha, bundle.getText("data_instalacao"), wcfLabel);
						addLabel(sheet, 3, linha, "TAG", wcfLabel);
						linha++;
						headerLinha = true;
					}
					addLabel(sheet, 1, linha, rsMed.getString("mmed_idleitura") , wcfLabel);
					if (rsMed.getString("mmed_dtinstalacao") != null){
						addLabel(sheet, 2, linha, formataDataPadrao.format(formataDataSQL.parse(rsMed.getString("mmed_dtinstalacao"))) , wcfLabel);
					}	
					addLabel(sheet, 3, linha, rsMed.getString("mmed_tag") , wcfLabel);
					linha++;
				}
				//Fonte de Captação
				sql = "SELECT 'EAB' AS mmed_tipofonte, eeab_nome AS mmed_nmfonte, mmed_idleitura, mmed_dtinstalacao, mmed_tag"
					+ "  FROM operacao.eeab_fontecaptacao A"
					+ " INNER JOIN operacao.macro_medidor B ON A.mmed_identrada = B.mmed_id"
					+ " INNER JOIN operacao.eeab C ON A.eabf_fonte = C.eeab_id"
					+ " WHERE A.eabf_tipofonte = 1"
					+ "   AND A.eeab_id =" + rs.getString("eeab_id")
					+ " UNION ALL"
					+ " SELECT 'EXTERNA' AS mmed_tipofonte, ftcp_dsfontecaptacao AS mmed_nmfonte, B.mmed_idleitura, A.mmed_dtinstalacao, A.mmed_tag"
					+ "   FROM operacao.eeab_fontecaptacao A"
					+ "  INNER JOIN operacao.macro_medidor B ON A.mmed_identrada = B.mmed_id"
					+ "  INNER JOIN operacional.fonte_captacao C ON A.eabf_fonte = C.ftcp_id"
					+ "  WHERE A.eabf_tipofonte = 2"
					+ "    AND A.eeab_id = " + rs.getString("eeab_id")
					+ "	  ORDER BY mmed_tipofonte";
				
				ResultSet rsFonte = stm.executeQuery(sql);
				headerLinha = false;
				while (rsFonte.next()) {
					if (!headerLinha){
						addLabel(sheet, 1, linha, bundle.getText("fonte_captacao"), wcfLabelBold);
						sheet.mergeCells(1, linha, 5, linha);
						linha++;
						addLabel(sheet, 1, linha, "Tipo Fonte", wcfLabel);
						addLabel(sheet, 2, linha, "Nome Fonte", wcfLabel);
						addLabel(sheet, 3, linha, "Medidor", wcfLabel);
						addLabel(sheet, 4, linha, bundle.getText("data_instalacao"), wcfLabel);
						addLabel(sheet, 5, linha, "TAG", wcfLabel);
						linha++;
						headerLinha = true;
					}						
					addLabel(sheet, 1, linha, rsFonte.getString("mmed_tipofonte") , wcfLabel);
					addLabel(sheet, 2, linha, rsFonte.getString("mmed_nmfonte") , wcfLabel);
					addLabel(sheet, 3, linha, rsFonte.getString("mmed_idleitura") , wcfLabel);
					if (rsFonte.getString("mmed_dtinstalacao") != null){
						addLabel(sheet, 4, linha, formataDataPadrao.format(formataDataSQL.parse(rsFonte.getString("mmed_dtinstalacao"))) , wcfLabel);
					}	
					addLabel(sheet, 5, linha, rsFonte.getString("mmed_tag") , wcfLabel);
					linha++;
				}
				linha++;
			}

			break;
		case 2: //ETA
			//Nome Planilha
			sheet.setName("Cadastro ETA");
			//Titulo			
			addLabel(sheet, 0, 0, "CADASTRO DE ETA", wcfLabelBold);
			sheet.mergeCells(0, 0, 5, 0);
			//Filtro
			addLabel(sheet, 0, 1, filtroRelatorio, wcfLabelLeft);
			sheet.mergeCells(0, 1, 5, 1);
			//Dados
			while (rs.next()) {
				addLabel(sheet, 0, linha, "ETA:", wcfLabelHeader);
				addLabel(sheet, 1, linha, rs.getString("eta_nome") , wcfLabelHeader);
				linha++;				
				addLabel(sheet, 1, linha, bundle.getText("volume_util"),  wcfLabel);
				addLabel(sheet, 2, linha, bundle.getText("altura_util"), wcfLabel);
				addLabel(sheet, 3, linha, "Capacidade", wcfLabel);
				linha++;
				addNumero(sheet, 1, linha, Double.parseDouble(rs.getString("eta_volumeutil")) , wcfNumero);
				addNumero(sheet, 2, linha, Double.parseDouble(rs.getString("eta_alturautil")) , wcfNumero);
				addNumero(sheet, 3, linha, Double.parseDouble(rs.getString("eta_capacidade")) , wcfNumero);
				linha++;
				addLabel(sheet, 1, linha, "CMB - Conjunto Motor-Bomba", wcfLabelBold);
				sheet.mergeCells(1, linha, 5, linha);
				linha++;
				addLabel(sheet, 1, linha, "Qtd", wcfLabel);
				addLabel(sheet, 2, linha, "Modelo", wcfLabel);
				addLabel(sheet, 3, linha, bundle.getText("vazao"), wcfLabel);
				addLabel(sheet, 4, linha, bundle.getText("potencia"), wcfLabel);
				addLabel(sheet, 5, linha, "MCA", wcfLabel);
				linha++;
				addNumero(sheet, 1, linha, Double.parseDouble(rs.getString("eta_cmb")) , wcfInteiro);
				addLabel(sheet, 2, linha, rs.getString("eta_cmbmodelo") , wcfLabel);
				addNumero(sheet, 3, linha, Double.parseDouble(rs.getString("eta_cmbvazao")) , wcfNumero);
				addNumero(sheet, 4, linha, Double.parseDouble(rs.getString("eta_cmbpotencia")) , wcfInteiro);
				addNumero(sheet, 5, linha, Double.parseDouble(rs.getString("eta_cmbmca")) , wcfNumero);
				linha++;
				//Medidor de Saída
				sql = "SELECT mmed_idleitura, mmed_dtinstalacao, mmed_tag"
					+ "  FROM operacao.eta_medidor A"
					+ " INNER JOIN operacao.macro_medidor B ON A.mmed_idsaida = B.mmed_id"
					+ " WHERE A.eta_id = " + rs.getString("eta_id")
					+ " ORDER BY mmed_idleitura";
				
				ResultSet rsMed = stm.executeQuery(sql);
				headerLinha = false;
				while (rsMed.next()) {
					if (!headerLinha){
						addLabel(sheet, 1, linha, bundle.getText("medidor_saida"), wcfLabelBold);
						sheet.mergeCells(1, linha, 3, linha);
						linha++;
						addLabel(sheet, 1, linha, "Medidor", wcfLabel);
						addLabel(sheet, 2, linha, bundle.getText("data_instalacao"), wcfLabel);
						addLabel(sheet, 3, linha, "TAG", wcfLabel);
						linha++;
						headerLinha = true;
					}
					addLabel(sheet, 1, linha, rsMed.getString("mmed_idleitura") , wcfLabel);
					if (rsMed.getString("mmed_dtinstalacao") != null){
						addLabel(sheet, 2, linha, formataDataPadrao.format(formataDataSQL.parse(rsMed.getString("mmed_dtinstalacao"))) , wcfLabel);
					}	
					addLabel(sheet, 3, linha, rsMed.getString("mmed_tag") , wcfLabel);
					linha++;
				}
				//Fonte de Captação
				sql = "SELECT eeab_nome AS mmed_nmfonte, B.mmed_idleitura, A.mmed_dtinstalacao, A.mmed_tag"
					+ "  FROM operacao.eta_fontecaptacao A"
					+ " INNER JOIN operacao.macro_medidor B ON A.mmed_identrada = B.mmed_id"
					+ " INNER JOIN operacao.eeab C ON A.eeab_id = C.eeab_id"
					+ "   AND A.eta_id = " + rs.getString("eta_id")
					+ "	 ORDER BY mmed_nmfonte";
				
				ResultSet rsFonte = stm.executeQuery(sql);
				headerLinha = false;
				while (rsFonte.next()) {
					if (!headerLinha){
						addLabel(sheet, 1, linha, bundle.getText("fonte_captacao"), wcfLabelBold);
						sheet.mergeCells(1, linha, 4, linha);
						linha++;
						addLabel(sheet, 1, linha, "Nome Fonte", wcfLabel);
						addLabel(sheet, 2, linha, "Medidor", wcfLabel);
						addLabel(sheet, 3, linha, bundle.getText("data_instalacao"), wcfLabel);
						addLabel(sheet, 4, linha, "TAG", wcfLabel);
						linha++;
						headerLinha = true;
					}						
					addLabel(sheet, 1, linha, rsFonte.getString("mmed_nmfonte") , wcfLabel);
					addLabel(sheet, 2, linha, rsFonte.getString("mmed_idleitura") , wcfLabel);
					if (rsFonte.getString("mmed_dtinstalacao") != null){
						addLabel(sheet, 3, linha, formataDataPadrao.format(formataDataSQL.parse(rsFonte.getString("mmed_dtinstalacao"))) , wcfLabel);
					}	
					addLabel(sheet, 4, linha, rsFonte.getString("mmed_tag") , wcfLabel);
					linha++;
				}
				linha++;
			}

			break;
		case 3: //EAT
			//Nome Planilha
			sheet.setName("Cadastro EAT");
			//Titulo			
			addLabel(sheet, 0, 0, "CADASTRO DE EAT", wcfLabelBold);
			sheet.mergeCells(0, 0, 5, 0);
			//Filtro
			addLabel(sheet, 0, 1, filtroRelatorio, wcfLabelLeft);
			sheet.mergeCells(0, 1, 5, 1);
			//Dados
			while (rs.next()) {
				addLabel(sheet, 0, linha, "EAT:", wcfLabelHeader);
				addLabel(sheet, 1, linha, rs.getString("eeat_nome") , wcfLabelHeader);
				addLabel(sheet, 2, linha, bundle.getText("caracteristica"), wcfLabelHeader);
				addLabel(sheet, 3, linha, rs.getString("eeat_tipo") , wcfLabelHeader);
				linha++;				
				addLabel(sheet, 1, linha, bundle.getText("volume_util"), wcfLabel);
				addLabel(sheet, 2, linha, bundle.getText("altura_util"), wcfLabel);
				addLabel(sheet, 3, linha, "Capacidade", wcfLabel);
				linha++;
				addNumero(sheet, 1, linha, Double.parseDouble(rs.getString("eeat_volumeutil")) , wcfNumero);
				addNumero(sheet, 2, linha, Double.parseDouble(rs.getString("eeat_alturautil")) , wcfNumero);
				addNumero(sheet, 3, linha, Double.parseDouble(rs.getString("eeat_capacidade")) , wcfNumero);
				linha++;
				addLabel(sheet, 1, linha, "CMB - Conjunto Motor-Bomba", wcfLabelBold);
				sheet.mergeCells(1, linha, 5, linha);
				linha++;
				addLabel(sheet, 1, linha, "Qtd", wcfLabel);
				addLabel(sheet, 2, linha, "Modelo", wcfLabel);
				addLabel(sheet, 3, linha, bundle.getText("vazao"), wcfLabel);
				addLabel(sheet, 4, linha, bundle.getText("potencia"), wcfLabel);
				addLabel(sheet, 5, linha, "MCA", wcfLabel);
				linha++;
				addNumero(sheet, 1, linha, Double.parseDouble(rs.getString("eeat_cmb")) , wcfInteiro);
				addLabel(sheet, 2, linha, rs.getString("eeat_cmbmodelo") , wcfLabel);
				addNumero(sheet, 3, linha, Double.parseDouble(rs.getString("eeat_cmbvazao")) , wcfNumero);
				addNumero(sheet, 4, linha, Double.parseDouble(rs.getString("eeat_cmbpotencia")) , wcfInteiro);
				addNumero(sheet, 5, linha, Double.parseDouble(rs.getString("eeat_cmbmca")) , wcfNumero);
				linha++;
				//Medidor de Saída
				sql = "SELECT mmed_idleitura, mmed_dtinstalacao, mmed_tag"
					+ "  FROM operacao.eeat_medidor A"
					+ " INNER JOIN operacao.macro_medidor B ON A.mmed_idsaida = B.mmed_id"
					+ " WHERE A.eeat_id = " + rs.getString("eeat_id")
					+ " ORDER BY mmed_idleitura";
				
				ResultSet rsMed = stm.executeQuery(sql);
				headerLinha = false;
				while (rsMed.next()) {
					if (!headerLinha){
						addLabel(sheet, 1, linha, bundle.getText("medidor_saida"), wcfLabelBold);
						sheet.mergeCells(1, linha, 3, linha);
						linha++;
						addLabel(sheet, 1, linha, "Medidor", wcfLabel);
						addLabel(sheet, 2, linha, bundle.getText("data_instalacao"), wcfLabel);
						addLabel(sheet, 3, linha, "TAG", wcfLabel);
						linha++;
						headerLinha = true;
					}
					addLabel(sheet, 1, linha, rsMed.getString("mmed_idleitura") , wcfLabel);
					if (rsMed.getString("mmed_dtinstalacao") != null){
						addLabel(sheet, 2, linha, formataDataPadrao.format(formataDataSQL.parse(rsMed.getString("mmed_dtinstalacao"))) , wcfLabel);
					}	
					addLabel(sheet, 3, linha, rsMed.getString("mmed_tag") , wcfLabel);
					linha++;
				}
				//Fonte de Captação
				sql = "SELECT 'EAT' AS mmed_tipofonte, eeat_nome AS mmed_nmfonte, B.mmed_idleitura, A.mmed_dtinstalacao, A.mmed_tag"
					+ "  FROM operacao.eeat_fontecaptacao A"
					+ " INNER JOIN operacao.macro_medidor B ON A.mmed_identrada = B.mmed_id"
					+ " INNER JOIN operacao.eeat C ON A.etft_fonte = C.eeat_id"
					+ " WHERE A.etft_tipofonte = 1"
					+ "   AND A.eeat_id = " + rs.getString("eeat_id")
					+ " UNION ALL"
					+ " SELECT 'ETA' AS mmed_tipofonte, eta_nome AS mmed_nmfonte, B.mmed_idleitura, A.mmed_dtinstalacao, A.mmed_tag"
					+ "  FROM operacao.eeat_fontecaptacao A"
					+ " INNER JOIN operacao.macro_medidor B ON A.mmed_identrada = B.mmed_id"
					+ " INNER JOIN operacao.eta C ON A.etft_fonte = C.eta_id"
					+ " WHERE A.etft_tipofonte = 2"
					+ "   AND A.eeat_id = " + rs.getString("eeat_id")
					+ "	 ORDER BY mmed_tipofonte";
				
				ResultSet rsFonte = stm.executeQuery(sql);
				headerLinha = false;
				while (rsFonte.next()) {
					if (!headerLinha){
						addLabel(sheet, 1, linha, bundle.getText("fonte_captacao"), wcfLabelBold);
						sheet.mergeCells(1, linha, 5, linha);
						linha++;
						addLabel(sheet, 1, linha, "Tipo Fonte", wcfLabel);
						addLabel(sheet, 2, linha, "Nome Fonte", wcfLabel);
						addLabel(sheet, 3, linha, "Medidor", wcfLabel);
						addLabel(sheet, 4, linha, bundle.getText("data_instalacao"), wcfLabel);
						addLabel(sheet, 5, linha, "TAG", wcfLabel);
						linha++;
						headerLinha = true;
					}						
					addLabel(sheet, 1, linha, rsFonte.getString("mmed_tipofonte") , wcfLabel);
					addLabel(sheet, 2, linha, rsFonte.getString("mmed_nmfonte") , wcfLabel);
					addLabel(sheet, 3, linha, rsFonte.getString("mmed_idleitura") , wcfLabel);
					if (rsFonte.getString("mmed_dtinstalacao") != null){
						addLabel(sheet, 4, linha, formataDataPadrao.format(formataDataSQL.parse(rsFonte.getString("mmed_dtinstalacao"))) , wcfLabel);
					}	
					addLabel(sheet, 5, linha, rsFonte.getString("mmed_tag") , wcfLabel);
					linha++;
				}
				linha++;
			}
			break;
		case 4: //RSO
			//Nome Planilha
			sheet.setName("Cadastro RSO");
			//Titulo			
			addLabel(sheet, 0, 0, "CADASTRO DE RSO", wcfLabelBold);
			sheet.mergeCells(0, 0, 5, 0);
			//Filtro
			addLabel(sheet, 0, 1, filtroRelatorio, wcfLabelLeft);
			sheet.mergeCells(0, 1, 5, 1);
			//Dados
			while (rs.next()) {
				addLabel(sheet, 0, linha, "RSO:", wcfLabelHeader);
				addLabel(sheet, 1, linha, rs.getString("rso_nome") , wcfLabelHeader);
				addLabel(sheet, 2, linha, bundle.getText("estacao_agua_tratada"), wcfLabelHeader);
				addLabel(sheet, 3, linha, rs.getString("eeat_nome") , wcfLabelHeader);
				linha++;				
				addLabel(sheet, 1, linha, bundle.getText("volume_util"), wcfLabel);
				addLabel(sheet, 2, linha, bundle.getText("altura_util"), wcfLabel);
				addLabel(sheet, 3, linha, "Capacidade", wcfLabel);
				linha++;
				addNumero(sheet, 1, linha, Double.parseDouble(rs.getString("rso_volumeutil")) , wcfNumero);
				addNumero(sheet, 2, linha, Double.parseDouble(rs.getString("rso_alturautil")) , wcfNumero);
				addNumero(sheet, 3, linha, Double.parseDouble(rs.getString("rso_capacidade")) , wcfNumero);
				linha++;
				addLabel(sheet, 1, linha, "CMB - Conjunto Motor-Bomba", wcfLabelBold);
				sheet.mergeCells(1, linha, 5, linha);
				linha++;
				addLabel(sheet, 1, linha, "Qtd", wcfLabel);
				addLabel(sheet, 2, linha, "Modelo", wcfLabel);
				addLabel(sheet, 3, linha, bundle.getText("vazao"), wcfLabel);
				addLabel(sheet, 4, linha, bundle.getText("potencia"), wcfLabel);
				addLabel(sheet, 5, linha, "MCA", wcfLabel);
				linha++;
				addNumero(sheet, 1, linha, Double.parseDouble(rs.getString("rso_cmb")) , wcfInteiro);
				addLabel(sheet, 2, linha, rs.getString("rso_cmbmodelo") , wcfLabel);
				addNumero(sheet, 3, linha, Double.parseDouble(rs.getString("rso_cmbvazao")) , wcfNumero);
				addNumero(sheet, 4, linha, Double.parseDouble(rs.getString("rso_cmbpotencia")) , wcfInteiro);
				addNumero(sheet, 5, linha, Double.parseDouble(rs.getString("rso_cmbmca")) , wcfNumero);
				linha++;
				addLabel(sheet, 1, linha, bundle.getText("medidor_saida"), wcfLabelBold);
				sheet.mergeCells(1, linha, 3, linha);
				linha++;
				addLabel(sheet, 1, linha, "Medidor", wcfLabel);
				addLabel(sheet, 2, linha, bundle.getText("data_instalacao"), wcfLabel);
				addLabel(sheet, 3, linha, "TAG", wcfLabel);
				addLabel(sheet, 1, linha, rs.getString("mmed_idleitura") , wcfLabel);
				if (rs.getString("mmed_dtinstalacao") != null){
					addLabel(sheet, 2, linha, formataDataPadrao.format(formataDataSQL.parse(rs.getString("mmed_dtinstalacao"))) , wcfLabel);
				}	
				addLabel(sheet, 3, linha, rs.getString("mmed_tag") , wcfLabel);
				linha++;
			}
			break;
		case 5: //ETA
			//Nome Planilha
			sheet.setName("Cadastro ETE");
			//Titulo			
			addLabel(sheet, 0, 0, "CADASTRO DE ETE", wcfLabelBold);
			sheet.mergeCells(0, 0, 5, 0);
			//Filtro
			addLabel(sheet, 0, 1, filtroRelatorio, wcfLabelLeft);
			sheet.mergeCells(0, 1, 5, 1);
			//Dados
			while (rs.next()) {
				addLabel(sheet, 0, linha, "ETE:", wcfLabelBoldLeft);
				addLabel(sheet, 1, linha, rs.getString("ete_nome") , wcfLabelLeft);
				linha++;
			}
			break;
		}	
		//REALIZA DOWNLOAD DA PLANILHA
    	downloadFile(wb);
    }
}

