package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import jxl.common.Logger;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.pa.cosanpa.gopera.exception.BaseRuntimeException;
import br.gov.pa.cosanpa.gopera.exception.MesReferenciaJaCadastradoException;
import br.gov.pa.cosanpa.gopera.fachada.IAnaliseClinica;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.model.AnaliseClinica;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@ManagedBean
@SessionScoped
public class AnaliseLaboratorialBean extends BaseBean<AnaliseClinica> {
	private Logger logger = Logger.getLogger(AnaliseLaboratorialBean.class);

	@EJB
	private IAnaliseClinica fachada;
	@EJB
	private IProxy fachadaProxy;

	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private LazyDataModel<AnaliseClinica> listaConsumo = null;

	public LazyDataModel<AnaliseClinica> getListaConsumo() {
		return listaConsumo;
	}
	
	public List<RegionalProxy> getRegionais() {
		try {
			regionais = fachadaProxy.getListaRegional();
			return regionais; 
		} catch (Exception e) {
			logger.error(bundle.getText("erro_consulta_sistema_externo"), e);
			mostrarMensagemErro(bundle.getText("erro_consulta_sistema_externo"));
		}
		return regionais;
	}

	public List<UnidadeNegocioProxy> getUnidadesNegocio() {
		if ((this.registro.getRegionalProxy().getCodigo() != null)) {
			try {
				this.unidadesNegocio =  fachadaProxy.getListaUnidadeNegocio(this.registro.getRegionalProxy().getCodigo());
				return unidadesNegocio;
			} catch (Exception e) {
				logger.error(bundle.getText("erro_consulta_sistema_externo"), e);
				mostrarMensagemErro(bundle.getText("erro_consulta_sistema_externo"));
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
				logger.error(bundle.getText("erro_consulta_sistema_externo"), e);
				mostrarMensagemErro(bundle.getText("erro_consulta_sistema_externo"));
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
				logger.error(bundle.getText("erro_consulta_sistema_externo"), e);
				mostrarMensagemErro(bundle.getText("erro_consulta_sistema_externo"));
			}
		}
		this.localidades = new ArrayList<LocalidadeProxy>();
		return this.localidades;
	}
	
	@Override
	public String iniciar() {
		// Fachada do EJB
		this.setFachadaBean(this.fachada);
		iniciarLazy();
		this.visualizar();			// Cria uma nova instância do registro para um novo cadastro
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new AnaliseClinica();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "AnaliseLaboratorial.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}

	private void iniciarLazy(){
		if (listaConsumo == null) {  
			listaConsumo = new LazyDataModel<AnaliseClinica>() {
				private static final long serialVersionUID = 1L;

				@Override
				public List<AnaliseClinica> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {
					try {					
						List<AnaliseClinica> listaLazy = fachada.obterListaLazy(startingAt, maxPerPage, filters);
						setRowCount(fachada.obterQtdRegistros(filters));
						setPageSize(maxPerPage);						
						return listaLazy;						
					} catch (Exception e) {
						e.printStackTrace();
					}	
					return null;
				}
				
				@Override
				public Object getRowKey(AnaliseClinica consumo) {
					return consumo.getCodigo();
				}
				
				@Override
				public AnaliseClinica getRowData(String consumoId) {
					if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
						Integer id = Integer.valueOf(consumoId);
						
						for (AnaliseClinica consumo : listaConsumo) {
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
		this.registro = new AnaliseClinica();
		return super.novo();
	}

	@Override
	public String cadastrar() {
		try {
			//Verifica se não está cadastrado para o mes de referencia corrente
			if (!this.getEditando()){
				if(!fachada.verificaMesReferencia(this.registro.getRegionalProxy().getCodigo(),
						  this.registro.getUnidadeNegocioProxy().getCodigo(),
						  this.registro.getMunicipioProxy().getCodigo(),
						  this.registro.getLocalidadeProxy().getCodigo(),
						  this.filtroData(this.registro.getReferencia(), "yyyy-MM-dd")) ){
					throw new MesReferenciaJaCadastradoException();
				}
			}
			if(!validaReferencia(this.registro.getReferencia())) return null;
			return super.cadastrar();
		}catch (BaseRuntimeException e) {
			logger.error(bundle.getText(e.getMessage()), e);
			mostrarMensagemErro(bundle.getText(e.getMessage()));
		} 
		catch (Exception e) {
			logger.error(bundle.getText("erro_salvar"), e);
			mostrarMensagemErro(bundle.getText("erro_salvar"));
		}
		return null;
	}
	
	@Override
	public String confirmar() {
		try {
			registro.setUsuario(usuarioProxy);
			registro.setUltimaAlteracao(new Date());			
			return super.confirmarLazy();
		} catch (Exception e) {
			logger.error(bundle.getText("erro_salvar"), e);
			mostrarMensagemErro(bundle.getText("erro_salvar"));
		}
		return null;
	}
}
