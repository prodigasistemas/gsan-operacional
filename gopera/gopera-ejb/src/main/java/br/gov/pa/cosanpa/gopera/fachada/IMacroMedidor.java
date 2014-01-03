package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.MacroMedidor;

@Remote
public interface IMacroMedidor extends IGeneric<MacroMedidor> {
	MacroMedidor obterMacroMedidor(Integer codigo) throws Exception;

	List<MacroMedidor> obterLista2(Integer codigo) throws Exception;
}