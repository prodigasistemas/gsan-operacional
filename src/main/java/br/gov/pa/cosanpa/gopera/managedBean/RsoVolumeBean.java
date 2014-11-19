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

import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.RSO;
import br.gov.model.operacao.RSOVolume;
import br.gov.model.operacao.RegionalProxy;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.servicos.operacao.RsoRepositorio;
import br.gov.servicos.operacao.RsoVolumeRepositorio;
import br.gov.servicos.operacao.UnidadeConsumidoraRepositorio;

@ManagedBean
@SessionScoped
public class RsoVolumeBean extends BaseBean<RSOVolume> {

	@EJB
	private RsoVolumeRepositorio fachada;
	
	@EJB
	private RsoRepositorio fachadaRSO;
	
	@EJB
	private UnidadeConsumidoraRepositorio fachadaUC;

	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private List<RSO> listaRSO = new ArrayList<RSO>();
	private LazyDataModel<RSOVolume> listaConsumo = null;
	private String valorMedicaoEntrada;
	
	public LazyDataModel<RSOVolume> getListaConsumo() {
		return listaConsumo;
	}
	
	public List<RegionalProxy> getRegionais() {
		try {
			regionais = fachadaUC.getListaRegional(usuarioProxy);
			return regionais; 
		} catch (Exception e) {
			mostrarMensagemErro("Erro ao consultar sistema externo.");
		}
		return regionais;
	}

	public List<UnidadeNegocioProxy> getUnidadesNegocio() {
		if ((this.registro.getRegionalProxy().getCodigo() != null)) {
			try {
				this.unidadesNegocio =  fachadaUC.getListaUnidadeNegocio(usuarioProxy, 
											 						    this.registro.getRegionalProxy().getCodigo());
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
				this.municipios =  fachadaUC.getListaMunicipio(usuarioProxy, 
															 this.registro.getRegionalProxy().getCodigo(), 
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
				this.localidades =  fachadaUC.getListaLocalidade(usuarioProxy, 
															   this.registro.getRegionalProxy().getCodigo(), 
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

	public List<RSO> getListaRSO() {
		if (this.registro.getLocalidadeProxy().getCodigo() != null) {
			try {
				this.listaRSO = fachadaUC.getListaRSO(usuarioProxy, 
												     this.registro.getRegionalProxy().getCodigo(), 
												     this.registro.getUnidadeNegocioProxy().getCodigo(), 
												     this.registro.getMunicipioProxy().getCodigo(),
												     this.registro.getLocalidadeProxy().getCodigo());
				return this.listaRSO;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar RSO´s.");
			}
		}
		listaRSO = new ArrayList<RSO>();
		return listaRSO;
	}

	public String getValorMedicaoEntrada() {
		return valorMedicaoEntrada;
	}
	
	public void setValorMedicaoEntrada(String valorMedicaoEntrada) {
		this.valorMedicaoEntrada = valorMedicaoEntrada;
	}
	
	@Override
	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		iniciarLazy();
		this.visualizar();			// Cria uma nova instância do registro para um novo cadastro
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new RSOVolume();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "RsoVolume.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}

	private void iniciarLazy(){
		if (listaConsumo == null) {  
			listaConsumo = new LazyDataModel<RSOVolume>() {
				private static final long serialVersionUID = 1L;

				@Override
				public List<RSOVolume> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
					try {					
						List<RSOVolume> listaLazy = fachada.obterListaLazy(startingAt, maxPerPage, filters);
						setRowCount(fachada.obterQtdRegistros(filters));
						setPageSize(maxPerPage);						
						return listaLazy;						
					} catch (Exception e) {
						e.printStackTrace();
					}	
					return null;
				}
				
				@Override
				public Object getRowKey(RSOVolume consumo) {
					return consumo.getCodigo();
				}
				
				@Override
				public RSOVolume getRowData(String consumoId) {
					if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
						Integer id = Integer.valueOf(consumoId);
						
						for (RSOVolume consumo : listaConsumo) {
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
		this.registro = new RSOVolume();
		this.setValorMedicaoEntrada("0,00");
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
			this.registro = fachada.obterRSOVolume(this.registro.getCodigo());
			DecimalFormat df = new DecimalFormat("#,##0.00");
			this.setValorMedicaoEntrada(df.format(this.registro.getValorMedicaoEntrada()));
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar Volume de Água");
		}
	}
	
	public void carregarMedidor(){
		try {
			RSO rso = fachadaRSO.obterRSO(this.registro.getRso().getCodigo());
			this.registro.setMedidorEntrada(rso.getMedidorEntrada());
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar Medidor");
		}
	}
	
	@Override
	public String cadastrar() {
		try {
			//Verifica se não está cadastrado para o mes de referencia corrente
			if (!this.getEditando()){
				if(!fachada.verificaMesReferencia(this.registro.getRso().getCodigo(), this.filtroData(this.registro.getReferencia(), "yyyy-MM-dd")) ){
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
			registro.setTipomedicao(registro.getMedidorEntrada().getTipoMedicao());
			registro.setValorMedicaoEntrada(Double.parseDouble(valorMedicaoEntrada.replace(".", "").replace(",", ".")));
			registro.setUsuario(usuarioProxy.getCodigo());
			registro.setUltimaAlteracao(new Date());
			return super.confirmarLazy();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar: " + e.getMessage());
		}
		return null;
	}
}
