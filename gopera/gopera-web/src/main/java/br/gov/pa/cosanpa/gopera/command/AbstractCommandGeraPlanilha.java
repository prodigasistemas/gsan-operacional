package br.gov.pa.cosanpa.gopera.command;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import br.gov.pa.cosanpa.gopera.fachada.IRelatorioEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.RelatorioExcel;
import br.gov.pa.cosanpa.gopera.util.WebBundle;

public abstract class AbstractCommandGeraPlanilha {

	private WritableCellFormat wcfNumero;
	private WritableCellFormat wcfNumeroSD;
	protected WritableCellFormat wcfNumeroB;
	private WritableCellFormat wcfInteiro;
	private WritableCellFormat wcfLabel;
	private WritableCellFormat wcfLabelLeft;
	private WritableCellFormat wcfLabelBold;
	
	protected RelatorioExcel relatorioExcel;
	protected WritableSheet sheet;
	
	protected WebBundle bundle;
	
	public abstract void execute(InformacoesParaRelatorio informacoes, IRelatorioEnergiaEletrica fachadaRel) throws Exception;
	
	public AbstractCommandGeraPlanilha(RelatorioExcel relatorioExcel, WritableSheet sheet) {
		prepare();
		this.relatorioExcel = relatorioExcel;
		this.sheet = sheet;
		bundle = new WebBundle();
	}

	protected void prepare() {
		try {
			WritableFont wf = new WritableFont(WritableFont.TAHOMA, 10);
			NumberFormat nf = new NumberFormat("#,##0.00");
			wcfNumero = new WritableCellFormat(wf, nf);
			// wcf = new WritableCellFormat(wf, NumberFormats.FLOAT);
			wcfNumero.setWrap(false);
			wcfNumero.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcfNumero.setAlignment(Alignment.RIGHT);
			wcfNumero.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableFont wfB = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.BOLD);
			wcfNumeroB = new WritableCellFormat(wfB, nf);
			// wcf = new WritableCellFormat(wf, NumberFormats.FLOAT);
			wcfNumeroB.setWrap(false);
			wcfNumeroB.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcfNumeroB.setAlignment(Alignment.RIGHT);
			wcfNumeroB.setBorder(Border.ALL, BorderLineStyle.THIN);

			NumberFormat nfSD = new NumberFormat("#,##0");
			wcfNumeroSD = new WritableCellFormat(wf, nfSD);
			// wcf = new WritableCellFormat(wf, NumberFormats.FLOAT);
			wcfNumeroSD.setWrap(false);
			wcfNumeroSD.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcfNumeroSD.setAlignment(Alignment.RIGHT);
			wcfNumeroSD.setBorder(Border.ALL, BorderLineStyle.THIN);

			wcfInteiro = new WritableCellFormat(wf, NumberFormats.INTEGER);
			wcfInteiro.setWrap(false);
			wcfInteiro.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcfInteiro.setAlignment(Alignment.CENTRE);
			wcfInteiro.setBorder(Border.ALL, BorderLineStyle.THIN);

			wcfLabel = new WritableCellFormat(wf);
			wcfLabel.setWrap(false);
			wcfLabel.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcfLabel.setAlignment(Alignment.CENTRE);
			wcfLabel.setBorder(Border.ALL, BorderLineStyle.THIN);

			wcfLabelLeft = new WritableCellFormat(wf);
			wcfLabelLeft.setWrap(false);
			wcfLabelLeft.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcfLabelLeft.setAlignment(Alignment.LEFT);
			wcfLabelLeft.setBorder(Border.ALL, BorderLineStyle.THIN);

			wcfLabelBold = new WritableCellFormat(wfB);
			wcfLabelBold.setWrap(false);
			wcfLabelBold.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcfLabelBold.setAlignment(Alignment.CENTRE);
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}

	protected void addNumero(WritableSheet planilha, int coluna, int linha, Double num) throws WriteException {
		if (num == null)
			num = 0.0;
		num = round(num);
		jxl.write.Number numero;
		numero = new jxl.write.Number(coluna, linha, num, wcfNumero);
		planilha.addCell(numero);
	}

	protected void addNumeroSD(WritableSheet planilha, int coluna, int linha, Double num) throws WriteException {
		if (num == null)
			num = 0.0;
		num = round(num);
		jxl.write.Number numero;
		numero = new jxl.write.Number(coluna, linha, num, wcfNumeroSD);
		planilha.addCell(numero);
	}

	protected void addInteiro(WritableSheet planilha, int coluna, int linha, Integer num) throws WriteException {
		if (num == null)
			num = 0;
		jxl.write.Number numero;
		numero = new jxl.write.Number(coluna, linha, num, wcfInteiro);
		planilha.addCell(numero);
	}

	protected void addLabel(WritableSheet planilha, int coluna, int linha, String s) throws WriteException {
		if (s == null)
			s = "";
		Label label;
		label = new Label(coluna, linha, s, wcfLabel);
		planilha.addCell(label);
	}

	protected void addLabelLeft(WritableSheet planilha, int coluna, int linha, String s) throws WriteException {
		if (s == null)
			s = "";
		Label label;
		label = new Label(coluna, linha, s, wcfLabelLeft);
		planilha.addCell(label);
	}

	protected double round(double d) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		DecimalFormat formata = new DecimalFormat("#.##", symbols);
		return Double.valueOf(Double.parseDouble(formata.format(d)));
	}

}
