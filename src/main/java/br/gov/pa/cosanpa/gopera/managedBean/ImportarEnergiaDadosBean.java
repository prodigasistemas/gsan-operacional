package br.gov.pa.cosanpa.gopera.managedBean;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.model.operacao.EnergiaEletrica;
import br.gov.model.operacao.EnergiaEletricaDados;
import br.gov.pa.cosanpa.gopera.enums.EstadoManageBeanEnum;
import br.gov.servicos.operacao.EnergiaEletricaDadosRepositorio;
import br.gov.servicos.operacao.EnergiaEletricaRepositorio;

@ManagedBean
@SessionScoped
public class ImportarEnergiaDadosBean extends BaseBean<EnergiaEletricaDados>{
	
	@EJB
	private EnergiaEletricaDadosRepositorio fachada;
	
	@EJB
	private EnergiaEletricaRepositorio fachadaEnergia;	
	
	private EnergiaEletrica energia;
    private EnergiaEletricaDados energiaDados;
    private boolean estadoEdicao = false;
    private LazyDataModel<EnergiaEletricaDados> listaDados = null;
    
	private String uc;
	private String C_Kwh_Cv;
	private String C_Kwh_FS;
	private String C_Kwh_FU;
	private String C_Kwh_PS;
	private String C_Kwh_PU;
	private String DCv;
	private String DFS;
	private String DPS;
	private String DFU;
	private String DPU;
	private String Dem_Fat_Cv;
	private String Dem_Fat_FP;
	private String Dem_Fat_Pt;
	private String Dem_Med_Cv;
	private String Dem_Med_FP;
	private String Dem_Med_Pt;
	private String Dem_Ut_Cv;
	private String Dem_Ut_FP;
	private String Dem_Ut_Pt;
	private String Fcarga;
	private String Fpot_FP;
	private String Fpot_Cv;
	private String Fpot_Pt;
	private String vlr_Ult_Pt;
    private String vlr_Ult_FP;
	private String vlr_dem_Cv;
	private String vlr_dem_FP;
	private String vlr_dem_Pt;
	private String vlr_Ult_Cv;
	private String vlr_Multas;
	private String vlr_Kwh_FS;
	private String vlr_Kwh_FU;
	private String vlr_Kwh_Cv;
	private String vlr_Kwh_PS;
	private String vlr_Kwh_PU;
	private String vlr_ICMS;
	private String vlr_DRe_Cv;
	private String vlr_DRe_Pt;
	private String vlr_DRe_FP;
	private String vlr_ERe_FP;
	private String vlr_ERe_Cv;
	private String vlr_ERe_Pt;
	private String vlr_Total;
	private String referenciaMulta;
	
	public LazyDataModel<EnergiaEletricaDados> getListaDados() {
		return listaDados;
	}

	public boolean getEstadoEdicao() {
		return estadoEdicao;
	}

	public String getUc() {
		return uc;
	}

	public void setUc(String uc) {
		this.uc = uc;
	}

	public String getC_Kwh_Cv() {
		return C_Kwh_Cv;
	}

	public void setC_Kwh_Cv(String c_Kwh_Cv) {
		C_Kwh_Cv = c_Kwh_Cv;
	}

	public String getC_Kwh_FS() {
		return C_Kwh_FS;
	}

	public void setC_Kwh_FS(String c_Kwh_FS) {
		C_Kwh_FS = c_Kwh_FS;
	}

	public String getC_Kwh_FU() {
		return C_Kwh_FU;
	}

	public void setC_Kwh_FU(String c_Kwh_FU) {
		C_Kwh_FU = c_Kwh_FU;
	}

	public String getC_Kwh_PS() {
		return C_Kwh_PS;
	}

	public void setC_Kwh_PS(String c_Kwh_PS) {
		C_Kwh_PS = c_Kwh_PS;
	}

	public String getC_Kwh_PU() {
		return C_Kwh_PU;
	}

	public void setC_Kwh_PU(String c_Kwh_PU) {
		C_Kwh_PU = c_Kwh_PU;
	}

	public String getDCv() {
		return DCv;
	}

	public void setDCv(String dCv) {
		DCv = dCv;
	}

	public String getDFS() {
		return DFS;
	}

	public void setDFS(String dFS) {
		DFS = dFS;
	}

	public String getDPS() {
		return DPS;
	}

	public void setDPS(String dPS) {
		DPS = dPS;
	}

	public String getDFU() {
		return DFU;
	}

	public void setDFU(String dFU) {
		DFU = dFU;
	}

	public String getDPU() {
		return DPU;
	}

	public void setDPU(String dPU) {
		DPU = dPU;
	}

	public String getDem_Fat_Cv() {
		return Dem_Fat_Cv;
	}

	public void setDem_Fat_Cv(String dem_Fat_Cv) {
		Dem_Fat_Cv = dem_Fat_Cv;
	}

	public String getDem_Fat_FP() {
		return Dem_Fat_FP;
	}

	public void setDem_Fat_FP(String dem_Fat_FP) {
		Dem_Fat_FP = dem_Fat_FP;
	}

	public String getDem_Fat_Pt() {
		return Dem_Fat_Pt;
	}

	public void setDem_Fat_Pt(String dem_Fat_Pt) {
		Dem_Fat_Pt = dem_Fat_Pt;
	}

	public String getDem_Med_Cv() {
		return Dem_Med_Cv;
	}

	public void setDem_Med_Cv(String dem_Med_Cv) {
		Dem_Med_Cv = dem_Med_Cv;
	}

	public String getDem_Med_FP() {
		return Dem_Med_FP;
	}

	public void setDem_Med_FP(String dem_Med_FP) {
		Dem_Med_FP = dem_Med_FP;
	}

	public String getDem_Med_Pt() {
		return Dem_Med_Pt;
	}

	public void setDem_Med_Pt(String dem_Med_Pt) {
		Dem_Med_Pt = dem_Med_Pt;
	}

	public String getDem_Ut_Cv() {
		return Dem_Ut_Cv;
	}

	public void setDem_Ut_Cv(String dem_Ut_Cv) {
		Dem_Ut_Cv = dem_Ut_Cv;
	}

	public String getDem_Ut_FP() {
		return Dem_Ut_FP;
	}

	public void setDem_Ut_FP(String dem_Ut_FP) {
		Dem_Ut_FP = dem_Ut_FP;
	}

	public String getDem_Ut_Pt() {
		return Dem_Ut_Pt;
	}

	public void setDem_Ut_Pt(String dem_Ut_Pt) {
		Dem_Ut_Pt = dem_Ut_Pt;
	}

	public String getFcarga() {
		return Fcarga;
	}

	public void setFcarga(String fcarga) {
		Fcarga = fcarga;
	}

	public String getFpot_FP() {
		return Fpot_FP;
	}

	public void setFpot_FP(String fpot_FP) {
		Fpot_FP = fpot_FP;
	}

	public String getFpot_Cv() {
		return Fpot_Cv;
	}

	public void setFpot_Cv(String fpot_Cv) {
		Fpot_Cv = fpot_Cv;
	}

	public String getFpot_Pt() {
		return Fpot_Pt;
	}

	public void setFpot_Pt(String fpot_Pt) {
		Fpot_Pt = fpot_Pt;
	}

	public String getVlr_Ult_Pt() {
		return vlr_Ult_Pt;
	}

	public void setVlr_Ult_Pt(String vlr_Ult_Pt) {
		this.vlr_Ult_Pt = vlr_Ult_Pt;
	}

	public String getVlr_Ult_FP() {
		return vlr_Ult_FP;
	}

	public void setVlr_Ult_FP(String vlr_Ult_FP) {
		this.vlr_Ult_FP = vlr_Ult_FP;
	}

	public String getVlr_dem_Cv() {
		return vlr_dem_Cv;
	}

	public void setVlr_dem_Cv(String vlr_dem_Cv) {
		this.vlr_dem_Cv = vlr_dem_Cv;
	}

	public String getVlr_dem_FP() {
		return vlr_dem_FP;
	}

	public void setVlr_dem_FP(String vlr_dem_FP) {
		this.vlr_dem_FP = vlr_dem_FP;
	}

	public String getVlr_dem_Pt() {
		return vlr_dem_Pt;
	}

	public void setVlr_dem_Pt(String vlr_dem_Pt) {
		this.vlr_dem_Pt = vlr_dem_Pt;
	}

	public String getVlr_Ult_Cv() {
		return vlr_Ult_Cv;
	}

	public void setVlr_Ult_Cv(String vlr_Ult_Cv) {
		this.vlr_Ult_Cv = vlr_Ult_Cv;
	}

	public String getVlr_Multas() {
		return vlr_Multas;
	}

	public void setVlr_Multas(String vlr_Multas) {
		this.vlr_Multas = vlr_Multas;
	}

	public String getVlr_Kwh_FS() {
		return vlr_Kwh_FS;
	}

	public void setVlr_Kwh_FS(String vlr_Kwh_FS) {
		this.vlr_Kwh_FS = vlr_Kwh_FS;
	}

	public String getVlr_Kwh_FU() {
		return vlr_Kwh_FU;
	}

	public void setVlr_Kwh_FU(String vlr_Kwh_FU) {
		this.vlr_Kwh_FU = vlr_Kwh_FU;
	}

	public String getVlr_Kwh_Cv() {
		return vlr_Kwh_Cv;
	}

	public void setVlr_Kwh_Cv(String vlr_Kwh_Cv) {
		this.vlr_Kwh_Cv = vlr_Kwh_Cv;
	}

	public String getVlr_Kwh_PS() {
		return vlr_Kwh_PS;
	}

	public void setVlr_Kwh_PS(String vlr_Kwh_PS) {
		this.vlr_Kwh_PS = vlr_Kwh_PS;
	}

	public String getVlr_Kwh_PU() {
		return vlr_Kwh_PU;
	}

	public void setVlr_Kwh_PU(String vlr_Kwh_PU) {
		this.vlr_Kwh_PU = vlr_Kwh_PU;
	}

	public String getVlr_ICMS() {
		return vlr_ICMS;
	}

	public void setVlr_ICMS(String vlr_ICMS) {
		this.vlr_ICMS = vlr_ICMS;
	}

	public String getVlr_DRe_Cv() {
		return vlr_DRe_Cv;
	}

	public void setVlr_DRe_Cv(String vlr_DRe_Cv) {
		this.vlr_DRe_Cv = vlr_DRe_Cv;
	}

	public String getVlr_DRe_Pt() {
		return vlr_DRe_Pt;
	}

	public void setVlr_DRe_Pt(String vlr_DRe_Pt) {
		this.vlr_DRe_Pt = vlr_DRe_Pt;
	}

	public String getVlr_DRe_FP() {
		return vlr_DRe_FP;
	}

	public void setVlr_DRe_FP(String vlr_DRe_FP) {
		this.vlr_DRe_FP = vlr_DRe_FP;
	}

	public String getVlr_ERe_FP() {
		return vlr_ERe_FP;
	}

	public void setVlr_ERe_FP(String vlr_ERe_FP) {
		this.vlr_ERe_FP = vlr_ERe_FP;
	}

	public String getVlr_ERe_Cv() {
		return vlr_ERe_Cv;
	}

	public void setVlr_ERe_Cv(String vlr_ERe_Cv) {
		this.vlr_ERe_Cv = vlr_ERe_Cv;
	}

	public String getVlr_ERe_Pt() {
		return vlr_ERe_Pt;
	}

	public void setVlr_ERe_Pt(String vlr_ERe_Pt) {
		this.vlr_ERe_Pt = vlr_ERe_Pt;
	}

	public String getReferenciaMulta() {
		return referenciaMulta;
	}

	public void setReferenciaMulta(String referenciaMulta) {
		this.referenciaMulta = referenciaMulta;
	}

	public String getVlr_Total() {
		return vlr_Total;
	}

	public void setVlr_Total(String vlr_Total) {
		this.vlr_Total = vlr_Total;
	}

	public ImportarEnergiaDadosBean() {
		energiaDados = new EnergiaEletricaDados();
	}

	public EnergiaEletrica getEnergia() {
		return energia;
	}

	public void setEnergia(EnergiaEletrica energia) {
		this.energia = energia;
	}

	public EnergiaEletricaDados getEnergiaDados() {
		return energiaDados;
	}

	public void setEnergiaDados(EnergiaEletricaDados energiaDados) {
		this.energiaDados = energiaDados;
	}
	
	public String iniciar() {
		// Fachada do EJB
		this.setFachada(this.fachada);
		iniciarLazy();
		this.visualizar();
		// Cria uma nova instância do registro para um novo cadastro
		this.registro = new EnergiaEletricaDados();
		// Páginas de mudança de estados
		this.getPaginasRetorno().put("iniciar", "ImportarEnergiaDados.jsf");
		// Página inicial do managedBean
		return this.getPaginasRetorno().get("iniciar");	
	}
	
	private void iniciarLazy(){
		if (listaDados == null) {  
			listaDados = new LazyDataModel<EnergiaEletricaDados>() {
				private static final long serialVersionUID = 1L;

				
				public List<EnergiaEletricaDados> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
					try {					
						List<EnergiaEletricaDados> listaLazy = fachada.obterListaLazy(startingAt, maxPerPage, filters, energia.getCodigo());
						listaLazy = fachada.calculaDados(listaLazy);
						setRowCount(fachada.obterQtdRegistros(filters, energia.getCodigo()));
						setPageSize(maxPerPage);						
						return listaLazy;						
					} catch (Exception e) {
						e.printStackTrace();
					}	
					return null;
				}
				
				
				public Object getRowKey(EnergiaEletricaDados dados) {
					return dados.getCodigo();
				}
				
				
				public EnergiaEletricaDados getRowData(String dadosId) {
					if (dadosId != null && !dadosId.equals("") && !dadosId.equals("null")) {
						Integer id = Integer.valueOf(dadosId);
						
						for (EnergiaEletricaDados dados : listaDados) {
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
		super.novo();
		estadoEdicao = true;
		return "ImportarEnergiaDados_Consulta.jsf";		
	}
	
	
	public String consultar() {
		this.setFachada(this.fachada);
		iniciarLazy();
		carregar();
		super.visualizar();
		estadoEdicao = false;
		return "ImportarEnergia_Consulta.jsf";
	}	

	
	public String alterar() {
		this.setFachada(this.fachada);
		iniciarLazy();
		carregar();
		super.visualizar();
		estadoEdicao = true;
		return "ImportarEnergia_Consulta.jsf";		
	}
	
	public void consultarDados(){
		super.consultar();
		carregarDados();
	}

	public void alterarDados(){
		super.alterar();
		carregarDados();
	}

	public void carregar() {
		try {		
			energia = fachadaEnergia.obterEnergia(this.energia.getCodigo());
		} catch (Exception e) {
			this.mostrarMensagemErro("Erro ao Carregar Dados do Arquivo");
			e.printStackTrace();
		}
	}
	
	public String voltar(){
		if (estadoEdicao) {
			this.alterar();
		}
		return super.voltar();
	}
	
    public String confirmar() {
		try {
//			registro.setUsuario(UsuarioBean.getInstancia().getUsuarioProxy());
//			registro.setUltimaAlteracao(new Date());
			this.registro = energiaDados;
			this.registro.setEnergiaEletrica(energia);
			
			this.registro.setCodigoUC(Integer.parseInt(uc));
			this.registro.setC_Kwh_Cv(Double.parseDouble(C_Kwh_Cv.replace(".", "").replace(",", ".")));
			this.registro.setC_Kwh_FS(Double.parseDouble(C_Kwh_FS.replace(".", "").replace(",", ".")));
			this.registro.setC_Kwh_FU(Double.parseDouble(C_Kwh_FU.replace(".", "").replace(",", ".")));
			this.registro.setC_Kwh_PS(Double.parseDouble(C_Kwh_PS.replace(".", "").replace(",", ".")));
			this.registro.setC_Kwh_PU(Double.parseDouble(C_Kwh_PU.replace(".", "").replace(",", ".")));
			this.registro.setDCv(Double.parseDouble(DCv.replace(".", "").replace(",", ".")));
			this.registro.setDFS(Double.parseDouble(DFS.replace(".", "").replace(",", ".")));
			this.registro.setDPS(Double.parseDouble(DPS.replace(".", "").replace(",", ".")));
			this.registro.setDFU(Double.parseDouble(DFU.replace(".", "").replace(",", ".")));
			this.registro.setDPU(Double.parseDouble(DPU.replace(".", "").replace(",", ".")));
			this.registro.setDem_Fat_Cv(Double.parseDouble(Dem_Fat_Cv.replace(".", "").replace(",", ".")));
			this.registro.setDem_Fat_FP(Double.parseDouble(Dem_Fat_FP.replace(".", "").replace(",", ".")));
			this.registro.setDem_Fat_Pt(Double.parseDouble(Dem_Fat_Pt.replace(".", "").replace(",", ".")));
			this.registro.setDem_Med_Cv(Double.parseDouble(Dem_Med_Cv.replace(".", "").replace(",", ".")));
			this.registro.setDem_Med_FP(Double.parseDouble(Dem_Med_FP.replace(".", "").replace(",", ".")));
			this.registro.setDem_Med_Pt(Double.parseDouble(Dem_Med_Pt.replace(".", "").replace(",", ".")));
			this.registro.setDem_Ut_Cv(Double.parseDouble(Dem_Ut_Cv.replace(".", "").replace(",", ".")));
			this.registro.setDem_Ut_FP(Double.parseDouble(Dem_Ut_FP.replace(".", "").replace(",", ".")));
			this.registro.setDem_Ut_Pt(Double.parseDouble(Dem_Ut_Pt.replace(".", "").replace(",", ".")));
			this.registro.setFcarga(Double.parseDouble(Fcarga.replace(".", "").replace(",", ".")));
			this.registro.setFpot_FP(Double.parseDouble(Fpot_FP.replace(".", "").replace(",", ".")));
			this.registro.setFpot_Cv(Double.parseDouble(Fpot_Cv.replace(".", "").replace(",", ".")));
			this.registro.setFpot_Pt(Double.parseDouble(Fpot_Pt.replace(".", "").replace(",", ".")));
			this.registro.setVlr_Ult_Pt(Double.parseDouble(vlr_Ult_Pt.replace(".", "").replace(",", ".")));
		    this.registro.setVlr_Ult_FP(Double.parseDouble(vlr_Ult_FP.replace(".", "").replace(",", ".")));
			this.registro.setVlr_dem_Cv(Double.parseDouble(vlr_dem_Cv.replace(".", "").replace(",", ".")));
			this.registro.setVlr_dem_FP(Double.parseDouble(vlr_dem_FP.replace(".", "").replace(",", ".")));
			this.registro.setVlr_dem_Pt(Double.parseDouble(vlr_dem_Pt.replace(".", "").replace(",", ".")));
			this.registro.setVlr_Ult_Cv(Double.parseDouble(vlr_Ult_Cv.replace(".", "").replace(",", ".")));
			this.registro.setVlr_Multas(Double.parseDouble(vlr_Multas.replace(".", "").replace(",", ".")));
			this.registro.setVlr_Kwh_FS(Double.parseDouble(vlr_Kwh_FS.replace(".", "").replace(",", ".")));
			this.registro.setVlr_Kwh_FU(Double.parseDouble(vlr_Kwh_FU.replace(".", "").replace(",", ".")));
			this.registro.setVlr_Kwh_Cv(Double.parseDouble(vlr_Kwh_Cv.replace(".", "").replace(",", ".")));
			this.registro.setVlr_Kwh_PS(Double.parseDouble(vlr_Kwh_PS.replace(".", "").replace(",", ".")));
			this.registro.setVlr_Kwh_PU(Double.parseDouble(vlr_Kwh_PU.replace(".", "").replace(",", ".")));
			this.registro.setVlr_ICMS(Double.parseDouble(vlr_ICMS.replace(".", "").replace(",", ".")));
			this.registro.setVlr_DRe_Cv(Double.parseDouble(vlr_DRe_Cv.replace(".", "").replace(",", ".")));
			this.registro.setVlr_DRe_Pt(Double.parseDouble(vlr_DRe_Pt.replace(".", "").replace(",", ".")));
			this.registro.setVlr_DRe_FP(Double.parseDouble(vlr_DRe_FP.replace(".", "").replace(",", ".")));
			this.registro.setVlr_ERe_FP(Double.parseDouble(vlr_ERe_FP.replace(".", "").replace(",", ".")));
			this.registro.setVlr_ERe_Cv(Double.parseDouble(vlr_ERe_Cv.replace(".", "").replace(",", ".")));
			this.registro.setVlr_ERe_Pt(Double.parseDouble(vlr_ERe_Pt.replace(".", "").replace(",", ".")));
			this.registro.setVlr_Total(Double.parseDouble(vlr_Total.replace(".", "").replace(",", ".")));
			this.registro.setReferenciaMulta(referenciaMulta);
			
			if (this.getEstadoAnterior().equals(EstadoManageBeanEnum.CADASTRANDO)) {
				fachada.salvar(registro);
			} else {
				fachada.atualizar(registro);
			}
			//this.visualizar();
			super.consultar();
			mostrarMensagemSucesso("Operação realizada com sucesso!");
			setDesabilitaForm(true);
			return "ImportarEnergiaDados_Consulta.jsf";			
		} catch (Exception e) {
			e.printStackTrace();
			this.mostrarMensagemErro("Erro ao Salvar");
		}
		return null;
    }
	
	private void carregarDados(){
        DecimalFormat df = new DecimalFormat("#,##0.00");
		uc = this.energiaDados.getUnidadeConsumidora().toString();
		C_Kwh_Cv = df.format(this.energiaDados.getC_Kwh_Cv());
		C_Kwh_FS = df.format(this.energiaDados.getC_Kwh_FS());
		C_Kwh_FU = df.format(this.energiaDados.getC_Kwh_FU());
		C_Kwh_PS = df.format(this.energiaDados.getC_Kwh_PS());
		C_Kwh_PU = df.format(this.energiaDados.getC_Kwh_PU()); 
		DCv = df.format(this.energiaDados.getDCv());
		DFS = df.format(this.energiaDados.getDFS());
		DPS = df.format(this.energiaDados.getDPS());
		DFU = df.format(this.energiaDados.getDFU());
		DPU = df.format(this.energiaDados.getDPU());
		Dem_Fat_Cv = df.format(this.energiaDados.getDem_Fat_Cv());
		Dem_Fat_FP = df.format(this.energiaDados.getDem_Fat_FP());
		Dem_Fat_Pt = df.format(this.energiaDados.getDem_Fat_Pt());
		Dem_Med_Cv = df.format(this.energiaDados.getDem_Med_Cv());
		Dem_Med_FP = df.format(this.energiaDados.getDem_Med_FP());
		Dem_Med_Pt = df.format(this.energiaDados.getDem_Med_Pt());
		Dem_Ut_Cv = df.format(this.energiaDados.getDem_Ut_Cv());
		Dem_Ut_FP = df.format(this.energiaDados.getDem_Ut_FP());
		Dem_Ut_Pt = df.format(this.energiaDados.getDem_Ut_Pt());
		Fcarga = df.format(this.energiaDados.getFcarga()); 
		Fpot_FP = df.format(this.energiaDados.getFpot_FP()); 
		Fpot_Cv = df.format(this.energiaDados.getFpot_Cv()); 
		Fpot_Pt = df.format(this.energiaDados.getFpot_Pt());
		vlr_Ult_Pt = df.format(this.energiaDados.getVlr_Ult_Pt());
	    vlr_Ult_FP = df.format(this.energiaDados.getVlr_Ult_FP());
		vlr_dem_Cv = df.format(this.energiaDados.getVlr_dem_Cv());
		vlr_dem_FP = df.format(this.energiaDados.getVlr_dem_FP());
		vlr_dem_Pt = df.format(this.energiaDados.getVlr_dem_Pt());
		vlr_Ult_Cv = df.format(this.energiaDados.getVlr_Ult_Cv());
		vlr_Multas = df.format(this.energiaDados.getVlr_Multas());
		vlr_Kwh_FS = df.format(this.energiaDados.getVlr_Kwh_FS());
		vlr_Kwh_FU = df.format(this.energiaDados.getVlr_Kwh_FU());
		vlr_Kwh_Cv = df.format(this.energiaDados.getVlr_Kwh_Cv());
		vlr_Kwh_PS = df.format(this.energiaDados.getVlr_Kwh_PS());
		vlr_Kwh_PU = df.format(this.energiaDados.getVlr_Kwh_PU());
		vlr_ICMS = df.format(this.energiaDados.getVlr_ICMS());
		vlr_DRe_Cv = df.format(this.energiaDados.getVlr_DRe_Cv());
		vlr_DRe_Pt = df.format(this.energiaDados.getVlr_DRe_Pt());
		vlr_DRe_FP = df.format(this.energiaDados.getVlr_DRe_FP());
		vlr_ERe_FP = df.format(this.energiaDados.getVlr_ERe_FP());
		vlr_ERe_Cv = df.format(this.energiaDados.getVlr_ERe_Cv());
		vlr_ERe_Pt = df.format(this.energiaDados.getVlr_ERe_Pt());
		vlr_Total = df.format(this.energiaDados.getVlr_Total());
		referenciaMulta = this.energiaDados.getReferenciaMulta();
	}
}
