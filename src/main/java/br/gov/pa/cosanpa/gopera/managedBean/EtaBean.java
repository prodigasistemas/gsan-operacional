package br.gov.pa.cosanpa.gopera.managedBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.beanutils.BeanUtils;

import br.gov.model.operacao.EEAB;
import br.gov.model.operacao.ETA;
import br.gov.model.operacao.ETAFonteCaptacao;
import br.gov.model.operacao.ETAMedidor;
import br.gov.model.operacao.MacroMedidor;
import br.gov.servicos.operacao.EeabRepositorio;
import br.gov.servicos.operacao.EtaRepositorio;
import br.gov.servicos.operacao.MacroMedidorRepositorio;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;

@ManagedBean
@SessionScoped
public class EtaBean extends BaseBean<ETA> {

	@EJB
	private EtaRepositorio fachada;
	
	@EJB
	private MacroMedidorRepositorio fachadaMM;
	
	@EJB
	private EeabRepositorio fachadaEEAB;
	
	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;

	private EEAB eeab = new EEAB();
	private List<EEAB> listaEEAB = new ArrayList<EEAB>();
	private ETAFonteCaptacao fonteCaptacao;
	private MacroMedidor medidorEntrada = new MacroMedidor();
	private Date medidorEntradaDataInstalacao;
	private String medidorEntradaTag;
	private List<ETAMedidor> listaMedidorSaida = new ArrayList<ETAMedidor>();
	private ETAMedidor ETAMedidor;
	private MacroMedidor medidorSaida = new MacroMedidor();
	private Date medidorSaidaDataInstalacao;
	private String medidorSaidaTag;
	private String volumeUtil;
	private String alturaUtil;
	private String capacidade;
	private String cmbVazao;
	private String cmbMca;
	private List<MacroMedidor> listaMacroMedidor;
	
	public EtaBean() {
		
	}

	public EEAB getEeab() {
		return eeab;
	}

	public void setEeab(EEAB eeab) {
		this.eeab = eeab;
	}

	public List<EEAB> getListaEEAB() {
		try{
			listaEEAB = fachadaEEAB.obterLista();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao carregar lista de EEAB");
		}
		return listaEEAB;
	}

	public void setListaEEAB(List<EEAB> listaEEAB) {
		this.listaEEAB = listaEEAB;
	}

	public MacroMedidor getMedidorEntrada() {
		return medidorEntrada;
	}

	public void setMedidorEntrada(MacroMedidor medidorEntrada) {
		this.medidorEntrada = medidorEntrada;
	}

	public ETAFonteCaptacao getFonteCaptacao() {
		return fonteCaptacao;
	}

	public void setFonteCaptacao(ETAFonteCaptacao fonteCaptacao) {
		this.fonteCaptacao = fonteCaptacao;
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

	public List<ETAMedidor> getListaMedidorSaida() {
		return listaMedidorSaida;
	}

	public void setListaMedidorSaida(List<ETAMedidor> listaMedidorSaida) {
		this.listaMedidorSaida = listaMedidorSaida;
	}

	public ETAMedidor getETAMedidor() {
		return ETAMedidor;
	}

	public void setETAMedidor(ETAMedidor eTAMedidor) {
		ETAMedidor = eTAMedidor;
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
	
	
	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new ETA();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "Eta.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}
	
	
	public String novo() {
		this.registro = new ETA();
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
			this.registro = fachada.obterETA(this.registro.getCodigo());
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
			ETAMedidor ETAMedidor = new ETAMedidor();
			medidorSaida = fachadaMM.obterMacroMedidor(medidorSaida.getCodigo());
			MacroMedidor medidor = new MacroMedidor();
			BeanUtils.copyProperties(medidor, medidorSaida);
			ETAMedidor.setMedidorSaida(medidor);
			ETAMedidor.setDataInstalacao(medidorSaidaDataInstalacao);
			ETAMedidor.setTag(medidorSaidaTag);
			ETAMedidor.setEta(this.registro);
			this.registro.getMedidorSaida().add(ETAMedidor);
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar");
		}
	}	
	
	public void excluirMedidorSaida() {
		for (ETAMedidor medidor : this.registro.getMedidorSaida()) {
			if (medidor.getMedidorSaida().getCodigo().equals(ETAMedidor.getMedidorSaida().getCodigo())) {
				this.registro.getMedidorSaida().remove(medidor);
				break;
			}
		}
	}
	
	public void incluirFonte() {
		try{
			ETAFonteCaptacao ETAFonte = new ETAFonteCaptacao();
			eeab = fachadaEEAB.obterEEAB(eeab.getCodigo());
			EEAB eab = new EEAB();
			BeanUtils.copyProperties(eab, eeab);
			ETAFonte.setEeab(eab);
			medidorEntrada = fachadaMM.obterMacroMedidor(medidorEntrada.getCodigo());
			ETAFonte.setMedidorEntrada(medidorEntrada);
			ETAFonte.setDataInstalacao(medidorEntradaDataInstalacao);
			ETAFonte.setTag(medidorEntradaTag);
			ETAFonte.setEta(this.registro);
			
			this.registro.getFonteCaptacao().add(ETAFonte);
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Incluir Fonte");
		}
	}	

	public void excluirFonte() {
		for (ETAFonteCaptacao fonte : this.registro.getFonteCaptacao()) {
			if (fonte.getEeab().getCodigo().equals(fonteCaptacao.getEeab().getCodigo())) {
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
		}
		return null;
	}
	
	private boolean validaCMB(){
		boolean blnErro = false; 
		if (this.registro.getCmbQuantidade() == 0){
			//this.mostrarMensagemErro("Quantidade de CMB´s, deve ser informado.");
			return true;
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
