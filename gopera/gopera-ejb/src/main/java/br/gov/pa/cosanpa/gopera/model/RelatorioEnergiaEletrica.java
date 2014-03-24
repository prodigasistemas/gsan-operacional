package br.gov.pa.cosanpa.gopera.model;

import java.io.Serializable;
import java.util.Date;

public class RelatorioEnergiaEletrica implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer codigoUC;
	private String nomeUC;
	private Date dataReferencia;
	private String referencia;
	private String fatura;
	private Double valorTotal;
	private Double consumoKwh;
	private Double DemandaMedida;
	private Double DemandaFaturada;
	
	private Date dataLeitura;
	private String endereco;
	private String bairro;
	private String cep;
	private String codGrupo;
	private String codTipo;
	private Double consumoKwhCv;
	private Double consumoKwhFs;
	private Double consumoKwhFu;
	private Double consumoKwhPs;
	private Double consumoKwhPu;
	private Double dcv;
	private Double dfs;
	private Double dfu;
	private Double dps;
	private Double dpu;
	private Double demFatCv;
	private Double demFatFp;
	private Double demFatPt;
	private Double demMedCv;
	private Double demMedFp;
	private Double demMedPt;
	private Double demUltCv;
	private Double demUltFp;
	private Double demUltPt;
	private Double fatorCarga;
	private Double fPotFp;
	private Double fPotCv;
	private Double fPotPt;
	private Double valorUltFp;
	private Double valorUltPt;
	private Double valorDemCv;
	private Double valorDemFp;
	private Double valorDemPt;
	private Double valorUltCv;
	private Double valorMultas;
	private Double valorKwhFs;
	private Double valorKwhFu;
	private Double valorKwhCv;
	private Double valorKwhPs;
	private Double valorKwhPu;
	private Double valorICMS;
	private Double valorDreCv;
	private Double valorDrePt;
	private Double valorDreFp;
	private Double valorEreFp;
	private Double valorEreCv;
	private Double valorErePt;
	
	private String municipio;
	private String localidade;
	private Double consumo;
	private Double total;
	private Double ajusteFatorPotencia;
	private Double ultrapassagemR$;
	private Double ultrapassagemKwh;
	public Integer getCodigoUC() {
		return codigoUC;
	}
	public void setCodigoUC(Integer codigoUC) {
		this.codigoUC = codigoUC;
	}
	public String getNomeUC() {
		return nomeUC;
	}
	public void setNomeUC(String nomeUC) {
		this.nomeUC = nomeUC;
	}
	public Date getDataReferencia() {
		return dataReferencia;
	}
	public void setDataReferencia(Date dataReferencia) {
		this.dataReferencia = dataReferencia;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getFatura() {
		return fatura;
	}
	public void setFatura(String fatura) {
		this.fatura = fatura;
	}
	public Double getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}
	public Double getConsumoKwh() {
		return consumoKwh;
	}
	public void setConsumoKwh(Double consumoKwh) {
		this.consumoKwh = consumoKwh;
	}
	public Double getDemandaMedida() {
		return DemandaMedida;
	}
	public void setDemandaMedida(Double demandaMedida) {
		DemandaMedida = demandaMedida;
	}
	public Double getDemandaFaturada() {
		return DemandaFaturada;
	}
	public void setDemandaFaturada(Double demandaFaturada) {
		DemandaFaturada = demandaFaturada;
	}
	public Date getDataLeitura() {
		return dataLeitura;
	}
	public void setDataLeitura(Date dataLeitura) {
		this.dataLeitura = dataLeitura;
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
	public String getCodGrupo() {
		return codGrupo;
	}
	public void setCodGrupo(String codGrupo) {
		this.codGrupo = codGrupo;
	}
	public String getCodTipo() {
		return codTipo;
	}
	public void setCodTipo(String codTipo) {
		this.codTipo = codTipo;
	}
	public Double getConsumoKwhCv() {
		return consumoKwhCv;
	}
	public void setConsumoKwhCv(Double consumoKwhCv) {
		this.consumoKwhCv = consumoKwhCv;
	}
	public Double getConsumoKwhFs() {
		return consumoKwhFs;
	}
	public void setConsumoKwhFs(Double consumoKwhFs) {
		this.consumoKwhFs = consumoKwhFs;
	}
	public Double getConsumoKwhFu() {
		return consumoKwhFu;
	}
	public void setConsumoKwhFu(Double consumoKwhFu) {
		this.consumoKwhFu = consumoKwhFu;
	}
	public Double getConsumoKwhPs() {
		return consumoKwhPs;
	}
	public void setConsumoKwhPs(Double consumoKwhPs) {
		this.consumoKwhPs = consumoKwhPs;
	}
	public Double getConsumoKwhPu() {
		return consumoKwhPu;
	}
	public void setConsumoKwhPu(Double consumoKwhPu) {
		this.consumoKwhPu = consumoKwhPu;
	}
	public Double getDcv() {
		return dcv;
	}
	public void setDcv(Double dcv) {
		this.dcv = dcv;
	}
	public Double getDfs() {
		return dfs;
	}
	public void setDfs(Double dfs) {
		this.dfs = dfs;
	}
	public Double getDfu() {
		return dfu;
	}
	public void setDfu(Double dfu) {
		this.dfu = dfu;
	}
	public Double getDps() {
		return dps;
	}
	public void setDps(Double dps) {
		this.dps = dps;
	}
	public Double getDpu() {
		return dpu;
	}
	public void setDpu(Double dpu) {
		this.dpu = dpu;
	}
	public Double getDemFatCv() {
		return demFatCv;
	}
	public void setDemFatCv(Double demFatCv) {
		this.demFatCv = demFatCv;
	}
	public Double getDemFatFp() {
		return demFatFp;
	}
	public void setDemFatFp(Double demFatFp) {
		this.demFatFp = demFatFp;
	}
	public Double getDemFatPt() {
		return demFatPt;
	}
	public void setDemFatPt(Double demFatPt) {
		this.demFatPt = demFatPt;
	}
	public Double getDemMedCv() {
		return demMedCv;
	}
	public void setDemMedCv(Double demMedCv) {
		this.demMedCv = demMedCv;
	}
	public Double getDemMedFp() {
		return demMedFp;
	}
	public void setDemMedFp(Double demMedFp) {
		this.demMedFp = demMedFp;
	}
	public Double getDemMedPt() {
		return demMedPt;
	}
	public void setDemMedPt(Double demMedPt) {
		this.demMedPt = demMedPt;
	}
	public Double getDemUltCv() {
		return demUltCv;
	}
	public void setDemUltCv(Double demUltCv) {
		this.demUltCv = demUltCv;
	}
	public Double getDemUltFp() {
		return demUltFp;
	}
	public void setDemUltFp(Double demUltFp) {
		this.demUltFp = demUltFp;
	}
	public Double getDemUltPt() {
		return demUltPt;
	}
	public void setDemUltPt(Double demUltPt) {
		this.demUltPt = demUltPt;
	}
	public Double getFatorCarga() {
		return fatorCarga;
	}
	public void setFatorCarga(Double fatorCarga) {
		this.fatorCarga = fatorCarga;
	}
	public Double getfPotFp() {
		return fPotFp;
	}
	public void setfPotFp(Double fPotFp) {
		this.fPotFp = fPotFp;
	}
	public Double getfPotCv() {
		return fPotCv;
	}
	public void setfPotCv(Double fPotCv) {
		this.fPotCv = fPotCv;
	}
	public Double getfPotPt() {
		return fPotPt;
	}
	public void setfPotPt(Double fPotPt) {
		this.fPotPt = fPotPt;
	}
	public Double getValorUltFp() {
		return valorUltFp;
	}
	public void setValorUltFp(Double valorUltFp) {
		this.valorUltFp = valorUltFp;
	}
	public Double getValorUltPt() {
		return valorUltPt;
	}
	public void setValorUltPt(Double valorUltPt) {
		this.valorUltPt = valorUltPt;
	}
	public Double getValorDemCv() {
		return valorDemCv;
	}
	public void setValorDemCv(Double valorDemCv) {
		this.valorDemCv = valorDemCv;
	}
	public Double getValorDemFp() {
		return valorDemFp;
	}
	public void setValorDemFp(Double valorDemFp) {
		this.valorDemFp = valorDemFp;
	}
	public Double getValorDemPt() {
		return valorDemPt;
	}
	public void setValorDemPt(Double valorDemPt) {
		this.valorDemPt = valorDemPt;
	}
	public Double getValorUltCv() {
		return valorUltCv;
	}
	public void setValorUltCv(Double valorUltCv) {
		this.valorUltCv = valorUltCv;
	}
	public Double getValorMultas() {
		return valorMultas;
	}
	public void setValorMultas(Double valorMultas) {
		this.valorMultas = valorMultas;
	}
	public Double getValorKwhFs() {
		return valorKwhFs;
	}
	public void setValorKwhFs(Double valorKwhFs) {
		this.valorKwhFs = valorKwhFs;
	}
	public Double getValorKwhFu() {
		return valorKwhFu;
	}
	public void setValorKwhFu(Double valorKwhFu) {
		this.valorKwhFu = valorKwhFu;
	}
	public Double getValorKwhCv() {
		return valorKwhCv;
	}
	public void setValorKwhCv(Double valorKwhCv) {
		this.valorKwhCv = valorKwhCv;
	}
	public Double getValorKwhPs() {
		return valorKwhPs;
	}
	public void setValorKwhPs(Double valorKwhPs) {
		this.valorKwhPs = valorKwhPs;
	}
	public Double getValorKwhPu() {
		return valorKwhPu;
	}
	public void setValorKwhPu(Double valorKwhPu) {
		this.valorKwhPu = valorKwhPu;
	}
	public Double getValorICMS() {
		return valorICMS;
	}
	public void setValorICMS(Double valorICMS) {
		this.valorICMS = valorICMS;
	}
	public Double getValorDreCv() {
		return valorDreCv;
	}
	public void setValorDreCv(Double valorDreCv) {
		this.valorDreCv = valorDreCv;
	}
	public Double getValorDrePt() {
		return valorDrePt;
	}
	public void setValorDrePt(Double valorDrePt) {
		this.valorDrePt = valorDrePt;
	}
	public Double getValorDreFp() {
		return valorDreFp;
	}
	public void setValorDreFp(Double valorDreFp) {
		this.valorDreFp = valorDreFp;
	}
	public Double getValorEreFp() {
		return valorEreFp;
	}
	public void setValorEreFp(Double valorEreFp) {
		this.valorEreFp = valorEreFp;
	}
	public Double getValorEreCv() {
		return valorEreCv;
	}
	public void setValorEreCv(Double valorEreCv) {
		this.valorEreCv = valorEreCv;
	}
	public Double getValorErePt() {
		return valorErePt;
	}
	public void setValorErePt(Double valorErePt) {
		this.valorErePt = valorErePt;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getLocalidade() {
		return localidade;
	}
	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}
	public Double getConsumo() {
		return consumo;
	}
	public void setConsumo(Double consumo) {
		this.consumo = consumo;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getAjusteFatorPotencia() {
		return ajusteFatorPotencia;
	}
	public void setAjusteFatorPotencia(Double ajusteFatorPotencia) {
		this.ajusteFatorPotencia = ajusteFatorPotencia;
	}
	public Double getUltrapassagemR$() {
		return ultrapassagemR$;
	}
	public void setUltrapassagemR$(Double ultrapassagemR$) {
		this.ultrapassagemR$ = ultrapassagemR$;
	}
	public Double getUltrapassagemKwh() {
		return ultrapassagemKwh;
	}
	public void setUltrapassagemKwh(Double ultrapassagemKwh) {
		this.ultrapassagemKwh = ultrapassagemKwh;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((DemandaFaturada == null) ? 0 : DemandaFaturada.hashCode());
		result = prime * result
				+ ((DemandaMedida == null) ? 0 : DemandaMedida.hashCode());
		result = prime
				* result
				+ ((ajusteFatorPotencia == null) ? 0 : ajusteFatorPotencia
						.hashCode());
		result = prime * result + ((bairro == null) ? 0 : bairro.hashCode());
		result = prime * result + ((cep == null) ? 0 : cep.hashCode());
		result = prime * result
				+ ((codGrupo == null) ? 0 : codGrupo.hashCode());
		result = prime * result + ((codTipo == null) ? 0 : codTipo.hashCode());
		result = prime * result
				+ ((codigoUC == null) ? 0 : codigoUC.hashCode());
		result = prime * result + ((consumo == null) ? 0 : consumo.hashCode());
		result = prime * result
				+ ((consumoKwh == null) ? 0 : consumoKwh.hashCode());
		result = prime * result
				+ ((consumoKwhCv == null) ? 0 : consumoKwhCv.hashCode());
		result = prime * result
				+ ((consumoKwhFs == null) ? 0 : consumoKwhFs.hashCode());
		result = prime * result
				+ ((consumoKwhFu == null) ? 0 : consumoKwhFu.hashCode());
		result = prime * result
				+ ((consumoKwhPs == null) ? 0 : consumoKwhPs.hashCode());
		result = prime * result
				+ ((consumoKwhPu == null) ? 0 : consumoKwhPu.hashCode());
		result = prime * result
				+ ((dataLeitura == null) ? 0 : dataLeitura.hashCode());
		result = prime * result
				+ ((dataReferencia == null) ? 0 : dataReferencia.hashCode());
		result = prime * result + ((dcv == null) ? 0 : dcv.hashCode());
		result = prime * result
				+ ((demFatCv == null) ? 0 : demFatCv.hashCode());
		result = prime * result
				+ ((demFatFp == null) ? 0 : demFatFp.hashCode());
		result = prime * result
				+ ((demFatPt == null) ? 0 : demFatPt.hashCode());
		result = prime * result
				+ ((demMedCv == null) ? 0 : demMedCv.hashCode());
		result = prime * result
				+ ((demMedFp == null) ? 0 : demMedFp.hashCode());
		result = prime * result
				+ ((demMedPt == null) ? 0 : demMedPt.hashCode());
		result = prime * result
				+ ((demUltCv == null) ? 0 : demUltCv.hashCode());
		result = prime * result
				+ ((demUltFp == null) ? 0 : demUltFp.hashCode());
		result = prime * result
				+ ((demUltPt == null) ? 0 : demUltPt.hashCode());
		result = prime * result + ((dfs == null) ? 0 : dfs.hashCode());
		result = prime * result + ((dfu == null) ? 0 : dfu.hashCode());
		result = prime * result + ((dps == null) ? 0 : dps.hashCode());
		result = prime * result + ((dpu == null) ? 0 : dpu.hashCode());
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + ((fPotCv == null) ? 0 : fPotCv.hashCode());
		result = prime * result + ((fPotFp == null) ? 0 : fPotFp.hashCode());
		result = prime * result + ((fPotPt == null) ? 0 : fPotPt.hashCode());
		result = prime * result
				+ ((fatorCarga == null) ? 0 : fatorCarga.hashCode());
		result = prime * result + ((fatura == null) ? 0 : fatura.hashCode());
		result = prime * result
				+ ((localidade == null) ? 0 : localidade.hashCode());
		result = prime * result
				+ ((municipio == null) ? 0 : municipio.hashCode());
		result = prime * result + ((nomeUC == null) ? 0 : nomeUC.hashCode());
		result = prime * result
				+ ((referencia == null) ? 0 : referencia.hashCode());
		result = prime * result + ((total == null) ? 0 : total.hashCode());
		result = prime
				* result
				+ ((ultrapassagemKwh == null) ? 0 : ultrapassagemKwh.hashCode());
		result = prime * result
				+ ((ultrapassagemR$ == null) ? 0 : ultrapassagemR$.hashCode());
		result = prime * result
				+ ((valorDemCv == null) ? 0 : valorDemCv.hashCode());
		result = prime * result
				+ ((valorDemFp == null) ? 0 : valorDemFp.hashCode());
		result = prime * result
				+ ((valorDemPt == null) ? 0 : valorDemPt.hashCode());
		result = prime * result
				+ ((valorDreCv == null) ? 0 : valorDreCv.hashCode());
		result = prime * result
				+ ((valorDreFp == null) ? 0 : valorDreFp.hashCode());
		result = prime * result
				+ ((valorDrePt == null) ? 0 : valorDrePt.hashCode());
		result = prime * result
				+ ((valorEreCv == null) ? 0 : valorEreCv.hashCode());
		result = prime * result
				+ ((valorEreFp == null) ? 0 : valorEreFp.hashCode());
		result = prime * result
				+ ((valorErePt == null) ? 0 : valorErePt.hashCode());
		result = prime * result
				+ ((valorICMS == null) ? 0 : valorICMS.hashCode());
		result = prime * result
				+ ((valorKwhCv == null) ? 0 : valorKwhCv.hashCode());
		result = prime * result
				+ ((valorKwhFs == null) ? 0 : valorKwhFs.hashCode());
		result = prime * result
				+ ((valorKwhFu == null) ? 0 : valorKwhFu.hashCode());
		result = prime * result
				+ ((valorKwhPs == null) ? 0 : valorKwhPs.hashCode());
		result = prime * result
				+ ((valorKwhPu == null) ? 0 : valorKwhPu.hashCode());
		result = prime * result
				+ ((valorMultas == null) ? 0 : valorMultas.hashCode());
		result = prime * result
				+ ((valorTotal == null) ? 0 : valorTotal.hashCode());
		result = prime * result
				+ ((valorUltCv == null) ? 0 : valorUltCv.hashCode());
		result = prime * result
				+ ((valorUltFp == null) ? 0 : valorUltFp.hashCode());
		result = prime * result
				+ ((valorUltPt == null) ? 0 : valorUltPt.hashCode());
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
		RelatorioEnergiaEletrica other = (RelatorioEnergiaEletrica) obj;
		if (DemandaFaturada == null) {
			if (other.DemandaFaturada != null)
				return false;
		} else if (!DemandaFaturada.equals(other.DemandaFaturada))
			return false;
		if (DemandaMedida == null) {
			if (other.DemandaMedida != null)
				return false;
		} else if (!DemandaMedida.equals(other.DemandaMedida))
			return false;
		if (ajusteFatorPotencia == null) {
			if (other.ajusteFatorPotencia != null)
				return false;
		} else if (!ajusteFatorPotencia.equals(other.ajusteFatorPotencia))
			return false;
		if (bairro == null) {
			if (other.bairro != null)
				return false;
		} else if (!bairro.equals(other.bairro))
			return false;
		if (cep == null) {
			if (other.cep != null)
				return false;
		} else if (!cep.equals(other.cep))
			return false;
		if (codGrupo == null) {
			if (other.codGrupo != null)
				return false;
		} else if (!codGrupo.equals(other.codGrupo))
			return false;
		if (codTipo == null) {
			if (other.codTipo != null)
				return false;
		} else if (!codTipo.equals(other.codTipo))
			return false;
		if (codigoUC == null) {
			if (other.codigoUC != null)
				return false;
		} else if (!codigoUC.equals(other.codigoUC))
			return false;
		if (consumo == null) {
			if (other.consumo != null)
				return false;
		} else if (!consumo.equals(other.consumo))
			return false;
		if (consumoKwh == null) {
			if (other.consumoKwh != null)
				return false;
		} else if (!consumoKwh.equals(other.consumoKwh))
			return false;
		if (consumoKwhCv == null) {
			if (other.consumoKwhCv != null)
				return false;
		} else if (!consumoKwhCv.equals(other.consumoKwhCv))
			return false;
		if (consumoKwhFs == null) {
			if (other.consumoKwhFs != null)
				return false;
		} else if (!consumoKwhFs.equals(other.consumoKwhFs))
			return false;
		if (consumoKwhFu == null) {
			if (other.consumoKwhFu != null)
				return false;
		} else if (!consumoKwhFu.equals(other.consumoKwhFu))
			return false;
		if (consumoKwhPs == null) {
			if (other.consumoKwhPs != null)
				return false;
		} else if (!consumoKwhPs.equals(other.consumoKwhPs))
			return false;
		if (consumoKwhPu == null) {
			if (other.consumoKwhPu != null)
				return false;
		} else if (!consumoKwhPu.equals(other.consumoKwhPu))
			return false;
		if (dataLeitura == null) {
			if (other.dataLeitura != null)
				return false;
		} else if (!dataLeitura.equals(other.dataLeitura))
			return false;
		if (dataReferencia == null) {
			if (other.dataReferencia != null)
				return false;
		} else if (!dataReferencia.equals(other.dataReferencia))
			return false;
		if (dcv == null) {
			if (other.dcv != null)
				return false;
		} else if (!dcv.equals(other.dcv))
			return false;
		if (demFatCv == null) {
			if (other.demFatCv != null)
				return false;
		} else if (!demFatCv.equals(other.demFatCv))
			return false;
		if (demFatFp == null) {
			if (other.demFatFp != null)
				return false;
		} else if (!demFatFp.equals(other.demFatFp))
			return false;
		if (demFatPt == null) {
			if (other.demFatPt != null)
				return false;
		} else if (!demFatPt.equals(other.demFatPt))
			return false;
		if (demMedCv == null) {
			if (other.demMedCv != null)
				return false;
		} else if (!demMedCv.equals(other.demMedCv))
			return false;
		if (demMedFp == null) {
			if (other.demMedFp != null)
				return false;
		} else if (!demMedFp.equals(other.demMedFp))
			return false;
		if (demMedPt == null) {
			if (other.demMedPt != null)
				return false;
		} else if (!demMedPt.equals(other.demMedPt))
			return false;
		if (demUltCv == null) {
			if (other.demUltCv != null)
				return false;
		} else if (!demUltCv.equals(other.demUltCv))
			return false;
		if (demUltFp == null) {
			if (other.demUltFp != null)
				return false;
		} else if (!demUltFp.equals(other.demUltFp))
			return false;
		if (demUltPt == null) {
			if (other.demUltPt != null)
				return false;
		} else if (!demUltPt.equals(other.demUltPt))
			return false;
		if (dfs == null) {
			if (other.dfs != null)
				return false;
		} else if (!dfs.equals(other.dfs))
			return false;
		if (dfu == null) {
			if (other.dfu != null)
				return false;
		} else if (!dfu.equals(other.dfu))
			return false;
		if (dps == null) {
			if (other.dps != null)
				return false;
		} else if (!dps.equals(other.dps))
			return false;
		if (dpu == null) {
			if (other.dpu != null)
				return false;
		} else if (!dpu.equals(other.dpu))
			return false;
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		if (fPotCv == null) {
			if (other.fPotCv != null)
				return false;
		} else if (!fPotCv.equals(other.fPotCv))
			return false;
		if (fPotFp == null) {
			if (other.fPotFp != null)
				return false;
		} else if (!fPotFp.equals(other.fPotFp))
			return false;
		if (fPotPt == null) {
			if (other.fPotPt != null)
				return false;
		} else if (!fPotPt.equals(other.fPotPt))
			return false;
		if (fatorCarga == null) {
			if (other.fatorCarga != null)
				return false;
		} else if (!fatorCarga.equals(other.fatorCarga))
			return false;
		if (fatura == null) {
			if (other.fatura != null)
				return false;
		} else if (!fatura.equals(other.fatura))
			return false;
		if (localidade == null) {
			if (other.localidade != null)
				return false;
		} else if (!localidade.equals(other.localidade))
			return false;
		if (municipio == null) {
			if (other.municipio != null)
				return false;
		} else if (!municipio.equals(other.municipio))
			return false;
		if (nomeUC == null) {
			if (other.nomeUC != null)
				return false;
		} else if (!nomeUC.equals(other.nomeUC))
			return false;
		if (referencia == null) {
			if (other.referencia != null)
				return false;
		} else if (!referencia.equals(other.referencia))
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		if (ultrapassagemKwh == null) {
			if (other.ultrapassagemKwh != null)
				return false;
		} else if (!ultrapassagemKwh.equals(other.ultrapassagemKwh))
			return false;
		if (ultrapassagemR$ == null) {
			if (other.ultrapassagemR$ != null)
				return false;
		} else if (!ultrapassagemR$.equals(other.ultrapassagemR$))
			return false;
		if (valorDemCv == null) {
			if (other.valorDemCv != null)
				return false;
		} else if (!valorDemCv.equals(other.valorDemCv))
			return false;
		if (valorDemFp == null) {
			if (other.valorDemFp != null)
				return false;
		} else if (!valorDemFp.equals(other.valorDemFp))
			return false;
		if (valorDemPt == null) {
			if (other.valorDemPt != null)
				return false;
		} else if (!valorDemPt.equals(other.valorDemPt))
			return false;
		if (valorDreCv == null) {
			if (other.valorDreCv != null)
				return false;
		} else if (!valorDreCv.equals(other.valorDreCv))
			return false;
		if (valorDreFp == null) {
			if (other.valorDreFp != null)
				return false;
		} else if (!valorDreFp.equals(other.valorDreFp))
			return false;
		if (valorDrePt == null) {
			if (other.valorDrePt != null)
				return false;
		} else if (!valorDrePt.equals(other.valorDrePt))
			return false;
		if (valorEreCv == null) {
			if (other.valorEreCv != null)
				return false;
		} else if (!valorEreCv.equals(other.valorEreCv))
			return false;
		if (valorEreFp == null) {
			if (other.valorEreFp != null)
				return false;
		} else if (!valorEreFp.equals(other.valorEreFp))
			return false;
		if (valorErePt == null) {
			if (other.valorErePt != null)
				return false;
		} else if (!valorErePt.equals(other.valorErePt))
			return false;
		if (valorICMS == null) {
			if (other.valorICMS != null)
				return false;
		} else if (!valorICMS.equals(other.valorICMS))
			return false;
		if (valorKwhCv == null) {
			if (other.valorKwhCv != null)
				return false;
		} else if (!valorKwhCv.equals(other.valorKwhCv))
			return false;
		if (valorKwhFs == null) {
			if (other.valorKwhFs != null)
				return false;
		} else if (!valorKwhFs.equals(other.valorKwhFs))
			return false;
		if (valorKwhFu == null) {
			if (other.valorKwhFu != null)
				return false;
		} else if (!valorKwhFu.equals(other.valorKwhFu))
			return false;
		if (valorKwhPs == null) {
			if (other.valorKwhPs != null)
				return false;
		} else if (!valorKwhPs.equals(other.valorKwhPs))
			return false;
		if (valorKwhPu == null) {
			if (other.valorKwhPu != null)
				return false;
		} else if (!valorKwhPu.equals(other.valorKwhPu))
			return false;
		if (valorMultas == null) {
			if (other.valorMultas != null)
				return false;
		} else if (!valorMultas.equals(other.valorMultas))
			return false;
		if (valorTotal == null) {
			if (other.valorTotal != null)
				return false;
		} else if (!valorTotal.equals(other.valorTotal))
			return false;
		if (valorUltCv == null) {
			if (other.valorUltCv != null)
				return false;
		} else if (!valorUltCv.equals(other.valorUltCv))
			return false;
		if (valorUltFp == null) {
			if (other.valorUltFp != null)
				return false;
		} else if (!valorUltFp.equals(other.valorUltFp))
			return false;
		if (valorUltPt == null) {
			if (other.valorUltPt != null)
				return false;
		} else if (!valorUltPt.equals(other.valorUltPt))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "RelatorioEnergiaEletrica [codigoUC=" + codigoUC + ", referencia=" + referencia + "]";
	}
	
	
}
