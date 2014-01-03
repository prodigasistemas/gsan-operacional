package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.fachada.IRegistroConsumo;
import br.gov.pa.cosanpa.gopera.fachada.IRegistroConsumoSistemaAbastecimento;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.RegistroConsumo;
import br.gov.pa.cosanpa.gopera.model.RegistroConsumoSistemaAbastecimento;
import br.gov.pa.cosanpa.gopera.model.SistemaAbastecimentoProxy;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@ManagedBean
@SessionScoped
public class RegistroConsumoSistemaAbastecimentoBean extends BaseBean<RegistroConsumoSistemaAbastecimento> {

//	protected List<RegistroConsumoSistemaAbastecimento> lista = new ArrayList<RegistroConsumoSistemaAbastecimento>();

	@EJB
	private IRegistroConsumoSistemaAbastecimento fachada;

	@EJB
	private IRegistroConsumo fachadaRegistroConsumo;

	@EJB
	private IProxy fachadaProxy;

	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private List<SistemaAbastecimentoProxy> sistemaAbastecimentos = new ArrayList<SistemaAbastecimentoProxy>();
	private List<RegistroConsumo> registrosConsumo = new ArrayList<RegistroConsumo>();
	private List<String> registrosConsumoSelecionados = new ArrayList<String>();
	
	public RegistroConsumoSistemaAbastecimentoBean() {
		limparListaRegistroConsumo();
	}

	private void limparListaRegistroConsumo() {
		registrosConsumoSelecionados.clear();
		registrosConsumo.clear();
	}

	public List<SelectItem> getListaRegistroConsumo() throws Exception {
		List<SelectItem> listaCheck = new ArrayList<SelectItem>();

		if (registrosConsumo.isEmpty()) {
			this.registrosConsumo = this.fachadaRegistroConsumo.obterLista(0, 0);
		}
		SelectItem selectItem;
		for (RegistroConsumo registroConsumo : this.registrosConsumo) {
			selectItem = new SelectItem(registroConsumo.getCodigo(), registroConsumo.getDescricao(), "", false, false, false);
			listaCheck.add(selectItem);
		}
		return listaCheck;
	}
	
	public List<SistemaAbastecimentoProxy> getSistemaAbastecimentos() {
		if (this.registro.getLocalidadeProxy().getCodigo() != null) {
			try {
				this.sistemaAbastecimentos =  fachadaProxy.getListaSistemaAbastecimento(this.registro.getRegionalProxy().getCodigo(), this.registro.getUnidadeNegocioProxy().getCodigo(),this.registro.getMunicipioProxy().getCodigo(), this.registro.getLocalidadeProxy().getCodigo());
//				this.sistemaAbastecimentos.add(0, new SistemaAbastecimentoProxy(-1, "Selecione"));
				return this.sistemaAbastecimentos;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.sistemaAbastecimentos = new ArrayList<SistemaAbastecimentoProxy>();
		return this.sistemaAbastecimentos;
	}

	public void setSistemaAbastecimentos(List<SistemaAbastecimentoProxy> sistemaAbastecimentos) {
		this.sistemaAbastecimentos = sistemaAbastecimentos;
	}

	public List<RegionalProxy> getRegionais() {
		try {
			regionais = fachadaProxy.getListaRegional();
//			regionais.add(0, new RegionalProxy(-1, "Selecione"));
			return regionais; 
		} catch (Exception e) {
			mostrarMensagemErro("Erro ao consultar sistema externo.");
		}
		return regionais;
	}
	
	public boolean getHabilitarUnidadeNegocio() {
		return (unidadesNegocio.isEmpty() || (unidadesNegocio.size() == 0));
	}
	
	public boolean getHabilitarSistemaAbastecimento() {
		return (sistemaAbastecimentos.isEmpty() || (sistemaAbastecimentos.size() == 0));
	}
	
	public boolean getHabilitarMunicipios() {
		return (municipios.isEmpty() || (municipios.size() == 0));
	}
	
	public boolean getHabilitarLocalidades() {
		return (localidades.isEmpty() || (localidades.size() == 0));
	}

	public void setRegionais(List<RegionalProxy> regionais) {
		this.regionais = regionais;
	}

	public List<UnidadeNegocioProxy> getUnidadesNegocio() {
		if ((this.registro.getRegionalProxy().getCodigo() != null)) {
			try {
				this.unidadesNegocio =  fachadaProxy.getListaUnidadeNegocio(this.registro.getRegionalProxy().getCodigo());
//				this.unidadesNegocio.add(0, new UnidadeNegocioProxy(-1, "Selecione"));
				return unidadesNegocio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		return unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	}

	public void setUnidadesNegocio(List<UnidadeNegocioProxy> unidadesNegocio) {
		this.unidadesNegocio = unidadesNegocio;
	}

	public List<MunicipioProxy> getMunicipios() {
		if (this.registro.getUnidadeNegocioProxy().getCodigo() != null) {
			try {
				this.municipios =  fachadaProxy.getListaMunicipio(this.registro.getRegionalProxy().getCodigo(), this.registro.getUnidadeNegocioProxy().getCodigo());
//				this.municipios.add(0, new MunicipioProxy(-1, "Selecione"));
				return this.municipios;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.municipios = new ArrayList<MunicipioProxy>();
		return this.municipios;
	}

	public void setMunicipios(List<MunicipioProxy> municipios) {
		this.municipios = municipios;
	}

	public List<LocalidadeProxy> getLocalidades() {
		if (this.registro.getMunicipioProxy().getCodigo() != null) {
			try {
				this.localidades =  fachadaProxy.getListaLocalidade(this.registro.getRegionalProxy().getCodigo(), this.registro.getUnidadeNegocioProxy().getCodigo(), this.registro.getMunicipioProxy().getCodigo());
//				this.localidades.add(0, new LocalidadeProxy(-1, "Selecione"));
				return this.localidades;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.localidades = new ArrayList<LocalidadeProxy>();
		return this.localidades;
	}

	public void setLocalidades(List<LocalidadeProxy> localidades) {
		this.localidades = localidades;
	}

	public RegistroConsumoSistemaAbastecimento getRegistro() {
		return registro;
	}

	public void setRegistro(RegistroConsumoSistemaAbastecimento registro) {
		this.registro = registro;
	}

	public void consultarRegistroConsumo() {
		try {
			this.registro = fachada.obterRegistroConsumo(this.registro.getCodigo());
			this.registrosConsumoSelecionados.clear();
			for (RegistroConsumo consumo : this.registro.getRegistrosConsumo()) {
				registrosConsumoSelecionados.add(consumo.getCodigo().toString());
			}
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao carregar Registro de Consumo");
			e.printStackTrace();
		}			
	}

	@Override
	public String novo() {
		this.registrosConsumoSelecionados.clear();
		this.registro = new RegistroConsumoSistemaAbastecimento();
		return super.novo();
	}
	
	@Override	
	public String consultar() {
		consultarRegistroConsumo();
		return super.consultar();
	}

	@Override
	public String alterar() {
		consultarRegistroConsumo();
		return super.alterar();
	}

	public String confirmar() {
		try {
			registro.setUsuario(usuarioProxy);
			registro.setUltimaAlteracao(new Date());
			adicionarRegistroConsumoSelecionados();
			return super.confirmar();
		} catch (Exception e) {
			this.mostrarMensagemErro(e.getMessage());
		}
		return null;
	}

	private void adicionarRegistroConsumoSelecionados() {
		this.registro.getRegistrosConsumo().clear();

		for (String itemSelecionado : registrosConsumoSelecionados) {
			for (RegistroConsumo registroConsumo : this.getRegistrosConsumo()) {
				if (registroConsumo.getCodigo().equals(Integer.parseInt(itemSelecionado))) {
					this.registro.getRegistrosConsumo().add(registroConsumo);
				}
			}
		}
	}

	public List<RegistroConsumo> getRegistrosConsumo() {
		return registrosConsumo;
	}

	public List<String> getRegistrosConsumoSelecionados() {
		return registrosConsumoSelecionados;
	}

	public void setRegistrosConsumoSelecionados(List<String> registrosConsumoSelecionados) {
		this.registrosConsumoSelecionados = registrosConsumoSelecionados;
	}

	@Override
	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new RegistroConsumoSistemaAbastecimento();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "RegistroConsumoSistemaAbastecimento.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}

}
