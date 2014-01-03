package br.gov.pa.cosanpa.gopera.fachada;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.RegistroConsumoSistemaAbastecimento;

@Remote
public interface IRegistroConsumoSistemaAbastecimento extends IGeneric<RegistroConsumoSistemaAbastecimento> {
	public RegistroConsumoSistemaAbastecimento obterRegistroConsumo(Integer codigo) throws Exception;
}
