package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@SequenceGenerator(name="sequence_energiaeletrica_dados", sequenceName="sequence_energiaeletrica_dados", schema="operacao", initialValue=1, allocationSize=1)
@Table(name="energiaeletrica_dados",schema="operacao")
public class EnergiaEletricaDados  implements Serializable {
	
	private static final long serialVersionUID= 1L;
	
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_energiaeletrica_dados")
    @Column(name="enld_id", unique=true, nullable=false, precision=3, scale=0)
    private Integer codigo;
	
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="enel_id")
    private EnergiaEletrica energiaEletrica;

    @Column(name="enld_dataleitura")
    @Temporal(TemporalType.DATE) //2
    private Date dataLeitura;

    @Column(name="enld_refmulta", length=10)  
    private String referenciaMulta;
    
    @Column(name="enld_uc", precision=12, scale=0) //3
	private Integer codigoUC;

    @Column(name="enld_fatura", length=20) //4 Número da Fatura
	private String fatura;

    @Column(name="enld_nome", length=50) //5 Razão Social
	private String nome;

    @Column(name="enld_endereco", length=50) //6 Endereço de Leitura
	private String endereco;

    @Column(name="enld_bairro", length=50) //7 Bairro de Leitura
	private String bairro; 

    @Column(name="enld_cep", length=9) //8 CEP de leitura
	private String cep;

    @Column(name="enld_codgrupo", length=10) //9 Grupo tarifário: A2  A3a  A4  AS  B3 etc
	private String codigoGrupo;

    @Column(name="enld_codtipo", length=10) //10 Tipo tarifário 01-Convencional,  02-THS Azul,    04- THS Verde
	private String codigoTipo;

    @Column(name="enld_c_kwh_cv") //11 Consumo geral medido em kWh (Tarifa Convencional)
	private Double C_Kwh_Cv;

    @Column(name="enld_c_kwh_fs") //12 Consumo medido fora da ponta seca, em kWh (Tarifas Azul e Verde)
	private Double C_Kwh_FS;

    @Column(name="enld_c_kwh_fu") //13 Consumo medido fora da ponta úmida, em kW (Tarifas Azul e Verde)
	private Double C_Kwh_FU;
    
    @Column(name="enld_c_kwh_ps") //14 Consumo medido na ponta seca, em kWh (Tarifas Azul e Verde)
	private Double C_Kwh_PS;

    @Column(name="enld_c_kwh_pu") //15 Consumo medido na ponta úmida, em kWh (Tarifas Azul e Verde)
	private Double C_Kwh_PU;

    @Column(name="enld_dcv") //16 Demanda Contratada período único (Tarifa convencional)
	private Double DCv;

    @Column(name="enld_dfs") //17 Demanda Contratada fora da ponta,  em KW (SECO)
	private Double DFS;
    
    @Column(name="enld_dps") //18 Demanda Contratada na ponta, em KW (SECO)
	private Double DPS;
    
    @Column(name="enld_dfu") //19 Demanda Contratada fora da ponta,  em KW (ÚMIDO)
	private Double DFU;

    @Column(name="enld_dpu") //20 Demanda Contratada na ponta, em KW (ÚMIDO)
	private Double DPU;

    @Column(name="enld_Dem_Fat_Cv") //21 Demanda faturada  kW (Tarifas Convencional e Verde)
	private Double Dem_Fat_Cv;

    @Column(name="enld_Dem_Fat_FP") //22 Demanda faturada (fora da ponta)  kW
	private Double Dem_Fat_FP;

    @Column(name="enld_Dem_Fat_Pt") //23 Demanda faturada (ponta) kW
	private Double Dem_Fat_Pt;

    @Column(name="enld_Dem_Med_Cv") //24 Demanda medida (Tarifas Convencional e Verde)
	private Double Dem_Med_Cv;

    @Column(name="enld_Dem_Med_FP") //25 Demanda medida fora da ponta em kW   (Tarifa Azul)
	private Double Dem_Med_FP;

    @Column(name="enld_Dem_Med_Pt") //26 Demanda medida na ponta em kW  (Tarifa Azul)
	private Double Dem_Med_Pt;

    @Column(name="enld_Dem_Ut_Cv") //27 Demanda ultrapassada  (Tarifas Convencional e Verde)
	private Double Dem_Ut_Cv;
    
    @Column(name="enld_Dem_Ut_FP") //28 Demanda ultrapassada fora da ponta, em kW
	private Double Dem_Ut_FP;
    
    @Column(name="enld_Dem_Ut_Pt") //29 Demanda ultrapassada na ponta, em kW
	private Double Dem_Ut_Pt;
    
    @Column(name="enld_Fcarga")  //30 Fator de carga geral
	private Double Fcarga;
    
    @Column(name="enld_Fpot_FPa")  //31 Fator de potência fora da ponta   (Tarifa Azul)
	private Double Fpot_FP;
    
    @Column(name="enld_Fpot_Cv") //32 Fator de potência geral  (Tarifas  Convencional e Verde)
	private Double Fpot_Cv;
    
    @Column(name="enld_Fpot_Pt") //33 Fator de potência na da ponta   (Tarifa Azul)
	private Double Fpot_Pt;

    @Column(name="enld_vlr_Ult_Pt") //34 Valor da demanda de ultrapassagem (fora da ponta)  R$
	private Double vlr_Ult_Pt;
    
    @Column(name="enld_vlr_Ult_FP") //35 Valor da demanda de ultrapassagem (ponta)  R$
    private Double vlr_Ult_FP;

    @Column(name="enld_vlr_dem_Cv") //36 Valor da demanda faturada  R$ (Tarifas Convencional e Verde)
	private Double vlr_dem_Cv;

    @Column(name="enld_vlr_dem_FP") //37 Valor da demanda faturada (fora da ponta)  R$
	private Double vlr_dem_FP;

    @Column(name="enld_vlr_dem_Pt") //38 Valor da demanda faturada (ponta)  R$
	private Double vlr_dem_Pt;

    @Column(name="enld_vlr_Ult_Cv") //39 Valor da demanda ultrapassada (geral) - R$ (Tarifas Convencional e Verde)
	private Double vlr_Ult_Cv;

    @Column(name="enld_vlr_Multas") //40 Valor das Multas  R$  não relacionadas a fator de potência 
	private Double vlr_Multas;

    @Column(name="enld_vlr_Kwh_FS") //41 Valor do consumo faturado (fora da ponta seca)  R$
	private Double vlr_Kwh_FS;

    @Column(name="enld_vlr_Kwh_FU") //42 Valor do consumo faturado (fora da ponta úmida)  R$
	private Double vlr_Kwh_FU;

    @Column(name="enld_vlr_Kwh_Cv") //43 Valor do consumo faturado (geral)  R$
	private Double vlr_Kwh_Cv;

    @Column(name="enld_vlr_Kwh_PS") //44 Valor do consumo faturado (ponta seca)   R$
	private Double vlr_Kwh_PS;

    @Column(name="enld_vlr_Kwh_PU") //45 Valor do consumo faturado (ponta úmida)   R$
	private Double vlr_Kwh_PU;

    @Column(name="enld_vlr_ICMS") //46 Valor do ICMS  R$
	private Double vlr_ICMS;

    @Column(name="enld_vlr_DRe_Cv") //47 Valor faturado  de Demanda Reativa Excedente geral (tarifa convencional e verde) R$
	private Double vlr_DRe_Cv;

    @Column(name="enld_vlr_DRe_Pt") //48 Valor faturado  de  Demanda Reativa Excedente na ponta  (tarifa Azul) R$
	private Double vlr_DRe_Pt;

    @Column(name="enld_vlr_DRe_FP") //49 Valor faturado  de Demanda Reativa Excedente fora da ponta (tarifa  Azul) R$
	private Double vlr_DRe_FP;
   
    @Column(name="enld_vlr_ERe_FP") //50 Valor faturado  de Energia Reativa Excedente  fora  ponta  (tarifas Azul e Verde)  R$
	private Double vlr_ERe_FP;
    
    @Column(name="enld_vlr_ERe_Cv") //51 Valor faturado  de Energia Reativa Excedente  geral  (tarifa convencional)   R$
	private Double vlr_ERe_Cv;
    
    @Column(name="enld_vlr_ERe_Pt") //52 Valor faturado  de Energia Reativa Excedente  na ponta  (tarifas Azul e Verde)   R$
	private Double vlr_ERe_Pt;
    
    @Column(name="enld_vlr_TotalP") //53 Valor Total da conta (Valor de Emissão)  R$
	private Double vlr_Total;

    @OneToOne(optional=true)
    @JoinColumn(name="ucon_id")
    private UnidadeConsumidora unidadeConsumidora;

    @OneToOne(optional=true)
    @JoinColumn(name="cene_id")
    private ContratoEnergia contrato;
    
    @Transient
    private Double cvlr_Consumo; //Consumo Total
    
    @Transient
    private Double caj_fat_Pt; //Total Ajuste Fator Potência
    
    @Transient
    private Double cult_Demanda; //Ultra Demanda

    @Transient
    private Double cfat_pot_FP_Umida; //Fator de Potência Fora de Ponta Úmida

    @Transient
    private Double cfat_pot_Pt_Umida; //Fator de Potência na Ponta Úmida

    @Transient
    private Double cfat_pot_FP_Seca; //Fator de Potência Fora de Ponta Seca

    @Transient
    private Double cfat_pot_Pt_Seca; //Fator de Potência na Ponta Seca

    @Transient
    private Double cfat_pot_Cv; //Fator de Potência Convencional

    @Transient
    private Date cdataLeituraAnterior; //Data de Leitura Anterior
    
    @Transient
    private Long cdias_fat; //Nº Dias Faturado
	
    @Transient
    private Double ccons_KW; //Consumo KWh 30 dias

    @Transient
    private Double ccons_total; //Total R$ 30 dias

    @Transient
    private Double ctarifa_media; //Tarifa Média R$/KWh
    
    @Transient
    private Double cvariacao_consumo; //Variação de Consumo
    
    @Transient
    private Double cfat_carga; //Fator de Carga

    @Transient
    private Double cfat_carga_Pt; //Fator de Carga na Ponta

    @Transient
    private Double cfat_carga_FP; //Fator de Carga Fora da Ponta

    @Transient
    private Double ccons_FP_Umida_Ind; //Fator de Carga Fora da Ponta Umida Indutivo

    @Transient
    private Double ccons_FP_Umida_Cap; //Fator de Carga Fora da Ponta Umida Capacitivo

    @Transient
    private Double ccons_FP_Seca_Ind; //Fator de Carga Fora da Ponta Seca Indutivo

    @Transient
    private Double ccons_FP_Seca_Cap; //Fator de Carga Fora da Ponta Seca Capacitivo

    @Transient
    private Double ccons_KW_FP_Seca; //Consumo KWh Fora da Ponta Seca

    @Transient
    private Double ccons_KW_Pt_Seca; //Consumo KWh na Ponta Seca

    @Transient
    private Double ccons_KW_FP_Umida; //Consumo KWh Fora da Ponta Umida

    @Transient
    private Double ccons_KW_Pt_Umida; //Consumo KWh na Ponta Umida

    @Transient
    private Double cvlr_Consumo_FP_Seca; //Valor Faturado Consumo Fora da Ponta Seca

    @Transient
    private Double cvlr_Consumo_Pt_Seca; //Valor Faturado Consumo na Ponta Seca
    
    @Transient
    private Double cvlr_Consumo_FP_Umida; //Valor Faturado Consumo Fora da Ponta Umida

    @Transient
    private Double cvlr_Consumo_Pt_Umida; //Valor Faturado Consumo na Ponta Umida

    @Transient
    private Double ccons_KW_Cv; //Consumo KWh Convencional

    @Transient
    private Double cvlr_Consumo_Cv; //Valor Faturado Consumo Convencional

    public EnergiaEletricaDados() {
		super();
		unidadeConsumidora = new UnidadeConsumidora();
		contrato = new ContratoEnergia();
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public EnergiaEletrica getEnergiaEletrica() {
		return energiaEletrica;
	}

	public void setEnergiaEletrica(EnergiaEletrica energiaEletrica) {
		this.energiaEletrica = energiaEletrica;
	}

	public Date getDataLeitura() {
		return dataLeitura;
	}

	public void setDataLeitura(Date dataLeitura) {
		this.dataLeitura = dataLeitura;
	}

	public String getReferenciaMulta() {
		return referenciaMulta;
	}

	public void setReferenciaMulta(String referenciaMulta) {
		this.referenciaMulta = referenciaMulta;
	}

	public Integer getCodigoUC() {
		return codigoUC;
	}

	public void setCodigoUC(Integer codigoUC) {
		this.codigoUC = codigoUC;
	}

	public String getFatura() {
		return fatura;
	}

	public void setFatura(String fatura) {
		this.fatura = fatura;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCodigoGrupo() {
		return codigoGrupo;
	}

	public void setCodigoGrupo(String codigoGrupo) {
		this.codigoGrupo = codigoGrupo;
	}

	public String getCodigoTipo() {
		return codigoTipo;
	}

	public void setCodigoTipo(String codigoTipo) {
		this.codigoTipo = codigoTipo;
	}

	public Double getC_Kwh_Cv() {
		return C_Kwh_Cv;
	}

	public void setC_Kwh_Cv(Double c_Kwh_Cv) {
		C_Kwh_Cv = c_Kwh_Cv;
	}

	public Double getC_Kwh_FS() {
		return C_Kwh_FS;
	}

	public void setC_Kwh_FS(Double c_Kwh_FS) {
		C_Kwh_FS = c_Kwh_FS;
	}

	public Double getC_Kwh_FU() {
		return C_Kwh_FU;
	}

	public void setC_Kwh_FU(Double c_Kwh_FU) {
		C_Kwh_FU = c_Kwh_FU;
	}

	public Double getC_Kwh_PS() {
		return C_Kwh_PS;
	}

	public void setC_Kwh_PS(Double c_Kwh_PS) {
		C_Kwh_PS = c_Kwh_PS;
	}

	public Double getC_Kwh_PU() {
		return C_Kwh_PU;
	}

	public void setC_Kwh_PU(Double c_Kwh_PU) {
		C_Kwh_PU = c_Kwh_PU;
	}

	public Double getDCv() {
		return DCv;
	}

	public void setDCv(Double dCv) {
		DCv = dCv;
	}

	public Double getDFS() {
		return DFS;
	}

	public void setDFS(Double dFS) {
		DFS = dFS;
	}

	public Double getDPS() {
		return DPS;
	}

	public void setDPS(Double dPS) {
		DPS = dPS;
	}

	public Double getDFU() {
		return DFU;
	}

	public void setDFU(Double dFU) {
		DFU = dFU;
	}

	public Double getDPU() {
		return DPU;
	}

	public void setDPU(Double dPU) {
		DPU = dPU;
	}

	public Double getDem_Fat_Cv() {
		return Dem_Fat_Cv;
	}

	public void setDem_Fat_Cv(Double dem_Fat_Cv) {
		Dem_Fat_Cv = dem_Fat_Cv;
	}

	public Double getDem_Fat_FP() {
		return Dem_Fat_FP;
	}

	public void setDem_Fat_FP(Double dem_Fat_FP) {
		Dem_Fat_FP = dem_Fat_FP;
	}

	public Double getDem_Fat_Pt() {
		return Dem_Fat_Pt;
	}

	public void setDem_Fat_Pt(Double dem_Fat_Pt) {
		Dem_Fat_Pt = dem_Fat_Pt;
	}

	public Double getDem_Med_Cv() {
		return Dem_Med_Cv;
	}

	public void setDem_Med_Cv(Double dem_Med_Cv) {
		Dem_Med_Cv = dem_Med_Cv;
	}

	public Double getDem_Med_FP() {
		return Dem_Med_FP;
	}

	public void setDem_Med_FP(Double dem_Med_FP) {
		Dem_Med_FP = dem_Med_FP;
	}

	public Double getDem_Med_Pt() {
		return Dem_Med_Pt;
	}

	public void setDem_Med_Pt(Double dem_Med_Pt) {
		Dem_Med_Pt = dem_Med_Pt;
	}

	public Double getDem_Ut_Cv() {
		return Dem_Ut_Cv;
	}

	public void setDem_Ut_Cv(Double dem_Ut_Cv) {
		Dem_Ut_Cv = dem_Ut_Cv;
	}

	public Double getDem_Ut_FP() {
		return Dem_Ut_FP;
	}

	public void setDem_Ut_FP(Double dem_Ut_FP) {
		Dem_Ut_FP = dem_Ut_FP;
	}

	public Double getDem_Ut_Pt() {
		return Dem_Ut_Pt;
	}

	public void setDem_Ut_Pt(Double dem_Ut_Pt) {
		Dem_Ut_Pt = dem_Ut_Pt;
	}

	public Double getFcarga() {
		return Fcarga;
	}

	public void setFcarga(Double fcarga) {
		Fcarga = fcarga;
	}

	public Double getFpot_FP() {
		return Fpot_FP;
	}

	public void setFpot_FP(Double fpot_FP) {
		Fpot_FP = fpot_FP;
	}

	public Double getFpot_Cv() {
		return Fpot_Cv;
	}

	public void setFpot_Cv(Double fpot_Cv) {
		Fpot_Cv = fpot_Cv;
	}

	public Double getFpot_Pt() {
		return Fpot_Pt;
	}

	public void setFpot_Pt(Double fpot_Pt) {
		Fpot_Pt = fpot_Pt;
	}

	public Double getVlr_Ult_Pt() {
		return vlr_Ult_Pt;
	}

	public void setVlr_Ult_Pt(Double vlr_Ult_Pt) {
		this.vlr_Ult_Pt = vlr_Ult_Pt;
	}

	public Double getVlr_Ult_FP() {
		return vlr_Ult_FP;
	}

	public void setVlr_Ult_FP(Double vlr_Ult_FP) {
		this.vlr_Ult_FP = vlr_Ult_FP;
	}

	public Double getVlr_dem_Cv() {
		return vlr_dem_Cv;
	}

	public void setVlr_dem_Cv(Double vlr_dem_Cv) {
		this.vlr_dem_Cv = vlr_dem_Cv;
	}

	public Double getVlr_dem_FP() {
		return vlr_dem_FP;
	}

	public void setVlr_dem_FP(Double vlr_dem_FP) {
		this.vlr_dem_FP = vlr_dem_FP;
	}

	public Double getVlr_dem_Pt() {
		return vlr_dem_Pt;
	}

	public void setVlr_dem_Pt(Double vlr_dem_Pt) {
		this.vlr_dem_Pt = vlr_dem_Pt;
	}

	public Double getVlr_Ult_Cv() {
		return vlr_Ult_Cv;
	}

	public void setVlr_Ult_Cv(Double vlr_Ult_Cv) {
		this.vlr_Ult_Cv = vlr_Ult_Cv;
	}

	public Double getVlr_Multas() {
		return vlr_Multas;
	}

	public void setVlr_Multas(Double vlr_Multas) {
		this.vlr_Multas = vlr_Multas;
	}

	public Double getVlr_Kwh_FS() {
		return vlr_Kwh_FS;
	}

	public void setVlr_Kwh_FS(Double vlr_Kwh_FS) {
		this.vlr_Kwh_FS = vlr_Kwh_FS;
	}

	public Double getVlr_Kwh_FU() {
		return vlr_Kwh_FU;
	}

	public void setVlr_Kwh_FU(Double vlr_Kwh_FU) {
		this.vlr_Kwh_FU = vlr_Kwh_FU;
	}

	public Double getVlr_Kwh_Cv() {
		return vlr_Kwh_Cv;
	}

	public void setVlr_Kwh_Cv(Double vlr_Kwh_Cv) {
		this.vlr_Kwh_Cv = vlr_Kwh_Cv;
	}

	public Double getVlr_Kwh_PS() {
		return vlr_Kwh_PS;
	}

	public void setVlr_Kwh_PS(Double vlr_Kwh_PS) {
		this.vlr_Kwh_PS = vlr_Kwh_PS;
	}

	public Double getVlr_Kwh_PU() {
		return vlr_Kwh_PU;
	}

	public void setVlr_Kwh_PU(Double vlr_Kwh_PU) {
		this.vlr_Kwh_PU = vlr_Kwh_PU;
	}

	public Double getVlr_ICMS() {
		return vlr_ICMS;
	}

	public void setVlr_ICMS(Double vlr_ICMS) {
		this.vlr_ICMS = vlr_ICMS;
	}

	public Double getVlr_DRe_Cv() {
		return vlr_DRe_Cv;
	}

	public void setVlr_DRe_Cv(Double vlr_DRe_Cv) {
		this.vlr_DRe_Cv = vlr_DRe_Cv;
	}

	public Double getVlr_DRe_Pt() {
		return vlr_DRe_Pt;
	}

	public void setVlr_DRe_Pt(Double vlr_DRe_Pt) {
		this.vlr_DRe_Pt = vlr_DRe_Pt;
	}

	public Double getVlr_DRe_FP() {
		return vlr_DRe_FP;
	}

	public void setVlr_DRe_FP(Double vlr_DRe_FP) {
		this.vlr_DRe_FP = vlr_DRe_FP;
	}

	public Double getVlr_ERe_FP() {
		return vlr_ERe_FP;
	}

	public void setVlr_ERe_FP(Double vlr_ERe_FP) {
		this.vlr_ERe_FP = vlr_ERe_FP;
	}

	public Double getVlr_ERe_Cv() {
		return vlr_ERe_Cv;
	}

	public void setVlr_ERe_Cv(Double vlr_ERe_Cv) {
		this.vlr_ERe_Cv = vlr_ERe_Cv;
	}

	public Double getVlr_ERe_Pt() {
		return vlr_ERe_Pt;
	}

	public void setVlr_ERe_Pt(Double vlr_ERe_Pt) {
		this.vlr_ERe_Pt = vlr_ERe_Pt;
	}

	public Double getVlr_Total() {
		return vlr_Total;
	}

	public void setVlr_Total(Double vlr_Total) {
		this.vlr_Total = vlr_Total;
	}

	public UnidadeConsumidora getUnidadeConsumidora() {
		return unidadeConsumidora;
	}

	public void setUnidadeConsumidora(UnidadeConsumidora unidadeConsumidora) {
		this.unidadeConsumidora = unidadeConsumidora;
	}

	public ContratoEnergia getContrato() {
		return contrato;
	}

	public void setContrato(ContratoEnergia contrato) {
		this.contrato = contrato;
	}

	public Double getCvlr_Consumo() {
		return cvlr_Consumo;
	}

	public void setCvlr_Consumo(Double cvlr_Consumo) {
		this.cvlr_Consumo = cvlr_Consumo;
	}

	public Double getCaj_fat_Pt() {
		return caj_fat_Pt;
	}

	public void setCaj_fat_Pt(Double caj_fat_Pt) {
		this.caj_fat_Pt = caj_fat_Pt;
	}

	public Double getCult_Demanda() {
		return cult_Demanda;
	}

	public void setCult_Demanda(Double cult_Demanda) {
		this.cult_Demanda = cult_Demanda;
	}

	public Double getCfat_pot_FP_Umida() {
		return cfat_pot_FP_Umida;
	}

	public void setCfat_pot_FP_Umida(Double cfat_pot_FP_Umida) {
		this.cfat_pot_FP_Umida = cfat_pot_FP_Umida;
	}

	public Double getCfat_pot_Pt_Umida() {
		return cfat_pot_Pt_Umida;
	}

	public void setCfat_pot_Pt_Umida(Double cfat_pot_Pt_Umida) {
		this.cfat_pot_Pt_Umida = cfat_pot_Pt_Umida;
	}

	public Double getCfat_pot_FP_Seca() {
		return cfat_pot_FP_Seca;
	}

	public void setCfat_pot_FP_Seca(Double cfat_pot_FP_Seca) {
		this.cfat_pot_FP_Seca = cfat_pot_FP_Seca;
	}

	public Double getCfat_pot_Pt_Seca() {
		return cfat_pot_Pt_Seca;
	}

	public void setCfat_pot_Pt_Seca(Double cfat_pot_Pt_Seca) {
		this.cfat_pot_Pt_Seca = cfat_pot_Pt_Seca;
	}

	public Double getCfat_pot_Cv() {
		return cfat_pot_Cv;
	}

	public void setCfat_pot_Cv(Double cfat_pot_Cv) {
		this.cfat_pot_Cv = cfat_pot_Cv;
	}

	public Date getCdataLeituraAnterior() {
		return cdataLeituraAnterior;
	}

	public void setCdataLeituraAnterior(Date cdataLeituraAnterior) {
		this.cdataLeituraAnterior = cdataLeituraAnterior;
	}

	public Long getCdias_fat() {
		return cdias_fat;
	}

	public void setCdias_fat(Long cdias_fat) {
		this.cdias_fat = cdias_fat;
	}

	public Double getCcons_KW() {
		return ccons_KW;
	}

	public void setCcons_KW(Double ccons_KW) {
		this.ccons_KW = ccons_KW;
	}

	public Double getCcons_total() {
		return ccons_total;
	}

	public void setCcons_total(Double ccons_total) {
		this.ccons_total = ccons_total;
	}

	public Double getCtarifa_media() {
		return ctarifa_media;
	}

	public void setCtarifa_media(Double ctarifa_media) {
		this.ctarifa_media = ctarifa_media;
	}

	public Double getCvariacao_consumo() {
		return cvariacao_consumo;
	}

	public void setCvariacao_consumo(Double cvariacao_consumo) {
		this.cvariacao_consumo = cvariacao_consumo;
	}

	public Double getCfat_carga() {
		return cfat_carga;
	}

	public void setCfat_carga(Double cfat_carga) {
		this.cfat_carga = cfat_carga;
	}

	public Double getCfat_carga_Pt() {
		return cfat_carga_Pt;
	}

	public void setCfat_carga_Pt(Double cfat_carga_Pt) {
		this.cfat_carga_Pt = cfat_carga_Pt;
	}

	public Double getCfat_carga_FP() {
		return cfat_carga_FP;
	}

	public void setCfat_carga_FP(Double cfat_carga_FP) {
		this.cfat_carga_FP = cfat_carga_FP;
	}

	public Double getCcons_FP_Umida_Ind() {
		return ccons_FP_Umida_Ind;
	}

	public void setCcons_FP_Umida_Ind(Double ccons_FP_Umida_Ind) {
		this.ccons_FP_Umida_Ind = ccons_FP_Umida_Ind;
	}

	public Double getCcons_FP_Umida_Cap() {
		return ccons_FP_Umida_Cap;
	}

	public void setCcons_FP_Umida_Cap(Double ccons_FP_Umida_Cap) {
		this.ccons_FP_Umida_Cap = ccons_FP_Umida_Cap;
	}

	public Double getCcons_FP_Seca_Ind() {
		return ccons_FP_Seca_Ind;
	}

	public void setCcons_FP_Seca_Ind(Double ccons_FP_Seca_Ind) {
		this.ccons_FP_Seca_Ind = ccons_FP_Seca_Ind;
	}

	public Double getCcons_FP_Seca_Cap() {
		return ccons_FP_Seca_Cap;
	}

	public void setCcons_FP_Seca_Cap(Double ccons_FP_Seca_Cap) {
		this.ccons_FP_Seca_Cap = ccons_FP_Seca_Cap;
	}

	public Double getCcons_KW_FP_Seca() {
		return ccons_KW_FP_Seca;
	}

	public void setCcons_KW_FP_Seca(Double ccons_KW_FP_Seca) {
		this.ccons_KW_FP_Seca = ccons_KW_FP_Seca;
	}

	public Double getCcons_KW_Pt_Seca() {
		return ccons_KW_Pt_Seca;
	}

	public void setCcons_KW_Pt_Seca(Double ccons_KW_Pt_Seca) {
		this.ccons_KW_Pt_Seca = ccons_KW_Pt_Seca;
	}

	public Double getCcons_KW_FP_Umida() {
		return ccons_KW_FP_Umida;
	}

	public void setCcons_KW_FP_Umida(Double ccons_KW_FP_Umida) {
		this.ccons_KW_FP_Umida = ccons_KW_FP_Umida;
	}

	public Double getCcons_KW_Pt_Umida() {
		return ccons_KW_Pt_Umida;
	}

	public void setCcons_KW_Pt_Umida(Double ccons_KW_Pt_Umida) {
		this.ccons_KW_Pt_Umida = ccons_KW_Pt_Umida;
	}

	public Double getCvlr_Consumo_FP_Seca() {
		return cvlr_Consumo_FP_Seca;
	}

	public void setCvlr_Consumo_FP_Seca(Double cvlr_Consumo_FP_Seca) {
		this.cvlr_Consumo_FP_Seca = cvlr_Consumo_FP_Seca;
	}

	public Double getCvlr_Consumo_Pt_Seca() {
		return cvlr_Consumo_Pt_Seca;
	}

	public void setCvlr_Consumo_Pt_Seca(Double cvlr_Consumo_Pt_Seca) {
		this.cvlr_Consumo_Pt_Seca = cvlr_Consumo_Pt_Seca;
	}

	public Double getCvlr_Consumo_FP_Umida() {
		return cvlr_Consumo_FP_Umida;
	}

	public void setCvlr_Consumo_FP_Umida(Double cvlr_Consumo_FP_Umida) {
		this.cvlr_Consumo_FP_Umida = cvlr_Consumo_FP_Umida;
	}

	public Double getCvlr_Consumo_Pt_Umida() {
		return cvlr_Consumo_Pt_Umida;
	}

	public void setCvlr_Consumo_Pt_Umida(Double cvlr_Consumo_Pt_Umida) {
		this.cvlr_Consumo_Pt_Umida = cvlr_Consumo_Pt_Umida;
	}

	public Double getCcons_KW_Cv() {
		return ccons_KW_Cv;
	}

	public void setCcons_KW_Cv(Double ccons_KW_Cv) {
		this.ccons_KW_Cv = ccons_KW_Cv;
	}

	public Double getCvlr_Consumo_Cv() {
		return cvlr_Consumo_Cv;
	}

	public void setCvlr_Consumo_Cv(Double cvlr_Consumo_Cv) {
		this.cvlr_Consumo_Cv = cvlr_Consumo_Cv;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((codigoGrupo == null) ? 0 : codigoGrupo.hashCode());
		result = prime * result + ((codigoTipo == null) ? 0 : codigoTipo.hashCode());
		result = prime * result + ((codigoUC == null) ? 0 : codigoUC.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnergiaEletricaDados other = (EnergiaEletricaDados) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (codigoGrupo == null) {
			if (other.codigoGrupo != null)
				return false;
		} else if (!codigoGrupo.equals(other.codigoGrupo))
			return false;
		if (codigoTipo == null) {
			if (other.codigoTipo != null)
				return false;
		} else if (!codigoTipo.equals(other.codigoTipo))
			return false;
		if (codigoUC == null) {
			if (other.codigoUC != null)
				return false;
		} else if (!codigoUC.equals(other.codigoUC))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
}