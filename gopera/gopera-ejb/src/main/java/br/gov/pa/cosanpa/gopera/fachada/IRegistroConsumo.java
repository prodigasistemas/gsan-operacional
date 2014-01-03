package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.Produto;
import br.gov.pa.cosanpa.gopera.model.RegistroConsumo;

@Remote
public interface IRegistroConsumo extends IGeneric<RegistroConsumo> {
	List<Produto> listarProdutos();
}
