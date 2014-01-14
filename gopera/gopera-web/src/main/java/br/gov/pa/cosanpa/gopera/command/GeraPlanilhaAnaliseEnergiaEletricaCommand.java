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
			List<Mes> meses = new DateUtil().mesesPeriodo(informacoes.getPrimeiroDiaReferenciaInicial(), informacoes.getPrimeiroDiaReferenciaFinal());
			
			preencheCabecalho(meses);
			
			List<DadosRelatorioEnergiaEletrica> dados = fachadaRel
					.analiseEnergiaEletrica(informacoes.getPrimeiroDiaReferenciaInicial(), informacoes.getPrimeiroDiaReferenciaFinal(),
							informacoes.getCodigoRegional(), informacoes.getCodigoUnidadeNegocio(), informacoes.getCodigoMunicipio(),
							informacoes.getCodigoLocalidade());
			
			int colMeses = 4;
			int rowDados = 3;
			int municipio = 0;
			int localidade = 0;
			int uc = 0;
			int mergeMunicipio = 0;
			int mergeLocalidade = 0;
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
						preencheZeros(meses, rowDados);
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
				}
			}
			sheet.mergeCells(0, rowDados - mergeMunicipio, 0, rowDados - 1);
			sheet.mergeCells(1, rowDados - mergeLocalidade, 1, rowDados - 1);
			
			preencheTotaisUcs(meses, rowDados);
		} catch (Exception e) {
			logger.error("Erro na exportacao.", e);
			throw new Exception("Erro na exportacao", e);
		}
		
	}

	private int preencheCabecalho(List<Mes> meses) throws Exception {
		sheet.setName(bundle.getText("analise_energia_eletrica"));
		
		StringBuilder title = new StringBuilder();
		title.append( bundle.getText("diretoria_operacao").toUpperCase() + " \n")
		.append(bundle.getText("controle_operacional_reducao_perdas") + " \n")
		.append(bundle.getText("controle_energia"));

		sheet.addCell(new Label(0, 0, title.toString(), fontBoldSimplesC));
		sheet.mergeCells(0, 0, 4 + meses.size(), 0);
		sheet.addCell(new Label(0, 2, bundle.getText("municipio"), fontBoldSimples));
		
		int colMeses = 4;

		for (Mes mes : meses) {
			sheet.addCell(new Label(colMeses++, 2, mes.getMesAno(), fontBoldSimples));
		}
		
		sheet.addCell(new Label(colMeses, 2, "TOTAL", fontBoldSimples));
		return colMeses;
	}

	private void preencheZeros(List<Mes> meses, int rowDados) throws WriteException {
		for (Mes mes : meses) {
			this.addNumero(sheet, 3 + mes.getNumeral(), rowDados, 0.0);
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
