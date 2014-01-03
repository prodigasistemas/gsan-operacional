package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.pa.cosanpa.gopera.fachada.IETE;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.model.ETE;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@ManagedBean
@SessionScoped
public class EteBean extends BaseBean<ETE> {

	@EJB
	private IProxy fachadaProxy;
	@EJB
	private IETE fachadaETE;
	
	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();	
	private LazyDataModel<ETE> listaConsumo = null;
	
	public EteBean() {
		
	}

	public LazyDataModel<ETE> getListaConsumo() {
		return listaConsumo;
	}
	
	public List<RegionalProxy> getRegionais() {
		try {
			regionais = fachadaProxy.getListaRegional();
			return regionais; 
		} catch (Exception e) {
			mostrarMensagemErro("Erro ao consultar sistema externo.");
		}
		return regionais;
	}

	public List<UnidadeNegocioProxy> getUnidadesNegocio() {
		if ((this.registro.getRegionalProxy().getCodigo() != null)) {
			try {
				this.unidadesNegocio =  fachadaProxy.getListaUnidadeNegocio(this.registro.getRegionalProxy().getCodigo());
				return unidadesNegocio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		return unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	}

	public List<MunicipioProxy> getMunicipios() {
		if (this.registro.getUnidadeNegocioProxy().getCodigo() != null) {
			try {
				this.municipios =  fachadaProxy.getListaMunicipio(this.registro.getRegionalProxy().getCodigo(), 
															 	  this.registro.getUnidadeNegocioProxy().getCodigo());
				return this.municipios;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.municipios = new ArrayList<MunicipioProxy>();
		return this.municipios;
	}

	public List<LocalidadeProxy> getLocalidades() {
		if (this.registro.getMunicipioProxy().getCodigo() != null) {
			try {
				this.localidades =  fachadaProxy.getListaLocalidade(this.registro.getRegionalProxy().getCodigo(), 
															   		this.registro.getUnidadeNegocioProxy().getCodigo(), 
															   		this.registro.getMunicipioProxy().getCodigo());
				return this.localidades;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.localidades = new ArrayList<LocalidadeProxy>();
		return this.localidades;
	}

	@Override
	public String iniciar() {
		// Fachada do EJB
		this.setFachadaBean(this.fachadaETE);
		iniciarLazy();
		this.visualizar();	
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new ETE();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "Ete.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}

	private void iniciarLazy(){
		if (listaConsumo == null) {  
			listaConsumo = new LazyDataModel<ETE>() {
				private static final long serialVersionUID = 1L;

				@Override
				public List<ETE> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {
					try {					
						List<ETE> listaLazy = fachadaETE.obterListaLazy(startingAt, maxPerPage, filters);
						setRowCount(fachadaETE.obterQtdRegistros(filters));
						setPageSize(maxPerPage);						
						return listaLazy;						
					} catch (Exception e) {
						e.printStackTrace();
					}	
					return null;
				}
				
				@Override
				public Object getRowKey(ETE consumo) {
					return consumo.getCodigo();
				}
				
				@Override
				public ETE getRowData(String consumoId) {
					if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
						Integer id = Integer.valueOf(consumoId);
						
						for (ETE consumo : listaConsumo) {
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
	public String novo() {
		this.registro = new ETE();
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
	
	public void carregar(){
		try {
			this.registro = fachadaETE.obterETELazy(this.registro.getCodigo());
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao carregar Horas ETE");
		}
	}
	
	@Override
	public String confirmar() {
		try {
			registro.setUsuario(usuarioProxy);
			registro.setUltimaAlteracao(new Date());
			return super.confirmarLazy();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar: " + e.getMessage());
		}
		return null;
	}
}
