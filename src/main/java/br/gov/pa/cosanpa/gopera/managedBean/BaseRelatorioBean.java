package br.gov.pa.cosanpa.gopera.managedBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import br.gov.model.exception.ErroConsultaSistemaExterno;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;

public class BaseRelatorioBean<T> extends BaseBean<T> {

	public WritableCellFormat wcfNumero;
	public WritableCellFormat wcfNumeroBorder;
	public WritableCellFormat wcfPercentual;
	public WritableCellFormat wcfNumeroBold;
	public WritableCellFormat wcfInteiro;
	public WritableCellFormat wcfInteiroBold;
	public WritableCellFormat wcfLabelHeader;
	public WritableCellFormat wcfLabelHeaderCenter;
	public WritableCellFormat wcfLabel;
	public WritableCellFormat wcfLabelBorder;
	public WritableCellFormat wcfLabelLeftBorder;
	public WritableCellFormat wcfLabelLeft;
	public WritableCellFormat wcfLabelRight;
	public WritableCellFormat wcfLabelBold;	
	public WritableCellFormat wcfLabelBoldBorder;
	public WritableCellFormat wcfLabelBoldW;
	public WritableCellFormat wcfLabelBoldLeft;
	public WritableCellFormat wcfLabelBoldLeftBorder;
	
	protected Map<String, Object> reportParametros;
	protected JRDataSource reportDataSource;
	protected String reportPath = "";
	protected Integer tipoExportacao;
	protected String nomeArquivo;
	protected String nomeRelatorio = "";

	@EJB
	public ProxyOperacionalRepositorio fachadaProxy;

	File relatorioExcel;

	public void addNumero(WritableSheet planilha, int coluna, int linha, Double num, WritableCellFormat formato) throws WriteException {
     	if(num == null) num = 0.0;
 	    planilha.addCell(new jxl.write.Number(coluna, linha, num, formato));
    }
	
    public void addLabel(WritableSheet planilha, int coluna, int linha, String s, WritableCellFormat formato) throws WriteException {
     	if(s == null) s = "";
 	    planilha.addCell(new Label(coluna, linha, s, formato));
    }
     
    public WritableWorkbook geraPlanilha() throws Exception{
		String nomeArquivoPadrao = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/relatorioPadrao.xls");
 		File arquivo = new File(nomeArquivoPadrao);
		String diretorio = fachadaProxy.getParametroSistema(9);
		File dir = new File(diretorio);
		dir.mkdirs();
		WorkbookSettings ws = new WorkbookSettings();
        ws.setLocale(new Locale("pt", "BR"));
        ws.setEncoding("ISO-8859-1");
        //ABRE ARQUIVO PADRÃO
 	    Workbook workbook = Workbook.getWorkbook(arquivo, ws);
 	    //CRIA NOVA PLANILHA
 	    relatorioExcel = new File(diretorio + this.nomeArquivo + ".xls");
 	    if (!dir.exists()){
 	    	throw new ErroConsultaSistemaExterno();
 	    }
 	    WritableWorkbook wb = Workbook.createWorkbook(relatorioExcel, workbook);
 	    return wb;
    }
    
    public void downloadFile(WritableWorkbook wb) throws IOException, WriteException {
		wb.write();
		wb.close();
     	ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
     	//Obtem o caminho para o arquivo e efetua a leitura
     	String path = this.relatorioExcel.getCanonicalPath();
     	byte[] arquivo = readFile(new File(path));
     	HttpServletResponse response =(HttpServletResponse) context.getResponse();
     	//configura o arquivo que vai voltar para o usuario.
     	response.setHeader("Content-Disposition","attachment;filename=\"" + this.nomeArquivo + ".xls\"");
     	response.setContentLength(arquivo.length);
     	//isso faz abrir a janelinha de download
     	response.setContentType("application/download");
     	response.setCharacterEncoding("ISO-8859-1");
     	//envia o arquivo de volta
     	try {
 	    	OutputStream out= response.getOutputStream();
 	    	out.write(arquivo);
 	    	out.flush();
 	    	out.close();
 	    	FacesContext.getCurrentInstance().responseComplete();
     	} catch (IOException e) {
 	    	System.out.print("Erro no envio do arquivo");
 	    	e.printStackTrace();
     	}
     	return;    	 
    }
    
 	//efetua a leitura do arquivo
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

	@Override
	public String iniciar() {
		try{
	 		WritableFont wf = new WritableFont(WritableFont.TAHOMA, 10);
			WritableFont wfBold = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.BOLD);	 		
	 		NumberFormat nf = new NumberFormat("#,##0.00");
	 		NumberFormat nfInt = new NumberFormat("#,##0");
	 		NumberFormat nfDec6 = new NumberFormat("#,##0.000000");
	 		
	 		
	 		wcfNumero = new WritableCellFormat(wf, nf);
	 		wcfNumero.setWrap(false);
	 		wcfNumero.setVerticalAlignment(VerticalAlignment.CENTRE);
	 		wcfNumero.setAlignment(Alignment.RIGHT);
	 		//wcfNumero.setBorder(Border.ALL, BorderLineStyle.THIN);

	 		wcfNumeroBorder = new WritableCellFormat(wf, nf);
	 		wcfNumeroBorder.setWrap(false);
	 		wcfNumeroBorder.setVerticalAlignment(VerticalAlignment.CENTRE);
	 		wcfNumeroBorder.setAlignment(Alignment.RIGHT);
	 		wcfNumeroBorder.setBorder(Border.ALL, BorderLineStyle.THIN);
	 		
	 		wcfPercentual = new WritableCellFormat(wf, nf);
	 		wcfPercentual.setWrap(false);
	 		wcfPercentual.setVerticalAlignment(VerticalAlignment.CENTRE);
	 		wcfPercentual.setAlignment(Alignment.RIGHT);
	 		wcfPercentual.setBackground(Colour.SKY_BLUE);
	 		wcfPercentual.setBorder(Border.ALL, BorderLineStyle.THIN);
	 		
	 		wcfNumeroBold = new WritableCellFormat(wfBold, nf);
	 		wcfNumeroBold.setWrap(false);
	 		wcfNumeroBold.setVerticalAlignment(VerticalAlignment.CENTRE);
	 		wcfNumeroBold.setAlignment(Alignment.RIGHT);
	 		
	 		wcfInteiro = new WritableCellFormat(wf, nfInt);
	 		wcfInteiro.setWrap(false);
	 		wcfInteiro.setVerticalAlignment(VerticalAlignment.CENTRE);
	 		wcfInteiro.setAlignment(Alignment.RIGHT);
	 		//wcfInteiro.setBorder(Border.ALL, BorderLineStyle.THIN);

	 		wcfInteiroBold = new WritableCellFormat(wfBold, nfInt);
	 		wcfInteiroBold.setWrap(false);
	 		wcfInteiroBold.setVerticalAlignment(VerticalAlignment.CENTRE);
	 		wcfInteiroBold.setAlignment(Alignment.RIGHT);
	 		
	 		wcfLabelHeader = new WritableCellFormat(wfBold);
	 	    wcfLabelHeader.setWrap(false);
	 	    wcfLabelHeader.setVerticalAlignment(VerticalAlignment.CENTRE);
	 	    wcfLabelHeader.setAlignment(Alignment.LEFT);
	 	    wcfLabelHeader.setBackground(Colour.GRAY_25);
	 	    wcfLabelHeader.setBorder(Border.ALL, BorderLineStyle.THIN);

	 		wcfLabelHeaderCenter = new WritableCellFormat(wfBold);
	 		wcfLabelHeaderCenter.setWrap(false);
	 		wcfLabelHeaderCenter.setVerticalAlignment(VerticalAlignment.CENTRE);
	 		wcfLabelHeaderCenter.setAlignment(Alignment.CENTRE);
	 		wcfLabelHeaderCenter.setBackground(Colour.GRAY_25);
	 		wcfLabelHeaderCenter.setBorder(Border.ALL, BorderLineStyle.THIN);
	 	    
	 	    wcfLabel = new WritableCellFormat(wf);
	 	    wcfLabel.setWrap(false);
	 	    wcfLabel.setVerticalAlignment(VerticalAlignment.CENTRE);
	 	    wcfLabel.setAlignment(Alignment.CENTRE);
	 	    //wcfLabel.setBorder(Border.ALL, BorderLineStyle.THIN);
	 	    //wcfLabel.setShrinkToFit(false);

	 	    wcfLabelBorder = new WritableCellFormat(wf);
	 	    wcfLabelBorder.setWrap(false);
	 	    wcfLabelBorder.setVerticalAlignment(VerticalAlignment.CENTRE);
	 	    wcfLabelBorder.setAlignment(Alignment.CENTRE);
	 	    wcfLabelBorder.setBorder(Border.ALL, BorderLineStyle.THIN);
	 	    
	 	    wcfLabelLeft = new WritableCellFormat(wf);
	 	    wcfLabelLeft.setWrap(false);
	 	    wcfLabelLeft.setVerticalAlignment(VerticalAlignment.CENTRE);
	 	    wcfLabelLeft.setAlignment(Alignment.LEFT);
	 	    //wcfLabelLeft.setBorder(Border.ALL, BorderLineStyle.THIN);

	 	    wcfLabelLeftBorder = new WritableCellFormat(wf);
	 	    wcfLabelLeftBorder.setWrap(true);
	 	    wcfLabelLeftBorder.setVerticalAlignment(VerticalAlignment.CENTRE);
	 	    wcfLabelLeftBorder.setAlignment(Alignment.LEFT);
	 	    wcfLabelLeftBorder.setBorder(Border.ALL, BorderLineStyle.THIN);
	 	    
	 	    wcfLabelRight = new WritableCellFormat(wf);
	 	    wcfLabelRight.setWrap(true);
	 	    wcfLabelRight.setVerticalAlignment(VerticalAlignment.CENTRE);
	 	    wcfLabelRight.setAlignment(Alignment.RIGHT);
	 	    //wcfLabelRight.setBorder(Border.ALL, BorderLineStyle.THIN);
	 	    
	 	    wcfLabelBold = new WritableCellFormat(wfBold);
	 	    wcfLabelBold.setWrap(true);
	 	    wcfLabelBold.setVerticalAlignment(VerticalAlignment.CENTRE);
	 	    wcfLabelBold.setAlignment(Alignment.CENTRE);
	 	    //wcfLabelBold.setBorder(Border.ALL, BorderLineStyle.THIN);

	 	    wcfLabelBoldBorder = new WritableCellFormat(wfBold);
	 	    wcfLabelBoldBorder.setWrap(false);
	 	    wcfLabelBoldBorder.setVerticalAlignment(VerticalAlignment.CENTRE);
	 	    wcfLabelBoldBorder.setAlignment(Alignment.CENTRE);
	 	    wcfLabelBoldBorder.setBorder(Border.ALL, BorderLineStyle.THIN);
	 	    
	 	    wcfLabelBoldW = new WritableCellFormat(wfBold);
	 	    wcfLabelBoldW.setWrap(false);
	 	    wcfLabelBoldW.setVerticalAlignment(VerticalAlignment.CENTRE);
	 	    wcfLabelBoldW.setAlignment(Alignment.CENTRE);
	 	    
	 	    wcfLabelBoldLeft = new WritableCellFormat(wfBold);
	 	    wcfLabelBoldLeft.setWrap(false);
	 	    wcfLabelBoldLeft.setVerticalAlignment(VerticalAlignment.CENTRE);
	 	    wcfLabelBoldLeft.setAlignment(Alignment.LEFT);
	 	    //wcfLabelBoldLeft.setBorder(Border.ALL, BorderLineStyle.THIN);

	 	    wcfLabelBoldLeftBorder = new WritableCellFormat(wfBold);
	 	    wcfLabelBoldLeftBorder.setWrap(true);
	 	    wcfLabelBoldLeftBorder.setVerticalAlignment(VerticalAlignment.CENTRE);
	 	    wcfLabelBoldLeftBorder.setAlignment(Alignment.LEFT);
	 	    wcfLabelBoldLeftBorder.setBorder(Border.ALL, BorderLineStyle.THIN);

	 	    
     	} catch (WriteException e) {
 	    	e.printStackTrace();
     	}	
		return null;
	}
	
	protected void geraRelatorio(Map<String, Object> parametros) throws Exception{
		HttpServletResponse httpServletResponse=(HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		httpServletResponse.setCharacterEncoding("ISO-8859-1");
		
		JasperPrint jasperPrint;
		
		reportParametros = new HashMap<String, Object>();
		if (parametros != null){
			reportParametros.putAll(parametros);
		}
		reportParametros.put("logoRelatorio", "logoRelatorio.jpg");
		reportParametros.put("nomeRelatorio", nomeRelatorio);
		reportParametros.put("nomeUsuario", usuarioProxy.getNome());
		
		switch (tipoExportacao) {
		case 1: //PDF
			reportParametros.put("exibirExcel", false);
 	        jasperPrint = JasperFillManager.fillReport(reportPath, reportParametros, reportDataSource);

			httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + nomeArquivo + ".pdf");
			httpServletResponse.setContentType("application/pdf");

			ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
		    JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
		    break;
	 
		case 2: //EXCEL
			reportParametros.put("exibirExcel", true);
			jasperPrint = JasperFillManager.fillReport(reportPath, reportParametros, reportDataSource);
			
			httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + nomeArquivo + ".xls");
			httpServletResponse.setContentType("application/vnd.ms-excel");
			servletOutputStream = httpServletResponse.getOutputStream();
			gerarExcel(servletOutputStream, jasperPrint);
			
		    break;
		}
		FacesContext.getCurrentInstance().responseComplete();  
	}
	
	protected void gerarExcel(ServletOutputStream servletOutputStream, JasperPrint jasperPrint) throws JRException, IOException {
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
		exporter.setParameter(JRExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
		exporter.setParameter(JExcelApiExporterParameter.IS_IGNORE_GRAPHICS,Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		exporter.exportReport();
		servletOutputStream.flush();
		servletOutputStream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}
}
