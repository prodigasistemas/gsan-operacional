package br.gov.pa.cosanpa.gopera.fachada;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.Parametro;

@Remote
public interface IParametro extends IGeneric<Parametro> {
}
