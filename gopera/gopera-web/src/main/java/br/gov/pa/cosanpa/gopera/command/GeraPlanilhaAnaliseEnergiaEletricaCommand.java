package br.gov.pa.cosanpa.gopera.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import jxl.biff.CellReferenceHelper;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;

import org.jboss.logging.Logger;

import br.gov.pa.cosanpa.gopera.fachada.IRelatorioEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.RelatorioExcel;
import br.gov.pa.cosanpa.gopera.util.DadoRelatorio;

public class GeraPlanilhaAnaliseEnergiaEletricaCommand extends AbstractCommandGeraPlanilha {

	private static Logger logger = Logger.getLogger(GeraPlanilhaAnaliseEnergiaEletricaCommand.class);

	public GeraPlanilhaAnaliseEnergiaEletricaCommand(RelatorioExcel relatorioExcel, WritableSheet sheet) {
		super(relatorioExcel, sheet);
	}

	public void execute(InformacoesParaRelatorio informacoes, IRelatorioEnergiaEletrica fachadaRel) throws Exception {
		try {
			// monta query
			String query = fachadaRel
					.queryEnergiaEletricaAnalise(informacoes.getPrimeiroDiaReferenciaInicial(), informacoes.getPrimeiroDiaReferenciaFinal(),
							informacoes.getCodigoRegional(), informacoes.getCodigoUnidadeNegocio(), informacoes.getCodigoMunicipio(),
							informacoes.getCodigoLocalidade());
			
			SortedMap<String, DadoRelatorio> mapDados = informacoes.getMapDados();

			List<String> dadosSelecionados = informacoes.getDadosSelecionados();

			// Pega os meses do periodo
			List<List> meses = fachadaRel.getMesesPeriodo(query);
			List<List> ucs = fachadaRel.getUCs(query);
			int colMeses = 4;
			int rowDados;
			List<String> codigoUC = new ArrayList<String>();
			List<String> localidade = new ArrayList<String>();
			List<String> municipio = new ArrayList<String>();
			String refIni = null, refFim = null;
			// CABECALHO
			sheet.setName(bundle.getText("analise_energia_eletrica"));

			WritableFont boldSimples = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.BOLD);
			WritableCellFormat fontBoldSimplesC = new WritableCellFormat(boldSimples);
			fontBoldSimplesC.setVerticalAlignment(VerticalAlignment.CENTRE);
			fontBoldSimplesC.setAlignment(Alignment.CENTRE);
			fontBoldSimplesC.setWrap(true);

			WritableCellFormat fontBoldSimples = new WritableCellFormat(boldSimples);
			fontBoldSimples.setWrap(false);
			fontBoldSimples.setAlignment(Alignment.CENTRE);
			fontBoldSimples.setBackground(Colour.GRAY_25);
			fontBoldSimples.setBorder(Border.ALL, BorderLineStyle.THIN);

			sheet.addCell(new Label(0, 0, bundle.getText("diretoria_operacao").toUpperCase() + " \n" + bundle.getText("controle_operacional_reducao_perdas") + " \n"
					+ bundle.getText("controle_energia"), fontBoldSimplesC));
			sheet.mergeCells(0, 0, 4 + meses.size(), 0);

			sheet.addCell(new Label(0, 2, bundle.getText("municipio"), fontBoldSimples));

			// Preencho os meses
			for (List mes : meses) {
				rowDados = 3;
				// INCLUSAO MES
				sheet.addCell(new Label(colMeses, 2, mes.get(0).toString(), fontBoldSimples));
				// INCLUSAO TOTALIZADOR
				sheet.addCell(new Label(colMeses + 1, 2, "TOTAL", fontBoldSimples));
				for (List uc : ucs) {
					// Coloca o municipio
					if (!municipio.contains(uc.get(2).toString())) {
						municipio.add(uc.get(2).toString());
						this.addLabel(sheet, 0, rowDados, uc.get(2).toString());
						// Mescla Células
						List<List> countLocalidade = fachadaRel.getCountLocalidade(query, uc.get(2).toString());
						for (List count : countLocalidade) {
							sheet.mergeCells(0, rowDados, 0, rowDados + (Integer.parseInt(count.get(0).toString()) / meses.size() * (dadosSelecionados.size()))
									- 1);
						}
					}
					// Coloca a Localidade
					if (!localidade.contains(uc.get(1).toString())) {
						localidade.add(uc.get(1).toString());
						this.addLabel(sheet, 1, rowDados, uc.get(1).toString());
						List<List> countUcs = fachadaRel.getCountUcs(query, uc.get(1).toString());
						for (List count : countUcs) {
							sheet.mergeCells(1, rowDados, 1, rowDados
									+ (Integer.parseInt(count.get(0).toString()) / meses.size() * (dadosSelecionados.size()) - 1));
						}
					}
					// Coloca a UC
					if (!codigoUC.contains(uc.get(0).toString())) {
						codigoUC.add(uc.get(0).toString());
						// Mescla Células
						sheet.mergeCells(2, rowDados, 2, rowDados + dadosSelecionados.size() - 1);
						this.addInteiro(sheet, 2, rowDados, Double.parseDouble(uc.get(0).toString()));
					}

					List<List> dados = fachadaRel.getDados(query, mes.get(0).toString(), uc.get(0).toString());

					for (List dado : dados) {
						Formula totalFormula;
						for (String item : dadosSelecionados) {
							this.addLabel(sheet, 3, rowDados, String.valueOf(mapDados.get(item).getLabel()));
							if (Integer.valueOf(item) == 1 || Integer.valueOf(item) == 2) {
								this.addNumeroSD(sheet, colMeses, rowDados, this.round(Double.parseDouble(dado.get(Integer.valueOf(item) - 1).toString())));
							} else {
								this.addNumero(sheet, colMeses, rowDados, this.round(Double.parseDouble(dado.get(Integer.valueOf(item) - 1).toString())));
							}

							refIni = CellReferenceHelper.getCellReference(4, rowDados);
							refFim = CellReferenceHelper.getCellReference(colMeses, rowDados);
							totalFormula = new Formula(colMeses + 1, rowDados, "SUM(" + refIni + ":" + refFim + ")", wcfNumeroB);
							sheet.addCell(totalFormula);
							rowDados++;
						}
					}
				}
				colMeses++;
			}
		} catch (Exception e) {
			logger.error("Erro na exportacao.", e);
			throw new Exception("Erro na exportacao", e);
		}
	}

}
