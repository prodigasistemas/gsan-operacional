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
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RelatorioGerencial;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@ManagedBean
@SessionScoped
public class RelatorioCadastroEEBean extends BaseRelatorioBean<RelatorioGerencial> {

	@Resource(lookup="java:/gopera")
	private DataSource	dataSource;

	@EJB
	private IProxy fachadaProxy;
	
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
	
	@Override
	public String iniciar(){
		//ENERGIA ELÉTRICA
		tipoRelatorio = 1;
		tipoExportacao = 1;	
		listaRelatorio.clear();
		SelectItem tipoRel = new SelectItem();  
		tipoRel.setValue(1);  
		tipoRel.setLabel("Unidade Consumidora");  
		listaRelatorio.add(tipoRel);
		tipoRel = new SelectItem();
		tipoRel.setValue(2);  
		tipoRel.setLabel("Contrato de Energia Elétrica");  
		listaRelatorio.add(tipoRel);
		return "RelatorioCadastroEE.jsf";
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
			case 1: //Unidade Consumidora
				nomeRelatorio = "Cadastro de Unidade Consumidora";
				nomeArquivo = "cadastroUnidadeConsumidora";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/" + nomeArquivo + ".jasper");
				sql = "SELECT ucon_id, ucon_uc, ucon_nmconsumidora, B.uneg_nmunidadenegocio, C.muni_nmmunicipio, D.loca_nmlocalidade,"
				    + "		  ucon_undoperacional, ucon_natureza, ucon_endereco, ucon_endereconumero, ucon_enderecocomplemento,"
				    + "		  ucon_bairro, ucon_cep, ucon_equipamento, ucon_dtinstalacao, ucon_potencia, ucon_alimentador"
				    + "  FROM operacao.unidade_consumidora A"
				    + " INNER JOIN cadastro.unidade_negocio B ON A.uneg_id = B.uneg_id"
				    + " INNER JOIN cadastro.municipio C ON A.muni_id = C.muni_id"
				    + " INNER JOIN cadastro.localidade D ON A.loca_id = D.loca_id"
				    + " WHERE 1 = 1 " + sqlFiltro
				    + " ORDER BY uneg_nmunidadenegocio, muni_nmmunicipio, loca_nmlocalidade, ucon_uc";
				break;
			case 2: //Contrato de Energia Eletrica
				nomeRelatorio = "Cadastro de Contrato de Energia Elétrica";
				nomeArquivo = "cadastroContratoEnergiaEletrica";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/" + nomeArquivo + ".jasper");
				sql = "SELECT cene_id, cene_nmcontrato, ucon_uc, ucon_nmconsumidora, cene_dataini, cene_datafim, cene_dataassinatura,"
					+ "		  cene_periodoajuste, cene_periodoteste, cene_tensaonominal, cene_tensaocontratada, cene_subgrupotarifario, cene_frequencia," 
					+ "		  cene_perdastransformacao, cene_potenciainstalada, cene_horariopontaini, cene_horariopontafim, cene_horarioreservadoini, cene_horarioreservadofim,"
					+ "		  cene_opcaofaturamento, cene_modalidadetarifaria, cene_agrupadorfatura"
				    + "  FROM operacao.contrato_energia B"
				    + " INNER JOIN operacao.unidade_consumidora A ON A.ucon_id = B.ucon_id"
				    + " WHERE 1 = 1 " + sqlFiltro
				    + " ORDER BY ucon_uc";
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
		SimpleDateFormat formataDataMes = new SimpleDateFormat("MM/yyyy");;
		Boolean headerLinha = false;
    	//GERA PLANILHA 
 		super.nomeArquivo = nomeArquivo; 
 		super.iniciar();
 		WritableWorkbook wb = geraPlanilha();
 		WritableSheet sheet = wb.getSheet(0);
 		sheet.setName(nomeRelatorio);
 		
		switch (tipoRelatorio) {
		case 1: //CADASTRO DE UNIDADE CONSUMIDORA
			//Titulo			
			addLabel(sheet, 0, 0, "CADASTRO DE UNIDADE CONSUMIDORA", wcfLabelBold);
			sheet.mergeCells(0, 0, 6, 0);
			//Filtro
			addLabel(sheet, 0, 1, filtroRelatorio, wcfLabelLeft);
			sheet.mergeCells(0, 1, 6, 1);
			
			//Dados
			while (rs.next()) {
				addLabel(sheet, 0, linha, bundle.getText("nome") + ":", wcfLabelHeader);
				addLabel(sheet, 1, linha, rs.getString("ucon_nmconsumidora") , wcfLabelHeader);
				addLabel(sheet, 2, linha, bundle.getText("codigo_uc") + ":", wcfLabelHeader);
				addLabel(sheet, 3, linha, rs.getString("ucon_uc") , wcfLabelHeader);
				linha++;				
				addLabel(sheet, 1, linha, bundle.getText("unidade_negocio") + ":", wcfLabelLeft);
				addLabel(sheet, 2, linha, rs.getString("uneg_nmunidadenegocio") , wcfLabelLeft);
				addLabel(sheet, 3, linha, bundle.getText("municipio") + ":", wcfLabelLeft);
				addLabel(sheet, 4, linha, rs.getString("muni_nmmunicipio") , wcfLabelLeft);
				addLabel(sheet, 5, linha, bundle.getText("localidade") + ":", wcfLabelLeft);
				addLabel(sheet, 6, linha, rs.getString("loca_nmlocalidade") , wcfLabelLeft);
				linha++;
				addLabel(sheet, 1, linha, "Unidade Administrativa:", wcfLabelLeft);
				addLabel(sheet, 2, linha, rs.getString("ucon_undoperacional") , wcfLabelLeft);
				addLabel(sheet, 5, linha, "Natureza Atividade:", wcfLabelLeft);
				addLabel(sheet, 6, linha, rs.getString("ucon_natureza") , wcfLabelLeft);
				linha++;
				addLabel(sheet, 1, linha, "Endereço:", wcfLabelLeft);
				addLabel(sheet, 2, linha, rs.getString("ucon_endereco") , wcfLabelLeft);
				addLabel(sheet, 5, linha, "Número:", wcfLabelLeft);
				addLabel(sheet, 6, linha, rs.getString("ucon_endereconumero") , wcfLabelLeft);
				linha++;				
				addLabel(sheet, 1, linha, "Complemento:", wcfLabelLeft);
				addLabel(sheet, 2, linha, rs.getString("ucon_enderecocomplemento") , wcfLabelLeft);
				addLabel(sheet, 3, linha, "Bairro:", wcfLabelLeft);
				addLabel(sheet, 4, linha, rs.getString("ucon_bairro") , wcfLabelLeft);
				addLabel(sheet, 5, linha, "CEP:", wcfLabelLeft);
				addLabel(sheet, 6, linha, rs.getString("ucon_cep") , wcfLabelLeft);
				linha++;
				addLabel(sheet, 1, linha, "Nº Equipamento:", wcfLabelLeft);
				addLabel(sheet, 2, linha, rs.getString("ucon_equipamento") , wcfLabelLeft);
				addLabel(sheet, 5, linha, "Data Instalação:", wcfLabelLeft);
				if (rs.getString("ucon_dtinstalacao") != null){
					addLabel(sheet, 6, linha, formataDataPadrao.format(formataDataSQL.parse(rs.getString("ucon_dtinstalacao"))) , wcfLabelLeft);
				}	
				linha++;
				addLabel(sheet, 1, linha, "Potência Banco Kva:", wcfLabelLeft);
				addLabel(sheet, 2, linha, rs.getString("ucon_potencia") , wcfLabelLeft);
				addLabel(sheet, 5, linha, "Alimentador:", wcfLabelLeft);
				addLabel(sheet, 6, linha, rs.getString("ucon_alimentador") , wcfLabelLeft);
				linha++;				
				//Unidade Operacionais
				sql = "SELECT C.unop_nome, eeab_nome AS ucop_nome, A.ucop_percentual"
					+ "  FROM operacao.unidade_consumidora_operacional A"
					+ "	  INNER JOIN operacao.eeab B ON A.ucop_idoperacional = B.eeab_id"
					+ "	  INNER JOIN operacao.tipo_unidade_operacional C ON A.ucop_tipooperacional = C.unop_tipo"
					+ "	  WHERE A.ucop_tipooperacional = 1"
					+ "	    AND A.ucon_id = " + rs.getString("ucon_id")
					+ "	  UNION ALL"
					+ "	  SELECT C.unop_nome, eta_nome AS ucop_nome, A.ucop_percentual"
					+ "	   FROM operacao.unidade_consumidora_operacional A"
					+ "	  INNER JOIN operacao.eta B ON A.ucop_idoperacional = B.eta_id"
					+ "	  INNER JOIN operacao.tipo_unidade_operacional C ON A.ucop_tipooperacional = C.unop_tipo"
					+ "	  WHERE A.ucop_tipooperacional = 2"
					+ "	    AND A.ucon_id = " + rs.getString("ucon_id")
					+ "	   UNION ALL"
					+ "	  SELECT C.unop_nome, eeat_nome AS ucop_nome, A.ucop_percentual"
					+ "	   FROM operacao.unidade_consumidora_operacional A"
					+ "	  INNER JOIN operacao.eeat B ON A.ucop_idoperacional = B.eeat_id"
					+ "	  INNER JOIN operacao.tipo_unidade_operacional C ON A.ucop_tipooperacional = C.unop_tipo"
					+ "	  WHERE A.ucop_tipooperacional = 3"
					+ "	    AND A.ucon_id = " + rs.getString("ucon_id")
					+ "	  UNION ALL"
					+ "	  SELECT C.unop_nome, rso_nome AS ucop_nome, A.ucop_percentual"
					+ "	   FROM operacao.unidade_consumidora_operacional A"
					+ "	  INNER JOIN operacao.rso B ON A.ucop_idoperacional = B.rso_id"
					+ "	  INNER JOIN operacao.tipo_unidade_operacional C ON A.ucop_tipooperacional = C.unop_tipo"
					+ "	  WHERE A.ucop_tipooperacional = 4"
					+ "	    AND A.ucon_id = " + rs.getString("ucon_id")
					+ "	  UNION ALL"
					+ "	  SELECT C.unop_nome, resd_nome AS ucop_nome, A.ucop_percentual"
					+ "	   FROM operacao.unidade_consumidora_operacional A"
					+ "	  INNER JOIN operacao.residencia B ON A.ucop_idoperacional = B.resd_id"
					+ "	  INNER JOIN operacao.tipo_unidade_operacional C ON A.ucop_tipooperacional = C.unop_tipo"
					+ "	  WHERE A.ucop_tipooperacional = 5"
					+ "	    AND A.ucon_id = " + rs.getString("ucon_id")
					+ "	  UNION ALL"
					+ "	  SELECT C.unop_nome, escr_nome AS ucop_nome, A.ucop_percentual"
					+ "	   FROM operacao.unidade_consumidora_operacional A"
					+ "	  INNER JOIN operacao.escritorio B ON A.ucop_idoperacional = B.escr_id"
					+ "	  INNER JOIN operacao.tipo_unidade_operacional C ON A.ucop_tipooperacional = C.unop_tipo"
					+ "	  WHERE A.ucop_tipooperacional = 6"
					+ "	    AND A.ucon_id = " + rs.getString("ucon_id")
				    + "  ORDER BY unop_nome, ucop_nome";
				
				ResultSet rsMed = stm.executeQuery(sql);
				headerLinha = false;
				while (rsMed.next()) {
					if (!headerLinha){
						addLabel(sheet, 1, linha, "Unidades Operacionais", wcfLabelBold);
						sheet.mergeCells(1, linha, 3, linha);
						linha++;
						addLabel(sheet, 1, linha, "Tipo Unidade", wcfLabel);
						addLabel(sheet, 2, linha, "Unidade Operacional", wcfLabel);
						addLabel(sheet, 3, linha, "(%) Utilização", wcfLabel);
						linha++;
						headerLinha = true;
					}
					addLabel(sheet, 1, linha, rsMed.getString("unop_nome") , wcfLabel);
					addLabel(sheet, 2, linha, rsMed.getString("ucop_nome") , wcfLabel);
					addNumero(sheet, 3, linha, Double.parseDouble(rsMed.getString("ucop_percentual")) , wcfNumero);
					linha++;
				}
				linha++;
			}

			break;
		case 2: //CADASTRO DE CONTRATO DE ENERGIA ELÉTRICA
			//Titulo			
			addLabel(sheet, 0, 0, "CADASTRO DE CONTRATO DE ENERGIA ELÉTRICA", wcfLabelBold);
			sheet.mergeCells(0, 0, 5, 0);
			//Filtro
			addLabel(sheet, 0, 1, filtroRelatorio, wcfLabelLeft);
			sheet.mergeCells(0, 1, 5, 1);
			//Dados
			while (rs.next()) {
				addLabel(sheet, 0, linha, "Unidade Consumidora:", wcfLabelHeader);
				addLabel(sheet, 1, linha, rs.getString("ucon_uc") , wcfLabelHeader);
				addLabel(sheet, 2, linha, rs.getString("ucon_nmconsumidora") , wcfLabelHeader);
				addLabel(sheet, 3, linha, "Nº Contrato:", wcfLabelHeader);
				addLabel(sheet, 4, linha, rs.getString("cene_nmcontrato") , wcfLabelHeader);
				linha++;				
				addLabel(sheet, 1, linha, "Data Início:", wcfLabelLeft);
				if (rs.getString("cene_dataini") != null){
					addLabel(sheet, 2, linha, formataDataPadrao.format(formataDataSQL.parse(rs.getString("cene_dataini"))) , wcfLabelLeft);
				}
				addLabel(sheet, 3, linha, "Data Fim:", wcfLabelLeft);
				if (rs.getString("cene_datafim") != null){
					addLabel(sheet, 4, linha, formataDataPadrao.format(formataDataSQL.parse(rs.getString("cene_datafim"))) , wcfLabelLeft);
				}	
				linha++;
				addLabel(sheet, 1, linha, "Período Ajuste:", wcfLabelLeft);
				addLabel(sheet, 2, linha, rs.getString("cene_periodoajuste"), wcfLabelLeft);				
				addLabel(sheet, 3, linha, "Período Testes:", wcfLabelLeft);
				addLabel(sheet, 4, linha, rs.getString("cene_periodoteste"), wcfLabelLeft);
				linha++;
				addLabel(sheet, 1, linha, "Características Técnicas de Fornecimento", wcfLabelBold);
				sheet.mergeCells(1, linha, 6, linha);
				linha++;
				addLabel(sheet, 1, linha, "Tensão Nominal (kV)", wcfLabel);
				addLabel(sheet, 2, linha, "Tensão Contratada (kV)", wcfLabel);
				addLabel(sheet, 3, linha, "Sub-Grupo Tarifário", wcfLabel);
				addLabel(sheet, 4, linha, "Frequência (Hz)", wcfLabel);
				addLabel(sheet, 5, linha, "Perdas Transformação (%)", wcfLabel);
				addLabel(sheet, 6, linha, "Potência Instalada (kVA)", wcfLabel);
				linha++;
				addNumero(sheet, 1, linha, Double.parseDouble(rs.getString("cene_tensaonominal")) , wcfNumero);
				addNumero(sheet, 2, linha, Double.parseDouble(rs.getString("cene_tensaocontratada")) , wcfNumero);
				addLabel(sheet, 3, linha, rs.getString("cene_subgrupotarifario") , wcfLabel);
				addNumero(sheet, 4, linha, Double.parseDouble(rs.getString("cene_frequencia")) , wcfNumero);
				addNumero(sheet, 5, linha, Double.parseDouble(rs.getString("cene_perdastransformacao")) , wcfNumero);
				addNumero(sheet, 6, linha, Double.parseDouble(rs.getString("cene_potenciainstalada")) , wcfNumero);
				linha++;
				addLabel(sheet, 1, linha, "Horários", wcfLabelBold);
				sheet.mergeCells(1, linha, 4, linha);
				linha++;
				addLabel(sheet, 1, linha, "Ponta Inicial", wcfLabel);
				addLabel(sheet, 2, linha, "Ponta Final", wcfLabel);
				addLabel(sheet, 3, linha, "Reservado Inicial", wcfLabel);
				addLabel(sheet, 4, linha, "Reservado Final", wcfLabel);
				linha++;
				addLabel(sheet, 1, linha, rs.getString("cene_horariopontaini") , wcfLabel);
				addLabel(sheet, 2, linha, rs.getString("cene_horariopontafim") , wcfLabel);
				addLabel(sheet, 3, linha, rs.getString("cene_horarioreservadoini") , wcfLabel);
				addLabel(sheet, 4, linha, rs.getString("cene_horarioreservadofim") , wcfLabel);
				linha++;
				addLabel(sheet, 1, linha, "Opção Faturamento:", wcfLabelLeft);
				addLabel(sheet, 2, linha, rs.getString("cene_opcaofaturamento") , wcfLabelLeft);
				addLabel(sheet, 3, linha, "Modalidade Tarifa:", wcfLabelLeft);
				addLabel(sheet, 4, linha, rs.getString("cene_modalidadetarifaria") , wcfLabelLeft);
				addLabel(sheet, 5, linha, "Agrupador Fatura:", wcfLabelLeft);
				addLabel(sheet, 6, linha, rs.getString("cene_agrupadorfatura") , wcfLabelLeft);
				linha++;
				//Demanda Contratada
				sql = "SELECT cend_dataini, cend_datafim, cend_demandasecoponta, cend_demandasecoforaponta, cend_demandaumidoponta, cend_demandaumidoforaponta"
					+ "  FROM operacao.contrato_energia_demanda"
					+ "	WHERE cene_id = " + rs.getString("cene_id")
					+ "	ORDER BY cend_dataini";
				
				ResultSet rsMed = stm.executeQuery(sql);
				headerLinha = false;
				while (rsMed.next()) {
					if (!headerLinha){
						addLabel(sheet, 1, linha, "Período de Faturamento / Demanda Contratada", wcfLabelBold);
						sheet.mergeCells(1, linha, 6, linha);
						linha++;
						addLabel(sheet, 3, linha, "Período Seco", wcfLabel);
						sheet.mergeCells(3, linha, 4, linha);
						addLabel(sheet, 5, linha, "Período Úmido", wcfLabel);
						sheet.mergeCells(5, linha, 6, linha);
						linha++;
						addLabel(sheet, 1, linha, "Período Inicial", wcfLabel);
						addLabel(sheet, 2, linha, "Período Final", wcfLabel);
						addLabel(sheet, 3, linha, "Ponta (Kw)", wcfLabel);
						addLabel(sheet, 4, linha, "Fora da Ponta (Kw)", wcfLabel);
						addLabel(sheet, 5, linha, "Ponta (Kw)", wcfLabel);
						addLabel(sheet, 6, linha, "Fora da Ponta (Kw)", wcfLabel);						
						linha++;
						headerLinha = true;
					}
					if (rsMed.getString("cend_dataini") != null){
						addLabel(sheet, 1, linha, formataDataMes.format(formataDataSQL.parse(rsMed.getString("cend_dataini"))) , wcfLabel);
					}
					if (rsMed.getString("cend_datafim") != null){
						addLabel(sheet, 2, linha, formataDataMes.format(formataDataSQL.parse(rsMed.getString("cend_datafim"))) , wcfLabel);
					}	
					addLabel(sheet, 3, linha, rsMed.getString("cend_demandasecoponta") , wcfLabel);
					addLabel(sheet, 4, linha, rsMed.getString("cend_demandasecoforaponta") , wcfLabel);
					addLabel(sheet, 5, linha, rsMed.getString("cend_demandaumidoponta") , wcfLabel);
					addLabel(sheet, 6, linha, rsMed.getString("cend_demandaumidoforaponta") , wcfLabel);
					linha++;
				}
				linha++;
			}
			break;
		}	
		//REALIZA DOWNLOAD DA PLANILHA
    	downloadFile(wb);
    }
    
    /*************************************************
     * GETTERS AND SETTERS
     *************************************************/
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
}

