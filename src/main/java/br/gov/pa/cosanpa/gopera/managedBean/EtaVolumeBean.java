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

import br.gov.model.operacao.ETA;
import br.gov.model.operacao.ETAFonteCaptacao;
import br.gov.model.operacao.ETAMedidor;
import br.gov.model.operacao.ETAVolume;
import br.gov.model.operacao.ETAVolumeEntrada;
import br.gov.model.operacao.ETAVolumeSaida;
import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.RegionalProxy;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.servicos.operacao.EtaRepositorio;
import br.gov.servicos.operacao.EtaVolumeRepositorio;
import br.gov.servicos.operacao.UnidadeConsumidoraRepositorio;

@ManagedBean
@SessionScoped
public class EtaVolumeBean extends BaseBean<ETAVolume> {

	@EJB
	private EtaVolumeRepositorio fachada;
	
	@EJB
	private EtaRepositorio fachadaETA;
	
	@EJB
	private UnidadeConsumidoraRepositorio fachadaUC;

	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private List<ETA> listaETA = new ArrayList<ETA>();
	private LazyDataModel<ETAVolume> listaConsumo = null;
	private String volumeAux;
	
	public LazyDataModel<ETAVolume> getListaConsumo() {
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

	public List<ETA> getListaETA() {
		if (this.registro.getLocalidadeProxy().getCodigo() != null) {
			try {
				this.listaETA = fachadaUC.getListaETA(usuarioProxy, 
												     this.registro.getRegionalProxy().getCodigo(), 
												     this.registro.getUnidadeNegocioProxy().getCodigo(), 
												     this.registro.getMunicipioProxy().getCodigo(),
												     this.registro.getLocalidadeProxy().getCodigo());
				return this.listaETA;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar ETA´s.");
			}
		}
		listaETA = new ArrayList<ETA>();
		return listaETA;
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
		this.visualizar();			// Cria uma nova instância do registro para um novo cadastro
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new ETAVolume();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "EtaVolume.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}

	private void iniciarLazy(){
		if (listaConsumo == null) {  
			listaConsumo = new LazyDataModel<ETAVolume>() {
				private static final long serialVersionUID = 1L;

				
				public List<ETAVolume> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
					try {					
						List<ETAVolume> listaLazy = fachada.obterListaLazy(startingAt, maxPerPage, filters);
						setRowCount(fachada.obterQtdRegistros(filters));
						setPageSize(maxPerPage);						
						return listaLazy;						
					} catch (Exception e) {
						e.printStackTrace();
					}	
					return null;
				}
				
				
				public Object getRowKey(ETAVolume consumo) {
					return consumo.getCodigo();
				}
				
				
				public ETAVolume getRowData(String consumoId) {
					if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
						Integer id = Integer.valueOf(consumoId);
						
						for (ETAVolume consumo : listaConsumo) {
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
		this.registro = new ETAVolume();
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
			this.registro = fachada.obterETAVolume(this.registro.getCodigo());
			DecimalFormat df = new DecimalFormat("#,##0.00");
			//Volume
			volumeAux = df.format(registro.getVolume());
			//Medida Saida
			for (ETAVolumeSaida volumeSaida : this.registro.getVolumeSaida()){
				volumeSaida.setValorMedicaoSaidaAux(df.format(volumeSaida.getValorMedicaoSaida()));		
			}	
			//Medida Entrada
			for (ETAVolumeEntrada volumeEntrada : this.registro.getVolumeEntrada()){
				volumeEntrada.setValorMedicaoEntradaAux(df.format(volumeEntrada.getValorMedicaoEntrada()));		
			}
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar Volume de Água");
		}
	}
	
	public void carregarMedidor(){
		try {
			ETA eta = fachadaETA.obterETA(this.registro.getEta().getCodigo());
			this.registro.getVolumeSaida().clear();
			for (ETAMedidor medidor : eta.getMedidorSaida()){
				ETAVolumeSaida volumeSaida = new ETAVolumeSaida();
				volumeSaida.setMedidorSaida(medidor.getMedidorSaida());
				volumeSaida.setEtaVolume(this.registro);
				volumeSaida.setTipomedicao(medidor.getMedidorSaida().getTipoMedicao());
				this.registro.getVolumeSaida().add(volumeSaida);
			}
			for (ETAFonteCaptacao fonte : eta.getFonteCaptacao()){
				ETAVolumeEntrada volumeEntrada = new ETAVolumeEntrada();
				volumeEntrada.setMedidorEntrada(fonte.getMedidorEntrada());
				volumeEntrada.setEtaVolume(this.registro);
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
				if(!fachada.verificaMesReferencia(this.registro.getEta().getCodigo(), this.filtroData(this.registro.getReferencia(), "yyyy-MM-dd")) ){
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
			for (ETAVolumeSaida volumeSaida : this.registro.getVolumeSaida()){
				volumeSaida.setValorMedicaoSaida(Double.parseDouble(volumeSaida.getValorMedicaoSaidaAux().replace(".", "").replace(",", ".")));		
			}
			//Medidor de Entrada
			for (ETAVolumeEntrada volumeEntrada : this.registro.getVolumeEntrada()){
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
