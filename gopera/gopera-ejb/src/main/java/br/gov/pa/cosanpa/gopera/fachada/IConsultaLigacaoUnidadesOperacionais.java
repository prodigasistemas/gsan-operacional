package br.gov.pa.cosanpa.gopera.fachada;

import java.util.List;

import javax.ejb.Remote;

import br.gov.pa.cosanpa.gopera.model.LigacaoUnidadeOperacional;
import br.gov.pa.cosanpa.gopera.model.NodeSigma;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;

@Remote
public interface IConsultaLigacaoUnidadesOperacionais {
	
	public List<UnidadeNegocioProxy> todasUnidadesNegocio();
	
	public List<NodeSigma> etas(int unidadeNegocio);

	public List<NodeSigma> eabs(int unidadeNegocio);

	public List<NodeSigma> eats(int unidadeNegocio);

	public List<LigacaoUnidadeOperacional> montaLigacaoEabEab(int unidadeNegocio);

	public List<LigacaoUnidadeOperacional> montaLigacaoEabEta(int unidadeNegocio);

	public List<LigacaoUnidadeOperacional> montaLigacaoEatEta(int unidadeNegocio);

	public List<LigacaoUnidadeOperacional> montaLigacaoEatEat(int unidadeNegocio);
}
