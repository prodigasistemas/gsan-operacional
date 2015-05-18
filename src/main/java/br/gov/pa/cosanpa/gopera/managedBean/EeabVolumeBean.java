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

import br.gov.model.operacao.EEAB;
import br.gov.model.operacao.EEABFonteCaptacao;
import br.gov.model.operacao.EEABMedidor;
import br.gov.model.operacao.EEABVolume;
import br.gov.model.operacao.EEABVolumeEntrada;
import br.gov.model.operacao.EEABVolumeSaida;
import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.RegionalProxy;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.servicos.operacao.EeabRepositorio;
import br.gov.servicos.operacao.EeabVolumeRepositorio;
import br.gov.servicos.operacao.UnidadeConsumidoraRepositorio;
import br.gov.servicos.operacao.to.EEABVolumeTO;

@ManagedBean
@SessionScoped
public class EeabVolumeBean extends BaseBean<EEABVolume> {

	@EJB
	private EeabVolumeRepositorio fachada;
	
	@EJB
	private EeabRepositorio fachadaEEAB;
	
	@EJB
	private UnidadeConsumidoraRepositorio fachadaUC;
	
	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private List<EEAB> listaEEAB = new ArrayList<EEAB>();
	private LazyDataModel<EEABVolume> listaConsumo = null;
	private String volumeAux;
	
	public LazyDataModel<EEABVolume> getListaConsumo() {
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

	public List<EEAB> getListaEEAB() {
		if (this.registro.getLocalidadeProxy().getCodigo() != null) {
			try {
				this.listaEEAB = fachadaUC.getListaEAB(usuarioProxy, 
												     this.registro.getRegionalProxy().getCodigo(), 
												     this.registro.getUnidadeNegocioProxy().getCodigo(), 
												     this.registro.getMunicipioProxy().getCodigo(),
												     this.registro.getLocalidadeProxy().getCodigo());
				return this.listaEEAB;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar ETA´s.");
			}
		}
		listaEEAB = new ArrayList<EEAB>();
		return listaEEAB;
	}
	
	public String getVolumeAux() {
		return volumeAux;
	}

	public void setVolumeAux(String volumeAux) {
		this.volumeAux = volumeAux;
	}

	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		iniciarLazy();
		this.visualizar();	
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new EEABVolume();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "EeabVolume.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}
	
	private void iniciarLazy(){
		if (listaConsumo == null) {  
			listaConsumo = new LazyDataModel<EEABVolume>() {
				private static final long serialVersionUID = 1L;

				
				public List<EEABVolume> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
					try {					
						List<EEABVolume> listaLazy = fachada.obterListaLazy(filters,maxPerPage,startingAt);
						setRowCount(fachada.obterQtdRegistros(filters));
						setPageSize(maxPerPage);						
						return listaLazy;					
					} catch (Exception e) {
						e.printStackTrace();
					}	
					return null;
				}
				
				public Object getRowKey(EEABVolume consumo) {
					return consumo.getCodigo();
				}
				
				
				public EEABVolume getRowData(String consumoId) {
					if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
						Integer id = Integer.valueOf(consumoId);
						
						for (EEABVolume consumo : listaConsumo) {
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

	
	public String novo() {
		this.registro = new EEABVolume();
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
			this.registro = fachada.obterEEABVolume(this.registro.getCodigo());
			DecimalFormat df = new DecimalFormat("#,##0.00");
			//Volume
			volumeAux = df.format(registro.getVolume());
			//Medida Saida
			for (EEABVolumeSaida volumeSaida : this.registro.getVolumeSaida()){
				volumeSaida.setValorMedicaoSaidaAux(df.format(volumeSaida.getValorMedicaoSaida()));		
			}
			//Medidor Entrada
			for (EEABVolumeEntrada volumeEntrada : this.registro.getVolumeEntrada()){
				volumeEntrada.setValorMedicaoEntradaAux(df.format(volumeEntrada.getValorMedicaoEntrada()));		
			}
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar Volume de Água");
		}
	}

	public void carregarMedidor(){
		try {
			EEAB eeab = fachadaEEAB.obterEEAB(this.registro.getEeab().getCodigo());
			this.registro.getVolumeSaida().clear();
			for (EEABMedidor medidor : eeab.getMedidorSaida()){
				EEABVolumeSaida volumeSaida = new EEABVolumeSaida();
				volumeSaida.setMedidorSaida(medidor.getMedidorSaida());
				volumeSaida.setEeabVolume(this.registro);
				volumeSaida.setTipomedicao(medidor.getMedidorSaida().getTipoMedicao());
				this.registro.getVolumeSaida().add(volumeSaida);
			}
			this.registro.getVolumeEntrada().clear();
			for (EEABFonteCaptacao fonte : eeab.getFonteCaptacao()){
				EEABVolumeEntrada volumeEntrada = new EEABVolumeEntrada();
				volumeEntrada.setMedidorEntrada(fonte.getMedidorEntrada());
				volumeEntrada.setEeabVolume(this.registro);
				volumeEntrada.setTipomedicao(fonte.getMedidorEntrada().getTipoMedicao());
				this.registro.getVolumeEntrada().add(volumeEntrada);
			}
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar Medidor");
		}
	}
	
	
	public String cadastrar() {
		try {
			//Verifica se não está cadastrado para o mes de referencia corrente
			if (!this.getEditando()){
				if(!fachada.verificaMesReferencia(this.registro.getEeab().getCodigo(), this.filtroData(this.registro.getReferencia(), "yyyy-MM-dd")) ){
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
			registro.setVolume(Double.parseDouble(this.volumeAux.replace(".", "").replace(",", ".")));
			//Medidor de Saída
			for (EEABVolumeSaida volumeSaida : this.registro.getVolumeSaida()){
				volumeSaida.setValorMedicaoSaida(Double.parseDouble(volumeSaida.getValorMedicaoSaidaAux().replace(".", "").replace(",", ".")));		
			}
			//Medidor de Entrada
			for (EEABVolumeEntrada volumeEntrada : this.registro.getVolumeEntrada()){
				volumeEntrada.setValorMedicaoEntrada(Double.parseDouble(volumeEntrada.getValorMedicaoEntradaAux().replace(".", "").replace(",", ".")));		
			}
			registro.setUsuario(usuarioProxy.getCodigo());
			registro.setUltimaAlteracao(new Date());			
			return super.confirmarLazy();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
