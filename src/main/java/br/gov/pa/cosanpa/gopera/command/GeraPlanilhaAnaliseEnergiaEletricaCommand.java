package br.gov.pa.cosanpa.gopera.command;

import java.lang.reflect.Method;
import java.util.List;

import jxl.biff.CellReferenceHelper;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.jboss.logging.Logger;

import br.gov.model.exception.ConsultaNaoRetornouDados;
import br.gov.model.operacao.Mes;
import br.gov.model.operacao.RelatorioExcel;
import br.gov.model.util.DateUtil;
import br.gov.model.util.Utilitarios;
import br.gov.servicos.operacao.RelatorioEnergiaEletricaRepositorio;
import br.gov.servicos.operacao.to.DadosRelatorioEnergiaEletrica;

public class GeraPlanilhaAnaliseEnergiaEletricaCommand extends AbstractCommandGeraPlanilha {

	private static Logger logger = Logger.getLogger(GeraPlanilhaAnaliseEnergiaEletricaCommand.class);

	public GeraPlanilhaAnaliseEnergiaEletricaCommand(RelatorioExcel relatorioExcel, WritableSheet sheet) {
		super(relatorioExcel, sheet);
	}

	public void execute(InformacoesParaRelatorio informacoes, RelatorioEnergiaEletricaRepositorio fachadaRel) throws Exception {
		List<Mes> meses = new DateUtil().mesesPeriodo(Utilitarios.converteParaDataComPrimeiroDiaMes(informacoes.getReferenciaInicial())
		        , Utilitarios.converteParaDataComPrimeiroDiaMes(informacoes.getReferenciaFinal()));
		
		preencheCabecalho(meses);
		
		List<DadosRelatorioEnergiaEletrica> dados = fachadaRel
				.analiseEnergiaEletrica(informacoes.getReferenciaInicial(), informacoes.getReferenciaFinal(),
						informacoes.getCodigoRegional(), informacoes.getCodigoUnidadeNegocio(), informacoes.getCodigoMunicipio(),
						informacoes.getCodigoLocalidade());
		
		if (dados.size() == 0){
		    throw new ConsultaNaoRetornouDados();
		}
		
		int colMeses = 4;
		int rowDados = 3;
		int municipio = 0;
		int localidade = 0;
		int uc = 0;
		int mergeMunicipio = 0;
		int mergeLocalidade = 0;
		for(DadosRelatorioEnergiaEletrica item: dados){
			if (item.getIdMunicipio() != municipio){
				if (mergeMunicipio != 0)
					sheet.mergeCells(0, rowDados - mergeMunicipio, 0, rowDados - 1);
				municipio = item.getIdMunicipio();
				this.addLabel(sheet, 0, rowDados, item.getNomeMunicipio());
				mergeMunicipio = 0;
			}
			if (item.getIdLocalidade() != localidade){
				if (mergeLocalidade != 0)
					sheet.mergeCells(1, rowDados - mergeLocalidade, 1, rowDados - 1);
				localidade = item.getIdLocalidade();
				this.addLabel(sheet, 1, rowDados, item.getNomeLocalidade());
				mergeLocalidade = 0;
			}
			if (item.getUc() != uc){
				uc = item.getUc();
				this.addInteiro(sheet, 2, rowDados, item.getUc());
				
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
				if (item.getReferencia() == mes.getReferencia())
					colMeses = 3 + mes.getPosicao();
			}
					
			for (int linha = 0; linha < informacoes.getDadosSelecionados().size(); linha++) {
				String dado = informacoes.getDadosSelecionados().get(linha);
				Method metodo  = DadosRelatorioEnergiaEletrica.class.getMethod("get" + informacoes.getMapDados().get(dado).getNome());
				Double valor = (Double) metodo.invoke(item);
				this.addNumero(sheet, colMeses, rowDados - informacoes.getDadosSelecionados().size() + linha, valor.doubleValue());
			}
		}
		sheet.mergeCells(0, rowDados - mergeMunicipio, 0, rowDados - 1);
		sheet.mergeCells(1, rowDados - mergeLocalidade, 1, rowDados - 1);
		
		preencheTotaisUcs(meses, rowDados);
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
