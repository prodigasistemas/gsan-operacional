package br.gov.pa.cosanpa.gopera.managedBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jboss.logging.Logger;

import net.sf.jasperreports.engine.JRResultSetDataSource;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RegionalProxy;
import br.gov.pa.cosanpa.gopera.model.RelatorioGerencial;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@ManagedBean
@SessionScoped
public class RelatorioAnaliseLaboratorialBean extends BaseRelatorioBean<RelatorioGerencial> {
	private static Logger logger = Logger.getLogger(RelatorioAnaliseLaboratorialBean.class);

	@Resource(lookup="java:/gopera")
	private DataSource	dataSource;

	@EJB
	private IProxy fachadaProxy;
	
	private List<RegionalProxy> listaRegional = new ArrayList<RegionalProxy>();
	private List<UnidadeNegocioProxy> listaUnidadeNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> listaMunicipio = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> listaLocalidade = new ArrayList<LocalidadeProxy>();
	private Integer tipoAgrupamento;
	private String referenciaInicial;
	private String referenciaFinal;

	@Override
	public String iniciar(){
		tipoExportacao = 1;	
		tipoAgrupamento = 5;
		this.registro = new RelatorioGerencial();
		return "RelatorioAnaliseLaboratorial.jsf";
	}	

	public void exibir() throws SQLException{
    	String sql = "", sqlFiltro = "";
    	Connection con = null; 
    	try{
    		con =  dataSource.getConnection();
    		Statement stm = con.createStatement();
    		
    		//VERIFICA FILTRO
    		StringBuilder filtroRelatorio = new StringBuilder();
			if (registro.getCodigoRegional() != -1) {
				sqlFiltro = sqlFiltro + " AND A.greg_id = " + registro.getCodigoRegional();
				
	    		filtroRelatorio.append(bundle.getText("regional"))
	    		.append(": ")
	    		.append(fachadaProxy.getRegional(this.registro.getCodigoRegional()).getNome());
			}
			if (registro.getCodigoUnidadeNegocio() != -1) {
				sqlFiltro = sqlFiltro + " AND A.uneg_id = " + registro.getCodigoUnidadeNegocio();
				
	    		filtroRelatorio.append("  ")
	    		.append(bundle.getText("unid_negocio"))
	    		.append(": ")
	    		.append(fachadaProxy.getUnidadeNegocio(this.registro.getCodigoUnidadeNegocio()).getNome());
			}
			if (registro.getCodigoMunicipio() != -1) {
				sqlFiltro = sqlFiltro + " AND A.muni_id = " + registro.getCodigoMunicipio();
				
	    		filtroRelatorio.append("  ")
	    		.append(bundle.getText("municipio"))
	    		.append(": ")
	    		.append(fachadaProxy.getMunicipio(this.registro.getCodigoMunicipio()).getNome());
			}
			if (registro.getCodigoLocalidade() != -1) {
				sqlFiltro = sqlFiltro + " AND A.loca_id = " + registro.getCodigoLocalidade();
				
	    		filtroRelatorio.append("  ")
	    		.append(bundle.getText("localidade"))
	    		.append(": ")
	    		.append(fachadaProxy.getLocalidade(this.registro.getCodigoLocalidade()).getNome());
			} 
    		
			SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");	
			GregorianCalendar gc = new GregorianCalendar();
			//Formata Data Inicial
			gc.setTime(primeiroDiaMes(referenciaInicial));
			String dataIni = formataData.format(gc.getTime());
			//Formata Data Final
			gc.setTime(primeiroDiaMes(referenciaFinal));
			String dataFim = formataData.format(gc.getTime());
			
			String nomeRelatorio = bundle.getText("analises_laboratoriais");
			nomeArquivo = "analiseLaboratorial";
			reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/" + nomeArquivo + ".jasper");
			sql = "SELECT A.greg_id, greg_nmregional, A.uneg_id, uneg_nmunidadenegocio, A.muni_id, muni_nmmunicipio, A.loca_id, loca_nmlocalidade,"
			    + "		  anac_referencia, anac_analisada, anac_conformidade"
			    + "  FROM operacao.analise_clinica A"
			    + " INNER JOIN cadastro.gerencia_regional B ON A.greg_id = B.greg_id"
			    + " INNER JOIN cadastro.unidade_negocio C ON A.uneg_id = C.uneg_id"
			    + " INNER JOIN cadastro.municipio D ON A.muni_id = D.muni_id"
			    + " INNER JOIN cadastro.localidade E ON A.loca_id = E.loca_id"
			    + " WHERE anac_referencia BETWEEN '" + dataIni + "' AND '" + dataFim + "'" + sqlFiltro
			    + " ORDER BY greg_nmregional, uneg_nmunidadenegocio, muni_nmmunicipio, loca_nmlocalidade, anac_referencia";

			//Monta ResultSet	
  			ResultSet rsAux = stm.executeQuery(sql);
    		if (!rsAux.next()){
    			mostrarMensagemErro(bundle.getText("erro_nao_existe_retorno_filtro"));
    			return;
    		}
			ResultSet rs = stm.executeQuery(sql);
    		reportDataSource = new JRResultSetDataSource(rs);
    		reportParametros = new HashMap<String, Object>();
    		reportParametros.put("logoRelatorio", "logoRelatorio.jpg");
    		reportParametros.put("nomeRelatorio", nomeRelatorio);
    		reportParametros.put("nomeUsuario", usuarioProxy.getNome());
    		reportParametros.put("dataInicial", primeiroDiaMes(referenciaInicial));
    		reportParametros.put("dataFinal", primeiroDiaMes(referenciaFinal));
    		reportParametros.put("filtro", filtroRelatorio.toString());
    		reportParametros.put("exibirRegional", (tipoAgrupamento >= 1 ? true : false));  
    		reportParametros.put("exibirUnidadeNegocio", (tipoAgrupamento >= 2 ? true : false));
    		reportParametros.put("exibirMunicipio", (tipoAgrupamento >= 3 ? true : false));
    		reportParametros.put("exibirLocalidade", (tipoAgrupamento >= 4 ? true : false));
    		reportParametros.put("exibirUnidadeOperacional", (tipoAgrupamento >= 5 ? true : false));
    		reportParametros.put("REPORT_CONNECTION", con);
    		reportParametros.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/") + "/");
    		reportParametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
    		
    		HttpServletResponse httpServletResponse=(HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
			httpServletResponse.setCharacterEncoding("ISO-8859-1");
			geraRelatorio(httpServletResponse);
    		
			FacesContext.getCurrentInstance().responseComplete();  
		}
		catch (Exception e){
			logger.error(bundle.getText("erro_gerar_relatorio"), e);
			mostrarMensagemErro(bundle.getText("erro_gerar_relatorio"));
		}
		finally {
			con.close();
		}    	
    }
    
    /*************************************************************
     * GETTERS AND SETTERS 
     *************************************************************/
	public List<RegionalProxy> getListaRegional() {
		try {
				this.listaRegional =  fachadaProxy.getListaRegional();
				this.listaRegional.add(0, new RegionalProxy(-1, "Selecione..."));
				return listaRegional;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		listaRegional = new ArrayList<RegionalProxy>();
		this.listaRegional.add(0, new RegionalProxy(-1, "Selecione..."));
		return listaRegional;
	}
	
	public List<UnidadeNegocioProxy> getListaUnidadeNegocio() {
		if (this.registro.getCodigoRegional() != null) {
			try {
					this.listaUnidadeNegocio =  fachadaProxy.getListaUnidadeNegocio(this.registro.getCodigoRegional());
					this.listaUnidadeNegocio.add(0, new UnidadeNegocioProxy(-1, "Selecione..."));
					return listaUnidadeNegocio;
				} catch (Exception e) {
					mostrarMensagemErro("Erro ao consultar sistema externo.");
				}
		} 	
		listaUnidadeNegocio = new ArrayList<UnidadeNegocioProxy>();
		this.listaUnidadeNegocio.add(0, new UnidadeNegocioProxy(-1, "Selecione..."));
		return listaUnidadeNegocio;
	}

	public List<MunicipioProxy> getListaMunicipio() {
		if (this.registro.getCodigoUnidadeNegocio() != null) {
			try {
				this.listaMunicipio =  fachadaProxy.getListaMunicipio(this.registro.getCodigoRegional(),
																	  this.registro.getCodigoUnidadeNegocio());
				this.listaMunicipio.add(0, new MunicipioProxy(-1, "Selecione..."));				
				return this.listaMunicipio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.listaMunicipio = new ArrayList<MunicipioProxy>();
		this.listaMunicipio.add(0, new MunicipioProxy(-1, "Selecione..."));
		return this.listaMunicipio;
	}

	public List<LocalidadeProxy> getListaLocalidade() {
		if (this.registro.getCodigoMunicipio() != null) {
			try {
				this.listaLocalidade =  fachadaProxy.getListaLocalidade(this.registro.getCodigoRegional(), 
																	    this.registro.getCodigoUnidadeNegocio(),
																	    this.registro.getCodigoMunicipio());
				this.listaLocalidade.add(0, new LocalidadeProxy(-1, "Selecione..."));
				return this.listaLocalidade;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		this.listaLocalidade = new ArrayList<LocalidadeProxy>();
		this.listaLocalidade.add(0, new LocalidadeProxy(-1, "Selecione..."));
		return this.listaLocalidade;
	}
	
	public Integer getTipoExportacao() {
		return tipoExportacao;
	}

	public void setTipoExportacao(Integer tipoExportacao) {
		this.tipoExportacao = tipoExportacao;
	}
	
    public Integer getTipoAgrupamento() {
		return tipoAgrupamento;
	}

	public void setTipoAgrupamento(Integer tipoAgrupamento) {
		this.tipoAgrupamento = tipoAgrupamento;
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
}

