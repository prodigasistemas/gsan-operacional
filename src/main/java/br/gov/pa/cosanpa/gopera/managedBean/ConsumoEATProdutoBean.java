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

import br.gov.model.operacao.ConsumoEAT;
import br.gov.model.operacao.ConsumoEATProduto;
import br.gov.model.operacao.EEAT;
import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.Produto;
import br.gov.model.operacao.RegionalProxy;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.servicos.operacao.ConsumoEATRepositorio;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;

@SessionScoped
@ManagedBean
public class ConsumoEATProdutoBean extends BaseBean<ConsumoEAT> {

	@EJB
	private ConsumoEATRepositorio fachada;
	
	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;
	
	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private List<EEAT> listaEAT = new ArrayList<EEAT>();
	private List<Produto> listaProdutos = new ArrayList<Produto>();
	private String valores[][][] = new String[50][50][2];
	private List<Date> datas = new ArrayList<Date>();
	private Date dataBase = null;
	private boolean sistemaSelecionado = false;
	private ConsumoEAT consumoEAT;
	private LazyDataModel<ConsumoEAT> listaConsumo = null;
	private String bloqueiaDataRetroativa = "1";

	public List<RegionalProxy> getRegionais() {
		try {
			regionais = fachada.getListaConsumoEATRegional(usuarioProxy);
			return regionais; 
		} catch (Exception e) {
			mostrarMensagemErro("Erro ao consultar sistema externo.");
		}
		return regionais;
	}

	public List<UnidadeNegocioProxy> getUnidadesNegocio() {
		if ((this.consumoEAT.getRegionalProxy().getCodigo() != null)) {
			try {
				this.unidadesNegocio =  fachada.getListaConsumoEATUnidadeNegocio(usuarioProxy, 
																						   this.consumoEAT.getRegionalProxy().getCodigo());
				return unidadesNegocio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		return unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	}

	public List<MunicipioProxy> getMunicipios() {
		if (this.consumoEAT.getUnidadeNegocioProxy().getCodigo() != null) {
			try {
				this.municipios =  fachada.getListaConsumoEATMunicipio(usuarioProxy, 
																				 this.consumoEAT.getRegionalProxy().getCodigo(), 
																				 this.consumoEAT.getUnidadeNegocioProxy().getCodigo());
				return this.municipios;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.municipios = new ArrayList<MunicipioProxy>();
		return this.municipios;
	}

	public List<LocalidadeProxy> getLocalidades() {
		if (this.consumoEAT.getMunicipioProxy().getCodigo() != null) {
			try {
				this.localidades =  fachada.getListaConsumoEATLocalidade(usuarioProxy, 
																				   this.consumoEAT.getRegionalProxy().getCodigo(), 
																				   this.consumoEAT.getUnidadeNegocioProxy().getCodigo(), 
																				   this.consumoEAT.getMunicipioProxy().getCodigo());
				return this.localidades;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.localidades = new ArrayList<LocalidadeProxy>();
		return this.localidades;
	}

	public List<EEAT> getListaEAT() {
		if (this.consumoEAT.getLocalidadeProxy().getCodigo() != null) {
			try {
				this.listaEAT = fachada.getListaConsumoEAT(usuarioProxy, 
																     this.consumoEAT.getRegionalProxy().getCodigo(), 
																     this.consumoEAT.getUnidadeNegocioProxy().getCodigo(), 
																     this.consumoEAT.getMunicipioProxy().getCodigo(),
																     this.consumoEAT.getLocalidadeProxy().getCodigo());
				return this.listaEAT;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar EAT´s.");
			}
		}
		listaEAT = new ArrayList<EEAT>();
		return listaEAT;
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

	public void setListaEAT(List<EEAT> listaEAT) {
		this.listaEAT = listaEAT;
	}	
	
	public LazyDataModel<ConsumoEAT> getListaConsumo() {
		return listaConsumo;
	}

	public ConsumoEAT getConsumoEAT() {
		return consumoEAT;
	}

	public void setConsumoEAT(ConsumoEAT consumoEAT) {
		this.consumoEAT = consumoEAT;
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
		if (this.consumoEAT.getEat().getCodigo() != null){
			this.sistemaSelecionado = (this.consumoEAT.getEat().getCodigo() != 0 && dataBase != null ? true : false);
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
	public List<ConsumoEAT> getListaEAT() {
		try {
			List<ConsumoEAT> lista = fachadaProxy.getListaConsumoEATUsuario(usuarioProxy);
			if (lista.size() > 0){
				if (consumoEAT == null) consumoEAT = lista.get(0);
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
			listaProdutos = fachadaProxy.getListaProdutoEAT(this.consumoEAT.getRegionalProxy().getCodigo(),
															 this.consumoEAT.getUnidadeNegocioProxy().getCodigo(),
															 this.consumoEAT.getMunicipioProxy().getCodigo(),
															 this.consumoEAT.getLocalidadeProxy().getCodigo(),
															 this.consumoEAT.getEat().getCodigo(),
															 dataIni, dataFim);
	
			//Carrega Lista de Valores
			valores = new String[50][50][2];
			Integer intI = 0, intJ = 0;
			String valorAux[];
			for (Produto produto : listaProdutos){
			    c.setTime(dataBase);
				for (intJ=0;intJ<=6;intJ++){
					valorAux = fachadaProxy.getConsumoEAT(this.consumoEAT.getRegionalProxy().getCodigo(),
															 this.consumoEAT.getUnidadeNegocioProxy().getCodigo(),
															 this.consumoEAT.getMunicipioProxy().getCodigo(),
															 this.consumoEAT.getLocalidadeProxy().getCodigo(),
															 this.consumoEAT.getEat().getCodigo(),
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
		this.registro = new ConsumoEAT();
		consumoEAT = new ConsumoEAT();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "ConsumoEATProduto.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");
	}

	private void iniciarLazy(){
		if (listaConsumo == null) {  
			listaConsumo = new LazyDataModel<ConsumoEAT>() {
				private static final long serialVersionUID = 1L;

				@Override
				public List<ConsumoEAT> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
					try {					
						List<ConsumoEAT> listaLazy = fachada.obterListaLazy(startingAt, maxPerPage, filters);
						setRowCount(fachada.obterQtdRegistros(filters));
						setPageSize(maxPerPage);						
						return listaLazy;						
					} catch (Exception e) {
						e.printStackTrace();
					}	
					return null;
				}
				
				@Override
				public Object getRowKey(ConsumoEAT consumo) {
					return consumo.getCodigo();
				}
				
				@Override
				public ConsumoEAT getRowData(String consumoId) {
					if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
						Integer id = Integer.valueOf(consumoId);
						
						for (ConsumoEAT consumo : listaConsumo) {
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

	@Override
	public String novo(){
		try{
			dataBase = null;
			consumoEAT = new ConsumoEAT();		
			this.registro = new ConsumoEAT();
			//Recupera Parametros
			bloqueiaDataRetroativa = fachadaProxy.getParametroSistema(8);
		} catch (Exception e) {
			this.mostrarMensagemErro(e.getMessage());
		}
		return super.novo();		
	}
	
	@Override
	public String consultar() {
		carregar();
		return super.consultar();
	}
	
	@Override
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
	
	@Override
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

	@Override
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
			consumoEAT = this.registro;
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
			this.mostrarMensagemErro("Erro ao carregar Consumo da EAT");
			e.printStackTrace();
		}			
			
	}

	private boolean adicionarDados() throws Exception {
		this.registro.getConsumoProduto().clear();
		ConsumoEATProduto consumoProduto;
		Produto produto;
		String strValor = "";
		
		// Valores dos dias
		for (int i = 0; i < 7; i++) {
			if (valores[0][i][1] == "0") {//INCLUSÃO
				if (!validaDataConsumo(this.datas.get(i))) return false;
				this.registro = new ConsumoEAT();
				
				this.registro.setRegionalProxy(this.consumoEAT.getRegionalProxy());
				this.registro.setUnidadeNegocioProxy(this.consumoEAT.getUnidadeNegocioProxy());
				this.registro.setMunicipioProxy(this.consumoEAT.getMunicipioProxy());
				this.registro.setLocalidadeProxy(this.consumoEAT.getLocalidadeProxy());
				this.registro.setEat(this.consumoEAT.getEat());
			
				this.registro.setDataConsumo(this.datas.get(i));
				
				for (int j = 0; j < this.listaProdutos.size(); j++) {
					consumoProduto = new ConsumoEATProduto();
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
					this.registro = new ConsumoEAT();
					this.registro = fachada.obterConsumoLazy(Integer.parseInt(valores[0][i][1]));
					if (!validaDataConsumo(registro.getDataConsumo())) return false;
					this.registro.setConsumoProduto(new ArrayList<ConsumoEATProduto>());
					for (int j = 0; j < this.listaProdutos.size(); j++) {
						consumoProduto = new ConsumoEATProduto();
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
