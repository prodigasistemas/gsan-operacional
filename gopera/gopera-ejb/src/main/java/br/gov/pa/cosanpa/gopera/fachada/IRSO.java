package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.RSO;

@Remote
public interface IRSO extends IGeneric<RSO> {
	public RSO obterRSO(Integer codigo) throws Exception;

	List<RSO> obterListaComCMB(Integer min, Integer max) throws Exception;
}
