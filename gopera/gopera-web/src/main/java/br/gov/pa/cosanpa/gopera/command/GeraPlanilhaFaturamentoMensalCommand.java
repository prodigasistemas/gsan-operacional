package br.gov.pa.cosanpa.gopera.command;

import java.text.SimpleDateFormat;
import java.util.List;

import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;

import org.jboss.logging.Logger;

import br.gov.pa.cosanpa.gopera.exception.UnidadeConsumidoraNaoRelacionada;
import br.gov.pa.cosanpa.gopera.fachada.IRelatorioEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.EnergiaEletricaDados;
import br.gov.pa.cosanpa.gopera.model.RelatorioExcel;

public class GeraPlanilhaFaturamentoMensalCommand extends AbstractCommandGeraPlanilha {
	private static Logger logger = Logger.getLogger(GeraPlanilhaFaturamentoMensalCommand.class);

	List<EnergiaEletricaDados> energiaEletricaDados = null;

	public GeraPlanilhaFaturamentoMensalCommand(RelatorioExcel relatorioExcel, WritableSheet sheet) {
		super(relatorioExcel, sheet);

	}

	@Override
	public void execute(InformacoesParaRelatorio informacoes, IRelatorioEnergiaEletrica fachadaRel) throws Exception {
		try {
			energiaEletricaDados = fachadaRel.getEnergiaEletricaDados(informacoes.getPrimeiroDiaReferencia());

			int i = 0, j = 0;
			// Adiciona data de referencia
			SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
			WritableFont times11ptbold3 = new WritableFont(WritableFont.TAHOMA, 11, WritableFont.BOLD);
			WritableCellFormat fontBold4 = new WritableCellFormat(times11ptbold3);
			fontBold4.setAlignment(Alignment.RIGHT);
			Label nmDataReferencia4 = new Label(3, 0, bundle.getText("mes_referencia") + ": " + informacoes.getReferencia(), fontBold4);
			sheet.addCell(nmDataReferencia4);

			for (i = 0; i < this.energiaEletricaDados.size(); i++) {
				j = i + 2;
				this.addLabel(sheet, 0, j, formataData.format(this.energiaEletricaDados.get(i).getDataLeitura()));
				this.addInteiro(sheet, 1, j, this.energiaEletricaDados.get(i).getCodigoUC());
				this.addLong(sheet, 2, j, Long.parseLong(this.energiaEletricaDados.get(i).getFatura()));
				this.addLabelLeft(sheet, 3, j, this.energiaEletricaDados.get(i).getNome());
				this.addLabelLeft(sheet, 4, j, this.energiaEletricaDados.get(i).getEndereco());
				this.addLabelLeft(sheet, 5, j, this.energiaEletricaDados.get(i).getBairro());
				this.addInteiro(sheet, 6, j, Integer.parseInt(this.energiaEletricaDados.get(i).getCep()));
				this.addLabel(sheet, 7, j, this.energiaEletricaDados.get(i).getCodigoGrupo());
				this.addInteiro(sheet, 8, j, Integer.parseInt(this.energiaEletricaDados.get(i).getCodigoTipo()));
				this.addNumeroSD(sheet, 9, j, this.energiaEletricaDados.get(i).getC_Kwh_Cv());
				this.addNumeroSD(sheet, 10, j, this.energiaEletricaDados.get(i).getC_Kwh_FS());
				this.addNumeroSD(sheet, 11, j, this.energiaEletricaDados.get(i).getC_Kwh_FU());
				this.addNumeroSD(sheet, 12, j, this.energiaEletricaDados.get(i).getC_Kwh_PS());
				this.addNumeroSD(sheet, 13, j, this.energiaEletricaDados.get(i).getC_Kwh_PU());
				this.addNumero(sheet, 14, j, this.energiaEletricaDados.get(i).getDCv());
				this.addNumero(sheet, 15, j, this.energiaEletricaDados.get(i).getDFS());
				this.addNumero(sheet, 16, j, this.energiaEletricaDados.get(i).getDPS());
				this.addNumero(sheet, 17, j, this.energiaEletricaDados.get(i).getDFU());
				this.addNumero(sheet, 18, j, this.energiaEletricaDados.get(i).getDPU());
				this.addNumero(sheet, 19, j, this.energiaEletricaDados.get(i).getDem_Fat_Cv());
				this.addNumero(sheet, 20, j, this.energiaEletricaDados.get(i).getDem_Fat_FP());
				this.addNumero(sheet, 21, j, this.energiaEletricaDados.get(i).getDem_Fat_Pt());
				this.addNumero(sheet, 22, j, this.energiaEletricaDados.get(i).getDem_Med_Cv());
				this.addNumero(sheet, 23, j, this.energiaEletricaDados.get(i).getDem_Med_FP());
				this.addNumero(sheet, 24, j, this.energiaEletricaDados.get(i).getDem_Med_Pt());
				this.addNumero(sheet, 25, j, this.energiaEletricaDados.get(i).getDem_Ut_Cv());
				this.addNumero(sheet, 26, j, this.energiaEletricaDados.get(i).getDem_Ut_FP());
				this.addNumero(sheet, 27, j, this.energiaEletricaDados.get(i).getDem_Ut_Pt());
				this.addNumero(sheet, 28, j, this.energiaEletricaDados.get(i).getFcarga());
				this.addNumero(sheet, 29, j, this.energiaEletricaDados.get(i).getFpot_FP());
				this.addNumero(sheet, 30, j, this.energiaEletricaDados.get(i).getFpot_Cv());
				this.addNumero(sheet, 31, j, this.energiaEletricaDados.get(i).getFpot_Pt());
				this.addNumero(sheet, 32, j, this.energiaEletricaDados.get(i).getVlr_Ult_FP());
				this.addNumero(sheet, 33, j, this.energiaEletricaDados.get(i).getVlr_Ult_Pt());
				this.addNumero(sheet, 34, j, this.energiaEletricaDados.get(i).getVlr_dem_Cv());
				this.addNumero(sheet, 35, j, this.energiaEletricaDados.get(i).getVlr_dem_FP());
				this.addNumero(sheet, 36, j, this.energiaEletricaDados.get(i).getVlr_dem_Pt());
				this.addNumero(sheet, 37, j, this.energiaEletricaDados.get(i).getVlr_Ult_Cv());
				this.addNumero(sheet, 38, j, this.energiaEletricaDados.get(i).getVlr_Multas());
				this.addNumero(sheet, 39, j, this.energiaEletricaDados.get(i).getVlr_Kwh_FS());
				this.addNumero(sheet, 40, j, this.energiaEletricaDados.get(i).getVlr_Kwh_FU());
				this.addNumero(sheet, 41, j, this.energiaEletricaDados.get(i).getVlr_Kwh_Cv());
				this.addNumero(sheet, 42, j, this.energiaEletricaDados.get(i).getVlr_Kwh_PS());
				this.addNumero(sheet, 43, j, this.energiaEletricaDados.get(i).getVlr_Kwh_PU());
				this.addNumero(sheet, 44, j, this.energiaEletricaDados.get(i).getVlr_ICMS());
				this.addNumero(sheet, 45, j, this.energiaEletricaDados.get(i).getVlr_DRe_Cv());
				this.addNumero(sheet, 46, j, this.energiaEletricaDados.get(i).getVlr_DRe_Pt());
				this.addNumero(sheet, 47, j, this.energiaEletricaDados.get(i).getVlr_DRe_FP());
				this.addNumero(sheet, 48, j, this.energiaEletricaDados.get(i).getVlr_ERe_FP());
				this.addNumero(sheet, 49, j, this.energiaEletricaDados.get(i).getVlr_ERe_Cv());
				this.addNumero(sheet, 50, j, this.energiaEletricaDados.get(i).getVlr_ERe_Pt());
				this.addNumero(sheet, 51, j, this.energiaEletricaDados.get(i).getVlr_Total());
				// Codigo UC
				if (null == this.energiaEletricaDados.get(i).getUnidadeConsumidora()
						|| null == this.energiaEletricaDados.get(i).getUnidadeConsumidora().getCodigo()) {
					continue;
					//throw new UnidadeConsumidoraNaoRelacionada();
				}
				this.addInteiro(sheet, 52, j, this.energiaEletricaDados.get(i).getUnidadeConsumidora().getCodigo());
				this.addLabel(sheet, 53, j, informacoes.getReferencia());
				this.addLabel(sheet, 54, j, "COSANPA");
				// Consumo Kwh
				Double consumoKwh = this.energiaEletricaDados.get(i).getC_Kwh_Cv() + this.energiaEletricaDados.get(i).getC_Kwh_FS()
						+ this.energiaEletricaDados.get(i).getC_Kwh_FU() + this.energiaEletricaDados.get(i).getC_Kwh_PS()
						+ this.energiaEletricaDados.get(i).getC_Kwh_PU();
				this.addNumeroSD(sheet, 55, j, consumoKwh);
				// Ativo
				if (this.energiaEletricaDados.get(i).getUnidadeConsumidora().getAtivo() != null) {
					if (this.energiaEletricaDados.get(i).getUnidadeConsumidora().getAtivo())
						this.addLabel(sheet, 56, j, "A");
					else
						this.addLabel(sheet, 56, j, "I");
				} else
					this.addLabel(sheet, 56, j, "I");

				this.addLabelLeft(sheet, 57, j, this.energiaEletricaDados.get(i).getUnidadeConsumidora().getUnidadeNegocioProxy().getNome());
				this.addLabelLeft(sheet, 58, j, this.energiaEletricaDados.get(i).getUnidadeConsumidora().getLocalidadeProxy().getNome());
				this.addLabelLeft(sheet, 59, j, this.energiaEletricaDados.get(i).getUnidadeConsumidora().getMunicipioProxy().getNome());
				this.addLabelLeft(sheet, 60, j, this.energiaEletricaDados.get(i).getUnidadeConsumidora().getRegionalProxy().getNome());

				this.addLabelLeft(sheet, 68, j, this.energiaEletricaDados.get(i).getUnidadeConsumidora().getUnidadeOperacional());
				this.addLabelLeft(sheet, 69, j, this.energiaEletricaDados.get(i).getUnidadeConsumidora().getUnidadeNegocioProxy().getNome());
				this.addLabelLeft(sheet, 70, j, this.energiaEletricaDados.get(i).getUnidadeConsumidora().getEndereco());
				if (this.energiaEletricaDados.get(i).getContrato() != null)
					this.addLabel(sheet, 71, j, this.energiaEletricaDados.get(i).getContrato().getSubGrupoTarifario() + " "
							+ this.energiaEletricaDados.get(i).getContrato().getModalidadeTarifaria());
				this.addLabel(sheet, 72, j, this.energiaEletricaDados.get(i).getUnidadeConsumidora().getEquipamento());
				if (this.energiaEletricaDados.get(i).getContrato() != null)
					this.addNumero(sheet, 73, j, this.energiaEletricaDados.get(i).getContrato().getTensaoNominal());
				this.addNumero(sheet, 74, j, this.energiaEletricaDados.get(i).getCaj_fat_Pt());
				this.addLabel(sheet, 75, j, this.energiaEletricaDados.get(i).getUnidadeConsumidora().getNaturezaAtividade());
				if (this.energiaEletricaDados.get(i).getUnidadeConsumidora().getDataInstalacao() != null)
					this.addLabel(sheet, 76, j, formataData.format(this.energiaEletricaDados.get(i).getUnidadeConsumidora().getDataInstalacao()));
				this.addLabel(sheet, 77, j, this.energiaEletricaDados.get(i).getUnidadeConsumidora().getPotencia());
				this.addNumero(sheet, 78, j, this.energiaEletricaDados.get(i).getCult_Demanda());
				this.addNumero(sheet, 79, j, this.energiaEletricaDados.get(i).getCfat_pot_FP_Umida());
				this.addNumero(sheet, 80, j, this.energiaEletricaDados.get(i).getCfat_pot_Pt_Umida());
				this.addNumero(sheet, 81, j, this.energiaEletricaDados.get(i).getCfat_pot_FP_Seca());
				this.addNumero(sheet, 82, j, this.energiaEletricaDados.get(i).getCfat_pot_Pt_Seca());
				this.addNumero(sheet, 83, j, this.energiaEletricaDados.get(i).getCfat_pot_Cv());
				if (this.energiaEletricaDados.get(i).getCdias_fat() != null)
					this.addLabel(sheet, 84, j, this.energiaEletricaDados.get(i).getCdias_fat().toString());
				this.addNumero(sheet, 85, j, this.energiaEletricaDados.get(i).getCcons_KW());
				this.addNumero(sheet, 86, j, this.energiaEletricaDados.get(i).getCcons_total());
				this.addNumero(sheet, 87, j, this.energiaEletricaDados.get(i).getCtarifa_media());
				if (this.energiaEletricaDados.get(i).getContrato() != null)
					this.addNumero(sheet, 88, j, this.energiaEletricaDados.get(i).getContrato().getPotenciaInstalada());
				if (this.energiaEletricaDados.get(i).getUnidadeConsumidora().getAlimentador() != null)
					this.addLabel(sheet, 89, j, this.energiaEletricaDados.get(i).getUnidadeConsumidora().getAlimentador());
				this.addNumero(sheet, 90, j, round(this.energiaEletricaDados.get(i).getCvariacao_consumo()));
				this.addNumero(sheet, 91, j, this.energiaEletricaDados.get(i).getCfat_carga());
				this.addNumero(sheet, 92, j, this.energiaEletricaDados.get(i).getCfat_carga_Pt());
				this.addNumero(sheet, 93, j, this.energiaEletricaDados.get(i).getCfat_carga_FP());
				this.addNumero(sheet, 95, j, this.energiaEletricaDados.get(i).getCcons_FP_Umida_Ind());
				this.addNumero(sheet, 96, j, this.energiaEletricaDados.get(i).getCcons_FP_Umida_Cap());
				this.addNumero(sheet, 97, j, this.energiaEletricaDados.get(i).getCcons_FP_Seca_Ind());
				this.addNumero(sheet, 98, j, this.energiaEletricaDados.get(i).getCcons_FP_Seca_Cap());
				this.addNumero(sheet, 99, j, this.energiaEletricaDados.get(i).getCcons_KW_FP_Seca());
				this.addNumero(sheet, 100, j, this.energiaEletricaDados.get(i).getCcons_KW_Pt_Seca());
				this.addNumero(sheet, 101, j, this.energiaEletricaDados.get(i).getCcons_KW_FP_Umida());
				this.addNumero(sheet, 102, j, this.energiaEletricaDados.get(i).getCcons_KW_Pt_Umida());
				this.addNumero(sheet, 103, j, this.energiaEletricaDados.get(i).getCvlr_Consumo_FP_Seca());
				this.addNumero(sheet, 104, j, this.energiaEletricaDados.get(i).getCvlr_Consumo_Pt_Seca());
				this.addNumero(sheet, 105, j, this.energiaEletricaDados.get(i).getCvlr_Consumo_FP_Umida());
				this.addNumero(sheet, 106, j, this.energiaEletricaDados.get(i).getCvlr_Consumo_Pt_Umida());
				this.addNumero(sheet, 107, j, this.energiaEletricaDados.get(i).getCcons_KW_Cv());
				this.addNumero(sheet, 108, j, this.energiaEletricaDados.get(i).getCvlr_Consumo_Cv());
				this.addLabel(sheet, 109, j, this.energiaEletricaDados.get(i).getReferenciaMulta());
				if (this.energiaEletricaDados.get(i).getContrato() != null)
					this.addLabel(sheet, 111, j, this.energiaEletricaDados.get(i).getContrato().getAgrupadorFatura());
			}
		} catch (UnidadeConsumidoraNaoRelacionada e) {
			logger.error("Erro ao gerar planinha", e);
			throw e;
		} catch (Exception e) {
			logger.error("Erro ao gerar planinha", e);
			throw new Exception("Erro ao gerar planilha. ", e);
		}

	}
}
