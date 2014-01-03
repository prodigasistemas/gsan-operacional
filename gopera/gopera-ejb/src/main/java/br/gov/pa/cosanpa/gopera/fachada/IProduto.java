package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.Produto;
import br.gov.pa.cosanpa.gopera.model.UnidadeMedida;

@Remote
public interface IProduto extends IGeneric<Produto> {
	List<UnidadeMedida> listarUnidadeMedidas();

	Produto obterProduto(Integer codigo) throws Exception;
}
