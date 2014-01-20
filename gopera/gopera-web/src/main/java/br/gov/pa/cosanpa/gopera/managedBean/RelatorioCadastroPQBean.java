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

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.jboss.logging.Logger;

import br.gov.pa.cosanpa.gopera.model.RelatorioGerencial;
import br.gov.pa.cosanpa.gopera.util.WebBundle;

@ManagedBean
@SessionScoped
public class RelatorioCadastroPQBean extends BaseRelatorioBean<RelatorioGerencial> {

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
	
	private Logger logger = Logger.getLogger(RelatorioCadastroPQBean.class);
	
	@Override
	public String iniciar(){
		if (bundle == null){
			bundle = new WebBundle();
		}
		//PRODUTOS QUÍMICOS
		tipoRelatorio = 1;
		tipoExportacao = 1;
		listaRelatorio.clear();
		SelectItem tipoRel = new SelectItem();  
		tipoRel.setValue(1);
		tipoRel.setLabel(bundle.getText("produtos_quimicos")); 
		listaRelatorio.add(tipoRel);
		tipoRel = new SelectItem();
		tipoRel.setValue(2);  
		tipoRel.setLabel(bundle.getText("registro_consumo"));  
		listaRelatorio.add(tipoRel);
		tipoRel = new SelectItem();
		tipoRel.setValue(3);  
		tipoRel.setLabel(bundle.getText("registro_consumo_eta"));  
		listaRelatorio.add(tipoRel);		
		tipoRel = new SelectItem();
		tipoRel.setValue(4);  
		tipoRel.setLabel(bundle.getText("registro_consumo_eat"));  
		listaRelatorio.add(tipoRel);
		tipoRel = new SelectItem();
		tipoRel.setValue(5);  
		tipoRel.setLabel(bundle.getText("tabela_precos")); 
		listaRelatorio.add(tipoRel);		
		return "RelatorioCadastroPQ.jsf";
	}	

    public void exibir() throws SQLException{
    	String sql = "";
    	Connection con = null; 
    	try{
    		con =  dataSource.getConnection();
    		Statement stm = con.createStatement();
    		JRResultSetDataSource jrsds = null;
    		nomeRelatorio = "Cadastro de ";
			switch (tipoRelatorio) {
			case 1: //Produtos Químicos
				nomeRelatorio += bundle.getText("produtos_quimicos");
				nomeArquivo = "cadastroProdutoQuimico";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/cadastroProdutoQuimico.jasper");
				sql = "SELECT prod_id, prod_nmproduto, umed_sigla"
					+ "  FROM operacao.produto A"
					+ " INNER JOIN operacao.unidademedida B ON A.umed_id = B.umed_id"
					+ " ORDER BY prod_nmproduto";
				break;
			case 2: //Registro de Consumo
				nomeRelatorio += bundle.getText("registro_consumo");
				nomeArquivo = "cadastroRegistroConsumo";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/cadastroRegistroConsumo.jasper");
				sql = "SELECT A.regc_id, regc_nmregistro, regc_dataini, regc_datafim, prod_nmproduto"
					+ "  FROM operacao.registroconsumo A"
					+ " INNER JOIN operacao.registroconsumo_produto B ON A.regc_id = B.regc_id"  
					+ " INNER JOIN operacao.produto C ON B.prod_id = C.prod_id"
					+ " ORDER BY A.regc_id, prod_nmproduto";
				break;
			case 3: //Relação Registro de Consumo X ETA
				nomeRelatorio += bundle.getText("registro_consumo_eta");				
				nomeArquivo = "cadastroRegistroConsumoETA";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/cadastroRegistroConsumoETA.jasper");
				sql = "SELECT B.greg_nmregional, C.uneg_nmunidadenegocio, D.muni_nmmunicipio, E.loca_nmlocalidade, F.eta_id, F.eta_nome, H.regc_nmregistro, H.regc_dataini, H.regc_datafim"
					+ "  FROM operacao.registroconsumoeta A"
					+ " INNER JOIN cadastro.gerencia_regional B ON A.greg_id = B.greg_id"
					+ " INNER JOIN cadastro.unidade_negocio C ON A.uneg_id = C.uneg_id"
					+ " INNER JOIN cadastro.municipio D ON A.muni_id = D.muni_id"
					+ " INNER JOIN cadastro.localidade E ON A.loca_id = E.loca_id"
					+ " INNER JOIN operacao.eta F ON A.eta_id = F.eta_id"
					+ " INNER JOIN operacao.registroconsumoeta_registroconsumo G ON A.rgcs_id = G.rgcs_id"
					+ " INNER JOIN operacao.registroconsumo H ON G.regc_id = H.regc_id"
					+ " ORDER BY B.greg_nmregional, C.uneg_nmunidadenegocio, D.muni_nmmunicipio, E.loca_nmlocalidade, F.eta_nome";
				break;
			case 4: //Relação Registro de Consumo x EAT
				nomeRelatorio += bundle.getText("registro_consumo_eat");				
				nomeArquivo = "cadastroRegistroConsumoEAT";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/cadastroRegistroConsumoEAT.jasper");
				sql = "SELECT B.greg_nmregional, C.uneg_nmunidadenegocio, D.muni_nmmunicipio, E.loca_nmlocalidade, F.eeat_id, F.eeat_nome, H.regc_nmregistro, H.regc_dataini, H.regc_datafim"
					+ "  FROM operacao.registroconsumoeat A"
					+ " INNER JOIN cadastro.gerencia_regional B ON A.greg_id = B.greg_id"
					+ " INNER JOIN cadastro.unidade_negocio C ON A.uneg_id = C.uneg_id"
					+ " INNER JOIN cadastro.municipio D ON A.muni_id = D.muni_id"
					+ " INNER JOIN cadastro.localidade E ON A.loca_id = E.loca_id"
					+ " INNER JOIN operacao.eeat F ON A.eat_id = F.eeat_id"
					+ " INNER JOIN operacao.registroconsumoeat_registroconsumo G ON A.rgcs_id = G.rgcs_id"
					+ " INNER JOIN operacao.registroconsumo H ON G.regc_id = H.regc_id"
					+ " ORDER BY B.greg_nmregional, C.uneg_nmunidadenegocio, D.muni_nmmunicipio, E.loca_nmlocalidade, F.eeat_nome";
				break;
			case 5: //Tabela de Preços
				nomeRelatorio += bundle.getText("tabela_precos");				
				nomeArquivo = "cadastroTabelaPreco";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/cadastroTabelaPreco.jasper");
				sql = "SELECT A.tabp_vigencia, C.prod_nmproduto, B.tbpp_preco"
					+ "  FROM operacao.tabelapreco A"
					+ " INNER JOIN operacao.tabelapreco_produto B ON A.tabp_id = B.tabp_id"
					+ " INNER JOIN operacao.produto C ON B.prod_id = C.prod_id"
					+ " WHERE A.tabp_id = (SELECT tabp_id FROM operacao.tabelapreco ORDER BY tabp_vigencia DESC LIMIT 1)"
					+ " ORDER BY C.prod_nmproduto";			
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
					httpServletResponse.setContentType("application/pdf");
					httpServletResponse.setCharacterEncoding("ISO-8859-1");
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
			logger.error(bundle.getText("erro_exibir_relatorio"), e);
			mostrarMensagemErro(bundle.getText("erro_exibir_relatorio"));
		}
		finally {
			con.close();
		}    	
    }
    
    private void ExportaExcel(String nomeArquivo, ResultSet rs) throws Exception{
    	Integer linha = 3;
		SimpleDateFormat formataDataSQL = new SimpleDateFormat("yyyy-MM-dd");;
		SimpleDateFormat formataDataPadrao = new SimpleDateFormat("dd/MM/yyyy");;
		Boolean headerLinha = false;
		Integer id = 0;
    	//GERA PLANILHA 
 		super.nomeArquivo = nomeArquivo; 
 		super.iniciar();
 		WritableWorkbook wb = geraPlanilha();
 		WritableSheet sheet = wb.getSheet(0);
		//Nome Planilha
 		sheet.setName(nomeRelatorio);
	 	
		switch (tipoRelatorio) {
		case 1: //Produto Químico
			//Titulo			
			addLabel(sheet, 0, 0, bundle.getText("rel_cad_prd_quimico"), wcfLabelBold);
			sheet.mergeCells(0, 0, 5, 0);
			//Filtro
			addLabel(sheet, 0, 1, filtroRelatorio, wcfLabelLeft);
			sheet.mergeCells(0, 1, 5, 1);
			//Dados
			addLabel(sheet, 0, linha, bundle.getText("produto_quimico"), wcfLabelHeader);
			sheet.mergeCells(0, linha, 2, linha);
			addLabel(sheet, 3, linha, bundle.getText("unidade_medida"), wcfLabelHeader);
			linha++;
			while (rs.next()) {
				addLabel(sheet, 0, linha, rs.getString("prod_nmproduto") , wcfLabelLeft);
				sheet.mergeCells(0, linha, 2, linha);
				addLabel(sheet, 3, linha, rs.getString("umed_sigla") , wcfLabel);
				linha++;
			}
			break;
		case 2: //Registro de Consumo
			//Titulo			
			addLabel(sheet, 0, 0, "CADASTRO DE REGISTRO DE CONSUMO", wcfLabelBold);
			sheet.mergeCells(0, 0, 5, 0);
			//Filtro
			addLabel(sheet, 0, 1, filtroRelatorio, wcfLabelLeft);
			sheet.mergeCells(0, 1, 5, 1);
			//Dados
			id = 0;
			while (rs.next()) {
				if (id != rs.getInt("regc_id")){
					addLabel(sheet, 0, linha, "Registro de Consumo:", wcfLabelHeader);
					addLabel(sheet, 1, linha, rs.getString("regc_nmregistro") , wcfLabelHeader);
					addLabel(sheet, 2, linha, bundle.getText("periodo") + ":", wcfLabelHeader);
					addLabel(sheet, 3, linha, formataDataPadrao.format(formataDataSQL.parse(rs.getString("regc_dataini"))) , wcfLabelHeader);
					addLabel(sheet, 4, linha, "a", wcfLabelHeader);
					addLabel(sheet, 5, linha, formataDataPadrao.format(formataDataSQL.parse(rs.getString("regc_datafim"))) , wcfLabelHeader);
					linha++;
					addLabel(sheet, 1, linha, "Produto", wcfLabelBold);
					sheet.mergeCells(1, linha, 3, linha);
					linha++;
					id = rs.getInt("regc_id");
				}
				addLabel(sheet, 1, linha, rs.getString("prod_nmproduto") , wcfLabelLeft);
				sheet.mergeCells(1, linha, 3, linha);
				linha++;
			}
			break;
		case 3: //Relação Registro de Consumo x ETA
			//Titulo			
			addLabel(sheet, 0, 0, "CADASTRO DE RELACIONAMENTO DE REGISTRO DE CONSUMO X ETA", wcfLabelBold);
			sheet.mergeCells(0, 0, 5, 0);
			//Filtro
			addLabel(sheet, 0, 1, filtroRelatorio, wcfLabelLeft);
			sheet.mergeCells(0, 1, 5, 1);
			//Dados
			id = 0;
			addLabel(sheet, 0, linha, "Gerência Regional", wcfLabelHeader);
			addLabel(sheet, 1, linha, "Unidade de Negócio", wcfLabelHeader);
			addLabel(sheet, 2, linha, "Município", wcfLabelHeader);
			addLabel(sheet, 3, linha, "Localidade", wcfLabelHeader);
			addLabel(sheet, 4, linha, "ETA", wcfLabelHeader);
			while (rs.next()) {
				if (id != rs.getInt("eta_id")){
					linha++;
					addLabel(sheet, 0, linha, rs.getString("greg_nmregional"), wcfLabel);
					addLabel(sheet, 1, linha, rs.getString("uneg_nmunidadenegocio"), wcfLabel);
					addLabel(sheet, 2, linha, rs.getString("muni_nmmunicipio"), wcfLabel);
					addLabel(sheet, 3, linha, rs.getString("loca_nmlocalidade"), wcfLabel);
					addLabel(sheet, 4, linha, rs.getString("eta_nome"), wcfLabel);
					linha++;
					addLabel(sheet, 1, linha, bundle.getText("registro_consumo"), wcfLabelBold);
					addLabel(sheet, 3, linha, bundle.getText("periodo_inicial"), wcfLabelBold);
					addLabel(sheet, 4, linha, bundle.getText("periodo_final"), wcfLabelBold);
					linha++;
					id = rs.getInt("eta_id");
				}
				addLabel(sheet, 1, linha, rs.getString("regc_nmregistro"), wcfLabel);
				addLabel(sheet, 3, linha, formataDataPadrao.format(formataDataSQL.parse(rs.getString("regc_dataini"))) , wcfLabel);
				addLabel(sheet, 4, linha, formataDataPadrao.format(formataDataSQL.parse(rs.getString("regc_datafim"))) , wcfLabel);
				linha++;
			}
			break;
		case 4: //Relação Registro de Consumo x EAT
			//Titulo			
			addLabel(sheet, 0, 0, "CADASTRO DE RELACIONAMENTO DE REGISTRO DE CONSUMO X EAT", wcfLabelBold);
			sheet.mergeCells(0, 0, 5, 0);
			//Filtro
			addLabel(sheet, 0, 1, filtroRelatorio, wcfLabelLeft);
			sheet.mergeCells(0, 1, 5, 1);
			//Dados
			id = 0;
			addLabel(sheet, 0, linha, bundle.getText("gerencia_regional"), wcfLabelHeader);
			addLabel(sheet, 1, linha, bundle.getText("unidade_negocio"), wcfLabelHeader);
			addLabel(sheet, 2, linha, bundle.getText("municipio"), wcfLabelHeader);
			addLabel(sheet, 3, linha, bundle.getText("localidade"), wcfLabelHeader);
			addLabel(sheet, 4, linha, bundle.getText("eat"), wcfLabelHeader);
			while (rs.next()) {
				if (id != rs.getInt("eeat_id")){
					linha++;
					addLabel(sheet, 0, linha, rs.getString("greg_nmregional"), wcfLabel);
					addLabel(sheet, 1, linha, rs.getString("uneg_nmunidadenegocio"), wcfLabel);
					addLabel(sheet, 2, linha, rs.getString("muni_nmmunicipio"), wcfLabel);
					addLabel(sheet, 3, linha, rs.getString("loca_nmlocalidade"), wcfLabel);
					addLabel(sheet, 4, linha, rs.getString("eeat_nome"), wcfLabel);
					linha++;
					addLabel(sheet, 1, linha, bundle.getText("registro_consumo"), wcfLabelBold);
					addLabel(sheet, 3, linha, bundle.getText("periodo_inicial"), wcfLabelBold);
					addLabel(sheet, 4, linha, bundle.getText("periodo_final"), wcfLabelBold);
					linha++;
					id = rs.getInt("eeat_id");
				}
				addLabel(sheet, 1, linha, rs.getString("regc_nmregistro"), wcfLabel);
				addLabel(sheet, 3, linha, formataDataPadrao.format(formataDataSQL.parse(rs.getString("regc_dataini"))) , wcfLabel);
				addLabel(sheet, 4, linha, formataDataPadrao.format(formataDataSQL.parse(rs.getString("regc_datafim"))) , wcfLabel);
				linha++;
			}
			break;
		case 5: //Tabela de Preços
			//Titulo			
			addLabel(sheet, 0, 0, bundle.getText("rel_cad_tab_preco"), wcfLabelBold);
			sheet.mergeCells(0, 0, 5, 0);
			//Filtro
			addLabel(sheet, 0, 1, filtroRelatorio, wcfLabelLeft);
			sheet.mergeCells(0, 1, 5, 1);
			//Dados
			while (rs.next()) {
				if (!headerLinha) {
					addLabel(sheet, 0, linha, bundle.getText("vigencia"), wcfLabelHeader);
					addLabel(sheet, 1, linha, formataDataPadrao.format(formataDataSQL.parse(rs.getString("tabp_vigencia"))) , wcfLabelHeader);
					linha++;
					addLabel(sheet, 1, linha, bundle.getText("produto"), wcfLabelBold);
					addLabel(sheet, 4, linha, bundle.getText("valor"), wcfLabelBold);
					linha++;
					headerLinha = true;
				}		
				addLabel(sheet, 1, linha, rs.getString("prod_nmproduto"), wcfLabelLeft);
				addNumero(sheet, 4, linha, Double.parseDouble(rs.getString("tbpp_preco")), wcfNumero);
				linha++;
			}
			break;
		}	
		//REALIZA DOWNLOAD DA PLANILHA
    	downloadFile(wb);
    }
    
	/*****************************************************
	 * GETTERS AND SETTERS 
	 *****************************************************/
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
}

