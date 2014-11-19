package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.jboss.logging.Logger;

import br.gov.model.exception.BaseRuntimeException;
import br.gov.model.operacao.GrupoIndicador;
import br.gov.model.operacao.Indicador;
import br.gov.model.operacao.IndicadorValor;
import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.RegionalProxy;
import br.gov.model.operacao.UnidadeConsumidoraOperacional;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.servicos.operacao.IndicadorRepositorio;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;
import br.gov.servicos.operacao.UnidadeConsumidoraRepositorio;

@ManagedBean
@SessionScoped
public class IndicadorBean extends BaseRelatorioBean<Indicador> {

	private static Logger logger = Logger.getLogger(IndicadorBean.class);
	
	private static final int INDICE_EFICIENCIA_RETIRADA_VAZAMENTO_DIAS = 203;
	
	private static final int PRAZO_MEDIO_ATENDIMENTO_OS_ESGOTO = 201;

	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private List<UnidadeConsumidoraOperacional> unidadesOperacional = new ArrayList<UnidadeConsumidoraOperacional>();
	private List<Indicador> relatorio = new ArrayList<Indicador>();
	private Map<String,String> listaIndicador;
	private List<String> indicadorSelecionado;
	private String referenciaInicial;
	private String referenciaFinal;
	private Integer tipoUnidadeOperacional;
	private Integer tipoAgrupamento = 0;
	private String grupoIndicador;
	private String exibeParaUsrp = "display: none";
	
	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;
	
	@EJB
	private UnidadeConsumidoraRepositorio fachadaUC;	
	
	@EJB
	private IndicadorRepositorio fachadaInd;

	public IndicadorBean() {
		this.registro = new Indicador();
		this.tipoUnidadeOperacional = 0;
	}

	public String iniciarUSAG(){
		this.registro = new Indicador();
		listaIndicador = new HashMap<String, String>();  
		listaIndicador.put(bundle.getText("consumo_produtos_quimicos"), "1");  
//		listaIndicador.put(bundle.getText("produtividade_capital_produto_quimico"), "2");  
//		listaIndicador.put(bundle.getText("produtividade_capital_energia_eletrica"), "3");  
//		listaIndicador.put(bundle.getText("horas_paradas_sistema"), "4");
		this.grupoIndicador = GrupoIndicador.USAG.name();
		tipoAgrupamento = 0;
		return "Indicadores.jsf";
	}	

	public String iniciarUSRP(){
		this.registro = new Indicador();
		listaIndicador = new HashMap<String, String>();  
		listaIndicador.put(bundle.getText("conformidade_demanda_potencia_eletrica_contratada"), "101");  
		listaIndicador.put(bundle.getText("despesa_decor_fator_pot_eletrica"), "102");  
		listaIndicador.put(bundle.getText("macromedidores"), "103");
		listaIndicador.put(bundle.getText("rede_instalada"), "104");
		this.grupoIndicador = GrupoIndicador.USRP.name();
		tipoAgrupamento = 0;
		return "Indicadores.jsf";
	}	

	public String iniciarBIGDO(){
		this.registro = new Indicador();
		listaIndicador = new HashMap<String, String>();  
		listaIndicador.put(bundle.getText("prazo_medio_atend_ordens_serv_esgoto"), "201");  
		listaIndicador.put(bundle.getText("indice_efic_retira_vaza_perc"), "202");  
		listaIndicador.put(bundle.getText("indice_efic_retira_vaza_dias"), "203");
		listaIndicador.put(bundle.getText("taxa_ret_vaza_agua"), "204");
		listaIndicador.put(bundle.getText("indice_trat_esgoto"), "205");
		listaIndicador.put(bundle.getText("indice_confor_quali_agua"), "206");
//		listaIndicador.put(bundle.getText("indice_macromed_total"), "209");
//		listaIndicador.put(bundle.getText("indice_perda_fisica_agua"), "210");
		listaIndicador.put(bundle.getText("indice_clientes_precariedade"), "211");
		this.grupoIndicador = GrupoIndicador.BIGDO.name();
		tipoAgrupamento = 0;
		return "Indicadores.jsf";
	}	
	
    private void indicadorMensal() throws Exception{
		relatorio.clear();
        relatorio = fachadaInd.getIndicadorMensal(this.registro.getCodigoRegional(),
        										   this.registro.getCodigoUnidadeNegocio(),
        										   this.registro.getCodigoMunicipio(), 
        										   this.registro.getCodigoLocalidade(),
        										   this.registro.getCodigoUnidadeOperacional(),
        										   this.getTipoUnidadeOperacional(),
        										   primeiroDiaMes(this.referenciaInicial),
        										   primeiroDiaMes(this.referenciaFinal),
        										   tipoAgrupamento,
        										   indicadorSelecionado);
    }

    public void exibir(){
    	try{
    		//Carrega informações do Relatório
    		indicadorMensal();
    		
    		if (relatorio.size() == 0){
    			mostrarMensagemErro(bundle.getText("erro_nao_existe_retorno_filtro"));
    			return;
    		}
    		exportaExcel();
		}
    	catch (BaseRuntimeException e){
    		logger.error(bundle.getText(e.getMessage()), e);
    		mostrarMensagemErro(bundle.getText(e.getMessage()));
    	}
		catch (Exception e){
			logger.error(bundle.getText("erro_exibir_relatorio"), e);
			mostrarMensagemErro(bundle.getText("erro_exibir_relatorio"));
		}
    }

	@Override
	public String iniciar() {
		return null;
	}

	private void exportaExcel() throws Exception {
 		Integer colDados = 5, rowDados = 3, cMeses = 5;
 		Integer codigoRegional = 0, codigoUnidadeNegocio = 0, codigoMunicipio = 0, codigoLocalidade = 0, tipoUnidadeOperacional = 0, codigoUnidadeOperacional = 0;

    	//GERA PLANILHA 
 		super.nomeArquivo = "IndicadorSetorial_" + this.referenciaInicial.replace("/", "") + '_' + this.referenciaFinal.replace("/", ""); 
 		super.iniciar();
 		WritableWorkbook wb = geraPlanilha();
 		WritableSheet sheet = wb.getSheet(0);
		//Nome Planilha
 		sheet.setName(bundle.getText("indicador_setorial"));
 		
		//Titulo
 		addLabel(sheet, 0, 0, bundle.getText("diretoria_operacao").toUpperCase() + "\n" + bundle.getText("indicadores_setoriais").toUpperCase(), wcfLabelBold);
		
		//Período
 		addLabel(sheet, 0, 2, bundle.getText("referencia") + ": " + this.referenciaInicial + " a " + this.referenciaFinal, wcfLabelHeaderCenter);

		rowDados++;
		
		preencheCabecalho(sheet, rowDados);
		
		rowDados++;
		//Preenchendo a valor dos indicadores
		for(Indicador indicador: relatorio) {
			if(tipoAgrupamento >= 1){//GERENCIA REGIONAL
				if (codigoRegional != indicador.getCodigoRegional()){
					addLabel(sheet, 0, rowDados, bundle.getText("gerencia_regional").toUpperCase() + ":",  wcfLabelBoldLeftBorder);
					addLabel(sheet, 1, rowDados, indicador.getNomeRegional(),  wcfLabelBoldLeftBorder);
					sheet.mergeCells(1, rowDados, cMeses - 1, rowDados);
					rowDados++;
					codigoRegional = indicador.getCodigoRegional();
				}
			}
			
			if(tipoAgrupamento >= 2){//GERENCIA REGIONAL + UNIDADE NEGOCIO
				if (codigoUnidadeNegocio != indicador.getCodigoUnidadeNegocio()){
					addLabel(sheet, 0, rowDados, bundle.getText("unidade_negocio").toUpperCase() + ":",  wcfLabelBoldLeftBorder);
					addLabel(sheet, 1, rowDados, indicador.getNomeUnidadeNegocio(),  wcfLabelBoldLeftBorder);
					sheet.mergeCells(1, rowDados, cMeses - 1, rowDados);
					rowDados++;
					codigoUnidadeNegocio = indicador.getCodigoUnidadeNegocio();
				}
			}
			
			if(tipoAgrupamento >= 3){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO
				if (codigoMunicipio != indicador.getCodigoMunicipio()){
					addLabel(sheet, 0, rowDados, bundle.getText("municipio").toUpperCase() + ":",  wcfLabelBoldLeftBorder);
					addLabel(sheet, 1, rowDados, indicador.getNomeMunicipio(),  wcfLabelBoldLeftBorder);
					sheet.mergeCells(1, rowDados, cMeses - 1, rowDados);
					rowDados++;
					codigoMunicipio = indicador.getCodigoMunicipio();
				}
			}
			
			if(tipoAgrupamento >= 4){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO + LOCALIDADE
				if (codigoLocalidade != indicador.getCodigoLocalidade()){
					addLabel(sheet, 0, rowDados, bundle.getText("localidade").toUpperCase() + ":",  wcfLabelBoldLeftBorder);
					addLabel(sheet, 1, rowDados, indicador.getNomeLocalidade(),  wcfLabelBoldLeftBorder);
					sheet.mergeCells(1, rowDados, cMeses - 1, rowDados);
					rowDados++;
					codigoLocalidade = indicador.getCodigoLocalidade();
				}
			}
			
			if(tipoAgrupamento >= 5){//GERENCIA REGIONAL + UNIDADE NEGOCIO + MUNICIPIO + LOCALIDADE + UNIDADE OPERACIONAL
				if (indicador.getTipoUnidadeOperacional() != null && indicador.getTipoUnidadeOperacional() != 0){
					if (tipoUnidadeOperacional !=  indicador.getTipoUnidadeOperacional() || 
						codigoUnidadeOperacional != indicador.getCodigoUnidadeOperacional()){
						addLabel(sheet, 0, rowDados, bundle.getText("unidade_operacional").toUpperCase() + ":",  wcfLabelBoldLeftBorder);
						addLabel(sheet, 1, rowDados, indicador.getNomeUnidadeOperacional(),  wcfLabelBoldLeftBorder);
						sheet.mergeCells(1, rowDados, cMeses - 1, rowDados);
						rowDados++;
						tipoUnidadeOperacional = indicador.getTipoUnidadeOperacional();
						codigoUnidadeOperacional = indicador.getCodigoUnidadeOperacional();
					}
				}
				else{
					addLabel(sheet, 0, rowDados, bundle.getText("unidade_operacional").toUpperCase() + ":",  wcfLabelBoldLeftBorder);
					addLabel(sheet, 1, rowDados, "Indefinida",  wcfLabelBoldLeftBorder);
					sheet.mergeCells(1, rowDados, cMeses - 1, rowDados);
					rowDados++;						
				}
			}
			
			preencheResumoIndicador(sheet, rowDados, indicador);
			
			preencheValoresIndicador(sheet, rowDados, indicador);
			
			rowDados = rowDados + 2;
		}			
		//Mesclagem de células horizontais
		sheet.mergeCells(0, 0, cMeses - 1 , 0); //Cabeçalho
		sheet.mergeCells(0, 2, cMeses - 1, 2); //Período
		sheet.mergeCells(0, 3, cMeses - 1, 3); //Período
		//REALIZA DOWNLOAD DA PLANILHA
		downloadFile(wb);
	}
	
	private void preencheValoresIndicador(WritableSheet sheet,  int linha, Indicador indicador) throws WriteException {
		for (IndicadorValor valor : indicador.getValor()){
			Date datIni = primeiroDiaMes(this.referenciaInicial);
			Date datFim = primeiroDiaMes(this.referenciaFinal);
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(datIni);  
	        int colDados = 5;
			Date refIndicador = valor.getMesReferencia();
	        Calendar calIndicador = Calendar.getInstance();
	        calIndicador.setTime(refIndicador);
			while (!datIni.after(datFim)){
				if (calendar.get(Calendar.YEAR) == calIndicador.get(Calendar.YEAR) 
						&& calendar.get(Calendar.MONTH) == calIndicador.get(Calendar.MONTH)){
					addNumero(sheet, colDados, linha, valor.getIndicador1(),wcfNumeroBorder);
					addNumero(sheet, colDados++, linha + 1, valor.getIndicador2(),wcfNumeroBorder);
					double percentual = valor.getTotal();
					if (indicadorMultiplicaPercentual(indicador)){
						percentual *= 100;
					}
					addNumero(sheet, colDados, linha, percentual, wcfPercentual);
					colDados++;
					break;
				}
				else{
					colDados+=2;
				}						
				calendar.add(Calendar.MONTH, 1 );
				datIni = calendar.getTime(); 
			}
		}
	}

	private boolean indicadorMultiplicaPercentual(Indicador indicador) {
		return indicador.getCodigo().intValue() != PRAZO_MEDIO_ATENDIMENTO_OS_ESGOTO && indicador.getCodigo().intValue() != INDICE_EFICIENCIA_RETIRADA_VAZAMENTO_DIAS;
	}

	private void preencheResumoIndicador(WritableSheet sheet, int linha, Indicador indicador) throws WriteException {
		addLabel(sheet, 0, linha, indicador.getSequencial().toString(),  wcfLabelBoldBorder);
		addLabel(sheet, 1, linha, indicador.getNomeIndicador(),  wcfLabelBoldLeftBorder);
		addLabel(sheet, 2, linha, indicador.getUnidadeIndicador(),  wcfLabelBorder);
		addLabel(sheet, 3, linha, indicador.getFormulaIndicador(),  wcfLabelLeftBorder);
		addLabel(sheet, 4, linha, indicador.getResponsavelIndicador(),  wcfLabelBorder);
		
		//Merge Células
		sheet.mergeCells(0, linha, 0, linha + 1);
		sheet.mergeCells(1, linha, 1, linha + 1);
		sheet.mergeCells(2, linha, 2, linha + 1);
		sheet.mergeCells(3, linha, 3, linha + 1);
		sheet.mergeCells(4, linha, 4, linha + 1);
	}

	private void preencheCabecalho(WritableSheet sheet, int linha) throws WriteException {
		addLabel(sheet, 0, linha, bundle.getText("num_indicador").toUpperCase(), wcfLabelHeaderCenter);
		addLabel(sheet, 1, linha, bundle.getText("informacoes_indicadores").toUpperCase(), wcfLabelHeaderCenter);
		addLabel(sheet, 2, linha, bundle.getText("unidade").toUpperCase(), wcfLabelHeaderCenter);
		addLabel(sheet, 3, linha, bundle.getText("formula").toUpperCase(), wcfLabelHeaderCenter);
		addLabel(sheet, 4, linha, bundle.getText("responsaveis").toUpperCase(), wcfLabelHeaderCenter);
		
		//Preenchendo o cabeçalho dos meses
		int coluna = 5;
		Date datIni = primeiroDiaMes(this.referenciaInicial);
		Date datFim = primeiroDiaMes(this.referenciaFinal);
		
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(datIni);  
		
		while (!datIni.after(datFim)){
			addLabel(sheet, coluna, linha, this.filtroData(datIni, "MM/yyyy"), wcfLabelHeaderCenter);
			sheet.mergeCells(coluna, linha, coluna + 1, linha);
			coluna += 2;
			calendar.add(Calendar.MONTH, 1 );
			datIni = calendar.getTime(); 
		}		
	}

	/****************************************************
	 * GETTERS AND SETTERS
	 ****************************************************/
	public Indicador getRegistro() {
		return registro;
	}

	public void setRegistro(Indicador registro) {
		this.registro = registro;
	}
	
	public String getReferenciaInicial() {
		return referenciaInicial;
	}

	public void setReferenciaInicial(String referenciaInicial) {
		this.referenciaInicial = referenciaInicial;
	}

	public String getReferenciaFinal() {
		return referenciaFinal;
	}

	public void setReferenciaFinal(String referenciaFinal) {
		this.referenciaFinal = referenciaFinal;
	}

	public Integer getTipoAgrupamento() {
		return tipoAgrupamento;
	}

	public void setTipoAgrupamento(Integer tipoAgrupamento) {
		this.tipoAgrupamento = tipoAgrupamento;
	}

	public Map<String, String> getListaIndicador() {
		return listaIndicador;
	}

	public List<String> getIndicadorSelecionado() {
		return indicadorSelecionado;
	}

	public void setIndicadorSelecionado(List<String> indicadorSelecionado) {
		this.indicadorSelecionado = indicadorSelecionado;
	}

	public Integer getTipoUnidadeOperacional() {
		return tipoUnidadeOperacional;
	}

	public void setTipoUnidadeOperacional(Integer tipoUnidadeOperacional) {
		this.tipoUnidadeOperacional = tipoUnidadeOperacional;
	}

	public String getGrupoIndicador() {
		return grupoIndicador;
	}

	public void setGrupoIndicador(String grupoIndicador) {
		this.grupoIndicador = grupoIndicador;
	}
	
	public List<RegionalProxy> getRegionais() {
		try {
			this.regionais =  fachadaProxy.getListaRegional();
			this.regionais.add(0, new RegionalProxy(-1, "Selecione..."));
			return regionais;
		} catch (Exception e) {
			mostrarMensagemErro("Erro ao consultar sistema externo.");
		}
		regionais = new ArrayList<RegionalProxy>();
		regionais.add(0, new RegionalProxy(-1, "Selecione..."));
		return regionais;
	}
	
	public List<UnidadeNegocioProxy> getUnidadesNegocio() {
		if (this.registro.getCodigoRegional() != null) {
			try {
				this.unidadesNegocio =  fachadaProxy.getListaUnidadeNegocio(this.registro.getCodigoRegional());
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

	public List<MunicipioProxy> getMunicipios() {
		if (this.registro.getCodigoUnidadeNegocio() != null) {
			try {
				this.municipios =  fachadaProxy.getListaMunicipio(this.registro.getCodigoRegional(), this.registro.getCodigoUnidadeNegocio());
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

	public List<LocalidadeProxy> getLocalidades() {
		if (this.registro.getCodigoMunicipio() != null) {
			try {
				this.localidades =  fachadaProxy.getListaLocalidade(this.registro.getCodigoRegional(), this.registro.getCodigoUnidadeNegocio(), this.registro.getCodigoMunicipio());
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

	public List<UnidadeConsumidoraOperacional> getUnidadesOperacional() {
		if (this.tipoUnidadeOperacional != 0) {
			try {
				this.unidadesOperacional =  fachadaUC.getListaUnidadeOperacional(this.tipoUnidadeOperacional, 
																				 this.registro.getCodigoRegional(), 
																				 this.registro.getCodigoUnidadeNegocio(), 
																				 this.registro.getCodigoMunicipio(), 
																				 this.registro.getCodigoLocalidade());
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

	public String getExibeParaUsrp() {
		if (grupoIndicador.equals(GrupoIndicador.USRP.name()))
			return "display: none";
		else 
			return "";
	}
	
	
}