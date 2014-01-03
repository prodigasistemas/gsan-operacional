package br.gov.pa.cosanpa.gopera.fachada;

import java.util.Date;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.EnergiaEletrica;

@Remote
public interface IEnergiaEletrica extends IGeneric<EnergiaEletrica> {
	public EnergiaEletrica obterEnergia(Integer codigo) throws Exception;

	public EnergiaEletrica obterEnergiaPorData(Date dataReferencia) throws Exception;
}
