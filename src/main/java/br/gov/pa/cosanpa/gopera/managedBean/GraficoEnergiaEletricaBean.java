package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.jboss.logging.Logger;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.RegionalProxy;
import br.gov.model.operacao.RelatorioEnergiaEletrica;
import br.gov.model.operacao.TipoGraficoEnergia;
import br.gov.model.operacao.UnidadeConsumidora;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.pa.cosanpa.gopera.util.WebBundle;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;
import br.gov.servicos.operacao.RelatorioEnergiaEletricaRepositorio;
import br.gov.servicos.operacao.UnidadeConsumidoraRepositorio;

@ManagedBean
@ViewScoped
public class GraficoEnergiaEletricaBean extends BaseBean<RelatorioEnergiaEletrica> {
	private static Logger logger = Logger.getLogger(GraficoEnergiaEletricaBean.class);

	private Integer referenciaInicial;
	private Integer referenciaFinal;
	private TipoGraficoEnergia tipoGrafico;
	
	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private List<UnidadeConsumidora> unidadesConsumidoras = new ArrayList<UnidadeConsumidora>();
	private Integer codigoRegional;
	private Integer codigoUnidadeNegocio;
	private Integer codigoMunicipio;
	private Integer codigoLocalidade;
	private List<String> unidadesConsumidorasSelecionadas;
	

	private LineChartModel lineModelo;  
	private boolean exibeLine;
	private String titulo;
	
	@EJB
	private UnidadeConsumidoraRepositorio unidadeConsumidoraEjb;
	
	@EJB
	private RelatorioEnergiaEletricaRepositorio fachadaRel;
	
	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;
	

	public String iniciar(){
		if (bundle == null){
			bundle = new WebBundle();
		}
		this.registro = new RelatorioEnergiaEletrica();
		return "GraficoEnergiaEletrica.jsf";
	}	

    public void exibir(){
    	try{
    	    List<RelatorioEnergiaEletrica> relatorio = fachadaRel.getEnergiaEletricaPeriodo(referenciaInicial,
        			 	 referenciaFinal,
						 this.codigoRegional,
						 this.codigoUnidadeNegocio,
						 this.codigoMunicipio,
						 this.codigoLocalidade,
						 this.unidadesConsumidorasSelecionadas);

     		if (relatorio.size() == 0){
    			mostrarMensagemErro(bundle.getText("erro_nao_existe_retorno_filtro"));
    			return;
    		}
        	 
             lineModelo = new LineChartModel();
             lineModelo.setLegendPosition("ne");
             lineModelo.setTitle(bundle.getText(tipoGrafico.getDescricao()));
             lineModelo.getAxes().put(AxisType.X, new CategoryAxis(bundle.getText("referencia")));
             Axis yAxis = lineModelo.getAxis(AxisType.Y);
             yAxis.setMin(0);
             Integer codigoUC = 0;
             ChartSeries lineUC = null; 
             for (RelatorioEnergiaEletrica rel : relatorio){
            	 if (codigoUC.intValue() != rel.getCodigoUC().intValue()){
            		 codigoUC = rel.getCodigoUC();
            		 lineUC = new ChartSeries();;
            		 lineUC.setLabel(rel.getCodigoUC().toString() + " - " + rel.getNomeUC());
            		 lineModelo.addSeries(lineUC);
            	 }

        		 switch (tipoGrafico) {
                 case CONSUMO_KWH:
                	 lineUC.set(String.valueOf(rel.getReferencia()), rel.getConsumoKwh());
                	 break;
                 case TOTAL_REAIS:
                	 lineUC.set(String.valueOf(rel.getReferencia()), rel.getTotal());
                	 break;
                 case AJUSTE_FATOR_POTENCIA:  
                	 lineUC.set(String.valueOf(rel.getReferencia()), rel.getAjusteFatorPotencia());
                	 break;
                 case ULTRAPASSAGEM_KWH:
                	 lineUC.set(String.valueOf(rel.getReferencia()),rel.getUltrapassagemKwh());
                	 break;
                 case ULTRAPASSAGEM_REAIS:
                	 lineUC.set(String.valueOf(rel.getReferencia()),rel.getUltrapassagemR$());
                	 break;
        		 }
             }
             exibeLine = true;
		}
		catch (Exception e){
			logger.error(bundle.getText("erro_gerar_relatorio"), e);
			mostrarMensagemErro(bundle.getText("erro_gerar_relatorio"));
		}
    }
    
	public RelatorioEnergiaEletrica getRegistro() {
		return registro;
	}

	public void setRegistro(RelatorioEnergiaEletrica registro) {
		this.registro = registro;
	}

	public List<RegionalProxy> getRegionais() {
		try {
			regionais = fachadaProxy.getListaRegional();
			regionais.add(0, new RegionalProxy(-1, "Selecione..."));
			return regionais; 
		} catch (Exception e) {
			mostrarMensagemErro("Erro ao consultar sistema externo.");
		}
		return regionais;
	}

	public List<UnidadeNegocioProxy> getUnidadesNegocio() {
		if ((this.getCodigoRegional() != null)) {
			try {
				this.unidadesNegocio =  fachadaProxy.getListaUnidadeNegocio(this.getCodigoRegional());
				this.unidadesNegocio.add(0, new UnidadeNegocioProxy(-1, "Selecione..."));
				return unidadesNegocio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
		unidadesNegocio.add(0, new UnidadeNegocioProxy(-1, "Selecione..."));
		return unidadesNegocio;
	}

	public List<MunicipioProxy> getMunicipios() {
		if (this.getCodigoUnidadeNegocio() != null) {
			try {
				this.municipios =  fachadaProxy.getListaMunicipio(this.getCodigoRegional(), this.getCodigoUnidadeNegocio());
				this.municipios.add(0, new MunicipioProxy(-1, "Selecione..."));
				return municipios;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		municipios = new ArrayList<MunicipioProxy>();
		this.municipios.add(0, new MunicipioProxy(-1, "Selecione..."));
		return municipios;
	}

	public List<LocalidadeProxy> getLocalidades() {
		if (this.getCodigoMunicipio() != null) {
			try {
				this.localidades =  fachadaProxy.getListaLocalidade(this.getCodigoRegional(), this.getCodigoUnidadeNegocio(), this.getCodigoMunicipio());
				this.localidades.add(0, new LocalidadeProxy(-1, "Selecione..."));
				return this.localidades;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		localidades = new ArrayList<LocalidadeProxy>();
		localidades.add(0, new LocalidadeProxy(-1, "Selecione..."));			
		return localidades;
	}
	
	public List<String> getUnidadesConsumidorasSelecionadas() {
		return unidadesConsumidorasSelecionadas;
	}

	public void setUnidadesConsumidorasSelecionadas(List<String> unidadesConsumidorasSelecionadas) {
		this.unidadesConsumidorasSelecionadas = unidadesConsumidorasSelecionadas;
	}

	public List<UnidadeConsumidora> getUnidadesConsumidoras() {
		if (this.getCodigoLocalidade() != null) {
			try {
				this.unidadesConsumidoras =  unidadeConsumidoraEjb.unidadesConsumidoras(codigoRegional, codigoUnidadeNegocio, codigoMunicipio, codigoLocalidade);
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		return unidadesConsumidoras;
	}

	public Integer getCodigoRegional() {
		return codigoRegional;
	}

	public void setCodigoRegional(Integer codigoRegional) {
		this.codigoRegional = codigoRegional;
	}

	public Integer getCodigoUnidadeNegocio() {
		return codigoUnidadeNegocio;
	}

	public void setCodigoUnidadeNegocio(Integer codigoUnidadeNegocio) {
		this.codigoUnidadeNegocio = codigoUnidadeNegocio;
	}

	public Integer getCodigoMunicipio() {
		return codigoMunicipio;
	}

	public void setCodigoMunicipio(Integer codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public Integer getCodigoLocalidade() {
		return codigoLocalidade;
	}

	public void setCodigoLocalidade(Integer codigoLocalidade) {
		this.codigoLocalidade = codigoLocalidade;
	}

	public void setLocalidades(List<LocalidadeProxy> localidades) {
		this.localidades = localidades;
	}
	
	public Integer getReferenciaInicial() {
        return referenciaInicial;
    }

    public void setReferenciaInicial(Integer referenciaInicial) {
        this.referenciaInicial = referenciaInicial;
    }

    public Integer getReferenciaFinal() {
        return referenciaFinal;
    }

    public void setReferenciaFinal(Integer referenciaFinal) {
        this.referenciaFinal = referenciaFinal;
    }
    
    public TipoGraficoEnergia[] getTiposGraficos(){
        return TipoGraficoEnergia.values();
    }

	public LineChartModel getLineModelo() {
        return lineModelo;
    }

    public boolean getExibeLine() {
		return exibeLine;
	}	
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

    public TipoGraficoEnergia getTipoGrafico() {
        return tipoGrafico;
    }

    public void setTipoGrafico(TipoGraficoEnergia tipoGrafico) {
        this.tipoGrafico = tipoGrafico;
    }    
}
