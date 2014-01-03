package br.gov.pa.cosanpa.gopera.managedBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.jboss.logging.Logger;
import org.primefaces.model.UploadedFile;

import br.gov.pa.cosanpa.gopera.fachada.IContratoEnergia;
import br.gov.pa.cosanpa.gopera.fachada.IEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.fachada.IUnidadeConsumidora;
import br.gov.pa.cosanpa.gopera.model.ContratoEnergia;
import br.gov.pa.cosanpa.gopera.model.EnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.EnergiaEletricaDados;
import br.gov.pa.cosanpa.gopera.model.UnidadeConsumidora;
import br.gov.pa.cosanpa.gopera.model.UsuarioProxy;

@ManagedBean
@SessionScoped
public class ImportarEnergiaBean extends BaseBean<EnergiaEletrica> {

	private static final Logger logger = Logger.getLogger(ImportarEnergiaBean.class);

	@EJB
	private IEnergiaEletrica fachada;
	@EJB
	private IProxy fachadaProxy;
	@EJB
	private IUnidadeConsumidora fachadaUC;
	@EJB
	private IContratoEnergia fachadaContrato;

	private UploadedFile arquivo;
	private EnergiaEletricaDados energiaDados;
	private UsuarioProxy usuarioProxy = (UsuarioProxy) session.getAttribute("usuarioProxy");

	public ImportarEnergiaBean() {
		energiaDados = new EnergiaEletricaDados();
	}

	public EnergiaEletricaDados getEnergiaDados() {
		return energiaDados;
	}

	public void setEnergiaDados(EnergiaEletricaDados energiaDados) {
		this.energiaDados = energiaDados;
	}

	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new EnergiaEletrica();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "ImportarEnergia.jsf");
		this.getPaginasRetorno().put("novo", "ImportarEnergia_Cadastro.jsf");
		this.getPaginasRetorno().put("confirmar", "ImportarEnergia_Cadastro.jsf");
		this.getPaginasRetorno().put("editar", "ImportarEnergia_Consulta.jsf");
		this.getPaginasRetorno().put("consultar", "ImportarEnergia_Consulta.jsf");
		this.getPaginasRetorno().put("excluir", "ImportarEnergia.jsf");
		this.getPaginasRetorno().put("voltar", "ImportarEnergia.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");
	}

	@Override
	public String novo() {
		this.registro = new EnergiaEletrica();
		return super.novo();
	}

	@Override
	public String consultar() {
		carregar();
		return super.consultar();
	}

	@Override
	public String alterar() {
		carregar();
		return super.alterar();
	}

	public void carregar() {
		try {
			this.registro = fachada.obterEnergia(this.registro.getCodigo());
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar Informações do Arquivo");
			e.printStackTrace();
		}
	}

	public UploadedFile getArquivo() {
		return arquivo;
	}

	public void setArquivo(UploadedFile arquivo) {
		this.arquivo = arquivo;
		try {
			String arquivoAux = arquivo.getFileName();
			Integer mes = Integer.parseInt(arquivoAux.substring(8, 10));
			Integer ano = Integer.parseInt(arquivoAux.substring(10, 14));
			Calendar gc = Calendar.getInstance();
			gc.set(ano, mes - 1, 1);
			Date dataReferencia = gc.getTime();
			this.registro.setDataReferencia(dataReferencia);
		} catch (NumberFormatException nfe) {
			this.mostrarMensagemErro(bundle.getText("erro_nome_arquivo"));
		} catch (Exception e) {
			this.mostrarMensagemErro(bundle.getText("erro_carga_arquivo"));
		}
	}

	@Override
	public String cadastrar() {
		if (validarArquivo()) {
			return super.cadastrar();
		}
		return "";
	}

	private boolean validarArquivo() {
		try {
			if (!fachadaProxy.getEnergiaEletricaVerificaData(this.registro.getDataReferencia())) {
				return true;
			} else {
				this.mostrarMensagemErro(bundle.getText("erro_referencia_arquivo_cadastrada"));
				return false;
			}
		} catch (Exception e) {
			logger.error(bundle.getText("erro_validacao_arquivo"), e);
			this.mostrarMensagemErro(bundle.getText("erro_validacao_arquivo"));
		}
		return false;
	}

	public String confirmar() {
		try {
			if (arquivo != null) {
				String diretorio = fachadaProxy.getParametroSistema(9);
				new File(diretorio).mkdir();
				String arquivoAux = diretorio + arquivo.getFileName();
				;
				copyFile(arquivoAux, arquivo.getInputstream());
				importaArquivo(arquivoAux);
				mostrarMensagemSucesso("Arquivo " + arquivo.getFileName() + " foi enviado.");
			}
		} catch (IOException e) {
			mostrarMensagemErro("Arquivo " + arquivo.getFileName() + " não foi enviado.");
			e.printStackTrace();
		} catch (BiffException e) {
			mostrarMensagemErro("Arquivo " + arquivo.getFileName() + " não foi importado.");
			e.printStackTrace();
		} catch (Exception e) {
			mostrarMensagemErro("Arquivo " + arquivo.getFileName() + " não foi enviado.");
			e.printStackTrace();
		}
		return "";
	}

	/*
	 * // Metodo responsavel pelo upload do arquivo public void
	 * handleFileUpload(FileUploadEvent event) { try { String filename = "D:\\"
	 * + event.getFile().getFileName(); cadastrar(); copyFile(filename,
	 * event.getFile().getInputstream()); importaArquivo(filename);
	 * mostrarMensagemSucesso("Arquivo " + event.getFile().getFileName() +
	 * " foi enviado."); } catch (IOException e) {
	 * mostrarMensagemErro("Arquivo " + event.getFile().getFileName() +
	 * " não foi enviado."); e.printStackTrace(); } catch (BiffException e) {
	 * mostrarMensagemErro("Arquivo " + event.getFile().getFileName() +
	 * " não foi importado."); e.printStackTrace(); } }
	 */

	private void copyFile(String fileName, java.io.InputStream inputStream) throws IOException {
		FileOutputStream out = new FileOutputStream(new File(fileName));

		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = inputStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		inputStream.close();
		out.flush();
		out.close();
	}

	private void importaArquivo(String nomeArquivo) throws BiffException, IOException {
		try {
			Workbook workbook = Workbook.getWorkbook(new File(nomeArquivo));
			Sheet sheet = workbook.getSheet(0);
			Double valorTotal = 0.0;
			Integer qtdUC = 0;
			int linhas = sheet.getRows();
			this.registro.getDados().clear();
			EnergiaEletricaDados dados;
			for (int i = 1; i < linhas; i++) {
				dados = new EnergiaEletricaDados();

				Cell col01 = sheet.getCell(0, i);
				Cell col02 = sheet.getCell(1, i);
				Cell col03 = sheet.getCell(2, i);
				Cell col04 = sheet.getCell(3, i);
				Cell col05 = sheet.getCell(4, i);
				Cell col06 = sheet.getCell(5, i);
				Cell col07 = sheet.getCell(6, i);
				Cell col08 = sheet.getCell(7, i);
				Cell col09 = sheet.getCell(8, i);
				Cell col10 = sheet.getCell(9, i);
				Cell col11 = sheet.getCell(10, i);
				Cell col12 = sheet.getCell(11, i);
				Cell col13 = sheet.getCell(12, i);
				Cell col14 = sheet.getCell(13, i);
				Cell col15 = sheet.getCell(14, i);
				Cell col16 = sheet.getCell(15, i);
				Cell col17 = sheet.getCell(16, i);
				Cell col18 = sheet.getCell(17, i);
				Cell col19 = sheet.getCell(18, i);
				Cell col20 = sheet.getCell(19, i);
				Cell col21 = sheet.getCell(20, i);
				Cell col22 = sheet.getCell(21, i);
				Cell col23 = sheet.getCell(22, i);
				Cell col24 = sheet.getCell(23, i);
				Cell col25 = sheet.getCell(24, i);
				Cell col26 = sheet.getCell(25, i);
				Cell col27 = sheet.getCell(26, i);
				Cell col28 = sheet.getCell(27, i);
				Cell col29 = sheet.getCell(28, i);
				Cell col30 = sheet.getCell(29, i);
				Cell col31 = sheet.getCell(30, i);
				Cell col32 = sheet.getCell(31, i);
				Cell col33 = sheet.getCell(32, i);
				Cell col34 = sheet.getCell(33, i);
				Cell col35 = sheet.getCell(34, i);
				Cell col36 = sheet.getCell(35, i);
				Cell col37 = sheet.getCell(36, i);
				Cell col38 = sheet.getCell(37, i);
				Cell col39 = sheet.getCell(38, i);
				Cell col40 = sheet.getCell(39, i);
				Cell col41 = sheet.getCell(40, i);
				Cell col42 = sheet.getCell(41, i);
				Cell col43 = sheet.getCell(42, i);
				Cell col44 = sheet.getCell(43, i);
				Cell col45 = sheet.getCell(44, i);
				Cell col46 = sheet.getCell(45, i);
				Cell col47 = sheet.getCell(46, i);
				Cell col48 = sheet.getCell(47, i);
				Cell col49 = sheet.getCell(48, i);
				Cell col50 = sheet.getCell(49, i);
				Cell col51 = sheet.getCell(50, i);
				Cell col52 = sheet.getCell(51, i);

				String CMPT = col01.getContents();
				String UC = col02.getContents();
				String LLLI = col03.getContents();
				String NOME = col04.getContents();
				String ENDE = col05.getContents();
				String BAIRRO = col06.getContents();
				String CEP = col07.getContents();
				String COD_FATUR = col08.getContents();
				String COD_FORNEC = col09.getContents();
				String EN_TP = col10.getContents();
				String EN_FP = col11.getContents();
				String EN_FPU = col12.getContents();
				String EN_PT = col13.getContents();
				String EN_PTU = col14.getContents();
				String DU = col15.getContents();
				String DFS = col16.getContents();
				String DPS = col17.getContents();
				String DFU = col18.getContents();
				String DPU = col19.getContents();
				String DEM_FAT_TP = col20.getContents();
				String DEM_FAT_FP = col21.getContents();
				String DEM_FAT_PT = col22.getContents();
				String DEM_TP = col23.getContents();
				String DEM_FP = col24.getContents();
				String DEM_PT = col25.getContents();
				String DEM_UT_TP = col26.getContents();
				String DEM_UT_FP = col27.getContents();
				String DEM_UT_PT = col28.getContents();
				String FC_TP = col29.getContents();
				String FP_F = col30.getContents();
				String FP_G = col31.getContents();
				String FP_P = col32.getContents();
				String VL_DEMU_FP = col33.getContents();
				String VL_DEMU_PT = col34.getContents();
				String VL_DEM_TP = col35.getContents();
				String VL_DEM_FP = col36.getContents();
				String VL_DEM_PT = col37.getContents();
				String VL_DEMU_TP = col38.getContents();
				String VLR_MULTAS = col39.getContents();
				String VLR_EN_FP = col40.getContents();
				String VLR_EN_FPU = col41.getContents();
				String VLR_EN_TP = col42.getContents();
				String VLR_EN_PT = col43.getContents();
				String VLR_EN_PTU = col44.getContents();
				String VLR_ICMS = col45.getContents();
				String VLR_UN_FTR = col46.getContents();
				String VLR_UN_P = col47.getContents();
				String VLR_UN_F = col48.getContents();
				String VLR_ENER_F = col49.getContents();
				String VLR_ENER_R = col50.getContents();
				String VLR_ENER_P = col51.getContents();
				String VLR_EMISS = col52.getContents();

				if (!CMPT.equals("")) {
					SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yy");
					Date dataLeitura = formataData.parse(CMPT);
					dados.setDataLeitura(dataLeitura);
					dados.setCodigoUC(Integer.parseInt(UC));
					dados.setFatura(LLLI);
					dados.setNome(NOME);
					dados.setEndereco(ENDE);
					dados.setBairro(BAIRRO);
					dados.setCep(CEP);
					dados.setCodigoGrupo(COD_FATUR);
					dados.setCodigoTipo(COD_FORNEC);
					dados.setC_Kwh_Cv(Double.parseDouble(EN_TP));
					dados.setC_Kwh_FS(Double.parseDouble(EN_FP));
					dados.setC_Kwh_FU(Double.parseDouble(EN_FPU));
					dados.setC_Kwh_PS(Double.parseDouble(EN_PT));
					dados.setC_Kwh_PU(Double.parseDouble(EN_PTU));
					dados.setDCv(Double.parseDouble(DU.replace(".", "").replace(",", ".")));
					dados.setDFS(Double.parseDouble(DFS.replace(".", "").replace(",", ".")));
					dados.setDPS(Double.parseDouble(DPS.replace(".", "").replace(",", ".")));
					dados.setDFU(Double.parseDouble(DFU.replace(".", "").replace(",", ".")));
					dados.setDPU(Double.parseDouble(DPU.replace(".", "").replace(",", ".")));
					dados.setDem_Fat_Cv(Double.parseDouble(DEM_FAT_TP.replace(".", "").replace(",", ".")));
					dados.setDem_Fat_FP(Double.parseDouble(DEM_FAT_FP.replace(".", "").replace(",", ".")));
					dados.setDem_Fat_Pt(Double.parseDouble(DEM_FAT_PT.replace(".", "").replace(",", ".")));
					dados.setDem_Med_Cv(Double.parseDouble(DEM_TP.replace(".", "").replace(",", ".")));
					dados.setDem_Med_FP(Double.parseDouble(DEM_FP.replace(".", "").replace(",", ".")));
					dados.setDem_Med_Pt(Double.parseDouble(DEM_PT.replace(".", "").replace(",", ".")));
					dados.setDem_Ut_Cv(Double.parseDouble(DEM_UT_TP.replace(".", "").replace(",", ".")));
					dados.setDem_Ut_FP(Double.parseDouble(DEM_UT_FP.replace(".", "").replace(",", ".")));
					dados.setDem_Ut_Pt(Double.parseDouble(DEM_UT_PT.replace(".", "").replace(",", ".")));
					dados.setFcarga(Double.parseDouble(FC_TP.replace(".", "").replace(",", ".")));
					dados.setFpot_FP(Double.parseDouble(FP_F.replace(".", "").replace(",", ".")));
					dados.setFpot_Cv(Double.parseDouble(FP_G.replace(".", "").replace(",", ".")));
					dados.setFpot_Pt(Double.parseDouble(FP_P.replace(".", "").replace(",", ".")));
					dados.setVlr_Ult_Cv(Double.parseDouble(VL_DEMU_TP.replace(".", "").replace(",", ".")));
					dados.setVlr_Ult_Pt(Double.parseDouble(VL_DEMU_PT.replace(".", "").replace(",", ".")));
					dados.setVlr_Ult_FP(Double.parseDouble(VL_DEMU_FP.replace(".", "").replace(",", ".")));
					dados.setVlr_dem_Cv(Double.parseDouble(VL_DEM_TP.replace(".", "").replace(",", ".")));
					dados.setVlr_dem_FP(Double.parseDouble(VL_DEM_FP.replace(".", "").replace(",", ".")));
					dados.setVlr_dem_Pt(Double.parseDouble(VL_DEM_PT.replace(".", "").replace(",", ".")));
					dados.setVlr_Multas(Double.parseDouble(VLR_MULTAS.replace(".", "").replace(",", ".")));
					dados.setVlr_Kwh_FS(Double.parseDouble(VLR_EN_FP.replace(".", "").replace(",", ".")));
					dados.setVlr_Kwh_FU(Double.parseDouble(VLR_EN_FPU.replace(".", "").replace(",", ".")));
					dados.setVlr_Kwh_Cv(Double.parseDouble(VLR_EN_TP.replace(".", "").replace(",", ".")));
					dados.setVlr_Kwh_PS(Double.parseDouble(VLR_EN_PT.replace(".", "").replace(",", ".")));
					dados.setVlr_Kwh_PU(Double.parseDouble(VLR_EN_PTU.replace(".", "").replace(",", ".")));
					dados.setVlr_ICMS(Double.parseDouble(VLR_ICMS.replace(".", "").replace(",", ".")));
					dados.setVlr_DRe_Cv(Double.parseDouble(VLR_UN_FTR.replace(".", "").replace(",", ".")));
					dados.setVlr_DRe_Pt(Double.parseDouble(VLR_UN_P.replace(".", "").replace(",", ".")));
					dados.setVlr_DRe_FP(Double.parseDouble(VLR_UN_F.replace(".", "").replace(",", ".")));
					dados.setVlr_ERe_FP(Double.parseDouble(VLR_ENER_F.replace(".", "").replace(",", ".")));
					dados.setVlr_ERe_Cv(Double.parseDouble(VLR_ENER_R.replace(".", "").replace(",", ".")));
					dados.setVlr_ERe_Pt(Double.parseDouble(VLR_ENER_P.replace(".", "").replace(",", ".")));
					dados.setVlr_Total(Double.parseDouble(VLR_EMISS.replace(".", "").replace(",", ".")));

					UnidadeConsumidora unidadeConsumidora = fachadaUC.obterUnidadeConsumidoraUC(dados.getCodigoUC());
					dados.setUnidadeConsumidora(unidadeConsumidora);
					if (unidadeConsumidora != null) {
						ContratoEnergia contrato = fachadaContrato.obterContratoVigente(unidadeConsumidora.getCodigo());
						dados.setContrato(contrato);
						if (contrato == null)
							mostrarMensagemAviso(bundle.getText("aviso_contrato_nao_localizado") + dados.getCodigoUC());
					} else {
						dados.setContrato(null);
						logger.info(bundle.getText("aviso_unidade_cons_nao_localizada") + dados.getCodigoUC());
						mostrarMensagemAviso(bundle.getText("aviso_unidade_cons_nao_localizada") + dados.getCodigoUC());
					}
					dados.setEnergiaEletrica(this.registro);
					// Totalizando Arquivo
					valorTotal = valorTotal + Double.parseDouble(VLR_EMISS.replace(".", "").replace(",", "."));
					qtdUC = qtdUC + 1;
					this.registro.getDados().add(dados);
				}
			}
			workbook.close();
			registro.setQtdUC(qtdUC);
			registro.setValorTotal(valorTotal);
			registro.setNomeArquivo(nomeArquivo);
			registro.setUsuario(usuarioProxy);
			registro.setUltimaAlteracao(new Date());
			super.confirmar();
		} catch (Exception e) {
			logger.error(bundle.getText("erro_importacao_arquivo"), e);
			this.mostrarMensagemErro(bundle.getText("erro_importacao_arquivo"));
		}
	}
}
