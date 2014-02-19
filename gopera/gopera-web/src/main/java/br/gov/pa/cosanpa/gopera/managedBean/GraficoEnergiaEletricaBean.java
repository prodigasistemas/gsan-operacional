package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.jboss.logging.Logger;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.fachada.IRelatorioEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.RelatorioEnergiaEletrica;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;
import br.gov.pa.cosanpa.gopera.util.WebBundle;

@ManagedBean
@SessionScoped
public class GraficoEnergiaEletricaBean extends BaseBean<RelatorioEnergiaEletrica> {
	private static Logger logger = Logger.getLogger(GraficoEnergiaEletricaBean.class);

	private RelatorioEnergiaEletrica registro = new RelatorioEnergiaEletrica();
	private List<RelatorioEnergiaEletrica> relatorio = new ArrayList<RelatorioEnergiaEletrica>();	
	private String referenciaInicial;
	private String referenciaFinal;
	private Integer tipoRelatorio;
	private List<SelectItem> listaRelatorio = new ArrayList<SelectItem>();
	private List<RegionalProxy> regionais = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> municipios = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> localidades = new ArrayList<LocalidadeProxy>();
	private Integer codigoRegional;
	private Integer codigoUnidadeNegocio;
	private Integer codigoMunicipio;
	private Integer codigoLocalidade;	
	private PieChartModel pieModelo;
	private CartesianChartModel barModelo; 
	private CartesianChartModel lineModelo;  
	private boolean exibePie;
	private boolean exibeBar;
	private boolean exibeLine;
	private String titulo;
	
	@EJB
	private IRelatorioEnergiaEletrica fachadaRel;
	@EJB
	private IProxy fachadaProxy;
	

	public String iniciar(){
		if (bundle == null){
			bundle = new WebBundle();
		}
		this.registro = new RelatorioEnergiaEletrica();
		this.tipoRelatorio = 1;
		listaRelatorio.clear();
		SelectItem tipoRel = new SelectItem();  
		tipoRel.setValue(1);  
		tipoRel.setLabel(bundle.getText("consumo_kwh"));  
		listaRelatorio.add(tipoRel);
		tipoRel = new SelectItem();
		tipoRel.setValue(2);  
		tipoRel.setLabel(bundle.getText("total_reais"));
		listaRelatorio.add(tipoRel);
		tipoRel = new SelectItem();
		tipoRel.setValue(3);  
		tipoRel.setLabel(bundle.getText("ajuste_fator_potencia"));
		listaRelatorio.add(tipoRel);
		tipoRel = new SelectItem();
		tipoRel.setValue(4);  
		tipoRel.setLabel(bundle.getText("ultrapassagem_kwh"));  
		listaRelatorio.add(tipoRel);
		tipoRel = new SelectItem();
		tipoRel.setValue(5);  
		tipoRel.setLabel(bundle.getText("ultrapassagem_reais"));  
		listaRelatorio.add(tipoRel);
		exibePie = false;
		exibeBar = false;
		exibeLine = false;
		return "GraficoEnergiaEletrica.jsf";
	}	

    public void exibir(){
    	try{
    		 exibePie = false;
    		 exibeBar = false;
             titulo = listaRelatorio.get(tipoRelatorio - 1).getLabel();
        	 relatorio = fachadaRel.getEnergiaEletricaPeriodo(primeiroDiaMes(referenciaInicial),
        			 	 primeiroDiaMes(referenciaFinal),
						 this.codigoRegional,
						 this.codigoUnidadeNegocio,
						 this.codigoMunicipio,
						 this.codigoLocalidade);

     		if (relatorio.size() == 0){
    			mostrarMensagemErro(bundle.getText("erro_nao_existe_retorno_filtro"));
    			return;
    		}
        	 
             lineModelo = new CartesianChartModel();
             Integer codigoUC = 0;
             ChartSeries lineUC = new ChartSeries();; 
             for (RelatorioEnergiaEletrica rel : relatorio){
            	 if (!codigoUC.equals(rel.getCodigoUC())){
            		 if (!codigoUC.equals(0)){
            			 lineModelo.addSeries(lineUC);
                		 lineUC = new ChartSeries();
            		 }
            		 lineUC.setLabel(rel.getCodigoUC().toString() + " - " + rel.getNomeUC());
            		 //lineUC.setLabel(rel.getCodigoUC().toString());
            		 codigoUC = rel.getCodigoUC();
            	 }

        		 switch (tipoRelatorio) {
                 case 1: //CONSUMO (KWh)
                	 lineUC.set(rel.getReferencia(), rel.getConsumoKwh());
                	 break;
                 case 2://Valor Total R$
                	 lineUC.set(rel.getReferencia(), rel.getTotal());
                	 break;
                 case 3: //Ajuste Fator Potência 
                	 lineUC.set(rel.getReferencia(), rel.getAjusteFatorPotencia());
                	 break;
                 case 4://Ultrapassagem Kwh
                	 lineUC.set(rel.getReferencia(),rel.getUltrapassagemKwh());
                	 break;
                 case 5://Ultrapassagem R$
                	 lineUC.set(rel.getReferencia(),rel.getUltrapassagemR$());
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
	
	public String getReferenciaInicial() {
		return referenciaInicial;
	}

	public void setReferenciaInicial(String referenciaInicial) {
		this.referenciaInicial = referenciaInicial;
	}

	public String getReferenciaFinal() {
		return referenciaFinal;
	}

	public void setReferenciaFinal(String referenciaFinal) {
		this.referenciaFinal = referenciaFinal;
	}

	public Integer getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(Integer tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}
	
	public List<SelectItem> getListaRelatorio() {
		return listaRelatorio;
	}

	public void setListaRelatorio(List<SelectItem> listaRelatorio) {
		this.listaRelatorio = listaRelatorio;
	}
	
	public PieChartModel getPieModelo() {
		return pieModelo;
	}
	
	public CartesianChartModel getbarModelo() {  
	   return barModelo;  
	} 
	
	public CartesianChartModel getlineModelo() {  
		   return lineModelo;  
	} 

	public boolean getExibePie() {
		return exibePie;
	}

	public boolean getExibeBar() {
		return exibeBar;
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
}
