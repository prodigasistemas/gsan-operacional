package br.gov.pa.cosanpa.gopera.managedBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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
import br.gov.pa.cosanpa.gopera.exception.ArquivoDeRelatorioNaoExiste;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;

public class BaseRelatorioBean<T> extends BaseBean<T> {

	public WritableCellFormat wcfNumero;
	public WritableCellFormat wcfNumeroBorder;
	public WritableCellFormat wcfNumeroBorderBlue;
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
	
	@EJB
	public IProxy fachadaProxy;

	String nomeArquivo;
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
 	    	throw new ArquivoDeRelatorioNaoExiste();
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
    
	protected String downloadFile(String nomeDoArquivoGeradoParaDownload, String caminhoRelativoComNomeEextensao) {
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
	 		
	 		wcfNumeroBorderBlue = new WritableCellFormat(wf, nfDec6);
	 		wcfNumeroBorderBlue.setWrap(false);
	 		wcfNumeroBorderBlue.setVerticalAlignment(VerticalAlignment.CENTRE);
	 		wcfNumeroBorderBlue.setAlignment(Alignment.RIGHT);
	 		wcfNumeroBorderBlue.setBackground(Colour.SKY_BLUE);
	 		wcfNumeroBorderBlue.setBorder(Border.ALL, BorderLineStyle.THIN);
	 		
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
}
