package br.gov.pa.cosanpa.gopera.managedBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.beanutils.BeanUtils;

import br.gov.pa.cosanpa.gopera.fachada.IEEAB;
import br.gov.pa.cosanpa.gopera.fachada.IMacroMedidor;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.model.EEAB;
import br.gov.pa.cosanpa.gopera.model.EEABFonteCaptacao;
import br.gov.pa.cosanpa.gopera.model.EEABMedidor;
import br.gov.pa.cosanpa.gopera.model.MacroMedidor;

@ManagedBean
@SessionScoped
public class EeabBean extends BaseBean<EEAB> {

	@EJB
	private IEEAB fachada;
	@EJB
	private IProxy fachadaProxy;	
	@EJB
	private IMacroMedidor fachadaMM;
	private List<MacroMedidor> listaMacroMedidor;	
	private Integer tipoFonte = 1;
	private EEABFonteCaptacao fonteCaptacao;
	private List<EEABFonteCaptacao> listaFonte = new ArrayList<EEABFonteCaptacao>();
	private MacroMedidor medidorEntrada = new MacroMedidor();
	private Date medidorEntradaDataInstalacao;
	private String medidorEntradaTag;
	private List<EEABMedidor> listaMedidorSaida = new ArrayList<EEABMedidor>();
	private EEABMedidor EEABMedidor;
	private MacroMedidor medidorSaida = new MacroMedidor();
	private Date medidorSaidaDataInstalacao;
	private String medidorSaidaTag;
	private String volumeUtil;
	private String alturaUtil;
	private String capacidade;
	private String cmbVazao;
	private String cmbMca;
	
	public EeabBean() {
		
	}

	public Integer getTipoFonte() {
		return tipoFonte;
	}

	public void setTipoFonte(Integer tipoFonte) {
		this.tipoFonte = tipoFonte;
	}

	public EEABFonteCaptacao getFonteCaptacao() {
		return fonteCaptacao;
	}

	public void setFonteCaptacao(EEABFonteCaptacao fonteCaptacao) {
		this.fonteCaptacao = fonteCaptacao;
	}

	public List<EEABFonteCaptacao> getListaFonte() {

		if ((this.tipoFonte != null)) {
			try {
				this.listaFonte =  fachadaProxy.getListaFonteCaptacaoEEAB(this.tipoFonte);
				return listaFonte;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar fontes de captação.");
			}
		}
		return listaFonte = new ArrayList<EEABFonteCaptacao>();
	}

	public void setListaFonte(List<EEABFonteCaptacao> listaFonte) {
		this.listaFonte = listaFonte;
	}

	public MacroMedidor getMedidorEntrada() {
		return medidorEntrada;
	}

	public void setMedidorEntrada(MacroMedidor medidorEntrada) {
		this.medidorEntrada = medidorEntrada;
	}
	
	public Date getMedidorEntradaDataInstalacao() {
		return medidorEntradaDataInstalacao;
	}

	public void setMedidorEntradaDataInstalacao(Date medidorEntradaDataInstalacao) {
		this.medidorEntradaDataInstalacao = medidorEntradaDataInstalacao;
	}
	
	public String getMedidorEntradaTag() {
		return medidorEntradaTag;
	}

	public void setMedidorEntradaTag(String medidorEntradaTag) {
		this.medidorEntradaTag = medidorEntradaTag;
	}

	public List<EEABMedidor> getListaMedidorSaida() {
		return listaMedidorSaida;
	}

	public void setListaMedidorSaida(List<EEABMedidor> listaMedidorSaida) {
		this.listaMedidorSaida = listaMedidorSaida;
	}

	public EEABMedidor getEEABMedidor() {
		return EEABMedidor;
	}

	public void setEEABMedidor(EEABMedidor eEABMedidor) {
		EEABMedidor = eEABMedidor;
	}

	public MacroMedidor getMedidorSaida() {
		return medidorSaida;
	}

	public void setMedidorSaida(MacroMedidor medidorSaida) {
		this.medidorSaida = medidorSaida;
	}

	public Date getMedidorSaidaDataInstalacao() {
		return medidorSaidaDataInstalacao;
	}

	public void setMedidorSaidaDataInstalacao(Date medidorSaidaDataInstalacao) {
		this.medidorSaidaDataInstalacao = medidorSaidaDataInstalacao;
	}

	public String getMedidorSaidaTag() {
		return medidorSaidaTag;
	}

	public void setMedidorSaidaTag(String medidorSaidaTag) {
		this.medidorSaidaTag = medidorSaidaTag;
	}

	public String getVolumeUtil() {
		return volumeUtil;
	}

	public void setVolumeUtil(String volumeUtil) {
		this.volumeUtil = volumeUtil;
	}

	public String getAlturaUtil() {
		return alturaUtil;
	}

	public void setAlturaUtil(String alturaUtil) {
		this.alturaUtil = alturaUtil;
	}

	public String getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(String capacidade) {
		this.capacidade = capacidade;
	}

	public String getCmbVazao() {
		return cmbVazao;
	}

	public void setCmbVazao(String cmbVazao) {
		this.cmbVazao = cmbVazao;
	}

	public String getCmbMca() {
		return cmbMca;
	}

	public void setCmbMca(String cmbMca) {
		this.cmbMca = cmbMca;
	}

	@Override
	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new EEAB();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "Eeab.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}

	@Override
	public String novo() {
		this.registro = new EEAB();
		medidorEntrada = new MacroMedidor();
		medidorSaida = new MacroMedidor();
		medidorEntradaDataInstalacao = null;
		medidorEntradaTag = "";
		medidorSaidaDataInstalacao = null;
		medidorSaidaTag = "";
		volumeUtil = "";
		alturaUtil = "";
		capacidade = "";
		cmbVazao = "";
		cmbMca = "";		
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
			this.registro = fachada.obterEEAB(this.registro.getCodigo());
			DecimalFormat df = new DecimalFormat("#,##0.00");
			if (this.registro.getVolumeUtil() != null)this.setVolumeUtil(df.format(this.registro.getVolumeUtil()));
			if (this.registro.getAlturaUtil() != null) this.setAlturaUtil(df.format(this.registro.getAlturaUtil()));
			if (this.registro.getCapacidade() != null) this.setCapacidade(df.format(this.registro.getCapacidade()));
			if (this.registro.getCmbVazao() != null) this.setCmbVazao(df.format(this.registro.getCmbVazao()));			
			if (this.registro.getCmbMca() != null) this.setCmbMca(df.format(this.registro.getCmbMca()));
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar");
		}
	}
	
	public void incluirMedidorSaida() {
		try {
			EEABMedidor EEABMedidor = new EEABMedidor();
			medidorSaida = fachadaMM.obterMacroMedidor(medidorSaida.getCodigo());
			MacroMedidor medidor = new MacroMedidor();
			BeanUtils.copyProperties(medidor, medidorSaida);
			EEABMedidor.setMedidorSaida(medidor);
			EEABMedidor.setDataInstalacao(medidorSaidaDataInstalacao);
			EEABMedidor.setTag(medidorSaidaTag);
			EEABMedidor.setEeab(this.registro);
			
			Integer eeab_id = this.registro.getCodigo();
			String verifica;
			
			if(eeab_id != null) {
				verifica = fachadaProxy.getVerificaMacroMedidorCadastrado(EEABMedidor.getMedidorSaida().getCodigo(), eeab_id, 5);
			} else {
				verifica = fachadaProxy.getVerificaMacroMedidorCadastrado(EEABMedidor.getMedidorSaida().getCodigo(), 0, 0);
			}
			
			if(verifica != "") {
				this.mostrarMensagemErro(verifica);
				return;
			}
			this.registro.getMedidorSaida().add(EEABMedidor);
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar");
		}
	}	
	
	public void excluirMedidorSaida() {
		for (EEABMedidor medidor : this.registro.getMedidorSaida()) {
			if (medidor.getMedidorSaida().getCodigo().equals(EEABMedidor.getMedidorSaida().getCodigo())) {
				this.registro.getMedidorSaida().remove(medidor);
				break;
			}
		}
	}
	
	public void incluirFonte() {
		try {
			EEABFonteCaptacao EEABFonte = new EEABFonteCaptacao();
			EEABFonte.setTipoFonte(tipoFonte);
			EEABFonte.setCodigoFonte(fonteCaptacao.getCodigoFonte());
			EEABFonte.setNomeFonte(fonteCaptacao.getNomeFonte());
			medidorEntrada = fachadaMM.obterMacroMedidor(medidorEntrada.getCodigo());
			EEABFonte.setMedidorEntrada(medidorEntrada);
			EEABFonte.setDataInstalacao(medidorEntradaDataInstalacao);
			EEABFonte.setTag(medidorEntradaTag);
			EEABFonte.setEeab(this.registro);
			
			Integer eeab_id = this.registro.getCodigo();
			String verifica;
			
			if(eeab_id != null) {
				verifica = fachadaProxy.getVerificaMacroMedidorCadastrado(EEABFonte.getMedidorEntrada().getCodigo(), eeab_id, 5);
			} else {
				verifica = fachadaProxy.getVerificaMacroMedidorCadastrado(EEABFonte.getMedidorEntrada().getCodigo(), 0, 0);
			}
			
			if(verifica != "") {
				this.mostrarMensagemErro(verifica);
				return;
			}
			this.registro.getFonteCaptacao().add(EEABFonte);
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar");
		}
	}	

	public void excluirFonte() {
		for (EEABFonteCaptacao fonte : this.registro.getFonteCaptacao()) {
			if (fonte.getCodigoFonte().equals(fonteCaptacao.getCodigoFonte())) {
				this.registro.getFonteCaptacao().remove(fonte);
				break;
			}
		}
	}

	@Override
	public String cadastrar() {
		registro.setVolumeUtil(Double.parseDouble(volumeUtil.replace(".", "").replace(",", ".")));
		registro.setAlturaUtil(Double.parseDouble(alturaUtil.replace(".", "").replace(",", ".")));
		registro.setCapacidade(Double.parseDouble(capacidade.replace(".", "").replace(",", ".")));
		registro.setCmbVazao(Double.parseDouble(cmbVazao.replace(".", "").replace(",", ".")));
		registro.setCmbMca(Double.parseDouble(cmbMca.replace(".", "").replace(",", ".")));
		//VALIDANDO MACRO MEDIDOR
		//if (!validaMedidor()) return null;
		//VALIDANDO CMB
		if (!validaCMB()) return null;
		
		return super.cadastrar();
	}
	
	@Override
	public String confirmar() {
		try {
			registro.setUsuario(usuarioProxy);
			registro.setUltimaAlteracao(new Date());
			return super.confirmar();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar");
		}
		return null;
	}

	/*
	private boolean validaMedidor(){
		Integer eeab_id = this.registro.getCodigo();
		String verifica;
		try {		
			if(eeab_id != null) {
				verifica = fachadaProxy.getVerificaMacroMedidorCadastrado(this.registro.getMedidorSaida().getCodigo(), eeab_id, 1);
			} else {
				verifica = fachadaProxy.getVerificaMacroMedidorCadastrado(this.registro.getMedidorSaida().getCodigo(), 0, 0);
			}
			
			if(verifica != "") {
				this.mostrarMensagemErro(verifica);
				return false;
			}
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Validar Medidor");
			return false;
		}
		return true;
	}
	*/
	
	private boolean validaCMB(){
		boolean blnErro = false; 
		if (this.registro.getCmbQuantidade() == 0){
			this.mostrarMensagemErro("Quantidade de CMB´s, deve ser informado.");
			return false;
		}
		else {
			if (this.registro.getCmbModelo().length() == 0) {
				this.mostrarMensagemErro("Modelo de CMB´s, deve ser informado.");
				blnErro = true;
			}
			if (this.registro.getCmbVazao() == 0) {
				this.mostrarMensagemErro("Vazão Nominal de CMB´s, deve ser informado.");
				blnErro = true;
			}
			if (this.registro.getCmbPotencia() == 0) {
				this.mostrarMensagemErro("Potência do Motor de CMB´s, deve ser informado.");
				blnErro = true;
			}
			if (this.registro.getCmbMca() == 0) {
				this.mostrarMensagemErro("Metro por Coluna de Água de CMB´s, deve ser informado.");
				blnErro = true;
			}
			
			if (blnErro) return false;
		}
		return true;
	}
	
	public List<MacroMedidor> getListaMacroMedidor() {
		try {
			listaMacroMedidor = fachadaMM.obterLista(0, 0);
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Consultar Lista de Macro Medidores");
		}
		return listaMacroMedidor;
	}
	
	public void setListaMacroMedidor(List<MacroMedidor> listaMacroMedidor) {
		this.listaMacroMedidor = listaMacroMedidor;
	}
}
