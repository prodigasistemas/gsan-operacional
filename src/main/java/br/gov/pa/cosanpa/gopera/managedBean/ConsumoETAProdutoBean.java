package br.gov.pa.cosanpa.gopera.managedBean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.model.operacao.ConsumoETA;
import br.gov.model.operacao.ConsumoETAProduto;
import br.gov.model.operacao.ETA;
import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.Produto;
import br.gov.model.operacao.RegionalProxy;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.servicos.operacao.ConsumoETARepositorio;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;

@SessionScoped
@ManagedBean
public class ConsumoETAProdutoBean extends BaseBean<ConsumoETA> {

	@EJB
	private ConsumoETARepositorio fachada;
	
	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;
	
	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private List<ETA> listaETA = new ArrayList<ETA>();
	private List<Produto> listaProdutos = new ArrayList<Produto>();
	private String valores[][][] = new String[50][50][2];
	private List<Date> datas = new ArrayList<Date>();
	private Date dataBase = null;
	private boolean sistemaSelecionado = false;
	private ConsumoETA consumoETA;
	private LazyDataModel<ConsumoETA> listaConsumo = null;
	private String bloqueiaDataRetroativa = "1";

	public List<RegionalProxy> getRegionais() {
		try {
			regionais = fachada.getListaConsumoETARegional(usuarioProxy);
			return regionais; 
		} catch (Exception e) {
			mostrarMensagemErro("Erro ao consultar sistema externo.");
		}
		return regionais;
	}

	public List<UnidadeNegocioProxy> getUnidadesNegocio() {
		if ((this.consumoETA.getRegionalProxy().getCodigo() != null)) {
			try {
				this.unidadesNegocio =  fachada.getListaConsumoETAUnidadeNegocio(usuarioProxy, 
																						   this.consumoETA.getRegionalProxy().getCodigo());
				return unidadesNegocio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		return unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	}

	public List<MunicipioProxy> getMunicipios() {
		if (this.consumoETA.getUnidadeNegocioProxy().getCodigo() != null) {
			try {
				this.municipios =  fachada.getListaConsumoETAMunicipio(usuarioProxy, 
																				 this.consumoETA.getRegionalProxy().getCodigo(), 
																				 this.consumoETA.getUnidadeNegocioProxy().getCodigo());
				return this.municipios;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.municipios = new ArrayList<MunicipioProxy>();
		return this.municipios;
	}

	public List<LocalidadeProxy> getLocalidades() {
		if (this.consumoETA.getMunicipioProxy().getCodigo() != null) {
			try {
				this.localidades =  fachada.getListaConsumoETALocalidade(usuarioProxy, 
																				   this.consumoETA.getRegionalProxy().getCodigo(), 
																				   this.consumoETA.getUnidadeNegocioProxy().getCodigo(), 
																				   this.consumoETA.getMunicipioProxy().getCodigo());
				return this.localidades;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.localidades = new ArrayList<LocalidadeProxy>();
		return this.localidades;
	}

	public List<ETA> getListaETA() {
		if (this.consumoETA.getLocalidadeProxy().getCodigo() != null) {
			try {
				this.listaETA = fachada.getListaConsumoETA(usuarioProxy, 
																     this.consumoETA.getRegionalProxy().getCodigo(), 
																     this.consumoETA.getUnidadeNegocioProxy().getCodigo(), 
																     this.consumoETA.getMunicipioProxy().getCodigo(),
																     this.consumoETA.getLocalidadeProxy().getCodigo());
				return this.listaETA;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar ETA´s.");
			}
		}
		listaETA = new ArrayList<ETA>();
		return listaETA;
	}
	
	public boolean getHabilitarUnidadeNegocio() {
		return (unidadesNegocio.isEmpty() || (unidadesNegocio.size() == 0));
	}
	
	public boolean getHabilitarMunicipios() {
		return (municipios.isEmpty() || (municipios.size() == 0));
	}
	
	public boolean getHabilitarLocalidades() {
		return (localidades.isEmpty() || (localidades.size() == 0));
	}

	public void setListaETA(List<ETA> listaETA) {
		this.listaETA = listaETA;
	}	
	
	public LazyDataModel<ConsumoETA> getListaConsumo() {
		return listaConsumo;
	}

	public ConsumoETA getConsumoETA() {
		return consumoETA;
	}

	public void setConsumoETA(ConsumoETA consumoETA) {
		this.consumoETA = consumoETA;
	}

	public String[][][] getValores() {
		return valores;
	}

	public void setValores(String[][][] valores) {
		this.valores = valores;
	}

	public Date getDataBase() {
		return dataBase;
	}

	public void setDataBase(Date dataBase) {
		this.dataBase = dataBase;
	}

	public List<Produto> getListaProdutos() throws Exception {
		return listaProdutos;
	}

	public void setListaProdutos(List<Produto> listaProdutos) {
		this.listaProdutos = listaProdutos;
	}

	public boolean getSistemaSelecionado() {
		if (this.consumoETA.getEta().getCodigo() != null){
			this.sistemaSelecionado = (this.consumoETA.getEta().getCodigo() != 0 && dataBase != null ? true : false);
		}
		else{
			this.sistemaSelecionado = false;
		}
		return sistemaSelecionado;
	}

	public void setSistemaSelecionado(boolean sistemaSelecionado) {
		this.sistemaSelecionado = sistemaSelecionado;
	}
/*
	public List<ConsumoETA> getListaETA() {
		try {
			List<ConsumoETA> lista = fachadaProxy.getListaConsumoETAUsuario(usuarioProxy);
			if (lista.size() > 0){
				if (consumoETA == null) consumoETA = lista.get(0);
			}
			return lista;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
*/	
	public void carregarListaProduto() {
		if (dataBase == null) return;
		Date dataIni = dataBase;
		Date dataFim;
		inicializarDatas();

		Calendar c = Calendar.getInstance();
		c.setTime(dataBase);
	    c.add(Calendar.DATE, 6); 
	    dataFim = c.getTime();

	    try{
			listaProdutos = fachadaProxy.getListaProdutoETA(this.consumoETA.getRegionalProxy().getCodigo(),
															 this.consumoETA.getUnidadeNegocioProxy().getCodigo(),
															 this.consumoETA.getMunicipioProxy().getCodigo(),
															 this.consumoETA.getLocalidadeProxy().getCodigo(),
															 this.consumoETA.getEta().getCodigo(),
															 dataIni, dataFim);
	
			//Carrega Lista de Valores
			valores = new String[50][50][2];
			Integer intI = 0, intJ = 0;
			String valorAux[];
			for (Produto produto : listaProdutos){
			    c.setTime(dataBase);
				for (intJ=0;intJ<=6;intJ++){
					valorAux = fachadaProxy.getConsumoETA(this.consumoETA.getRegionalProxy().getCodigo(),
															 this.consumoETA.getUnidadeNegocioProxy().getCodigo(),
															 this.consumoETA.getMunicipioProxy().getCodigo(),
															 this.consumoETA.getLocalidadeProxy().getCodigo(),
															 this.consumoETA.getEta().getCodigo(),
															 c.getTime(), produto.getCodigo());
					if (valorAux[0] == null){
						valores[intI][intJ][0] = "0,00";
						valores[intI][intJ][1] = "0"; //INSERT
					}else{
			            DecimalFormat df = new DecimalFormat("#,##0.00");
			            String dx = df.format(Double.parseDouble(valorAux[0]));
						valores[intI][intJ][0]	= dx;
						valores[intI][intJ][1]	= valorAux[1]; //UPDATE
					}
					c.add(Calendar.DATE, 1);
				}
				intI++;				
			}
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Consultar a Lista de Produtos");
			e.printStackTrace();
		}			
	}
	
	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		iniciarLazy();
		this.visualizar();	
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new ConsumoETA();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "ConsumoETAProduto.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");
	}

	private void iniciarLazy(){
		if (listaConsumo == null) {  
			listaConsumo = new LazyDataModel<ConsumoETA>() {
				private static final long serialVersionUID = 1L;

				
				public List<ConsumoETA> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
					try {					
						List<ConsumoETA> listaLazy = fachada.obterListaLazy(startingAt, maxPerPage, filters);
						setRowCount(fachada.obterQtdRegistros(filters));
						setPageSize(maxPerPage);						
						return listaLazy;						
					} catch (Exception e) {
						e.printStackTrace();
					}	
					return null;
				}
				
				
				public Object getRowKey(ConsumoETA consumo) {
					return consumo.getCodigo();
				}
				
				
				public ConsumoETA getRowData(String consumoId) {
					if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
						Integer id = Integer.valueOf(consumoId);
						
						for (ConsumoETA consumo : listaConsumo) {
							if(id.equals(consumo.getCodigo())){
								return consumo;
							}
						}
					}
					return null;				
				}
			};
	    }
	}

	
	public String novo(){
		try{
			dataBase = null;
			consumoETA = new ConsumoETA();		
			this.registro = new ConsumoETA();
			//Recupera Parametros
			bloqueiaDataRetroativa = fachadaProxy.getParametroSistema(8);
		} catch (Exception e) {
			this.mostrarMensagemErro(e.getMessage());
		}
		return super.novo();
	}
	
	
	public String consultar() {
		carregar();
		return super.consultar();
	}
	
	
	public String alterar(){
		try{
			carregar();
			//Recupera Parametros
			bloqueiaDataRetroativa = fachadaProxy.getParametroSistema(8);
		} catch (Exception e) {
			this.mostrarMensagemErro(e.getMessage());
		}
		return super.alterar();
	}
	
	
	public String cancelar(){
		return super.cancelarLazy();
	}
	
	public void inicializarDatas() {
		Calendar dataInicial = Calendar.getInstance();
		dataInicial.setTime(this.getDataBase());
		datas.clear();
		for (int i = 0; i < 7; i++) {
			datas.add(dataInicial.getTime());
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
		}
	}
	
	public List<Date> getDatas() {
		return datas;
	}

	public void setDatas(List<Date> datas) {
		this.datas = datas;
	}

	
	public String confirmar() {
		try {
			if (adicionarDados()){
				this.mostrarMensagemSucesso("Operação realizada com sucesso");
				super.consultar();
				//atualizarListas();
			}
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar." + e.getMessage());
			e.printStackTrace();
		}			
		return this.getPaginasRetorno().get("confirmar");
	}

	public void carregar() {
		try {
			dataBase = this.registro.getDataConsumo();
			this.registro = fachada.obterConsumo(this.registro.getCodigo());
			consumoETA = this.registro;
			valores = new String[50][50][2];
			listaProdutos.clear();
			for (int intI = 0; intI < this.registro.getConsumoProduto().size(); intI++) {
				listaProdutos.add(this.registro.getConsumoProduto().get(intI).getProduto());
	            DecimalFormat df = new DecimalFormat("#,##0.00");
	            String dx = df.format(this.registro.getConsumoProduto().get(intI).getQtdConsumo());
				this.valores[intI][0][0] = dx;
				this.valores[intI][0][1] = this.registro.getCodigo().toString(); //EDITAR
			}
			this.datas.clear();
			this.datas.add(this.registro.getDataConsumo());
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao carregar Consumo da ETA");
			e.printStackTrace();
		}			
			
	}

	private boolean adicionarDados() throws Exception {
		this.registro.getConsumoProduto().clear();
		ConsumoETAProduto consumoProduto;
		Produto produto;
		String strValor = "";
		
		// Valores dos dias
		for (int i = 0; i < 7; i++) {
			if (valores[0][i][1] == "0") {//INCLUSÃO
				if (!validaDataConsumo(this.datas.get(i))) return false;
				this.registro = new ConsumoETA();
				
				this.registro.setRegionalProxy(this.consumoETA.getRegionalProxy());
				this.registro.setUnidadeNegocioProxy(this.consumoETA.getUnidadeNegocioProxy());
				this.registro.setMunicipioProxy(this.consumoETA.getMunicipioProxy());
				this.registro.setLocalidadeProxy(this.consumoETA.getLocalidadeProxy());
				this.registro.setEta(this.consumoETA.getEta());
			
				this.registro.setDataConsumo(this.datas.get(i));
				
				for (int j = 0; j < this.listaProdutos.size(); j++) {
					consumoProduto = new ConsumoETAProduto();
					consumoProduto.setConsumo(this.registro);
					produto = this.listaProdutos.get(j);
					consumoProduto.setProduto(produto);
					strValor = valores[j][i][0].replace(".", "").replace(",", "."); 
					Double qtdConsumo = (strValor.isEmpty() ? 0.0f : Double.parseDouble(strValor));
					consumoProduto.setQtdConsumo(qtdConsumo);
					this.registro.getConsumoProduto().add(consumoProduto);
//					fachada.salvar(consumoProduto);
				}
				registro.setUsuario(usuarioProxy.getCodigo());
				registro.setUltimaAlteracao(new Date());
				fachada.salvar(registro);
//				fachada.salvar(registro.getConsumoProduto());
			}
			else{//ALTERAÇÃO
				if (valores[0][i][0] != null){
					this.registro = new ConsumoETA();
					this.registro = fachada.obterConsumoLazy(Integer.parseInt(valores[0][i][1]));
					if (!validaDataConsumo(registro.getDataConsumo())) return false;
					this.registro.setConsumoProduto(new ArrayList<ConsumoETAProduto>());
					for (int j = 0; j < this.listaProdutos.size(); j++) {
						consumoProduto = new ConsumoETAProduto();
						consumoProduto.setConsumo(this.registro);
						produto = this.listaProdutos.get(j);
						consumoProduto.setProduto(produto);
						strValor = valores[j][i][0].replace(".", "").replace(",", "."); 
						Double qtdConsumo = (strValor.isEmpty() ? 0.0f : Double.parseDouble(strValor));
						consumoProduto.setQtdConsumo(qtdConsumo);
						this.registro.getConsumoProduto().add(consumoProduto);
						fachada.atualizarConsumoProduto(consumoProduto);
					}
					registro.setUsuario(usuarioProxy.getCodigo());
					registro.setUltimaAlteracao(new Date());
					fachada.atualizar(registro);
				}
			}
		}
		return true;
	}
	
	private boolean validaDataConsumo(Date datConsumo){
		if (bloqueiaDataRetroativa.equals("0")){
			return true; 
		}
		else{
			Integer dataReferencia = Integer.parseInt((String) session.getAttribute("referencia"));
			SimpleDateFormat formataData = new SimpleDateFormat("yyyyMM");	
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(datConsumo);
			Integer dataConsumo = Integer.parseInt(formataData.format(gc.getTime()));
			SimpleDateFormat formataData2 = new SimpleDateFormat("dd/MM/yyyy");
			String strDataConsumo = formataData2.format(gc.getTime());
			gc.setTime(new Date());
			Integer dataCorrente = Integer.parseInt(formataData.format(gc.getTime()));
			//Permitir lançamentos entre Mês de Referência e o Mês Corrente
			if (dataConsumo >= dataReferencia && dataConsumo <= dataCorrente){
				return true;
			}
			this.mostrarMensagemErro("Data de Consumo fora do período permitido! (" + strDataConsumo + ")");
			return false;
		}
	}
}
