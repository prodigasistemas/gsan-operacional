package br.gov.pa.cosanpa.gopera.managedBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRResultSetDataSource;

import org.jboss.logging.Logger;

import br.gov.model.operacao.RelatorioGerencial;

@ManagedBean
@SessionScoped
public class RelatorioCadastroVABean extends BaseRelatorioBean<RelatorioGerencial> {
	private static Logger logger = Logger.getLogger(RelatorioCadastroVABean.class);

	@Resource(lookup="java:/jboss/datasources/GsanDS")
	private DataSource	dataSource;
	
	private Integer tipoRelatorio;
	private List<SelectItem> listaRelatorio = new ArrayList<SelectItem>();
	
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

	
	public String iniciar(){
		//VOLUME DE ÁGUA	
		tipoRelatorio = 1;
		tipoExportacao = 1;		
		listaRelatorio.clear();
		SelectItem tipoRel = new SelectItem();  
		tipoRel.setValue(1);  
		tipoRel.setLabel("Macro-Medidor");  
		listaRelatorio.add(tipoRel);
		return "RelatorioCadastroVA.jsf";
	}	

    public void exibir() throws SQLException{
    	StringBuilder sql = new StringBuilder();
    	Connection con = null; 
    	try{
    		con =  dataSource.getConnection();
    		Statement stm = con.createStatement();

			switch (tipoRelatorio) {
			case 1: //Macro-Medidor
				nomeRelatorio = bundle.getText("rel_cad_macromedidor");
				nomeArquivo = "cadastroMacroMedidor";
				reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/reports/" + nomeArquivo + ".jasper");
				sql.append("SELECT mmed_id, CASE mmed_tipo WHEN 1 THEN ")
					.append("'").append(bundle.getText("portatil")).append("'")
					.append("  WHEN 2 THEN ")
					.append("'").append(bundle.getText("fixo")).append("'")
					.append("  WHEN 3 THEN ")
					.append("'").append(bundle.getText("virtual")).append("'")
					.append(" END AS mmed_tipo, ")
					.append(" mmed_idleitura, CASE mmed_tipomedicao WHEN 1 THEN ")
					.append("'").append(bundle.getText("vazao")).append("'")
					.append("  WHEN 2 THEN ")
					.append("'").append(bundle.getText("pressao")).append("'")
					.append("  WHEN 3 THEN ")
					.append("'").append(bundle.getText("nivel")).append("'")
					.append(" END AS mmed_tipomedicao,")
					.append(" mmed_principio, mmed_tiposensor, mmed_fabricante, mmed_modelo, mmed_numeroserie, mmed_tombamento, mmed_range, mmed_grauprotecao,")
					.append(" mmed_alimentacao, mmed_sinalsaida, mmed_protocolo")
					.append("  FROM operacao.macro_medidor")
					.append(" ORDER BY mmed_idleitura");
				break;
			}
			//Monta ResultSet	
			ResultSet rs = stm.executeQuery(sql.toString());
			reportDataSource = new JRResultSetDataSource(rs);
			
			geraRelatorio(null);
		}
		catch (Exception e){
			logger.error(bundle.getText("erro_gerar_relatorio"), e);
			mostrarMensagemErro(bundle.getText("erro_gerar_relatorio"));
		}
		finally {
			con.close();
		}    	
    }
}

