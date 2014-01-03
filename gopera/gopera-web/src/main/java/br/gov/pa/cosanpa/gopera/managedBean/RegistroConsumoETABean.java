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

import br.gov.pa.cosanpa.gopera.fachada.IETA;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.fachada.IRegistroConsumo;
import br.gov.pa.cosanpa.gopera.fachada.IRegistroConsumoETA;
import br.gov.pa.cosanpa.gopera.model.ETA;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.RegistroConsumo;
import br.gov.pa.cosanpa.gopera.model.RegistroConsumoETA;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@ManagedBean
@SessionScoped
public class RegistroConsumoETABean extends BaseBean<RegistroConsumoETA> {
	@EJB
	private IRegistroConsumoETA fachada;
	@EJB
	private IETA fachadaETA;
	@EJB
	private IRegistroConsumo fachadaRegistroConsumo;

	@EJB
	private IProxy fachadaProxy;

	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private List<ETA> listaETA = new ArrayList<ETA>();
	private List<RegistroConsumo> registrosConsumo = new ArrayList<RegistroConsumo>();
	private List<String> registrosConsumoSelecionados = new ArrayList<String>();
	private LazyDataModel<RegistroConsumoETA> listaConsumo = null;

	public RegistroConsumoETABean() {
		carregarListaRegistroConsumo();
	}

	public LazyDataModel<RegistroConsumoETA> getListaConsumo() {
		return listaConsumo;
	}
	
	private void carregarListaRegistroConsumo() {
		registrosConsumoSelecionados.clear();
		registrosConsumo.clear();
	}

	public List<SelectItem> getListaRegistroConsumo() throws Exception {
		List<SelectItem> listaCheck = new ArrayList<SelectItem>();

		if (registrosConsumo.isEmpty()) {
			this.registrosConsumo = this.fachadaRegistroConsumo.obterLista(0, 0);
		}
		SelectItem selectItem;
		for (RegistroConsumo registroConsumo : this.registrosConsumo) {
			selectItem = new SelectItem(registroConsumo.getCodigo(), registroConsumo.getDescricao(), "", false, false, false);
			listaCheck.add(selectItem);
		}
		return listaCheck;
	}

	public List<ETA> getListaETA() {
		try {
			this.listaETA = fachadaETA.obterLista(0, 0);
			return this.listaETA;
		} catch (Exception e) {
			mostrarMensagemErro("Erro ao consultar ETA´s.");
		}
		listaETA = new ArrayList<ETA>();
		return listaETA;
	}

	public void setListaETA(List<ETA> listaETA) {
		this.listaETA = listaETA;
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

	public RegistroConsumoETA getRegistro() {
		return registro;
	}

	public void setRegistro(RegistroConsumoETA registro) {
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
		this.registro = new RegistroConsumoETA();
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
			registro.setUsuario(usuarioProxy);
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
		this.setFachadaBean(this.fachada);
		iniciarLazy();
		this.visualizar();	
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new RegistroConsumoETA();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "RegistroConsumoETA.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}

	private void iniciarLazy(){
		if (listaConsumo == null) {  
			listaConsumo = new LazyDataModel<RegistroConsumoETA>() {
				private static final long serialVersionUID = 1L;

				@Override
				public List<RegistroConsumoETA> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {
					try {					
						List<RegistroConsumoETA> listaLazy = fachada.obterListaLazy(startingAt, maxPerPage, filters);
						setRowCount(fachada.obterQtdRegistros(filters));
						setPageSize(maxPerPage);						
						return listaLazy;						
					} catch (Exception e) {
						e.printStackTrace();
					}	
					return null;
				}
				
				@Override
				public Object getRowKey(RegistroConsumoETA consumo) {
					return consumo.getCodigo();
				}
				
				@Override
				public RegistroConsumoETA getRowData(String consumoId) {
					if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
						Integer id = Integer.valueOf(consumoId);
						
						for (RegistroConsumoETA consumo : listaConsumo) {
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
