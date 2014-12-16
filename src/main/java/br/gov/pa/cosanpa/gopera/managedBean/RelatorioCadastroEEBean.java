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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRResultSetDataSource;

import org.jboss.logging.Logger;

import br.gov.model.operacao.LocalidadeProxy;
import br.gov.model.operacao.MunicipioProxy;
import br.gov.model.operacao.RelatorioGerencial;
import br.gov.model.operacao.TipoRelatorioCadastroEnergia;
import br.gov.model.operacao.UnidadeNegocioProxy;
import br.gov.pa.cosanpa.gopera.util.WebBundle;
import br.gov.servicos.operacao.ProxyOperacionalRepositorio;

@ManagedBean
@ViewScoped
public class RelatorioCadastroEEBean extends BaseRelatorioBean<RelatorioGerencial> {
	private static Logger logger = Logger.getLogger(RelatorioCadastroEEBean.class);

	@Resource(lookup="java:/jboss/datasources/GsanDS")
	private DataSource	dataSource;

	@EJB
	private ProxyOperacionalRepositorio fachadaProxy;
	
	private List<UnidadeNegocioProxy> listaUnidadeNegocio = new ArrayList<UnidadeNegocioProxy>();
	private List<MunicipioProxy> listaMunicipio = new ArrayList<MunicipioProxy>();
	private List<LocalidadeProxy> listaLocalidade = new ArrayList<LocalidadeProxy>();
	
	private TipoRelatorioCadastroEnergia tipoRelatorio;
	private UnidadeNegocioProxy unidadeNegocioProxy = new UnidadeNegocioProxy();
	private MunicipioProxy municipioProxy = new MunicipioProxy();
	private LocalidadeProxy localidadeProxy = new LocalidadeProxy(); 
	
	
	@PostConstruct
	public void init(){
	    super.iniciar();
	    
		if (bundle == null){
			bundle = new WebBundle();
		}
		
		tipoExportacao = 1;	
	}	

	public void exibir() throws SQLException{
    	String sql = "", sqlFiltro = "";
    	Connection con = null; 
    	try{
    		con =  dataSource.getConnection();
    		Statement stm = con.createStatement();

    		//VERIFICA FILTRO
    		StringBuilder filtroRelatorio = new StringBuilder();
			if (unidadeNegocioProxy != null && unidadeNegocioProxy.getCodigo() != null) {
				sqlFiltro = sqlFiltro + " AND A.uneg_id = " + unidadeNegocioProxy.getCodigo();
	    		filtroRelatorio.append(bundle.getText("unid_negocio"))
	    		.append(": ")
	    		.append(fachadaProxy.getUnidadeNegocio(this.unidadeNegocioProxy.getCodigo()).getNome());
			}
			if (municipioProxy != null && municipioProxy.getCodigo() != null) {
				sqlFiltro = sqlFiltro + " AND A.muni_id = " + municipioProxy.getCodigo();
	    		filtroRelatorio.append("  ")
	    		.append(bundle.getText("municipio"))
	    		.append(": ")
	    		.append(fachadaProxy.getMunicipio(this.municipioProxy.getCodigo()).getNome());
	    		
			}
			if (localidadeProxy != null && localidadeProxy.getCodigo() != null) {
				sqlFiltro = sqlFiltro + " AND A.loca_id = " + localidadeProxy.getCodigo();
	    		filtroRelatorio.append("  ")
	    		.append(bundle.getText("localidade"))
	    		.append(": ")
	    		.append(fachadaProxy.getLocalidade(this.localidadeProxy.getCodigo()).getNome());
			} 
    		
			switch (tipoRelatorio) {
			case UNIDADE_CONSUMIDORA:
				nomeRelatorio = bundle.getText("rel_cad_unid_consumidora");
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
			case CONTRATO_ENERGIA:
				nomeRelatorio = bundle.getText("rel_cad_contrato_energia");
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
			reportDataSource = new JRResultSetDataSource(rs);
    		
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("filtro", filtroRelatorio.toString());
			parametros.put("REPORT_CONNECTION", con);
			parametros.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/") + "/");
			parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
			
			geraRelatorio(parametros);
		}
		catch (Exception e){
			logger.error(bundle.getText("erro_gerar_relatorio"), e);
			mostrarMensagemErro(bundle.getText("erro_gerar_relatorio"));
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
	

	public TipoRelatorioCadastroEnergia getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(TipoRelatorioCadastroEnergia tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public Integer getTipoExportacao() {
		return tipoExportacao;
	}

	public void setTipoExportacao(Integer tipoExportacao) {
		this.tipoExportacao = tipoExportacao;
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
	
	public TipoRelatorioCadastroEnergia[] getTiposRelatorios(){
	    return TipoRelatorioCadastroEnergia.values();
	}
}