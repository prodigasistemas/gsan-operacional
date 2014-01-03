package br.gov.pa.cosanpa.gopera.managedBean;

import java.text.DecimalFormat;
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
import br.gov.pa.cosanpa.gopera.fachada.IETEVolume;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.model.ETE;
import br.gov.pa.cosanpa.gopera.model.ETEVolume;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@ManagedBean
@SessionScoped
public class EteVolumeBean extends BaseBean<ETEVolume> {

	@EJB
	private IETEVolume fachada;
	@EJB
	private IETE fachadaETE;
	@EJB
	private IProxy fachadaProxy;

	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private List<ETE> listaETE = new ArrayList<ETE>();
	private LazyDataModel<ETEVolume> listaConsumo = null;
	private String volumeColetado;
	private String volumeTratado;
	
	public String getVolumeColetado() {
		return volumeColetado;
	}

	public void setVolumeColetado(String volumeColetado) {
		this.volumeColetado = volumeColetado;
	}

	public String getVolumeTratado() {
		return volumeTratado;
	}

	public void setVolumeTratado(String volumeTratado) {
		this.volumeTratado = volumeTratado;
	}

	public LazyDataModel<ETEVolume> getListaConsumo() {
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

	public List<ETE> getListaETE() {
		if (this.registro.getLocalidadeProxy().getCodigo() != null) {
			try {
				this.listaETE = fachadaETE.getListaETE(this.registro.getRegionalProxy().getCodigo(), 
											     	this.registro.getUnidadeNegocioProxy().getCodigo(), 
											     	this.registro.getMunicipioProxy().getCodigo(),
											     	this.registro.getLocalidadeProxy().getCodigo());
				return this.listaETE;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar ETE´s.");
			}
		}
		listaETE = new ArrayList<ETE>();
		return listaETE;
	}
	
	@Override
	public String iniciar() {
		// Fachada do EJB
		this.setFachadaBean(this.fachada);
		iniciarLazy();
		this.visualizar();			// Cria uma nova instância do registro para um novo cadastro
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new ETEVolume();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "EteVolume.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}

	private void iniciarLazy(){
		if (listaConsumo == null) {  
			listaConsumo = new LazyDataModel<ETEVolume>() {
				private static final long serialVersionUID = 1L;

				@Override
				public List<ETEVolume> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {
					try {					
						List<ETEVolume> listaLazy = fachada.obterListaLazy(startingAt, maxPerPage, filters);
						setRowCount(fachada.obterQtdRegistros(filters));
						setPageSize(maxPerPage);						
						return listaLazy;						
					} catch (Exception e) {
						e.printStackTrace();
					}	
					return null;
				}
				
				@Override
				public Object getRowKey(ETEVolume consumo) {
					return consumo.getCodigo();
				}
				
				@Override
				public ETEVolume getRowData(String consumoId) {
					if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
						Integer id = Integer.valueOf(consumoId);
						
						for (ETEVolume consumo : listaConsumo) {
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
		this.registro = new ETEVolume();
		this.setVolumeColetado("0,00");
		this.setVolumeTratado("0,00");
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
	
	private void carregar(){
		try {
			this.registro = fachada.obterETEVolume(this.registro.getCodigo());
			DecimalFormat df = new DecimalFormat("#,##0.00");
			this.setVolumeColetado(df.format(this.registro.getVolumeColetado())); 
			this.setVolumeTratado(df.format(this.registro.getVolumeTratado()));
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar Volume de Água");
		}
	}
	
	@Override
	public String cadastrar() {
		try {
			//Verifica se não está cadastrado para o mes de referencia corrente
			if (!this.getEditando()){
				if(!fachada.verificaMesReferencia(this.registro.getEte().getCodigo(), this.filtroData(this.registro.getReferencia(), "yyyy-MM-dd")) ){
					throw new Exception("Mês de referência já cadastrado!");
				}
			}
			if(!validaReferencia(this.registro.getReferencia())) return null;
			return super.cadastrar();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar: " + e.getMessage());
				
		}
		return null;
	}
	
	@Override
	public String confirmar() {
		try {
			this.registro.setVolumeColetado(Double.parseDouble(this.getVolumeColetado().replace(".", "").replace(",", ".")));
			this.registro.setVolumeTratado(Double.parseDouble(this.getVolumeTratado().replace(".", "").replace(",", ".")));
			registro.setUsuario(usuarioProxy);
			registro.setUltimaAlteracao(new Date());			
			return super.confirmarLazy();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
