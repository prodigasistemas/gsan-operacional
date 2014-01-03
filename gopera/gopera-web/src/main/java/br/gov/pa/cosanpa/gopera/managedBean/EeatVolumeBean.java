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

import br.gov.pa.cosanpa.gopera.fachada.IEEAT;
import br.gov.pa.cosanpa.gopera.fachada.IEEATVolume;
import br.gov.pa.cosanpa.gopera.fachada.IUnidadeConsumidora;
import br.gov.pa.cosanpa.gopera.model.EEAT;
import br.gov.pa.cosanpa.gopera.model.EEATFonteCaptacao;
import br.gov.pa.cosanpa.gopera.model.EEATMedidor;
import br.gov.pa.cosanpa.gopera.model.EEATVolume;
import br.gov.pa.cosanpa.gopera.model.EEATVolumeEntrada;
import br.gov.pa.cosanpa.gopera.model.EEATVolumeSaida;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@ManagedBean
@SessionScoped
public class EeatVolumeBean extends BaseBean<EEATVolume> {

	@EJB
	private IEEATVolume fachada;
	@EJB
	private IEEAT fachadaEEAT;
	@EJB
	private IUnidadeConsumidora fachadaUC;

	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private List<EEAT> listaEEAT = new ArrayList<EEAT>();
	private LazyDataModel<EEATVolume> listaConsumo = null;
	private String volumeAux;
	
	public LazyDataModel<EEATVolume> getListaConsumo() {
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

	public List<EEAT> getListaEEAT() {
		if (this.registro.getLocalidadeProxy().getCodigo() != null) {
			try {
				this.listaEEAT = fachadaUC.getListaEAT(usuarioProxy, 
												     this.registro.getRegionalProxy().getCodigo(), 
												     this.registro.getUnidadeNegocioProxy().getCodigo(), 
												     this.registro.getMunicipioProxy().getCodigo(),
												     this.registro.getLocalidadeProxy().getCodigo());
				return this.listaEEAT;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar ETA´s.");
			}
		}
		listaEEAT = new ArrayList<EEAT>();
		return listaEEAT;
	}
	
	public String getVolumeAux() {
		return volumeAux;
	}

	public void setVolumeAux(String volumeAux) {
		this.volumeAux = volumeAux;
	}
	
	public String iniciar() {
		// Fachada do EJB
		this.setFachadaBean(this.fachada);
		iniciarLazy();
		this.visualizar();			// Cria uma nova instância do registro para um novo cadastro
		this.registro = new EEATVolume();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "EeatVolume.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}

	private void iniciarLazy(){
		if (listaConsumo == null) {  
			listaConsumo = new LazyDataModel<EEATVolume>() {
				private static final long serialVersionUID = 1L;

				@Override
				public List<EEATVolume> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {
					try {					
						List<EEATVolume> listaLazy = fachada.obterListaLazy(startingAt, maxPerPage, filters);
						setRowCount(fachada.obterQtdRegistros(filters));
						setPageSize(maxPerPage);						
						return listaLazy;						
					} catch (Exception e) {
						e.printStackTrace();
					}	
					return null;
				}
				
				@Override
				public Object getRowKey(EEATVolume consumo) {
					return consumo.getCodigo();
				}
				
				@Override
				public EEATVolume getRowData(String consumoId) {
					if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
						Integer id = Integer.valueOf(consumoId);
						
						for (EEATVolume consumo : listaConsumo) {
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
	public String cadastrar() {
		try {
			//Verifica se não está cadastrado para o mes de referencia corrente
			if (!this.getEditando()){
				if(!fachada.verificaMesReferencia(this.registro.getEeat().getCodigo(), this.filtroData(this.registro.getReferencia(), "yyyy-MM-dd")) ){
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
	
	public String confirmar() {
		try {
			//Volume
			registro.setVolume(Double.parseDouble(volumeAux.replace(".", "").replace(",", ".")));
			//Medidor de Saída
			for (EEATVolumeSaida volumeSaida : this.registro.getVolumeSaida()){
				volumeSaida.setValorMedicaoSaida(Double.parseDouble(volumeSaida.getValorMedicaoSaidaAux().replace(".", "").replace(",", ".")));		
			}
			//Medidor de Entrada
			for (EEATVolumeEntrada volumeEntrada : this.registro.getVolumeEntrada()){
				volumeEntrada.setValorMedicaoEntrada(Double.parseDouble(volumeEntrada.getValorMedicaoEntradaAux().replace(".", "").replace(",", ".")));		
			}
			registro.setUsuario(usuarioProxy);
			registro.setUltimaAlteracao(new Date());			
			return super.confirmarLazy();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public String novo() {
		this.registro = new EEATVolume();
		volumeAux = "0,00";
		return super.novo();
	}
	
	public String consultar() {
		carregar();
		return super.consultar();
	}
	
	public String alterar() {
		carregar();
		return super.alterar();
	}
	
	private void carregar(){
		try {
			this.registro = fachada.obterEEATVolume(this.registro.getCodigo());
			DecimalFormat df = new DecimalFormat("#,##0.00");
			//Volume
			volumeAux = df.format(registro.getVolume());
			//Medida Saida
			for (EEATVolumeSaida volumeSaida : this.registro.getVolumeSaida()){
				volumeSaida.setValorMedicaoSaidaAux(df.format(volumeSaida.getValorMedicaoSaida()));		
			}			
			//Medidor Entrada
			for (EEATVolumeEntrada volumeEntrada : this.registro.getVolumeEntrada()){
				volumeEntrada.setValorMedicaoEntradaAux(df.format(volumeEntrada.getValorMedicaoEntrada()));		
			}
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar Volume de Água");
		}
	}
	
	public void carregarMedidor(){
		try {
			EEAT eeat = fachadaEEAT.obterEEAT(this.registro.getEeat().getCodigo());
			this.registro.getVolumeSaida().clear();
			for (EEATMedidor medidor : eeat.getMedidorSaida()){
				EEATVolumeSaida volumeSaida = new EEATVolumeSaida();
				volumeSaida.setMedidorSaida(medidor.getMedidorSaida());
				volumeSaida.setEeatVolume(this.registro);
				volumeSaida.setTipomedicao(medidor.getMedidorSaida().getTipoMedicao());
				this.registro.getVolumeSaida().add(volumeSaida);
			}
			for (EEATFonteCaptacao fonte : eeat.getFonteCaptacao()){
				EEATVolumeEntrada volumeEntrada = new EEATVolumeEntrada();
				volumeEntrada.setMedidorEntrada(fonte.getMedidorEntrada());
				volumeEntrada.setEeatVolume(this.registro);
				volumeEntrada.setTipomedicao(fonte.getMedidorEntrada().getTipoMedicao());
				this.registro.getVolumeEntrada().add(volumeEntrada);
			}
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar Medidor");
		}
	}
}
