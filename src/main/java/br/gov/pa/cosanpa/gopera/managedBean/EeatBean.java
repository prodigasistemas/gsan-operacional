package br.gov.pa.cosanpa.gopera.managedBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.beanutils.BeanUtils;

import br.gov.model.operacao.EEAT;
import br.gov.model.operacao.EEATFonteCaptacao;
import br.gov.model.operacao.EEATMedidor;
import br.gov.model.operacao.MacroMedidor;
import br.gov.servicos.operacao.EeatRepositorio;
import br.gov.servicos.operacao.MacroMedidorRepositorio;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;

@ManagedBean
@SessionScoped
public class EeatBean extends BaseBean<EEAT> {

	@EJB
	private EeatRepositorio fachada;
	
	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;
	
	@EJB
	private MacroMedidorRepositorio fachadaMM;
	
	private List<MacroMedidor> listaMacroMedidor;	
	private Integer tipoFonte = 1;
	private EEATFonteCaptacao fonteCaptacao;
	private List<EEATFonteCaptacao> listaFonte = new ArrayList<EEATFonteCaptacao>();
	private MacroMedidor medidorEntrada = new MacroMedidor();
	private Date medidorEntradaDataInstalacao;
	private String medidorEntradaTag;
	private List<EEATMedidor> listaMedidorSaida = new ArrayList<EEATMedidor>();
	private EEATMedidor EEATMedidor;
	private MacroMedidor medidorSaida = new MacroMedidor();
	private Date medidorSaidaDataInstalacao;
	private String medidorSaidaTag;
	private String volumeUtil;
	private String alturaUtil;
	private String capacidade;
	private String cmbVazao;
	private String cmbMca;	

	public EeatBean() {
		
	}
	
	public Integer getTipoFonte() {
		return tipoFonte;
	}

	public void setTipoFonte(Integer tipoFonte) {
		this.tipoFonte = tipoFonte;
	}

	public EEATFonteCaptacao getFonteCaptacao() {
		return fonteCaptacao;
	}

	public void setFonteCaptacao(EEATFonteCaptacao fonteCaptacao) {
		this.fonteCaptacao = fonteCaptacao;
	}

	public List<EEATFonteCaptacao> getListaFonte() {

		if ((this.tipoFonte != null)) {
			try {
				this.listaFonte =  fachadaProxy.getListaFonteCaptacaoEEAT(this.tipoFonte);
				return listaFonte;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar fontes de captação.");
			}
		}
		return listaFonte = new ArrayList<EEATFonteCaptacao>();
	}

	public void setListaFonte(List<EEATFonteCaptacao> listaFonte) {
		this.listaFonte = listaFonte;
	}

	public MacroMedidor getMedidorEntrada() {
		return medidorEntrada;
	}

	public void setMedidorEntrada(MacroMedidor medidorEntrada) {
		this.medidorEntrada = medidorEntrada;
	}

	public String getMedidorEntradaTag() {
		return medidorEntradaTag;
	}

	public void setMedidorEntradaTag(String medidorEntradaTag) {
		this.medidorEntradaTag = medidorEntradaTag;
	}

	public List<EEATMedidor> getListaMedidorSaida() {
		return listaMedidorSaida;
	}

	public void setListaMedidorSaida(List<EEATMedidor> listaMedidorSaida) {
		this.listaMedidorSaida = listaMedidorSaida;
	}

	public EEATMedidor getEEATMedidor() {
		return EEATMedidor;
	}

	public void setEEATMedidor(EEATMedidor eEATMedidor) {
		EEATMedidor = eEATMedidor;
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

	public Date getMedidorEntradaDataInstalacao() {
		return medidorEntradaDataInstalacao;
	}

	public void setMedidorEntradaDataInstalacao(Date medidorEntradaDataInstalacao) {
		this.medidorEntradaDataInstalacao = medidorEntradaDataInstalacao;
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
	
	
	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new EEAT();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "Eeat.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}
	
	
	public String novo() {
		this.registro = new EEAT();
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
	
	
	public String consultar() {
		carregar();
		return super.consultar();
	}

	
	public String alterar() {
		carregar();
		return super.alterar();
	}
	
	public void carregar(){
		try {
			this.registro = fachada.obterEEAT(this.registro.getCodigo());
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
			EEATMedidor EEATMedidor = new EEATMedidor();
			medidorSaida = fachadaMM.obterMacroMedidor(medidorSaida.getCodigo());
			MacroMedidor medidor = new MacroMedidor();
			BeanUtils.copyProperties(medidor, medidorSaida);			
			EEATMedidor.setMedidorSaida(medidor);
			EEATMedidor.setDataInstalacao(medidorSaidaDataInstalacao);
			EEATMedidor.setTag(medidorSaidaTag);
			EEATMedidor.setEeat(this.registro);
			this.registro.getMedidorSaida().add(EEATMedidor);
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar");
		}
	}	
	
	public void excluirMedidorSaida() {
		for (EEATMedidor medidor : this.registro.getMedidorSaida()) {
			if (medidor.getMedidorSaida().getCodigo().equals(EEATMedidor.getMedidorSaida().getCodigo())) {
				this.registro.getMedidorSaida().remove(medidor);
				break;
			}
		}
	}
	
	public void incluirFonte() {
		try {
			EEATFonteCaptacao EEATFonte = new EEATFonteCaptacao();
			EEATFonte.setTipoFonte(tipoFonte);
			EEATFonte.setCodigoFonte(fonteCaptacao.getCodigoFonte());
			EEATFonte.setNomeFonte(fonteCaptacao.getNomeFonte());
			medidorEntrada = fachadaMM.obterMacroMedidor(medidorEntrada.getCodigo());
			EEATFonte.setMedidorEntrada(medidorEntrada);
			EEATFonte.setDataInstalacao(medidorEntradaDataInstalacao);
			EEATFonte.setTag(medidorEntradaTag);
			EEATFonte.setEeat(this.registro);			

			this.registro.getFonteCaptacao().add(EEATFonte);			
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar");
		}
	}	

	public void excluirFonte() {
		for (EEATFonteCaptacao fonte : this.registro.getFonteCaptacao()) {
			if (fonte.getCodigoFonte().equals(fonteCaptacao.getCodigoFonte())) {
				this.registro.getFonteCaptacao().remove(fonte);	
				break;
			}
		}
	}
	
	
	public String cadastrar() {
		registro.setVolumeUtil(Double.parseDouble(volumeUtil.replace(".", "").replace(",", ".")));
		registro.setAlturaUtil(Double.parseDouble(alturaUtil.replace(".", "").replace(",", ".")));
		registro.setCapacidade(Double.parseDouble(capacidade.replace(".", "").replace(",", ".")));
		registro.setCmbVazao(Double.parseDouble(cmbVazao.replace(".", "").replace(",", ".")));
		registro.setCmbMca(Double.parseDouble(cmbMca.replace(".", "").replace(",", ".")));
		//VALIDANDO CMB
		if (!validaCMB()) return null;
		
		return super.cadastrar();
	}
	
	
	public String confirmar() {
		try {
			registro.setUsuario(usuarioProxy.getCodigo());
			registro.setUltimaAlteracao(new Date());
			return super.confirmar();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar");
			e.printStackTrace();
		}
		return null;
	}
	
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
			listaMacroMedidor = fachadaMM.obterLista();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Consultar Lista de Macro Medidores");
		}
		return listaMacroMedidor;
	}
	
	public void setListaMacroMedidor(List<MacroMedidor> listaMacroMedidor) {
		this.listaMacroMedidor = listaMacroMedidor;
	}

}
