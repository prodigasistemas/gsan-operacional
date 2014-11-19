package br.gov.pa.cosanpa.gopera.managedBean;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.gov.model.operacao.EEAT;
import br.gov.model.operacao.MacroMedidor;
import br.gov.model.operacao.RSO;
import br.gov.servicos.operacao.EeatRepositorio;
import br.gov.servicos.operacao.MacroMedidorRepositorio;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;
import br.gov.servicos.operacao.RsoRepositorio;

@ManagedBean
@SessionScoped
public class RsoBean extends BaseBean<RSO> {

	@EJB
	private RsoRepositorio fachada;
	
	@EJB
	private MacroMedidorRepositorio fachadaMM;
	
	@EJB
	private EeatRepositorio fachadaEEAT;
	
	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;
	private List<MacroMedidor> listaMacroMedidor;	
	private List<EEAT> listaEEAT;
	private String volumeUtil;
	private String alturaUtil;
	private String capacidade;
	private String cmbVazao;
	private String cmbMca;
	
	public RsoBean() {
		
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
		this.registro = new RSO();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "Rso.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}
	
	@Override
	public String novo() {
		this.registro = new RSO();
		volumeUtil = "0,00";
		alturaUtil = "0,00";
		capacidade = "0,00";
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
			//this.registro = fachada.obterETA(this.registro.getCodigo());
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
	
	@Override
	public String cadastrar() {
		//VALIDANDO CMB
		if (!validaCMB()) return null;
		
		return super.cadastrar();
	}
	
	@Override
	public String confirmar() {
		try {
			registro.setVolumeUtil(Double.parseDouble(volumeUtil.replace(".", "").replace(",", ".")));
			registro.setAlturaUtil(Double.parseDouble(alturaUtil.replace(".", "").replace(",", ".")));
			registro.setCapacidade(Double.parseDouble(capacidade.replace(".", "").replace(",", ".")));
			registro.setCmbVazao(Double.parseDouble(cmbVazao.replace(".", "").replace(",", ".")));
			registro.setCmbMca(Double.parseDouble(cmbMca.replace(".", "").replace(",", ".")));
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

	public List<EEAT> getListaEEAT() {
		try {
			listaEEAT = fachadaEEAT.obterLista();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Consultar Lista de EEAT");
		}
		return listaEEAT;
	}
}
