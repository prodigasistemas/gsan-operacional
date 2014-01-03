package br.gov.pa.cosanpa.gopera.managedBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.jboss.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.pa.cosanpa.gopera.fachada.IEEAT;
import br.gov.pa.cosanpa.gopera.fachada.IEEATHoras;
import br.gov.pa.cosanpa.gopera.fachada.IUnidadeConsumidora;
import br.gov.pa.cosanpa.gopera.model.EEAT;
import br.gov.pa.cosanpa.gopera.model.EEATHoras;
import br.gov.pa.cosanpa.gopera.model.EEATHorasCMB;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@ManagedBean
@SessionScoped
public class EeatHorasBean extends BaseBean<EEATHoras> {
	
	private static Logger logger = Logger.getLogger(EeatHorasBean.class); 

	@EJB
	private IEEATHoras fachada;
	@EJB
	private IEEAT fachadaEEAT;
	@EJB
	private IUnidadeConsumidora fachadaUC;

	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private List<EEAT> listaEEAT = new ArrayList<EEAT>();
	private LazyDataModel<EEATHoras> listaConsumo = null;
	private Double horasMes;
	private Double horasTrabalhadas;

	public EeatHorasBean() {

	}
	
	/*********************************************
	 * PRIVATE METHODS
	 *********************************************/
	private Map<Integer, EEATHorasCMB> todasCMBsDaUnidade() throws Exception {
		Map<Integer, EEATHorasCMB> lista = new HashMap<Integer, EEATHorasCMB>();
		EEAT eeat = fachadaEEAT.obterEEAT(this.registro.getEeat().getCodigo());
		for (Integer cmbIndex = 1; cmbIndex <= eeat.getCmbQuantidade(); cmbIndex++) {
			EEATHorasCMB cmb = new EEATHorasCMB();
			cmb.setQuantidade(cmbIndex);
			cmb.setEeathoras(this.registro);
			lista.put(cmbIndex, cmb);
		}
		
		return lista;
	}
	
	private void carregar() {
		try {
			this.registro = fachada.obterEEATHoras(this.registro.getCodigo());
			DecimalFormat df = new DecimalFormat("#,##0.00");
			Map<Integer, EEATHorasCMB> todosCMBs = todasCMBsDaUnidade();
			for (EEATHorasCMB cmb : this.registro.getCmb()) {
				EEATHorasCMB horaCmb = todosCMBs.get(cmb.getQuantidade());
				horaCmb.setHoraAux(df.format(cmb.getHora()));
			}
			this.registro.getCmb().clear();
			this.registro.getCmb().addAll(todosCMBs.values());
			carregarHoras();
		} catch (Exception e) {
			logger.error(bundle.getText("erro_carregar_horas_eat"), e);
			this.mostrarMensagemErro(bundle.getText("erro_carregar_horas_eat"));
		}
	}

	private void iniciarLazy() {
		if (listaConsumo == null) {
			listaConsumo = new LazyDataModel<EEATHoras>() {
				private static final long serialVersionUID = 1L;

				@Override
				public List<EEATHoras> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {
					try {
						List<EEATHoras> listaLazy = fachada.obterListaLazy(startingAt, maxPerPage, filters);
						setRowCount(fachada.obterQtdRegistros(filters));
						setPageSize(maxPerPage);
						return listaLazy;
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}

				@Override
				public Object getRowKey(EEATHoras consumo) {
					return consumo.getCodigo();
				}

				@Override
				public EEATHoras getRowData(String consumoId) {
					if (consumoId != null && !consumoId.equals("") && !consumoId.equals("null")) {
						Integer id = Integer.valueOf(consumoId);

						for (EEATHoras consumo : listaConsumo) {
							if (id.equals(consumo.getCodigo())) {
								return consumo;
							}
						}
					}
					return null;
				}
			};
		}
	}

	public String iniciar() {
		// Fachada do EJB
		this.setFachadaBean(this.fachada);
		iniciarLazy();
		this.visualizar();
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new EEATHoras();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "EeatHoras.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");
	}
	
	@Override
	public String novo() {
		this.registro = new EEATHoras();
		carregarHoras();
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

	public void carregarHoras() {
		Calendar calendario = Calendar.getInstance();
		if (this.registro.getReferencia() != null) {
			calendario.setTime(this.registro.getReferencia());
		}

		horasMes = (double) calendario.getActualMaximum(Calendar.DAY_OF_MONTH) * 24;

		horasTrabalhadas = 0.0;
		for (EEATHorasCMB cmb : this.registro.getCmb()) {
			horasTrabalhadas = horasTrabalhadas + Double.parseDouble(cmb.getHoraAux().replace(".", "").replace(",", "."));
		}
	}

	public void carregarCMB() {
		try {
			this.registro.getCmb().clear();
			this.registro.getCmb().addAll(todasCMBsDaUnidade().values());
		} catch (Exception e) {
			logger.error(bundle.getText("erro_carregar_cmbs"), e);
			this.mostrarMensagemErro(bundle.getText("erro_carregar_cmbs"));
		}
	}

	@Override
	public String cadastrar() {
		try {
			if (horasTrabalhadas > horasMes) {
				this.mostrarMensagemErro("Quantidade de Horas Trabalhadas não pode ser superior a Quantidade de Horas do Mês.");
				return null;
			}
			// Verifica se não está cadastrado para o mes de referencia corrente
			if (!this.getEditando()) {
				if (!fachada.verificaMesReferencia(this.registro.getEeat().getCodigo(), this.filtroData(this.registro.getReferencia(), "yyyy-MM-dd"))) {
					this.mostrarMensagemErro("Mês de referência já cadastrado!");
					return null;
				}
			}
			if (!validaReferencia(this.registro.getReferencia()))
				return null;
			return super.cadastrar();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar: " + e.getMessage());
		}
		return null;
	}

	@Override
	public String confirmar() {
		try {
			for (EEATHorasCMB cmb : this.registro.getCmb()) {
				cmb.setHora(Double.parseDouble(cmb.getHoraAux().replace(".", "").replace(",", ".")));
			}
			registro.setUsuario(usuarioProxy);
			registro.setUltimaAlteracao(new Date());
			return super.confirmarLazy();
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar: " + e.getMessage());
		}
		return null;
	}

	/*********************************************
	 * GETTERS AND SETTERS
	 *********************************************/
	public Double getHorasMes() {
		return horasMes;
	}

	public void setHorasMes(Double horasMes) {
		this.horasMes = horasMes;
	}

	public Double getHorasTrabalhadas() {
		return horasTrabalhadas;
	}

	public void setHorasTrabalhadas(Double horasTrabalhadas) {
		this.horasTrabalhadas = horasTrabalhadas;
	}

	public LazyDataModel<EEATHoras> getListaConsumo() {
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
				this.unidadesNegocio = fachadaUC.getListaUnidadeNegocio(usuarioProxy, this.registro.getRegionalProxy().getCodigo());
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
				this.municipios = fachadaUC.getListaMunicipio(usuarioProxy, this.registro.getRegionalProxy().getCodigo(), this.registro
						.getUnidadeNegocioProxy().getCodigo());
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
				this.localidades = fachadaUC.getListaLocalidade(usuarioProxy, this.registro.getRegionalProxy().getCodigo(), this.registro
						.getUnidadeNegocioProxy().getCodigo(), this.registro.getMunicipioProxy().getCodigo());
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
				this.listaEEAT = fachadaUC.getListaEAT(usuarioProxy, this.registro.getRegionalProxy().getCodigo(), this.registro.getUnidadeNegocioProxy()
						.getCodigo(), this.registro.getMunicipioProxy().getCodigo(), this.registro.getLocalidadeProxy().getCodigo());
				return this.listaEEAT;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar ETA´s.");
			}
		}
		listaEEAT = new ArrayList<EEAT>();
		return listaEEAT;
	}
}
