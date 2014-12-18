package br.gov.pa.cosanpa.gopera.managedBean;

import static br.gov.model.util.Utilitarios.retiraCaracteresEspeciais;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.jboss.logging.Logger;
import org.primefaces.model.UploadedFile;

import br.gov.model.exception.BaseRuntimeException;
import br.gov.model.exception.UnidadesConsumidorasNaoLocalizadas;
import br.gov.model.operacao.ContratoEnergia;
import br.gov.model.operacao.EnergiaEletrica;
import br.gov.model.operacao.EnergiaEletricaDados;
import br.gov.model.operacao.UnidadeConsumidora;
import br.gov.model.operacao.UsuarioProxy;
import br.gov.servicos.operacao.ContratoEnergiaRepositorio;
import br.gov.servicos.operacao.EnergiaEletricaRepositorio;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;
import br.gov.servicos.operacao.UnidadeConsumidoraRepositorio;

@ManagedBean
@ViewScoped
public class ImportarEnergiaBean extends BaseBean<EnergiaEletrica> {

	private static final Logger logger = Logger.getLogger(ImportarEnergiaBean.class);

	@EJB
	private EnergiaEletricaRepositorio fachada;
	
	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;
	
	@EJB
	private UnidadeConsumidoraRepositorio fachadaUC;
	
	@EJB
	private ContratoEnergiaRepositorio fachadaContrato;

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
	
	@PostConstruct
	public void init(){
	    this.setFachada(this.fachada);
	    atualizarListas();
	    this.registro = new EnergiaEletrica();
	}
	
	public String novo() {
		this.registro = new EnergiaEletrica();
		super.novo();
		return null;
	}
	
	public String consultar() {
		carregar();
		super.consultar();
		return null;
	}

	public String alterar() {
		carregar();
		super.alterar();
		return null;
	}

	public String cadastrar() {
	    try {
    		if (arquivoValido()) {
    			super.cadastrar();
    		}
        } catch (Exception e) {
            logger.error(bundle.getText("erro_validacao_arquivo"), e);
            this.mostrarMensagemErro(bundle.getText("erro_validacao_arquivo"));
        }
		
		return null;
	}
	
	public String confirmar() {
        try {
            if (arquivo != null) {
                String diretorio = fachadaProxy.getParametroSistema(9);
                new File(diretorio).mkdir();
                String nomeArquivo = diretorio + arquivo.getFileName();
                
                registro.setNomeArquivo(nomeArquivo);

                copyFile(nomeArquivo, arquivo.getInputstream());
                
                List<EnergiaEletricaDados> dados = importaArquivo(nomeArquivo, this.registro);
                
                validarDados(dados);
                
                mostrarMensagemSucesso("Arquivo " + arquivo.getFileName() + " foi enviado.");
                
                super.confirmar();
            }
        } catch (Exception e) {
            if (e instanceof BaseRuntimeException || e.getCause() instanceof BaseRuntimeException){
                mostrarMensagemErro(e.getMessage());
                logger.error(e.getMessage(), e);
            }else{
                mostrarMensagemErro(bundle.getText("erro_envio_arquivo"));
                logger.error(bundle.getText("erro_envio_arquivo"), e);          
            }
        }
        return null;
    }	


    /************************************************
	 * PRIVATE METHODS
	 ************************************************/
	private void validarDados(List<EnergiaEletricaDados> lista) throws Exception{
	    Double valorTotal = 0.0;
	    
	    StringBuilder unidadesNaoLocalizadas = new StringBuilder();
	    
	    for (EnergiaEletricaDados dados : lista) {
            UnidadeConsumidora unidadeConsumidora = fachadaUC.obterUnidadeConsumidoraUC(dados.getCodigoUC());
            dados.setUnidadeConsumidora(unidadeConsumidora);
            if (unidadeConsumidora != null) {
                ContratoEnergia contrato = fachadaContrato.obterContratoVigente(unidadeConsumidora.getCodigo());
                dados.setContrato(contrato);
            } else {
                unidadesNaoLocalizadas.append(dados.getCodigoUC() + "  ");
            }
            dados.setEnergiaEletrica(this.registro);
            valorTotal += dados.getVlr_Total();
        }
	    
	    if (unidadesNaoLocalizadas.length() != 0){
	        throw new UnidadesConsumidorasNaoLocalizadas(unidadesNaoLocalizadas.toString());
	    }
	    registro.getDados().addAll(lista);
        registro.setQtdUC(lista.size());
        registro.setValorTotal(valorTotal);
        registro.setUsuario(usuarioProxy.getCodigo());
        registro.setUltimaAlteracao(new Date());	    
	}
	
    private void carregar() {
        try {
            this.registro = fachada.obterEnergia(this.registro.getCodigo());
        } catch (Exception e) {
            this.mostrarMensagemErro(bundle.getText("erro_carregar_informacoes_arquivo"));
            logger.error(bundle.getText("erro_carregar_informacoes_arquivo"), e);
        }
    }
	
	private boolean arquivoValido() throws Exception {
		if (arquivo != null && registro.getReferencia() != null){
		    if (!fachada.existeEnergiaEletricaNaReferencia(registro.getReferencia())) {
		        return true;
		    } else {
		        this.mostrarMensagemErro(bundle.getText("erro_referencia_arquivo_cadastrada"));
		        return false;
		    }
		}
		return false;
	}

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

	private List<EnergiaEletricaDados> importaArquivo(String nomeArquivo, EnergiaEletrica energia) throws Exception {
		Workbook workbook = Workbook.getWorkbook(new File(nomeArquivo));
		Sheet sheet = workbook.getSheet(0);
		int linhas = sheet.getRows();
		EnergiaEletricaDados dados;
		List<EnergiaEletricaDados> dadosEnergia = new ArrayList<EnergiaEletricaDados>();
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
				dados.setFatura(retiraCaracteresEspeciais(LLLI));
				dados.setNome(retiraCaracteresEspeciais(NOME));
				dados.setEndereco(retiraCaracteresEspeciais(ENDE));
				dados.setBairro(retiraCaracteresEspeciais(BAIRRO));
				dados.setCep(retiraCaracteresEspeciais(CEP));
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

				dados.setEnergiaEletrica(this.registro);
				dadosEnergia.add(dados);
			}
		}
		workbook.close();
		
		return dadosEnergia;
	}
	
    /************************************************
     * GETTERS AND SETTERS
     ************************************************/
    public UploadedFile getArquivo() {
        return arquivo;
    }

    public void setArquivo(UploadedFile arquivo) {
        this.arquivo = arquivo;
        try {
            String arquivoAux = arquivo.getFileName();
            String referencia = arquivoAux.substring(10, 14) + arquivoAux.substring(8, 10);
            this.registro.setReferencia(Integer.parseInt(referencia));
        } catch (NumberFormatException nfe) {
            this.mostrarMensagemErro(bundle.getText("erro_nome_arquivo"));
        } catch (Exception e) {
            this.mostrarMensagemErro(bundle.getText("erro_carga_arquivo"));
            logger.error(bundle.getText("erro_carga_arquivo"), e);          
        }
    }	
}
