package br.gov.pa.cosanpa.gopera.util;

import java.io.File;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import jxl.Cell;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public abstract class RelatorioExcel {

	private String template;
	
	private String arquivo;
	
	private WritableSheet planilha;

	private WritableWorkbook copy = null;
	
	protected WritableCellFormat celNumerica, celNumericaCinco, celNormalAlinCentro, celNormalAlinEsq, celNormalAlinDir, celulaCabecalho, celNegAlinEsq, celNegAlinDir;
	
	protected NumberFormat numberFormat = new NumberFormat("#,##0.00");
	
	protected NumberFormat numberFormatCinco = new NumberFormat("#,##0.00000");
	
	public RelatorioExcel(String template, String arquivo) {
		this.template = template;
		this.arquivo = arquivo;
	}


	protected void estruturaExcel() throws WriteException{
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');

		WritableFont fontNegrito = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.BOLD);
		WritableFont fontNormal  = new WritableFont(WritableFont.TAHOMA, 10);

		celulaCabecalho = new WritableCellFormat(fontNegrito);
		celulaCabecalho.setVerticalAlignment(VerticalAlignment.CENTRE);
		celulaCabecalho.setAlignment(Alignment.CENTRE);
		celulaCabecalho.setWrap(false);
		
		celNegAlinEsq = new WritableCellFormat(fontNegrito);
		celNegAlinEsq.setVerticalAlignment(VerticalAlignment.CENTRE);
		celNegAlinEsq.setAlignment(Alignment.LEFT);
		celNegAlinEsq.setWrap(false);

		celNegAlinDir = new WritableCellFormat(fontNegrito);
		celNegAlinDir.setVerticalAlignment(VerticalAlignment.CENTRE);
		celNegAlinDir.setAlignment(Alignment.RIGHT);
		celNegAlinDir.setWrap(false);
		
		this.celNumerica = new WritableCellFormat(fontNormal, numberFormat);
		this.celNumerica.setWrap(false);
		this.celNumerica.setVerticalAlignment(VerticalAlignment.CENTRE);
		this.celNumerica.setAlignment(Alignment.RIGHT);

		this.celNumericaCinco = new WritableCellFormat(fontNormal, numberFormatCinco);
		this.celNumericaCinco.setWrap(false);
		this.celNumericaCinco.setVerticalAlignment(VerticalAlignment.CENTRE);
		this.celNumericaCinco.setAlignment(Alignment.RIGHT);

		this.celNormalAlinCentro = new WritableCellFormat(fontNormal, NumberFormats.FLOAT);
		this.celNormalAlinCentro.setWrap(false);
		this.celNormalAlinCentro.setVerticalAlignment(VerticalAlignment.CENTRE);
		this.celNormalAlinCentro.setAlignment(Alignment.CENTRE);

		this.celNormalAlinEsq = new WritableCellFormat(fontNormal, NumberFormats.FLOAT);
		this.celNormalAlinEsq.setVerticalAlignment(VerticalAlignment.CENTRE);
		this.celNormalAlinEsq.setAlignment(Alignment.LEFT);
		this.celNormalAlinEsq.setWrap(false);

		this.celNormalAlinDir = new WritableCellFormat(fontNormal, NumberFormats.FLOAT);
		this.celNormalAlinDir.setVerticalAlignment(VerticalAlignment.CENTRE);
		this.celNormalAlinDir.setAlignment(Alignment.RIGHT);
		this.celNormalAlinDir.setWrap(false);
	}
	

	public File geraExcel() throws Exception {
		estruturaExcel();
		
		File fileNovo = new File(arquivo);
		String diretorio = fileNovo.getParent();
		new File(diretorio).mkdirs();
		File fileExistente = new File(template);
		WorkbookSettings ws = new WorkbookSettings();
		ws.setLocale(new Locale("pt", "BR"));
		ws.setEncoding("ISO-8859-1");
		Workbook workbook = Workbook.getWorkbook(fileExistente, ws);
		copy = Workbook.createWorkbook(fileNovo, workbook);
		planilha = copy.getSheet(0);
		
		defineConteudo();
		
		copy.write();
		copy.close();
		
		return fileNovo;
	}
	
	protected abstract void defineConteudo() throws Exception;
	

	protected void addCelulaTexto(Integer coluna, Integer linha, String texto, WritableCellFormat estilo) throws Exception {
		if (texto == null)
			texto = "";
		Label prod = new Label(coluna, linha, texto, estilo);
		planilha.addCell(prod);
	}

	protected void addCelulaNumero(int coluna, int linha, Double num, WritableCellFormat estilo) throws Exception {
		if (num == null)
			num = 0.0;
		Number numero = new Number(coluna, linha, num, estilo);
		planilha.addCell(numero);
	}
}
