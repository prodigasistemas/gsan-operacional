package br.gov.pa.cosanpa.gopera.command;

import org.jboss.logging.Logger;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import br.gov.pa.cosanpa.gopera.fachada.IRelatorioEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.RelatorioExcel;

public class GeraPlanilhaUCsNaoFaturadasCommand extends AbstractCommandGeraPlanilha {
	
	private static Logger logger = Logger.getLogger(GeraPlanilhaUCsNaoFaturadasCommand.class);

	public GeraPlanilhaUCsNaoFaturadasCommand(RelatorioExcel relatorioExcel, WritableSheet sheet) {
		super(relatorioExcel, sheet);
	}

	@Override
	public void execute(InformacoesParaRelatorio informacoes, IRelatorioEnergiaEletrica fachadaRel) throws Exception{
		Double total = 0.0;
		Integer contador = 0;
		
		try{
    		//Adiciona nome do relatório
    		WritableFont times11ptbold2 = new WritableFont(WritableFont.TAHOMA, 11, WritableFont.BOLD);
    		WritableCellFormat fontBold2 = new WritableCellFormat(times11ptbold2);
    		fontBold2.setAlignment(Alignment.LEFT);
    		fontBold2.setBorder(Border.TOP, BorderLineStyle.MEDIUM);
    		fontBold2.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM);
    		
    		Label nmRelatorio2 = new Label(0, 1, relatorioExcel.getNomeRelatorio(), fontBold2);
    		sheet.addCell(nmRelatorio2);
    		
    		//Adiciona data de referencia
    		WritableCellFormat fontBold3 = new WritableCellFormat(times11ptbold2);
    		fontBold3.setAlignment(Alignment.RIGHT);
    		fontBold3.setBorder(Border.TOP, BorderLineStyle.MEDIUM);
    		fontBold3.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM);
    		Label nmDataReferencia3 = new Label(2, 1, "Referência: " + informacoes.getReferencia(), fontBold3);
    		sheet.addCell(nmDataReferencia3);
    		for (int i = 3; i < relatorioExcel.getRelatorioEnergiaEletrica().size() + 3; i++) {
    			int j = i-3;
    			contador = i;
    			// Primeira coluna
    		    this.addInteiro(sheet, 0, i, Double.parseDouble(relatorioExcel.getRelatorioEnergiaEletrica().get(j).getCodigoUC().toString()));
    		    // Segunda coluna
    		    this.addLabel(sheet, 1, i, relatorioExcel.getRelatorioEnergiaEletrica().get(j).getNomeUC());
    		    // Terceira coluna
    		    this.addLabel(sheet, 2, i, relatorioExcel.getRelatorioEnergiaEletrica().get(j).getFatura());
    		    //Quarta coluna
    		    this.addNumero(sheet, 3, i, relatorioExcel.getRelatorioEnergiaEletrica().get(j).getValorTotal());
    		    //Totaliza Resultado
    		    //total += relatorioExcel.getRelatorioEnergiaEletrica().get(j).getValorTotal();
    		    }
    		
    		//Adiciona Totalizador
    		Label totalGeral2 = new Label(2, contador + 1, "Total Geral:", fontBold3);
    		sheet.addCell(totalGeral2);
    		
    		jxl.write.Number numero2;
    	    numero2 = new jxl.write.Number(3, contador + 1, total, fontBold3);
    	    sheet.addCell(numero2);
		} catch (Exception e) {
			logger.error("Erro na exportacao", e);
			throw new Exception("Erro na exportacao", e);
		}    	    
	}
}
