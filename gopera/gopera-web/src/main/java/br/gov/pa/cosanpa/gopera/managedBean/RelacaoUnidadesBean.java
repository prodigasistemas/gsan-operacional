package br.gov.pa.cosanpa.gopera.managedBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.jboss.logging.Logger;

import br.gov.pa.cosanpa.gopera.fachada.IConsultaLigacaoUnidadesOperacionais;
import br.gov.pa.cosanpa.gopera.model.LigacaoUnidade;
import br.gov.pa.cosanpa.gopera.model.LigacaoUnidadeOperacional;
import br.gov.pa.cosanpa.gopera.model.NodeSigma;
import br.gov.pa.cosanpa.gopera.model.UnidadeNegocioProxy;
import br.gov.pa.cosanpa.gopera.util.EdgeSigma;
import br.gov.pa.cosanpa.gopera.util.GraphicCreator;

@ManagedBean
@ViewScoped
public class RelacaoUnidadesBean extends BaseBean<LigacaoUnidade> {
	private static Logger logger = Logger.getLogger(RelacaoUnidadesBean.class);

	@EJB
	private IConsultaLigacaoUnidadesOperacionais consulta;

	private Map<String, NodeSigma> allNodes = null;
	
	private List<NodeSigma> nodesInEdges = null;
	
	private List<NodeSigma> nodesWithoutEdges = null;

	private List<UnidadeNegocioProxy> unidadesNegocio = new ArrayList<UnidadeNegocioProxy>();
	
	private Integer codigoUnidadeNegocio;
	
	private String comCaptacaoFile;
	private String semCaptacaoFile;

	public RelacaoUnidadesBean() {
	}
	
	@PostConstruct
	public void postConstruct(){
		logger.info("postConstruct");
		try {
			unidadesNegocio = consulta.todasUnidadesNegocio();
			unidadesNegocio.add(0, new UnidadeNegocioProxy(-1, "Selecione..."));
		} catch (Exception e) {
			logger.error("Erro ao construir bean", e);
		}		
	}
	

	/******************************************************
	 * Private methods
	 *****************************************************/
	private List<EdgeSigma> createEdgesForAloneNodes() {
		List<EdgeSigma> edges = new ArrayList<EdgeSigma>();
		Set<String> parents = new TreeSet<String>();
		for (NodeSigma node : nodesWithoutEdges) {
			parents.add(node.getType());
			EdgeSigma edge = new EdgeSigma(node.getType(), node.getId());
			edges.add(edge);
		}

		for (String type : parents) {
			NodeSigma parent = new NodeSigma();
			parent.setId(type);
			parent.setLabel(type + "s");
			parent.setType(type);
			nodesWithoutEdges.add(parent);
		}
		return edges;
	}

	private void separateNodes() {
		for (NodeSigma node : allNodes.values()) {
			if (node.isRelacionado()) {
				nodesInEdges.add(node);
			} else {
				nodesWithoutEdges.add(node);
			}
		}
	}

	private List<EdgeSigma> buildEdges(List<LigacaoUnidadeOperacional> lista) {
		List<EdgeSigma> edges = new ArrayList<EdgeSigma>();
		for (LigacaoUnidadeOperacional item : lista) {
			String idSource = item.getKey().getSourceId();
			String idTarget = item.getKey().getTargetId();
			edges.add(new EdgeSigma(idSource, idTarget));
			if (allNodes.containsKey(idSource)) {
				allNodes.get(idSource).setRelacionado(true);
			}
			if (allNodes.containsKey(idTarget)) {
				allNodes.get(idTarget).setRelacionado(true);
			}
		}

		return edges;
	}
	
	/******************************************************
	 * Action methods
	 *****************************************************/
	public String iniciar() {
		return "RelacionamentoUnidadesOperacionais.jsf";
	}
	
	public void doGenerateGraph(){
		logger.info("Solicitado relacionamento entre unidades");

		allNodes = new HashMap<String, NodeSigma>();
		nodesInEdges = new ArrayList<NodeSigma>();
		nodesWithoutEdges = new ArrayList<NodeSigma>();
		
		int unidadeNegocio = (Integer) codigoUnidadeNegocio;

		for (NodeSigma node : consulta.eabs(unidadeNegocio)) {
			allNodes.put(node.getId(), node);
		}
		for (NodeSigma node : consulta.etas(unidadeNegocio)) {
			allNodes.put(node.getId(), node);
		}
		for (NodeSigma node : consulta.eats(unidadeNegocio)) {
			allNodes.put(node.getId(), node);
		}

		Set<EdgeSigma> edges = new TreeSet<EdgeSigma>();
		edges.addAll(buildEdges(consulta.montaLigacaoEabEta(unidadeNegocio)));
		edges.addAll(buildEdges(consulta.montaLigacaoEabEab(unidadeNegocio)));
		edges.addAll(buildEdges(consulta.montaLigacaoEatEat(unidadeNegocio)));
		edges.addAll(buildEdges(consulta.montaLigacaoEatEta(unidadeNegocio)));

		separateNodes();

		GraphicCreator creator = new GraphicCreator();

		try {
			comCaptacaoFile = creator.write(nodesInEdges, edges);
			semCaptacaoFile = creator.write(nodesWithoutEdges, createEdgesForAloneNodes());
		} catch (Exception e) {
			this.mostrarMensagemErro(e.getMessage());
		}
	}

	/****************************************************
	 *Getters and Setters 
	 *****************************************************/
	public List<UnidadeNegocioProxy> getUnidadesNegocio() {
		return unidadesNegocio;
	}
	
	public void setUnidadesNegocio(List<UnidadeNegocioProxy> unidadesNegocio) {
		this.unidadesNegocio = unidadesNegocio;
	}

	public Integer getCodigoUnidadeNegocio() {
		return codigoUnidadeNegocio;
	}

	public void setCodigoUnidadeNegocio(Integer codigoUnidadeNegocio) {
		this.codigoUnidadeNegocio = codigoUnidadeNegocio;
	}

	public String getComCaptacaoFile() {
		return comCaptacaoFile;
	}

	public void setComCaptacaoFile(String comCaptacaoFile) {
		this.comCaptacaoFile = comCaptacaoFile;
	}

	public String getSemCaptacaoFile() {
		return semCaptacaoFile;
	}

	public void setSemCaptacaoFile(String semCaptacaoFile) {
		this.semCaptacaoFile = semCaptacaoFile;
	}
}
