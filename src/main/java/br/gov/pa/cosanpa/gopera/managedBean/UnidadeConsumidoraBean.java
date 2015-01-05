package br.gov.pa.cosanpa.gopera.managedBean;

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
import br.gov.model.operacao.UnidadeConsumidora;
import br.gov.model.operacao.UnidadeConsumidoraOperacional;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;
import br.gov.servicos.operacao.UnidadeConsumidoraRepositorio;

@ManagedBean
@SessionScoped
public class UnidadeConsumidoraBean extends BaseBean<UnidadeConsumidora> {

	@EJB
	private UnidadeConsumidoraRepositorio fachada;
	
	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;
	
	private List<UnidadeNegocioProxy> listaUnidadeNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> listaMunicipio = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> listaLocalidade = new ArrayList<LocalidadeProxy>();
	private List<UnidadeConsumidoraOperacional> listaUnidade = new ArrayList<UnidadeConsumidoraOperacional>();
	private Integer tipoUnidadeOperacional = 1;
	private Integer codigoUnidadeOperacional;
	private UnidadeConsumidoraOperacional operacional;
	private String percentual;
	private LazyDataModel<UnidadeConsumidora> listaDados = null;
	
	public UnidadeConsumidoraBean() {
		
	}

	public LazyDataModel<UnidadeConsumidora> getListaDados() {
		return listaDados;
	}
	
	public Integer getTipoUnidadeOperacional() {
		return tipoUnidadeOperacional;
	}

	public void setTipoUnidadeOperacional(Integer tipoUnidadeOperacional) {
		this.tipoUnidadeOperacional = tipoUnidadeOperacional;
	}

	public Integer getCodigoUnidadeOperacional() {
		return codigoUnidadeOperacional;
	}

	public void setCodigoUnidadeOperacional(Integer codigoUnidadeOperacional) {
		this.codigoUnidadeOperacional = codigoUnidadeOperacional;
	}

	public UnidadeConsumidoraOperacional getOperacional() {
		return operacional;
	}

	public void setOperacional(UnidadeConsumidoraOperacional operacional) {
		this.operacional = operacional;
	}

	public String getPercentual() {
		return percentual;
	}

	public void setPercentual(String percentual) {
		this.percentual = percentual;
	}

	public List<UnidadeNegocioProxy> getListaUnidadeNegocio() {
		try {
//				if (this.getEditando() || this.getCadastrando()) {
					this.listaUnidadeNegocio =  fachadaProxy.getListaUnidadeNegocio(0);
//				}
				return listaUnidadeNegocio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		return listaUnidadeNegocio = new ArrayList<UnidadeNegocioProxy>();
	}

	public List<MunicipioProxy> getListaMunicipio() {
		if (this.registro.getUnidadeNegocioProxy().getCodigo() != null) {
			try {
//				if (this.getEditando() || this.getCadastrando()) {
					this.listaMunicipio =  fachadaProxy.getListaMunicipio(0, this.registro.getUnidadeNegocioProxy().getCodigo());
//				}
				return this.listaMunicipio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		return this.listaMunicipio = new ArrayList<MunicipioProxy>();
	}

	public List<LocalidadeProxy> getListaLocalidade() {
		if (this.registro.getMunicipioProxy().getCodigo() != null) {
			try {
//				if (this.getEditando() || this.getCadastrando()) {
					this.listaLocalidade =  fachadaProxy.getListaLocalidade(0, this.registro.getUnidadeNegocioProxy().getCodigo(), this.registro.getMunicipioProxy().getCodigo());
//				}
				return this.listaLocalidade;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		return this.listaLocalidade= new ArrayList<LocalidadeProxy>();
	}
	
	
	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		iniciarLazy();
		this.visualizar();
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new UnidadeConsumidora();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "UnidadeConsumidora.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");		
	}
	
	private void iniciarLazy(){
		if (listaDados == null) {  
			listaDados = new LazyDataModel<UnidadeConsumidora>() {
				private static final long serialVersionUID = 1L;

				
				public List<UnidadeConsumidora> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
					try {					
						List<UnidadeConsumidora> listaLazy = fachada.obterListaLazy(startingAt, maxPerPage, filters);
						setRowCount(fachada.obterQtdRegistros(filters));
						setPageSize(maxPerPage);						
						return listaLazy;						
					} catch (Exception e) {
						e.printStackTrace();
					}	
					return null;
				}
				
				
				public Object getRowKey(UnidadeConsumidora dados) {
					return dados.getCodigo();
				}
				
				
				public UnidadeConsumidora getRowData(String dadosId) {
					if (dadosId != null && !dadosId.equals("") && !dadosId.equals("null")) {
						Integer id = Integer.valueOf(dadosId);
						
						for (UnidadeConsumidora dados : listaDados) {
							if(id.equals(dados.getCodigo())){
								return dados;
							}
						}
					}
					return null;				
				}
			};
	    }
	}
	
	
	public String novo() {
		this.registro = new UnidadeConsumidora();
		this.setPercentual("0,00");
		this.setTipoUnidadeOperacional(1);
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
			this.setPercentual("0,00");
			this.registro = fachada.obterUnidadeConsumidora(this.registro.getCodigo());
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar");
		}
	}

	
	public String cadastrar() {
		if (validarPercentualGeral()){
			super.cadastrar();
		}
		return "";
	}

	
	
	public String confirmar() {
		try {
		    
		    if (fachada.existeUnidadeConsumidora(registro.getUc())){
		        this.mostrarMensagemErro(bundle.getText("erro_unidade_consumidora_cadastrada_com_codigo"));
		    }else{
		        registro.setRegionalProxy(fachadaProxy.getRegionalUnidadeNegocio(registro.getUnidadeNegocioProxy().getCodigo()));
		        registro.setUsuario(usuarioProxy.getCodigo());
		        registro.setUltimaAlteracao(new Date());
		        super.confirmarLazy();
		    }
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Salvar");
		}
		return null;
	}
	
	public void incluirOperacional() {
		try {
			//Validar Percentual de 100% da UC
			if (validarPercentualUC()){
				UnidadeConsumidoraOperacional undOpe = new UnidadeConsumidoraOperacional();
				undOpe.setTipoUnidadeOperacional(tipoUnidadeOperacional);
				undOpe.setCodigoUnidadeOperacional(codigoUnidadeOperacional);
				undOpe.setDescricao(fachadaProxy.getUnidadeOperacional(tipoUnidadeOperacional, codigoUnidadeOperacional));
				undOpe.setPercentual(Double.parseDouble(percentual.replace(".", "").replace(",", ".")));
				undOpe.setUC(this.registro);
				this.registro.getOperacional().add(undOpe);
			}
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar");
		}
	}	

	public void excluirOperacional() {
		for (UnidadeConsumidoraOperacional undOpe : this.registro.getOperacional()) {
			if (undOpe.getCodigoUnidadeOperacional().equals(operacional.getCodigoUnidadeOperacional())
				&& undOpe.getTipoUnidadeOperacional().equals(operacional.getTipoUnidadeOperacional())	) {
				this.registro.getOperacional().remove(undOpe);
				break;
			}
		}
	}
	
	private boolean validarPercentualUC(){
		Double percAux = 0.00;
		for (UnidadeConsumidoraOperacional undOpe : this.registro.getOperacional()) {
			percAux = percAux + undOpe.getPercentual();
		}
		//Verifica se Total de Utilização Ultrapassa 100%
		Double dblPerc = Double.parseDouble(percentual.replace(".", "").replace(",", "."));
		if (dblPerc == 0) {
			this.mostrarMensagemErro(bundle.getText("erro_percentual_zero"));
			return false;
		}
		if (percAux + dblPerc > 100){
			this.mostrarMensagemErro(bundle.getText("erro_total_maior_que_100"));
			return false;
		}		
		else
			return true;
	}
	
	private boolean validarPercentualGeral(){
		Double percAux = 0.00;
		for (UnidadeConsumidoraOperacional undOpe : this.registro.getOperacional()) {
			percAux = percAux + undOpe.getPercentual();
		}
		if (percAux != 100){
			this.mostrarMensagemErro("Utilização Total de Unidade Operacionais deve ser 100%");
			return false;
		}		
		else
			return true;
	}
	
	public List<UnidadeConsumidoraOperacional> getListaUnidade() {

		if ((this.tipoUnidadeOperacional != null)) {
			try {
				this.listaUnidade =  fachadaProxy.getListaUnidadeOperacional(this.tipoUnidadeOperacional);
				return listaUnidade;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar Unidade Operacionais.");
			}
		}
		return listaUnidade = new ArrayList<UnidadeConsumidoraOperacional>();
	}
}
