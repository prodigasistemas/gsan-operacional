package br.gov.pa.cosanpa.gopera.command;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

import org.jboss.logging.Logger;

import br.gov.model.operacao.RelatorioExcel;
import br.gov.pa.cosanpa.gopera.exception.UnidadeConsumidoraNaoRelacionada;
import br.gov.servicos.operacao.RelatorioEnergiaEletricaRepositorio;

public class GeraPlanilhaUCNaoCadastradaCommand extends AbstractCommandGeraPlanilha {
	
	private static Logger logger = Logger.getLogger(GeraPlanilhaUCNaoCadastradaCommand.class);

	public GeraPlanilhaUCNaoCadastradaCommand(RelatorioExcel relatorioExcel, WritableSheet sheet) {
		super(relatorioExcel, sheet);
	}

	@Override
	public void execute(InformacoesParaRelatorio informacoes, RelatorioEnergiaEletricaRepositorio fachadaRel) throws Exception{
		Double total = 0.0;
		Integer contador = 0;
		try {

			// Adiciona nome do relatório
			WritableFont times11ptbold = new WritableFont(WritableFont.TAHOMA, 11, WritableFont.BOLD);
			WritableCellFormat fontBold1 = new WritableCellFormat(times11ptbold);
			fontBold1.setAlignment(Alignment.LEFT);
			fontBold1.setBorder(Border.TOP, BorderLineStyle.MEDIUM);
			fontBold1.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM);
			Label nmRelatorio = new Label(0, 1, relatorioExcel.getNomeRelatorio(), fontBold1);
			sheet.addCell(nmRelatorio);

			// Adiciona data de referencia
			WritableCellFormat fontBold = new WritableCellFormat(times11ptbold);
			fontBold.setAlignment(Alignment.RIGHT);
			fontBold.setBorder(Border.TOP, BorderLineStyle.MEDIUM);
			fontBold.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM);
			Label nmDataReferencia = new Label(2, 1, "Referência: " + informacoes.getReferencia(), fontBold);
			sheet.addCell(nmDataReferencia);

			for (int i = 3; i < relatorioExcel.getRelatorioEnergiaEletrica().size() + 3; i++) {
				int j = i - 3;
				contador = i;
				// Primeira coluna
				if (relatorioExcel.getRelatorioEnergiaEletrica().get(j).getCodigoUC() == null) {
					throw new UnidadeConsumidoraNaoRelacionada();
				}
				addInteiro(sheet, 0, i, relatorioExcel.getRelatorioEnergiaEletrica().get(j).getCodigoUC());
				// Segunda coluna
				addLabel(sheet, 1, i, relatorioExcel.getRelatorioEnergiaEletrica().get(j).getNomeUC());
				// Terceira coluna
				this.addLabel(sheet, 2, i, relatorioExcel.getRelatorioEnergiaEletrica().get(j).getFatura());
				// Quarta coluna
				this.addNumero(sheet, 3, i, relatorioExcel.getRelatorioEnergiaEletrica().get(j).getValorTotal());
				// Totaliza Resultado
				total += relatorioExcel.getRelatorioEnergiaEletrica().get(j).getValorTotal();
			}

			// Adiciona Totalizador
			Label totalGeral = new Label(2, contador + 1, "Total Geral:", fontBold);
			sheet.addCell(totalGeral);

			jxl.write.Number numero;
			numero = new jxl.write.Number(3, contador + 1, total, fontBold);
			sheet.addCell(numero);
		} catch (WriteException e) {
			logger.error("Erro na exportacao", e);
			throw new Exception("Erro na exportacao", e);
		}
	}

}
