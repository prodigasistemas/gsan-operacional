package br.gov.pa.cosanpa.gopera.managedBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import br.gov.pa.cosanpa.gopera.fachada.IProxy;
import br.gov.pa.cosanpa.gopera.model.LocalidadeProxy;
import br.gov.pa.cosanpa.gopera.model.MunicipioProxy;
import br.gov.pa.cosanpa.gopera.model.RelatorioGerencial;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;
import br.gov.pa.cosanpa.gopera.util.WebBundle;

@ManagedBean
@SessionScoped
public class RelatorioCadastroEEBean extends BaseRelatorioBean<RelatorioGerencial> {

	@Resource(lookup="java:/gopera")
	private DataSource	dataSource;

	@EJB
	private IProxy fachadaProxy;
	
	private List<UnidadeNegocioProxy> listaUnidadeNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> listaMunicipio = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> listaLocalidade = new ArrayList<LocalidadeProxy>();
	
	private Integer tipoRelatorio;
	private Integer tipoExportacao;
	private List<SelectItem> listaRelatorio = new ArrayList<SelectItem>();
	private String nomeRelatorio;
	private String nomeArquivo;
	private String reportPath; 
	private String filtroRelatorio;
	private UnidadeNegocioProxy unidadeNegocioProxy = new UnidadeNegocioProxy();
	private MunicipioProxy municipioProxy = new MunicipioProxy();
	private LocalidadeProxy localidadeProxy = new LocalidadeProxy(); 
	
	@Override
	public String iniciar(){
		if (bundle == null){
			bundle = new WebBundle();
		}
		
		//ENERGIA ELÉTRICA
		tipoRelatorio = 1;
		tipoExportacao = 1;	
		listaRelatorio.clear();
		SelectItem tipoRel = new SelectItem();  
		tipoRel.setValue(1);  
		tipoRel.setLabel(bundle.getText("unidade_consumidora"));  
		listaRelatorio.add(tipoRel);
		tipoRel = new SelectItem();
		tipoRel.setValue(2);  
		tipoRel.setLabel(bundle.getText("contrato_energia_eletrica"));  
		listaRelatorio.add(tipoRel);
		return "RelatorioCadastroEE.jsf";
	}	

	public void exibir() throws SQLException{
    	String sql = "", sqlFiltro = "";
    	Connection con = null; 
    	try{
    		con =  dataSource.getConnection();
    		Statement stm = con.createStatement();
    		
    		//VERIFICA FILTRO
    		filtroRelatorio = "";
			if (unidadeNegocioProxy.getCodigo() != 0) {
				sqlFiltro = sqlFiltro + " AND A.uneg_id = " + unidadeNegocioProxy.getCodigo();
	    		filtroRelatorio = filtroRelatorio + " Und Negócio: " + fachadaProxy.getUnidadeNegocio(this.unidadeNegocioProxy.getCodigo()).getNome();
			}
			if (municipioProxy.getCodigo() != 0) {
				sqlFiltro = sqlFiltro + " AND A.muni_id = " + municipioProxy.getCodigo();
	    		filtroRelatorio = filtroRelatorio + " Município: " + fachadaProxy.getMunicipio(this.municipioProxy.getCodigo()).getNome();
			}
			if (localidadeProxy.getCodigo() != 0) {
				sqlFiltro = sqlFiltro + " AND A.loca_id = " + localidadeProxy.getCodigo();
	    		filtroRelatorio = filtroRelatorio + " Localidade: " + fachadaProxy.getLocalidade(this.localidadeProxy.getCodigo()).getNome();
			} 
    		
			switch (tipoRelatorio) {
			case 1: //Unidade Consumidora
				nomeRelatorio = "Cadastro de Unidade Consumidora";
				nomeArquivo = "cadastroUnidadeConsumidora";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/" + nomeArquivo + ".jasper");
				sql = "SELECT ucon_id, ucon_uc, ucon_nmconsumidora, B.uneg_nmunidadenegocio, C.muni_nmmunicipio, D.loca_nmlocalidade,"
				    + "		  ucon_undoperacional, ucon_natureza, ucon_endereco, ucon_endereconumero, ucon_enderecocomplemento,"
				    + "		  ucon_bairro, ucon_cep, ucon_equipamento, ucon_dtinstalacao, ucon_potencia, ucon_alimentador"
				    + "  FROM operacao.unidade_consumidora A"
				    + " INNER JOIN cadastro.unidade_negocio B ON A.uneg_id = B.uneg_id"
				    + " INNER JOIN cadastro.municipio C ON A.muni_id = C.muni_id"
				    + " INNER JOIN cadastro.localidade D ON A.loca_id = D.loca_id"
				    + " WHERE 1 = 1 " + sqlFiltro
				    + " ORDER BY uneg_nmunidadenegocio, muni_nmmunicipio, loca_nmlocalidade, ucon_uc";
				break;
			case 2: //Contrato de Energia Eletrica
				nomeRelatorio = "Cadastro de Contrato de Energia Elétrica";
				nomeArquivo = "cadastroContratoEnergiaEletrica";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/" + nomeArquivo + ".jasper");
				sql = "SELECT cene_id, cene_nmcontrato, ucon_uc, ucon_nmconsumidora, cene_dataini, cene_datafim, cene_dataassinatura,"
					+ "		  cene_periodoajuste, cene_periodoteste, cene_tensaonominal, cene_tensaocontratada, cene_subgrupotarifario, cene_frequencia," 
					+ "		  cene_perdastransformacao, cene_potenciainstalada, cene_horariopontaini, cene_horariopontafim, cene_horarioreservadoini, cene_horarioreservadofim,"
					+ "		  cene_opcaofaturamento, cene_modalidadetarifaria, cene_agrupadorfatura"
				    + "  FROM operacao.contrato_energia B"
				    + " INNER JOIN operacao.unidade_consumidora A ON A.ucon_id = B.ucon_id"
				    + " WHERE 1 = 1 " + sqlFiltro
				    + " ORDER BY ucon_uc";
				break;
			}
			//Monta ResultSet	
  			ResultSet rsAux = stm.executeQuery(sql);
    		if (!rsAux.next()){
    			mostrarMensagemErro(bundle.getText("erro_nao_existe_retorno_filtro"));
    			return;
    		}
    		
			ResultSet rs = stm.executeQuery(sql);
    		
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("logoRelatorio", "logoRelatorio.jpg");
			parametros.put("nomeRelatorio", nomeRelatorio);
			parametros.put("nomeUsuario", usuarioProxy.getNome());
			parametros.put("filtro", filtroRelatorio);
			parametros.put("REPORT_CONNECTION", con);
			parametros.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/") + "/");
			parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

			HttpServletResponse httpServletResponse=(HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
			httpServletResponse.setCharacterEncoding("ISO-8859-1");
			JRResultSetDataSource jrsds = new JRResultSetDataSource(rs);
			JasperPrint jasperPrint;

			switch (tipoExportacao) {
				case 1: //PDF
					parametros.put("exibirExcel", false);
		 	        jasperPrint = JasperFillManager.fillReport(reportPath, parametros, jrsds);

					httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + nomeArquivo + ".pdf");
					httpServletResponse.setContentType("application/pdf");

					ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
				    JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
				    break;
			 
				case 2: //EXCEL
					parametros.put("exibirExcel", true);
					jasperPrint = JasperFillManager.fillReport(reportPath, parametros, jrsds);
					
					httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + nomeArquivo + ".xls");
					httpServletResponse.setContentType("application/vnd.ms-excel");
					servletOutputStream = httpServletResponse.getOutputStream();
					gerarExcel(servletOutputStream, jasperPrint);
					
				    break;
			 }	
			FacesContext.getCurrentInstance().responseComplete();  
		}
		catch (Exception e){
			e.printStackTrace();
			mostrarMensagemErro("Erro ao exibir Relatório: " + e.getMessage());
		}
		finally {
			con.close();
		}    	
    }
    
    /*************************************************
     * GETTERS AND SETTERS
     *************************************************/
	public List<UnidadeNegocioProxy> getListaUnidadeNegocio() {
		try {
				this.listaUnidadeNegocio =  fachadaProxy.getListaUnidadeNegocio(0);
				return listaUnidadeNegocio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		return listaUnidadeNegocio = new ArrayList<UnidadeNegocioProxy>();
	}

	public List<MunicipioProxy> getListaMunicipio() {
		if (this.getUnidadeNegocioProxy().getCodigo() != null) {
			try {
				this.listaMunicipio =  fachadaProxy.getListaMunicipio(0, this.getUnidadeNegocioProxy().getCodigo());
				return this.listaMunicipio;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		return this.listaMunicipio = new ArrayList<MunicipioProxy>();
	}

	public List<LocalidadeProxy> getListaLocalidade() {
		if (this.getMunicipioProxy().getCodigo() != null) {
			try {
				this.listaLocalidade =  fachadaProxy.getListaLocalidade(0, this.getUnidadeNegocioProxy().getCodigo(), this.getMunicipioProxy().getCodigo());
				return this.listaLocalidade;
			} catch (Exception e) {
				mostrarMensagemErro("Erro ao consultar sistema externo.");
			}
		}
		return this.listaLocalidade= new ArrayList<LocalidadeProxy>();
	}
	
	public Integer getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(Integer tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public Integer getTipoExportacao() {
		return tipoExportacao;
	}

	public void setTipoExportacao(Integer tipoExportacao) {
		this.tipoExportacao = tipoExportacao;
	}

	public List<SelectItem> getListaRelatorio() {
		return listaRelatorio;
	}

	public void setListaRelatorio(List<SelectItem> listaRelatorio) {
		this.listaRelatorio = listaRelatorio;
	}

	public UnidadeNegocioProxy getUnidadeNegocioProxy() {
		return unidadeNegocioProxy;
	}

	public void setUnidadeNegocioProxy(UnidadeNegocioProxy unidadeNegocioProxy) {
		this.unidadeNegocioProxy = unidadeNegocioProxy;
	}

	public MunicipioProxy getMunicipioProxy() {
		return municipioProxy;
	}

	public void setMunicipioProxy(MunicipioProxy municipioProxy) {
		this.municipioProxy = municipioProxy;
	}

	public LocalidadeProxy getLocalidadeProxy() {
		return localidadeProxy;
	}

	public void setLocalidadeProxy(LocalidadeProxy localidadeProxy) {
		this.localidadeProxy = localidadeProxy;
	}
}

