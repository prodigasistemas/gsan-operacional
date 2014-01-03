package br.gov.pa.cosanpa.gopera.util;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import br.gov.pa.cosanpa.gopera.model.RelatorioGerencial;

public class RelatorioExcelGerencial extends RelatorioExcel {
	private Date dataInicial;
	private Date dataFinal;
	private Integer tipoAgrupamento;
	private Integer tipoRelatorio;
	private TipoExibicao tipoExibicao;
	private List<RelatorioGerencial> relatorioComFiltro;
	private String filtroRelatorio;

	protected WebBundle bundle;

	private WebUtil util = new WebUtil();

	private SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");

	Integer linha = 7;

	// Controle de Codigos
	Integer codRegional = 0, codUnidadeNegocio = 0, codMunicipio = 0, codLocalidade = 0, codUnidadeOperacional = 0;

	// Controle de preenchimento de agrupamentos;
	boolean mRegional, mNegocio, mMunicipio, mLocalidade;

	// Totalizadores Unidade Operacional por Mês
	private Map<String, GrupoProdutoQuimico> resumosProdutos = new TreeMap<String, GrupoProdutoQuimico>();

	public RelatorioExcelGerencial(String template, String arquivo, Date dataInicial, Date dataFinal, Integer tipoAgrupamento, Integer tipoRelatorio,
			TipoExibicao tipoExibicao, List<RelatorioGerencial> relatorio, String filtroRelatorio) {
		super(template, arquivo);
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
		this.tipoAgrupamento = tipoAgrupamento;
		this.tipoRelatorio = tipoRelatorio;
		this.tipoExibicao = tipoExibicao;
		this.relatorioComFiltro = relatorio;
		this.filtroRelatorio = filtroRelatorio;
		bundle = new WebBundle();
	}

	protected void defineConteudo() throws Exception {
		montaTextoPeriodo();

		/* Relatório Consumo Gerencial Analitico */
		if (this.tipoExibicao == TipoExibicao.ANALITICO) {
			relatorioExcelConsumoGerencialAnalitico();
		}

		/* Relatório Consumo Gerencial Sintético */
		if (this.tipoExibicao == TipoExibicao.SINTETICO) {
			relatorioExcelConsumoGerencialSintetico();
		}

		/* Relatório Gerencial Mensal Início */
		if (this.tipoExibicao == TipoExibicao.MENSAL) {
			relatorioExcelGerencialMensal();
		}
	}

	private void relatorioExcelGerencialMensal() throws Exception {
		// Controle de preenchimento dos meses
		boolean cCabecalho = false;

		addCelulaTexto(0, 1, bundle.getText("rel_cons_prod_quim_mensal"), celulaCabecalho);
		addCelulaTexto(0, 5, filtroRelatorio, celNegAlinEsq);
		Integer i = 0;

		if (tipoAgrupamento == 0) {
			cCabecalho = true;
		}

		List<String> meses = (new WebUtil()).mesesPeriodo(dataInicial, dataFinal);
		int coluna = 0;

		for (RelatorioGerencial produto : this.relatorioComFiltro) {
			mRegional = mNegocio = mMunicipio = mLocalidade = false;
			coluna = 1;

			cCabecalho = agrupaProdutosQuimicos(cCabecalho, produto);

			if (cCabecalho == true) {
				cCabecalho = false;
				addCelulaTexto(coluna++, linha, "Produtos", celNegAlinEsq);

				for (String mes : meses) {
					addCelulaTexto(coluna++, linha, bundle.getText("mes_" + mes), celNormalAlinCentro);
				}

				linha++;
			}

			coluna = 1;

			addCelulaTexto(coluna++, linha, produto.getDescricaoProduto(), celNormalAlinEsq);

			for (String mes : meses) {
				String nomeMes = bundle.getText("mes_" + mes).substring(0, 3).toUpperCase();

				Method metodo = RelatorioGerencial.class.getMethod("getQtdConsumo" + nomeMes);
				Double valor = (Double) metodo.invoke(produto);
				addCelulaNumero(coluna++, linha, valor, this.celNumerica);
			}

			coluna = 0;

			linha++;

			List<String> nomesMeses = new LinkedList<String>();

			for (String mes : meses) {
				nomesMeses.add(bundle.getText("mes_" + mes).substring(0, 3).toUpperCase());
			}

			exibeResumo(i, produto, nomesMeses, false);

			i++;
		}
	}

	private void relatorioExcelConsumoGerencialSintetico() throws Exception {
		boolean escreveCabecalho = false;

		addCelulaTexto(0, 1, bundle.getText("rel_cons_prod_quim_sintetico"), celulaCabecalho);
		addCelulaTexto(0, 5, filtroRelatorio, celNegAlinEsq);
		Integer i = 0;

		if (tipoAgrupamento == 0) {
			escreveCabecalho = true;
		}

		int coluna = 1;

		for (RelatorioGerencial produto : this.relatorioComFiltro) {
			mRegional = mNegocio = mMunicipio = mLocalidade = false;

			escreveCabecalho = agrupaProdutosQuimicos(escreveCabecalho, produto);

			if (escreveCabecalho == true) {
				escreveCabecalho = false;
				addCelulaTexto(coluna++, linha, bundle.getText("produtos"), celNegAlinEsq);
				addCelulaTexto(coluna++, linha, bundle.getText("qtd_consumo"), celNegAlinEsq);

				if (this.tipoRelatorio == 2) { // FINANCEIRO
					addCelulaTexto(coluna++, linha, bundle.getText("vlr_unitario"), celNegAlinEsq);
					addCelulaTexto(coluna++, linha, bundle.getText("vlr_total"), celNegAlinEsq);
				}

				linha++;
			}

			coluna = 1;

			addCelulaTexto(coluna++, linha, produto.getDescricaoProduto(), celNormalAlinEsq);
			addCelulaNumero(coluna++, linha, produto.getQtdConsumo(), this.celNumerica);
			if (this.tipoRelatorio == 2) { // FINANCEIRO
				addCelulaNumero(coluna++, linha, produto.getValorUnitario(), this.celNumericaCinco);
				addCelulaNumero(coluna++, linha, produto.getValorTotal(), this.celNumerica);
			}
			linha++;

			List<String> campos = new ArrayList<String>();
			campos.add("");

			exibeResumo(i, produto, campos, false);

			i++;
		}
	}

	private void relatorioExcelConsumoGerencialAnalitico() throws Exception {
		boolean escreveCabecalho = false;

		addCelulaTexto(0, 5, filtroRelatorio, celNegAlinEsq);
		Integer i = 0;

		if (tipoAgrupamento == 0) {
			escreveCabecalho = true;
		}

		List<String> textosColunas = util.descricoesDeCamposNoAgrupamento(tipoAgrupamento);

		List<Method> metodos = util.metodosPeloAgrupamento(tipoAgrupamento);

		int coluna = 0;

		for (RelatorioGerencial produto : this.relatorioComFiltro) {
			mRegional = mNegocio = mMunicipio = mLocalidade = false;
			coluna = 0;
			escreveCabecalho = agrupaProdutosQuimicos(escreveCabecalho, produto);

			if (escreveCabecalho == true) {
				escreveCabecalho = false;
				addCelulaTexto(coluna++, linha, bundle.getText("data_consumo"), celNegAlinDir);
				for (String nomeColuna : textosColunas) {
					addCelulaTexto(coluna++, linha, nomeColuna, celNegAlinEsq);
				}
				addCelulaTexto(coluna++, linha, bundle.getText("produtos"), celNegAlinEsq);
				addCelulaTexto(coluna++, linha, bundle.getText("qtd_consumo"), celNegAlinEsq);

				if (this.tipoRelatorio == 2) { // FINANCEIRO
					addCelulaTexto(coluna++, linha, bundle.getText("vlr_unitario"), celNegAlinDir);
					addCelulaTexto(coluna, linha, bundle.getText("vlr_total"), celNegAlinDir);
				}
				linha++;
			}
			
			coluna = 0;
			addCelulaTexto(coluna++, linha, formataData.format(produto.getDataConsumo()), celNormalAlinDir);
			for (Method metodo : metodos) {
				addCelulaTexto(coluna++, linha, metodo.invoke(produto).toString(), celNormalAlinEsq);
			}
			addCelulaTexto(coluna++, linha, produto.getDescricaoProduto(), celNormalAlinEsq);
			addCelulaNumero(coluna++, linha, produto.getQtdConsumo(), this.celNumerica);
			if (this.tipoRelatorio == 2) { // FINANCEIRO
				addCelulaNumero(coluna++, linha, produto.getValorUnitario(), this.celNumericaCinco);
				addCelulaNumero(coluna++, linha, produto.getValorTotal(), this.celNumerica);
			}
			
			coluna = 0;

			linha++;
			
			List<String> campos = new ArrayList<String>();
			campos.add("");
			
			exibeResumo(i, produto, campos, true);

			i++;
		}
	}

	private void montaTextoPeriodo() throws Exception {
		String period = bundle.getText("periodo") + ": " + formataData.format(this.dataInicial) + " a " + formataData.format(this.dataFinal);

		addCelulaTexto(0, 4, period, celulaCabecalho);
	}

	private void exibeResumo(Integer i, RelatorioGerencial produto, List<String> campos, boolean exibeUnidOperacional) throws Exception {
		
		if (tipoAgrupamento >= 5 && exibeUnidOperacional) { // UNIDADE OPERACIONAL
			String nomeGrupo = "unidadeoperacional_"+produto.getTipoUnidadeOperacional() + "_" + produto.getCodigoUnidadeOperacional();
			
			util.totalizadorGrupoProdutoQuimico(resumosProdutos, produto, produto.getNomeUnidadeOperacional(), nomeGrupo, 16, campos);
			
			if (exibeTotalizadorUnidadeOperacional(i, relatorioComFiltro)){
				escreveResumoProduto(nomeGrupo);
			}
		}
		
		if (tipoAgrupamento >= 4) { // LOCALIDADE
			String nomeGrupo = "localidade_"+produto.getCodigoLocalidade();
			
			util.totalizadorGrupoProdutoQuimico(resumosProdutos, produto, produto.getNomeLocalidade(), nomeGrupo, 12, campos);
			
			if (exibeTotalizadorLocalidade(i, relatorioComFiltro)){
				escreveResumoProduto(nomeGrupo);
			}
		}

		if (tipoAgrupamento >= 3) { // MUNICIPIO
			String nomeGrupo = "municipio_"+produto.getCodigoMunicipio();
			
			util.totalizadorGrupoProdutoQuimico(resumosProdutos, produto, produto.getNomeMunicipio(), nomeGrupo, 8, campos);
			
			if (exibeTotalizadorMunicipio(i, relatorioComFiltro)){
				escreveResumoProduto(nomeGrupo);
			}
		}

		if (tipoAgrupamento >= 2) { // UNIDADE DE NEGÓCIO
			String nomeGrupo = "unidadenegocio_"+produto.getCodigoUnidadeNegocio();
			
			util.totalizadorGrupoProdutoQuimico(resumosProdutos, produto, produto.getNomeUnidadeNegocio(), nomeGrupo, 4, campos);
			
			if (exibeTotalizadorUnidadeNegocio(i, relatorioComFiltro)){
				escreveResumoProduto(nomeGrupo);
			}
		}

		if (tipoAgrupamento >= 1) { // REGIONAL
			
			String nomeGrupo = "regional_"+produto.getCodigoRegional();
			
			util.totalizadorGrupoProdutoQuimico(resumosProdutos, produto, produto.getNomeRegional(), nomeGrupo, 0, campos);
			
			if (exibeTotalizadorRegional(i, relatorioComFiltro)){
				escreveResumoProduto(nomeGrupo);
			}
		}
	}

	private boolean agrupaProdutosQuimicos(boolean cCabecalho, RelatorioGerencial produtos) throws Exception {
		if (tipoAgrupamento >= 1 && codRegional != produtos.getCodigoRegional()) { // GERÊNCIA
			addCelulaTexto(0, linha, produtos.getNomeRegional(), celNegAlinEsq);
			linha++;
			codRegional = produtos.getCodigoRegional();
			cCabecalho = true;
			mRegional = true;
		}

		if (tipoAgrupamento >= 2 && (mRegional == true || codUnidadeNegocio != produtos.getCodigoUnidadeNegocio())) { // UNIDADE
			addCelulaTexto(0, linha, StringUtils.leftPad(" ", 4) + produtos.getNomeUnidadeNegocio(), celNegAlinEsq);
			linha++;
			codUnidadeNegocio = produtos.getCodigoUnidadeNegocio();
			mNegocio = true;
		}

		if (tipoAgrupamento >= 3 && (mNegocio == true || codMunicipio != produtos.getCodigoMunicipio())) { // MUNICÍPIO
			addCelulaTexto(0, linha, StringUtils.leftPad(" ", 8) + produtos.getNomeMunicipio(), celNegAlinEsq);
			linha++;
			codMunicipio = produtos.getCodigoMunicipio();
			mMunicipio = true;
		}

		if (tipoAgrupamento >= 4 && (mMunicipio == true || codLocalidade != produtos.getCodigoLocalidade())) { // LOCALIDADE
			addCelulaTexto(0, linha, StringUtils.leftPad(" ", 12) + produtos.getNomeLocalidade(), celNegAlinEsq);
			linha++;
			codLocalidade = produtos.getCodigoLocalidade();
			mLocalidade = true;
		}

		if (tipoAgrupamento == 5 && (mLocalidade == true || codUnidadeOperacional != produtos.getCodigoUnidadeOperacional())) { // UNIDADE
			addCelulaTexto(0, linha, StringUtils.leftPad(" ", 16) + produtos.getNomeUnidadeOperacional(), celNegAlinEsq);
			linha++;
			codUnidadeOperacional = produtos.getCodigoUnidadeOperacional();
		}
		return cCabecalho;
	}

	private void escreveResumoProduto(String nomeGrupo) throws Exception {
		GrupoProdutoQuimico grupo = resumosProdutos.get(nomeGrupo);

		addCelulaTexto(0, linha++, StringUtils.leftPad(" ", grupo.getLeftPad()) + "TOTAL " + grupo.getDescricao(), celNegAlinEsq);

		for (ResumoProdutoQuimico resumo : grupo.getResumos().values()) {
			addCelulaTexto(1, linha, resumo.getDescricaoProduto(), celNormalAlinEsq);
			List<String> nomesProdutos = new LinkedList<String>(resumo.getValores().keySet());
			for (int i = 0; i < nomesProdutos.size(); i++) {
				ValoresResumo valor = resumo.getValores().get(nomesProdutos.get(i));

				addCelulaNumero(i + 2, linha, valor.getQtdConsumo(), this.celNumerica);
				if (this.tipoRelatorio == 2 && this.tipoExibicao != TipoExibicao.MENSAL) { // FINANCEIRO
					addCelulaNumero(i + 3, linha, valor.getVlrUnitario(), this.celNumericaCinco);
					addCelulaNumero(i + 4, linha, valor.getVlrTotal(), this.celNumerica);
				}
			}

			linha++;
		}
	}

	private boolean exibeTotalizadorUnidadeOperacional(Integer indice, List<RelatorioGerencial> registros) {
		RelatorioGerencial produto = registros.get(indice);
		boolean totaliza = false;

		if (indice == registros.size() - 1) {
			totaliza = true;
		} else if (indice < registros.size()) {
			if (!(produto.getCodigoUnidadeOperacional() == registros.get(indice + 1).getCodigoUnidadeOperacional()
					&& produto.getTipoUnidadeOperacional() == registros.get(indice + 1).getTipoUnidadeOperacional() && produto.getCodigoLocalidade() == registros
					.get(indice + 1).getCodigoLocalidade())) {
				totaliza = true;
			}
		}

		return totaliza;
	}

	private boolean exibeTotalizadorLocalidade(Integer indice, List<RelatorioGerencial> registros) {
		RelatorioGerencial produto = registros.get(indice);
		boolean totaliza = false;

		if (indice == registros.size() - 1) {
			totaliza = true;
		} else if (indice < registros.size()) {
			if (!(produto.getCodigoLocalidade() == registros.get(indice + 1).getCodigoLocalidade() && produto.getCodigoMunicipio() == registros.get(indice + 1)
					.getCodigoMunicipio())) {
				totaliza = true;
			}
		}

		return totaliza;
	}

	private boolean exibeTotalizadorMunicipio(Integer indice, List<RelatorioGerencial> registros) {
		RelatorioGerencial produto = registros.get(indice);
		boolean totaliza = false;

		if (indice == registros.size() - 1) {
			totaliza = true;
		} else if (indice < registros.size()) {
			if (!(produto.getCodigoUnidadeNegocio() == registros.get(indice + 1).getCodigoUnidadeNegocio() && produto.getCodigoMunicipio() == registros.get(
					indice + 1).getCodigoMunicipio())) {
				totaliza = true;
			}
		}

		return totaliza;
	}

	private boolean exibeTotalizadorUnidadeNegocio(Integer indice, List<RelatorioGerencial> registros) {
		RelatorioGerencial produto = registros.get(indice);
		boolean totaliza = false;

		if (indice == registros.size() - 1) {
			totaliza = true;
		} else if (indice < registros.size()) {
			if (!(produto.getCodigoUnidadeNegocio() == this.relatorioComFiltro.get(indice + 1).getCodigoUnidadeNegocio())) {
				totaliza = true;
			}
		}

		return totaliza;
	}

	private boolean exibeTotalizadorRegional(Integer indice, List<RelatorioGerencial> registros) {
		RelatorioGerencial produto = registros.get(indice);
		boolean totaliza = false;

		if (indice == registros.size() - 1) {
			totaliza = true;
		} else if (indice < registros.size()) {
			if (!(produto.getCodigoRegional() == this.relatorioComFiltro.get(indice + 1).getCodigoRegional())) {
				totaliza = true;
			}
		}

		return totaliza;
	}
}
