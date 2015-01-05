package br.gov.pa.cosanpa.gopera.managedBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.jboss.logging.Logger;

import br.gov.model.exception.BaseRuntimeException;
import br.gov.model.operacao.EnergiaEletricaDados;
import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.RegionalProxy;
import br.gov.model.operacao.RelatorioEnergiaEletrica;
import br.gov.model.operacao.RelatorioExcel;
import br.gov.model.operacao.TipoRelatorioEnergia;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.model.util.FormatoData;
import br.gov.model.util.Utilitarios;
import br.gov.pa.cosanpa.gopera.command.GeraPlanilhaAnaliseEnergiaEletricaCommand;
import br.gov.pa.cosanpa.gopera.command.GeraPlanilhaFaturamentoMensalCommand;
import br.gov.pa.cosanpa.gopera.command.GeraPlanilhaUCNaoCadastradaCommand;
import br.gov.pa.cosanpa.gopera.command.GeraPlanilhaUCsNaoFaturadasCommand;
import br.gov.pa.cosanpa.gopera.command.InformacoesParaRelatorio;
import br.gov.pa.cosanpa.gopera.util.DadoRelatorio;
import br.gov.pa.cosanpa.gopera.util.DadosExcel;
import br.gov.pa.cosanpa.gopera.util.GeradorExcel;
import br.gov.pa.cosanpa.gopera.util.WebBundle;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;
import br.gov.servicos.operacao.RelatorioEnergiaEletricaRepositorio;
import br.gov.servicos.operacao.UnidadeConsumidoraRepositorio;
import br.gov.servicos.operacao.to.UnidadeConsumidoraSemContratoTO;

@ManagedBean
@ViewScoped
public class RelatorioEnergiaEletricaBean extends BaseBean<RelatorioEnergiaEletrica> {

	private static Logger logger = Logger.getLogger(RelatorioEnergiaEletricaBean.class);

	private List<RelatorioEnergiaEletrica> relatorio = new ArrayList<RelatorioEnergiaEletrica>();
	private List<EnergiaEletricaDados> energiaEletricaDados = new ArrayList<EnergiaEletricaDados>();
	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private Integer codigoRegional;
	private Integer codigoUnidadeNegocio;
	private Integer codigoMunicipio;
	private Integer codigoLocalidade;
	private Integer referencia;
	private Integer referenciaInicial;
	private Integer referenciaFinal;
	
	private TipoRelatorioEnergia tipoRelatorio;
	private SortedMap<String, DadoRelatorio> mapDados;
	private List<String> dadosSelecionados;

	@EJB
	private RelatorioEnergiaEletricaRepositorio fachadaRel;
	
	@EJB
	private UnidadeConsumidoraRepositorio unidadeConsumidoraRepositorio; 
	
	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;


	public RelatorioEnergiaEletricaBean() {
		super();
		this.registro = new RelatorioEnergiaEletrica();
		this.tipoRelatorio = TipoRelatorioEnergia.UCS_NAO_CADASTRADAS;
		if (bundle == null) {
			bundle = new WebBundle();
		}
		mapDados = new TreeMap<String, DadoRelatorio>();
		ResourceBundle dadosRelatorio = ResourceBundle.getBundle("dados_relatorio_energia");
		for (String key : dadosRelatorio.keySet()) {
			String valor = dadosRelatorio.getString(key);
			String[] labels = valor.split(";");
			mapDados.put(labels[1].trim(), new DadoRelatorio(labels[1].trim(), labels[0].trim(), labels[2].trim()));
		}
	}

	public String iniciar() {
		this.registro = new RelatorioEnergiaEletrica();

		return "RelatorioEnergiaEletrica.jsf";
	}

	public void exibir() {
		try {
			switch (tipoRelatorio) {
			case UCS_SEM_CONTRATO:
			    List<UnidadeConsumidoraSemContratoTO> lista = unidadeConsumidoraRepositorio.unidadesConsumidorasAtivasSemContrato();
			    
			    if (lista.size() == 0) {
			        mostrarMensagemAviso(bundle.getText("aviso_nao_existem_unidades_consumidoras_sem_contrato"));
			        return;
			    }else{
			        DadosExcel excel = new DadosExcel() {
                        
                        public String tituloRelatorio() {
                            return bundle.getText(tipoRelatorio.getDescricao());
                        }
                        
                        public String nomeArquivo() {
                            return tipoRelatorio.getDescricao();
                        }
                        
                        public List<List<String>> dados() {
                            List<List<String>> linhas = new ArrayList<List<String>>();
                            
                            lista.forEach(e -> linhas.add(e.toArray()));
                            
                            return linhas;
                        }
                        
                        public String[] cabecalho() {
                            return new String[]{bundle.getText("codigo_uc")
                                    , bundle.getText("unidade_consumidora")
                                    , bundle.getText("unidade_negocio")
                                    , bundle.getText("localidade")
                                    };
                        }
                    };
                    
			        GeradorExcel gerador = new GeradorExcel(excel);
			        gerador.geraPlanilha();
			    }
			    
			    return;
			case UCS_NAO_CADASTRADAS:
                relatorio = fachadaRel.getEnergiaEletricaUC(referencia, this.tipoRelatorio);
				if (relatorio.size() == 0) {
					mostrarMensagemErro(bundle.getText("erro_nao_existe_retorno_filtro"));
					return;
				}
				break;
			case UCS_NAO_FATURADAS:
                relatorio = fachadaRel.getEnergiaEletricaUC(referencia, this.tipoRelatorio);
				if (relatorio.size() == 0) {
					mostrarMensagemErro(bundle.getText("erro_nao_existe_retorno_filtro"));
					return;
				}
				break;
			case FATURAMENTO_MENSAL:
                energiaEletricaDados.clear();
                energiaEletricaDados = fachadaRel.getEnergiaEletricaDados(referencia);			    
				if (energiaEletricaDados.size() == 0) {
					mostrarMensagemErro(bundle.getText("erro_nao_existe_retorno_filtro"));
					return;
				}
				break;
			case ANALISE_ENERGIA_ELETRICA:
                this.setReferencia(referenciaFinal);
				break;
			}

			RelatorioExcel relatorioExcel = new RelatorioExcel();
			relatorioExcel.setCaminho(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports"));
			
			relatorioExcel.setNomeArquivoExistente(tipoRelatorio.getDescricao() + ".xls");
			relatorioExcel.setRelatorioEnergiaEletrica(this.relatorio);
			relatorioExcel.setEnergiaEletricaDados(this.energiaEletricaDados);
			relatorioExcel.setReferenciaInicial(referenciaInicial);
			relatorioExcel.setReferenciaFinal(referenciaFinal);
			relatorioExcel.setCodigoMunicipio(codigoMunicipio);
			relatorioExcel.setCodigoLocalidade(codigoLocalidade);
			relatorioExcel.setDadoSelecionado(dadosSelecionados);
			relatorioExcel.setNomeArquivoNovo(tipoRelatorio.getDescricao() + "_" + referencia + ".xls");
			relatorioExcel.setNomeRelatorio(tipoRelatorio.getDescricao());
			relatorioExcel.setDataReferencia(Utilitarios.converteAnoMesParaMesAno(referencia));
			relatorioExcel.setTipoRelatorio(tipoRelatorio.getId());
			
			if (geraPlanilha(relatorioExcel))
			    downloadFile(relatorioExcel.getNomeArquivoNovo(), relatorioExcel.getArquivoNovo().getAbsolutePath());
			
			FacesContext.getCurrentInstance().responseComplete();
		}
		catch (Exception e) {
		    if (e instanceof BaseRuntimeException || e.getCause() instanceof BaseRuntimeException){
	            mostrarMensagemErro(bundle.getText(e.getMessage()));		        
		    }else{
		        mostrarMensagemErro(bundle.getText("erro_exibir_relatorio"));
		    }
			logger.error("Erro ao exibir Relatorio", e);
		}
	}

	// Método responsável por fazer a escrita, a inserção dos dados na planilha
	private boolean geraPlanilha(RelatorioExcel relatorioExcel) throws Exception {
		// carrega planilha pre existente
		relatorioExcel.setArquivo(new File(relatorioExcel.getCaminho() + "/" + relatorioExcel.getNomeArquivoExistente()));
		String diretorio = fachadaProxy.getParametroSistema(9);
		new File(diretorio).mkdirs();
		relatorioExcel.setArquivoNovo(new File(diretorio + relatorioExcel.getNomeArquivoNovo()));
		WorkbookSettings ws = new WorkbookSettings();
		ws.setLocale(new Locale("pt", "BR"));
		ws.setEncoding("ISO-8859-1");
		Workbook workbook = Workbook.getWorkbook(relatorioExcel.getArquivo(), ws);
		WritableWorkbook copy = Workbook.createWorkbook(relatorioExcel.getArquivoNovo(), workbook);
		WritableSheet sheet = copy.getSheet(0);
		boolean retCode = defineConteudo(relatorioExcel, sheet);
		copy.write();
		copy.close();
		return retCode;
	}

	private boolean defineConteudo(RelatorioExcel relatorioExcel, WritableSheet sheet) throws Exception {
		InformacoesParaRelatorio infos = new InformacoesParaRelatorio();
		infos.setReferencia(referencia);

		switch (this.tipoRelatorio) {

		case UCS_NAO_CADASTRADAS:
			new GeraPlanilhaUCNaoCadastradaCommand(relatorioExcel, sheet).execute(infos, fachadaRel);
			break;
		case UCS_NAO_FATURADAS:
			new GeraPlanilhaUCsNaoFaturadasCommand(relatorioExcel, sheet).execute(infos, fachadaRel);
			break;
		case FATURAMENTO_MENSAL:
			new GeraPlanilhaFaturamentoMensalCommand(relatorioExcel, sheet).execute(infos, fachadaRel);
			break;
		case ANALISE_ENERGIA_ELETRICA:
			infos.setReferenciaInicial(referenciaInicial);
			infos.setReferenciaFinal(referenciaFinal);
			infos.setCodigoRegional(codigoRegional);
			infos.setCodigoUnidadeNegocio(codigoUnidadeNegocio);
			infos.setCodigoMunicipio(codigoMunicipio);
			infos.setCodigoLocalidade(codigoLocalidade);
			infos.setMapDados(mapDados);
			infos.setDadosSelecionados(dadosSelecionados);

			new GeraPlanilhaAnaliseEnergiaEletricaCommand(relatorioExcel, sheet).execute(infos, fachadaRel);
			break;
		}

		return true;
	}

	public String downloadFile(String nomeDoArquivoGeradoParaDownload, String caminhoRelativoComNomeEextensao) {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		// Obtem o caminho para o arquivo e efetua a leitura
		byte[] arquivo = readFile(new File(caminhoRelativoComNomeEextensao));
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		// configura o arquivo que vai voltar para o usuario.
		response.setHeader("Content-Disposition", "attachment;filename=\"" + nomeDoArquivoGeradoParaDownload + "\"");
		response.setContentLength(arquivo.length);
		// isso faz abrir a janelinha de download
		response.setContentType("application/download");
		response.setCharacterEncoding("ISO-8859-1");
		// envia o arquivo de volta
		try {
			OutputStream out = response.getOutputStream();
			out.write(arquivo);
			out.flush();
			out.close();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException e) {
			System.out.print("Erro no envio do arquivo");
			e.printStackTrace();
		}
		return "";
	}

	// efetua a leitura do arquivo
	public static byte[] readFile(File file) {
		int len = (int) file.length();
		byte[] sendBuf = new byte[len];
		FileInputStream inFile = null;
		try {
			inFile = new FileInputStream(file);
			inFile.read(sendBuf, 0, len);

		} catch (FileNotFoundException e) {
			System.out.print("Arquivo não encontrado");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.print("Erro na leitura do arquivo");
			e.printStackTrace();
		}
		return sendBuf;
	}
	
	
   /*********************************************
     * GETTERS AND SETTERS
     *********************************************/

    public List<RegionalProxy> getRegionais() {
        try {
            regionais = fachadaProxy.getListaRegional();
            regionais.add(0, new RegionalProxy(-1, "Selecione..."));
            return regionais;
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao consultar sistema externo.");
        }
        return regionais;
    }

    public void setRegionais(List<RegionalProxy> regionais) {
        this.regionais = regionais;
    }

    public List<UnidadeNegocioProxy> getUnidadesNegocio() {
        if ((this.getCodigoRegional() != null)) {
            try {
                this.unidadesNegocio = fachadaProxy.getListaUnidadeNegocio(this.getCodigoRegional());
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

    public void setUnidadesNegocio(List<UnidadeNegocioProxy> unidadesNegocio) {
        this.unidadesNegocio = unidadesNegocio;
    }

    public List<MunicipioProxy> getMunicipios() {
        if (this.getCodigoUnidadeNegocio() != null) {
            try {
                this.municipios = fachadaProxy.getListaMunicipio(this.getCodigoRegional(), this.getCodigoUnidadeNegocio());
                this.municipios.add(0, new MunicipioProxy(-1, "Selecione..."));
                return municipios;
            } catch (Exception e) {
                mostrarMensagemErro("Erro ao consultar sistema externo.");
            }
        }
        municipios = new ArrayList<MunicipioProxy>();
        this.municipios.add(0, new MunicipioProxy(-1, "Selecione..."));
        return municipios;
    }

    public void setMunicipios(List<MunicipioProxy> municipios) {
        this.municipios = municipios;
    }

    public List<LocalidadeProxy> getLocalidades() {
        if (this.getCodigoMunicipio() != null) {
            try {
                this.localidades = fachadaProxy.getListaLocalidade(this.getCodigoRegional(), this.getCodigoUnidadeNegocio(), this.getCodigoMunicipio());
                this.localidades.add(0, new LocalidadeProxy(-1, "Selecione..."));
                return this.localidades;
            } catch (Exception e) {
                mostrarMensagemErro("Erro ao consultar sistema externo.");
            }
        }
        localidades = new ArrayList<LocalidadeProxy>();
        localidades.add(0, new LocalidadeProxy(-1, "Selecione..."));
        return localidades;
    }

    public Integer getCodigoRegional() {
        return codigoRegional;
    }

    public void setCodigoRegional(Integer codigoRegional) {
        this.codigoRegional = codigoRegional;
    }

    public Integer getCodigoUnidadeNegocio() {
        return codigoUnidadeNegocio;
    }

    public void setCodigoUnidadeNegocio(Integer codigoUnidadeNegocio) {
        this.codigoUnidadeNegocio = codigoUnidadeNegocio;
    }

    public Integer getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(Integer codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public Integer getCodigoLocalidade() {
        return codigoLocalidade;
    }

    public void setCodigoLocalidade(Integer codigoLocalidade) {
        this.codigoLocalidade = codigoLocalidade;
    }

    public void setLocalidades(List<LocalidadeProxy> localidades) {
        this.localidades = localidades;
    }

    public Integer getReferencia() {
        return referencia;
    }

    public void setReferencia(Integer referencia) {
        this.referencia = referencia;
    }

    public Integer getReferenciaInicial() {
        return referenciaInicial;
    }

    public void setReferenciaInicial(Integer referenciaInicial) {
        this.referenciaInicial = referenciaInicial;
    }

    public Integer getReferenciaFinal() {
        return referenciaFinal;
    }

    public void setReferenciaFinal(Integer referenciaFinal) {
        this.referenciaFinal = referenciaFinal;
    }

    public SortedMap<String, DadoRelatorio> getMapDados() {
        return mapDados;
    }

    public void setMapDados(SortedMap<String, DadoRelatorio> mapDados) {
        this.mapDados = mapDados;
    }

    public List<String> getDadosSelecionados() {
        return dadosSelecionados;
    }

    public void setDadosSelecionados(List<String> dadosSelecionados) {
        this.dadosSelecionados = dadosSelecionados;
    }

    public TipoRelatorioEnergia getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(TipoRelatorioEnergia tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public TipoRelatorioEnergia[] getTiposRelatorios() {
        return TipoRelatorioEnergia.values();
    }
    
}
