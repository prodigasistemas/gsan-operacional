package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.model.operacao.EEAT;
import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.RegionalProxy;
import br.gov.model.operacao.RegistroConsumo;
import br.gov.model.operacao.RegistroConsumoEAT;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.servicos.operacao.EeatRepositorio;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;
import br.gov.servicos.operacao.RegistroConsumoEATRepositorio;
import br.gov.servicos.operacao.RegistroConsumoRepositorio;

@ManagedBean
@SessionScoped
public class RegistroConsumoEATBean extends BaseBean<RegistroConsumoEAT> {
	@EJB
	private RegistroConsumoEATRepositorio fachada;
	
	@EJB
	private EeatRepositorio fachadaEAT;
	
	@EJB
	private RegistroConsumoRepositorio fachadaRegistroConsumo;

	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;

	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private List<EEAT> listaEAT = new ArrayList<EEAT>();
	private List<RegistroConsumo> registrosConsumo = new ArrayList<RegistroConsumo>();
	private List<String> registrosConsumoSelecionados = new ArrayList<String>();
	private LazyDataModel<RegistroConsumoEAT> listaConsumo = null;
	
	public RegistroConsumoEATBean() {
		carregarListaRegistroConsumo();
	}
	
	public LazyDataModel<RegistroConsumoEAT> getListaConsumo() {
		return listaConsumo;
	}

	private void carregarListaRegistroConsumo() {
		registrosConsumoSelecionados.clear();
		registrosConsumo.clear();
	}

	public List<SelectItem> getListaRegistroConsumo() throws Exception {
		List<SelectItem> listaCheck = new ArrayList<SelectItem>();

		if (registrosConsumo.isEmpty()) {
			this.registrosConsumo = this.fachadaRegistroConsumo.obterLista();
		}
		SelectItem selectItem;
		for (RegistroConsumo registroConsumo : this.registrosConsumo) {
			selectItem = new SelectItem(registroConsumo.getCodigo(), registroConsumo.getDescricao(), "", false, false, false);
			listaCheck.add(selectItem);
		}
		return listaCheck;
	}

	public List<EEAT> getListaEAT() {
		try {
			this.listaEAT = fachadaEAT.obterLista();
			return this.listaEAT;
		} catch (Exception e) {
			mostrarMensagemErro("Erro ao consultar EAT´s.");
		}
		listaEAT = new ArrayList<EEAT>();
		return listaEAT;
	}

	public void setListaEAT(List<EEAT> listaEAT) {
		this.listaEAT = listaEAT;
	}

	public List<RegionalProxy> getRegionais() {
		try {
			regionais = fachadaProxy.getListaRegional();
//			regionais.add(0, new RegionalProxy(-1, "Selecione"));
			return regionais; 
		} catch (Exception e) {
			mostrarMensagemErro("Erro ao consultar sistema externo.");
		}
		return regionais;
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

	public void setRegionais(List<RegionalProxy> regionais) {
		this.regionais = regionais;
	}

	public List<UnidadeNegocioProxy> getUnidadesNegocio() {
		if ((this.registro.getRegionalProxy().getCodigo() != null)) {
			try {
				this.unidadesNegocio =  fachadaProxy.getListaUnidadeNegocio(this.registro.getRegionalProxy().getCodigo());
//				this.unidadesNegocio.add(0, new UnidadeNegocioProxy(-1, "Selecione"));
				return unidadesNegocio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		return unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	}

	public void setUnidadesNegocio(List<UnidadeNegocioProxy> unidadesNegocio) {
		this.unidadesNegocio = unidadesNegocio;
	}

	public List<MunicipioProxy> getMunicipios() {
		if (this.registro.getUnidadeNegocioProxy().getCodigo() != null) {
			try {
				this.municipios =  fachadaProxy.getListaMunicipio(this.registro.getRegionalProxy().getCodigo(), this.registro.getUnidadeNegocioProxy().getCodigo());
//				this.municipios.add(0, new MunicipioProxy(-1, "Selecione"));
				return this.municipios;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.municipios = new ArrayList<MunicipioProxy>();
		return this.municipios;
	}

	public void setMunicipios(List<MunicipioProxy> municipios) {
		this.municipios = municipios;
	}

	public List<LocalidadeProxy> getLocalidades() {
		if (this.registro.getMunicipioProxy().getCodigo() != null) {
			try {
				this.localidades =  fachadaProxy.getListaLocalidade(this.registro.getRegionalProxy().getCodigo(), this.registro.getUnidadeNegocioProxy().getCodigo(), this.registro.getMunicipioProxy().getCodigo());
//				this.localidades.add(0, new LocalidadeProxy(-1, "Selecione"));
				return this.localidades;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.localidades = new ArrayList<LocalidadeProxy>();
		return this.localidades;
	}

	public void setLocalidades(List<LocalidadeProxy> localidades) {
		this.localidades = localidades;
	}

	public RegistroConsumoEAT getRegistro() {
		return registro;
	}

	public void setRegistro(RegistroConsumoEAT registro) {
		this.registro = registro;
	}

	public void consultarRegistroConsumo() {
		try {
			this.registro = fachada.obterRegistroConsumo(this.registro.getCodigo());
			this.registrosConsumoSelecionados.clear();
			for (RegistroConsumo consumo : this.registro.getRegistrosConsumo()) {
				registrosConsumoSelecionados.add(consumo.getCodigo().toString());
			}
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao carregar Registro de Consumo");
			e.printStackTrace();
		}	
	}

	@Override
	public String novo() {
		this.registrosConsumoSelecionados.clear();
		this.registro = new RegistroConsumoEAT();
		return super.novo();
	}
	
	@Override	
	public String consultar() {
		consultarRegistroConsumo();
		return super.consultar();
	}

	@Override
	public String alterar() {
		consultarRegistroConsumo();
		return super.alterar();
	}

	public String confirmar() {
		try {
			registro.setUsuario(usuarioProxy.getCodigo());
			registro.setUltimaAlteracao(new Date());
			adicionarRegistroConsumoSelecionados();
			return super.confirmarLazy();
		} catch (Exception e) {
			this.mostrarMensagemErro(e.getMessage());
		}
		return null;
	}

	private void adicionarRegistroConsumoSelecionados() {
		this.registro.getRegistrosConsumo().clear();

		for (String itemSelecionado : registrosConsumoSelecionados) {
			for (RegistroConsumo registroConsumo : this.getRegistrosConsumo()) {
				if (registroConsumo.getCodigo().equals(Integer.parseInt(itemSelecionado))) {
					this.registro.getRegistrosConsumo().add(registroConsumo);
				}
			}
		}
	}

	public List<RegistroConsumo> getRegistrosConsumo() {
		return registrosConsumo;
	}

	public List<String> getRegistrosConsumoSelecionados() {
		return registrosConsumoSelecionados;
	}

	public void setRegistrosConsumoSelecionados(List<String> registrosConsumoSelecionados) {
		this.registrosConsumoSelecionados = registrosConsumoSelecionados;
	}

	@Override
	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		iniciarLazy();
		this.visualizar();	
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new RegistroConsumoEAT();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "RegistroConsumoEAT.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}

	private void iniciarLazy(){
		if (listaConsumo == null) {  
			listaConsumo = new LazyDataModel<RegistroConsumoEAT>() {
				private static final long serialVersionUID = 1L;

				@Override
				public List<RegistroConsumoEAT> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
					try {					
						List<RegistroConsumoEAT> listaLazy = fachada.obterListaLazy(startingAt, maxPerPage, filters);
						setRowCount(fachada.obterQtdRegistros(filters));
						setPageSize(maxPerPage);						
						return listaLazy;						
					} catch (Exception e) {
						e.printStackTrace();
					}	
					return null;
				}
				
				@Override
				public Object getRowKey(RegistroConsumoEAT consumo) {
					return consumo.getCodigo();
				}
				
				@Override
				public RegistroConsumoEAT getRowData(String consumoId) {
					if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
						Integer id = Integer.valueOf(consumoId);
						
						for (RegistroConsumoEAT consumo : listaConsumo) {
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
	
}
