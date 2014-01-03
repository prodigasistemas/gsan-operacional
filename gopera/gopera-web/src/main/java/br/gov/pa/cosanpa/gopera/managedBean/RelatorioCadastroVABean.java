package br.gov.pa.cosanpa.gopera.managedBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import br.gov.pa.cosanpa.gopera.model.RelatorioGerencial;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@ManagedBean
@SessionScoped
public class RelatorioCadastroVABean extends BaseRelatorioBean<RelatorioGerencial> {

	@Resource(lookup="java:/gopera")
	private DataSource	dataSource;
	
	private Integer tipoRelatorio;
	private Integer tipoExportacao;
	private List<SelectItem> listaRelatorio = new ArrayList<SelectItem>();
	private JasperPrint jasperPrint;
	private String nomeRelatorio;
	private String nomeArquivo;
	private String reportPath; 
	private String filtroRelatorio;
	
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

	@Override
	public String iniciar(){
		//VOLUME DE ÁGUA	
		tipoRelatorio = 1;
		tipoExportacao = 1;		
		listaRelatorio.clear();
		SelectItem tipoRel = new SelectItem();  
		tipoRel.setValue(1);  
		tipoRel.setLabel("Macro-Medidor");  
		listaRelatorio.add(tipoRel);
		return "RelatorioCadastroVA.jsf";
	}	

    public void exibir() throws SQLException{
    	String sql = "";
    	Connection con = null; 
    	try{
    		con =  dataSource.getConnection();
    		Statement stm = con.createStatement();
    		JRResultSetDataSource jrsds = null;
			switch (tipoRelatorio) {
			case 1: //Macro-Medidor
				nomeRelatorio = "Cadastro de Macro Medidor";
				nomeArquivo = "cadastroMacroMedidor";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/" + nomeArquivo + ".jasper");
				sql = "SELECT mmed_id, CASE mmed_tipo WHEN 1 THEN 'Portátil' WHEN 2 THEN 'Fixo' WHEN 3 THEN 'Virtual' END AS mmed_tipo,"   
					+ "       mmed_idleitura, CASE mmed_tipomedicao WHEN 1 THEN 'Vazão' WHEN 2 THEN 'Pressão' WHEN 3 THEN 'Nível' END AS mmed_tipomedicao,"   
					+ "       mmed_principio, mmed_tiposensor, mmed_fabricante, mmed_modelo, mmed_numeroserie, mmed_tombamento, mmed_range, mmed_grauprotecao,"
					+ "       mmed_alimentacao, mmed_sinalsaida, mmed_protocolo"
					+ "  FROM operacao.macro_medidor"
					+ " ORDER BY mmed_idleitura";
				break;
			}
			//Monta ResultSet	
			ResultSet rs = stm.executeQuery(sql);
			switch (tipoExportacao) {
				case 1: //PDF
					jrsds = new JRResultSetDataSource(rs);
					//Preenche Parâmetros	
					Map<String, Object> parametros = new HashMap<String, Object>();
		 	        parametros.put("logoRelatorio", "logoRelatorio.jpg");
		 	        parametros.put("nomeRelatorio", nomeRelatorio);
			        parametros.put("nomeUsuario", usuarioProxy.getNome());
			        
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
 		sheet.setName(nomeRelatorio);
 		
		switch (tipoRelatorio) {
		case 1: //CADASTRO DE MACRO-MEDIDOR
			//Titulo			
			addLabel(sheet, 0, 0, "CADASTRO DE MACRO-MEDIDOR", wcfLabelBold);
			sheet.mergeCells(0, 0, 6, 0);
			//Filtro
			addLabel(sheet, 0, 1, filtroRelatorio, wcfLabelLeft);
			sheet.mergeCells(0, 1, 6, 1);
			
			//Dados
			while (rs.next()) {
				addLabel(sheet, 0, linha, "Identificador:", wcfLabelHeader);
				addLabel(sheet, 1, linha, rs.getString("mmed_idleitura") , wcfLabelHeader);
				addLabel(sheet, 2, linha, "Tipo de Medidor:", wcfLabelHeader);
				addLabel(sheet, 3, linha, rs.getString("mmed_tipo") , wcfLabelHeader);
				linha++;				
				addLabel(sheet, 1, linha, "Tipo Medição:", wcfLabelLeft);
				addLabel(sheet, 2, linha, rs.getString("mmed_tipomedicao") , wcfLabelLeft);
				addLabel(sheet, 3, linha, "Princípio:", wcfLabelLeft);
				addLabel(sheet, 4, linha, rs.getString("mmed_principio") , wcfLabelLeft);
				addLabel(sheet, 5, linha, "Tipo Sensor:", wcfLabelLeft);
				addLabel(sheet, 6, linha, rs.getString("mmed_tiposensor") , wcfLabelLeft);
				linha++;
				addLabel(sheet, 1, linha, "Fabricante:", wcfLabelLeft);
				addLabel(sheet, 2, linha, rs.getString("mmed_fabricante") , wcfLabelLeft);
				addLabel(sheet, 3, linha, "Modelo:", wcfLabelLeft);
				addLabel(sheet, 4, linha, rs.getString("mmed_modelo") , wcfLabelLeft);
				addLabel(sheet, 5, linha, "Nº de Série:", wcfLabelLeft);
				addLabel(sheet, 6, linha, rs.getString("mmed_numeroserie") , wcfLabelLeft);				
				linha++;
				addLabel(sheet, 1, linha, "Tombamento:", wcfLabelLeft);
				addLabel(sheet, 2, linha, rs.getString("mmed_tombamento") , wcfLabelLeft);
				addLabel(sheet, 3, linha, "Range:", wcfLabelLeft);
				addLabel(sheet, 4, linha, rs.getString("mmed_range") , wcfLabelLeft);
				addLabel(sheet, 5, linha, "Grau de Proteção:", wcfLabelLeft);
				addLabel(sheet, 6, linha, rs.getString("mmed_grauprotecao") , wcfLabelLeft);
				linha++;				
				addLabel(sheet, 1, linha, "Alimentação:", wcfLabelLeft);
				addLabel(sheet, 2, linha, rs.getString("mmed_alimentacao") , wcfLabelLeft);
				addLabel(sheet, 3, linha, "Sinal de Saída:", wcfLabelLeft);
				addLabel(sheet, 4, linha, rs.getString("mmed_sinalsaida") , wcfLabelLeft);
				addLabel(sheet, 5, linha, "Protocolo:", wcfLabelLeft);
				addLabel(sheet, 6, linha, rs.getString("mmed_protocolo") , wcfLabelLeft);
				linha++;
				//Aferições
				sql = "SELECT meda_dtafericao"
					+ "  FROM operacao.macro_medidor_afericao A"
					+ "	WHERE A.mmed_id = " + rs.getString("mmed_id")
				    + " ORDER BY meda_dtafericao";
				
				ResultSet rsMed = stm.executeQuery(sql);
				headerLinha = false;
				while (rsMed.next()) {
					if (!headerLinha){
						addLabel(sheet, 1, linha, "Aferições", wcfLabelBold);
						sheet.mergeCells(1, linha, 3, linha);
						linha++;
						headerLinha = true;
					}
					if (rsMed.getString("meda_dtafericao") != null){
						addLabel(sheet, 1, linha, formataDataPadrao.format(formataDataSQL.parse(rsMed.getString("meda_dtafericao"))) , wcfLabel);
						sheet.mergeCells(1, linha, 3, linha);
					}
					linha++;
				}
				linha++;
			}

			break;
		}	
		//REALIZA DOWNLOAD DA PLANILHA
    	downloadFile(wb);
    }

}

