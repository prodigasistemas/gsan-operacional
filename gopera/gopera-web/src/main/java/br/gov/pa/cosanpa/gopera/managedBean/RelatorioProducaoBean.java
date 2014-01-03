package br.gov.pa.cosanpa.gopera.managedBean;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.jboss.logging.Logger;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import br.gov.pa.cosanpa.gopera.fachada.IRelatorioProducao;
import br.gov.pa.cosanpa.gopera.fachada.IUnidadeConsumidora;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.RelatorioGerencial;
import br.gov.pa.cosanpa.gopera.model.UnidadeConsumidoraOperacional;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@ManagedBean
@SessionScoped
@SuppressWarnings("rawtypes")
public class RelatorioProducaoBean extends BaseRelatorioBean<RelatorioGerencial> {

	private static Logger logger = Logger.getLogger(RelatorioProducaoBean.class);

	private RelatorioGerencial registro = new RelatorioGerencial();
	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<UnidadeConsumidoraOperacional> unidadesOperacional = new ArrayList<UnidadeConsumidoraOperacional>();
	private List<List> relatorio = new ArrayList<List>();

	private String referencia;
	private Integer tipoUnidadeOperacional;

	@EJB
	private IUnidadeConsumidora fachadaUC;
	@EJB
	private IRelatorioProducao fachadaRel;

	public RelatorioGerencial getRegistro() {
		return registro;
	}

	public void setRegistro(RelatorioGerencial registro) {
		this.registro = registro;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Integer getTipoUnidadeOperacional() {
		return tipoUnidadeOperacional;
	}

	public void setTipoUnidadeOperacional(Integer tipoUnidadeOperacional) {
		this.tipoUnidadeOperacional = tipoUnidadeOperacional;
	}

	public RelatorioProducaoBean() {
		this.registro = new RelatorioGerencial();
	}

	public List<RegionalProxy> getRegionais() {
		try {
			regionais = fachadaUC.getListaRegional(usuarioProxy);
			regionais.add(0, new RegionalProxy(-1, "Selecione..."));
			return regionais;
		} catch (Exception e) {
			mostrarMensagemErro("Erro ao consultar sistema externo.");
		}
		return regionais;
	}

	public List<UnidadeNegocioProxy> getUnidadesNegocio() {
		if ((this.registro.getCodigoRegional() != null)) {
			try {
				this.unidadesNegocio = fachadaUC.getListaUnidadeNegocio(usuarioProxy, this.registro.getCodigoRegional());
				this.unidadesNegocio.add(0, new UnidadeNegocioProxy(-1, "Selecione..."));
				return unidadesNegocio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
		unidadesNegocio.add(0, new UnidadeNegocioProxy(-1, "Selecione..."));
		return unidadesNegocio;
	}

	public List<UnidadeConsumidoraOperacional> getUnidadesOperacional() {
		if (this.tipoUnidadeOperacional != null && this.tipoUnidadeOperacional != 0) {
			try {
				this.unidadesOperacional = fachadaUC.getListaUnidadeOperacional(this.tipoUnidadeOperacional, this.registro.getCodigoRegional(),
						this.registro.getCodigoUnidadeNegocio(), 0, 0);
				this.unidadesOperacional.add(0, new UnidadeConsumidoraOperacional(-1, "Selecione..."));
				return this.unidadesOperacional;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		unidadesOperacional = new ArrayList<UnidadeConsumidoraOperacional>();
		unidadesOperacional.add(0, new UnidadeConsumidoraOperacional(-1, "Selecione..."));
		return unidadesOperacional;
	}

	public String iniciar() {
		this.registro = new RelatorioGerencial();
		referencia = this.filtroData(new Date(), "MM/yyyy");
		return "RelatorioProducao.jsf";
	}

	private void relatorioProducaoDistribuicao() throws Exception {
		Date mesReferencia = primeiroDiaMes(this.referencia);
		relatorio = fachadaRel.getProducaoDistribuicaoMensal(this.registro.getCodigoRegional(), this.registro.getCodigoUnidadeNegocio(),
				this.tipoUnidadeOperacional, this.registro.getCodigoUnidadeOperacional(), mesReferencia);
	}

	public void exibir() {
		try {
			relatorioProducaoDistribuicao();
			if (relatorio.size() == 0) {
				logger.info(bundle.getText("erro_nao_existe_retorno_filtro"));
				mostrarMensagemErro(bundle.getText("erro_nao_existe_retorno_filtro"));
				return;
			}
			// GERA PLANILHA
			super.nomeArquivo = "ProducaoDistribuicao-" + this.filtroData(primeiroDiaMes(this.referencia), "MM-yyyy");
			super.iniciar();
			WritableWorkbook wb = geraPlanilha();
			WritableSheet sheet = wb.getSheet(0);
			this.defineConteudo(sheet);
			downloadFile(wb);
		} catch (Exception e) {
			mostrarMensagemErro(bundle.getText("erro_exibir_relatorio") + ": " + e.getMessage());
			logger.error(bundle.getText("erro_exibir_relatorio"), e);
		}
	}

	private void defineConteudo(WritableSheet sheet) throws WriteException, ParseException {
		String dataMedicao = "", horaMedicao = "", unidadeNegocio = "", setor = "";
		Integer rowDados = 4, rowSetor1 = 0, rowSetor2 = 0;
		Double vazao, volume, pressao, horas;

		// CABEÇALHO
		sheet.setName(bundle.getText("producao_distribuicao"));
		
		StringBuilder titulo = new StringBuilder();
		titulo.append(bundle.getText("companhia_saneamento_para") + "\n");
		titulo.append(bundle.getText("unidade_serv_control_oper_redu_perdas") + "\n");
		titulo.append(bundle.getText("diretoria_operacao") + "\n");
		titulo.append(bundle.getText("unidade_exec_pilom_macromed") + "\n");
		titulo.append(bundle.getText("rel_mens_prod_distrib"));
		addLabel(sheet, 0, 0, titulo.toString(), wcfLabelBold);
		
		sheet.mergeCells(0, 0, 7, 0);
		addLabel(sheet, 0, 2, bundle.getText("referencia") + ": " + this.referencia, wcfLabelBold);
		sheet.mergeCells(0, 2, 7, 2);

		for (List producao : this.relatorio) {
			if (!unidadeNegocio.equals(producao.get(3).toString())) {
				unidadeNegocio = producao.get(3).toString();
				// Mescla Células
				sheet.mergeCells(0, rowDados, 7, rowDados);
				addLabel(sheet, 0, rowDados, unidadeNegocio, wcfLabelHeader);
				rowDados++;
				addLabel(sheet, 0, rowDados, bundle.getText("setor"), wcfLabelBoldW);
				addLabel(sheet, 1, rowDados, bundle.getText("ponto_medicao"), wcfLabelBoldW);
				addLabel(sheet, 2, rowDados, bundle.getText("data_medicao"), wcfLabelBoldW);
				addLabel(sheet, 3, rowDados, bundle.getText("hora_medicao"), wcfLabelBoldW);
				addLabel(sheet, 4, rowDados, bundle.getText("pressao_mca"), wcfLabelBoldW);
				addLabel(sheet, 5, rowDados, bundle.getText("vazao_m3h"), wcfLabelBoldW);
				addLabel(sheet, 6, rowDados, bundle.getText("horas_trabalhadas"), wcfLabelBoldW);
				addLabel(sheet, 7, rowDados, bundle.getText("volume_m3"), wcfLabelBoldW);
				rowDados++;
			}
			/*
			 * 0 - greg_id 1 - greg_nmregional 2 - uneg_id 3 -
			 * uneg_nmunidadenegocio 4 - loca_id 5 - loca_nmlocalidade 6 -
			 * unop_tipo 7 - unop_id 8 - unop_nmunidadeoperacional 9 -
			 * referencia 10 - unop_dtmedicao 11 - unop_pressao 12 - unop_vazao
			 * 13 - unop_horas 14 - unop_volume
			 */

			// DATA/HORA MEDIÇÃO
			SimpleDateFormat formataDataSQL = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formataHora = new SimpleDateFormat("hh:mm");

			dataMedicao = formataData.format(formataDataSQL.parse(producao.get(10).toString()));
			horaMedicao = formataHora.format(formataDataSQL.parse(producao.get(10).toString()));
			// PRESSÃO
			pressao = (producao.get(11) != null ? Double.parseDouble(producao.get(11).toString()) : 0.0);
			// VAZÃO
			vazao = (producao.get(12) != null ? Double.parseDouble(producao.get(12).toString()) : 0.0);
			// HORAS
			horas = (producao.get(13) != null ? Double.parseDouble(producao.get(13).toString()) : 0.0);
			// VOLUME
			volume = (producao.get(14) != null ? Double.parseDouble(producao.get(14).toString()) : 0.0);

			if (!setor.equals(producao.get(5).toString())) {
				if (rowSetor1 != rowSetor2)
					sheet.mergeCells(0, rowSetor1, 0, rowSetor2);
				rowSetor1 = rowDados;
				rowSetor2 = 0;
				setor = producao.get(5).toString();
				addLabel(sheet, 0, rowDados, setor, wcfLabel);
			}
			addLabel(sheet, 1, rowDados, producao.get(8).toString(), wcfLabel);
			addLabel(sheet, 2, rowDados, dataMedicao, wcfLabel);
			addLabel(sheet, 3, rowDados, horaMedicao, wcfLabel);
			addNumero(sheet, 4, rowDados, round(pressao), wcfNumero);
			addNumero(sheet, 5, rowDados, round(vazao), wcfNumero);
			addNumero(sheet, 6, rowDados, horas, wcfInteiro);
			addNumero(sheet, 7, rowDados, round(volume), wcfNumero);
			rowSetor2 = rowDados;
			rowDados++;
		}
		if (rowSetor1 != rowSetor2)
			sheet.mergeCells(0, rowSetor1, 0, rowSetor2);
	}

	public double round(double d) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		DecimalFormat formata = new DecimalFormat("#.##", symbols);
		return Double.valueOf(Double.parseDouble(formata.format(d)));
	}

}
