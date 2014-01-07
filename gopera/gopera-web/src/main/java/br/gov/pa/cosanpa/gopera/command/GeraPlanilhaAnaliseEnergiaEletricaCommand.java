package br.gov.pa.cosanpa.gopera.command;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.jboss.logging.Logger;

import br.gov.pa.cosanpa.gopera.fachada.IRelatorioEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.DadosRelatorioEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.RelatorioExcel;
import br.gov.pa.cosanpa.gopera.util.DadoRelatorio;
import br.gov.pa.cosanpa.gopera.util.DateUtil;
import br.gov.pa.cosanpa.gopera.util.Mes;

public class GeraPlanilhaAnaliseEnergiaEletricaCommand extends AbstractCommandGeraPlanilha {

	private static Logger logger = Logger.getLogger(GeraPlanilhaAnaliseEnergiaEletricaCommand.class);

	public GeraPlanilhaAnaliseEnergiaEletricaCommand(RelatorioExcel relatorioExcel, WritableSheet sheet) {
		super(relatorioExcel, sheet);
	}

	public void execute(InformacoesParaRelatorio informacoes, IRelatorioEnergiaEletrica fachadaRel) throws Exception {
		try {
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
			
			// Pega os meses do periodo
			DateUtil util = new DateUtil();
			List<Mes> meses = util.mesesPeriodo(informacoes.getPrimeiroDiaReferenciaInicial(), informacoes.getPrimeiroDiaReferenciaFinal());
			
			int colMeses = 4;
			int rowDados;
			// CABECALHO
			sheet.setName(bundle.getText("analise_energia_eletrica"));

			sheet.addCell(new Label(0, 0, bundle.getText("diretoria_operacao").toUpperCase() + " \n" + bundle.getText("controle_operacional_reducao_perdas") + " \n"
					+ bundle.getText("controle_energia"), fontBoldSimplesC));
			sheet.mergeCells(0, 0, 4 + meses.size(), 0);
			sheet.addCell(new Label(0, 2, bundle.getText("municipio"), fontBoldSimples));

			// Preencho os meses
			for (Mes mes : meses) {
				sheet.addCell(new Label(colMeses++, 2, mes.getMesAno(), fontBoldSimples));
			}
			sheet.addCell(new Label(colMeses, 2, "TOTAL", fontBoldSimples));
			
			rowDados = 3;
			List<DadosRelatorioEnergiaEletrica> dados = fachadaRel
					.analiseEnergiaEletrica(informacoes.getPrimeiroDiaReferenciaInicial(), informacoes.getPrimeiroDiaReferenciaFinal(),
							informacoes.getCodigoRegional(), informacoes.getCodigoUnidadeNegocio(), informacoes.getCodigoMunicipio(),
							informacoes.getCodigoLocalidade());
			int municipio = 0;
			int localidade = 0;
			int uc = 0;
			int mergeMunicipio = 0;
			int mergeLocalidade = 0;
			
			Map<Integer, Map<String, BigDecimal>> totaisMeses = new LinkedHashMap<Integer, Map<String, BigDecimal>>();
			
			for(DadosRelatorioEnergiaEletrica item: dados){
				if (item.getId().getIdMunicipio() != municipio){
					if (mergeMunicipio != 0)
						sheet.mergeCells(0, rowDados - mergeMunicipio, 0, rowDados - 1);
					municipio = item.getId().getIdMunicipio();
					this.addLabel(sheet, 0, rowDados, item.getNomeMunicipio());
					mergeMunicipio = 0;
				}
				if (item.getId().getIdLocalidade() != localidade){
					if (mergeLocalidade != 0)
						sheet.mergeCells(1, rowDados - mergeLocalidade, 1, rowDados - 1);
					localidade = item.getId().getIdLocalidade();
					this.addLabel(sheet, 1, rowDados, item.getNomeLocalidade());
					mergeLocalidade = 0;
				}
				if (item.getId().getUc() != uc){
					uc = item.getId().getUc();
					this.addInteiro(sheet, 2, rowDados, item.getId().getUc());
					
					for (String info : informacoes.getDadosSelecionados()) {
						this.addLabel(sheet, 3, rowDados, informacoes.getMapDados().get(info).getLabel());
						rowDados++;				
						mergeMunicipio++;
						mergeLocalidade++;
					}
					
					if (informacoes.getDadosSelecionados().size() > 1)
						sheet.mergeCells(2, rowDados - informacoes.getDadosSelecionados().size(), 2, rowDados - 1);
				}
				for (Mes mes : meses) {
					if (item.getId().getMes().equals(mes.getMesAno()))
						colMeses = 3 + mes.getNumeral();
				}
						
				for (int linha = 0; linha < informacoes.getDadosSelecionados().size(); linha++) {
					String dado = informacoes.getDadosSelecionados().get(linha);
					Method metodo  = DadosRelatorioEnergiaEletrica.class.getMethod("get" + informacoes.getMapDados().get(dado).getNome());
					BigDecimal valor = (BigDecimal) metodo.invoke(item);
					this.addNumero(sheet, colMeses, rowDados - informacoes.getDadosSelecionados().size() + linha, valor.doubleValue());
					
					Map<String, BigDecimal> infos = totaisMeses.get(colMeses); 
					if (infos == null){
						infos = new LinkedHashMap<String, BigDecimal>();
						totaisMeses.put(colMeses, infos);
					}
					
					BigDecimal total = infos.get(dado);
					if (total == null){
						total = new BigDecimal(0);
						infos.put(dado, total);
					}
					
					total = total.add(valor);
				}
			}
			sheet.mergeCells(0, rowDados - mergeMunicipio, 0, rowDados - 1);
			sheet.mergeCells(1, rowDados - mergeLocalidade, 1, rowDados - 1);
			
			preencheTotaisUcs(meses, rowDados);
			
//			preencheTotaisMeses(totaisMeses, rowDados);
		} catch (Exception e) {
			logger.error("Erro na exportacao.", e);
			throw new Exception("Erro na exportacao", e);
		}
		
	}

	private void preencheTotaisMeses(Map<Integer, Map<String, BigDecimal>> totaisMeses, int rowDados) throws WriteException, RowsExceededException {
		int colMeses = 4;
		for(Integer colunaMes : totaisMeses.keySet()){
			Map<String, BigDecimal> totaisDados = totaisMeses.get(colunaMes);
//			this.addLabel(sheet, 3, rowDados, labelDados.get(key));
			int linha = rowDados;
			for(BigDecimal total : totaisDados.values()){
				this.addNumero(sheet, colMeses, linha++, total.doubleValue());
			}
			colMeses++;
		}
	}

	private void preencheTotaisUcs(List<Mes> meses, int rowDados) throws WriteException, RowsExceededException {
		String refMesIni = null; 
		String refMesFim = null; 
		for(int linha = 3; linha < rowDados; linha++){
			refMesIni = CellReferenceHelper.getCellReference(4, linha);
			refMesFim = CellReferenceHelper.getCellReference(4 + meses.size() - 1, linha);
			Formula totalFormula = new Formula(4 + meses.size(), linha, "SUM(" + refMesIni + ":" + refMesFim + ")", wcfNumeroB);
			sheet.addCell(totalFormula);
		}
	}

}
