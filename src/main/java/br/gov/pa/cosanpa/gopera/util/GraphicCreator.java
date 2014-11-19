package br.gov.pa.cosanpa.gopera.util;

import java.util.Collection;

import jxl.common.Logger;
import br.gov.model.operacao.NodeSigma;

public class GraphicCreator {
	private static Logger logger = Logger.getLogger(GraphicCreator.class);
	
	private StringBuilder builderHead = new StringBuilder();
	private StringBuilder builderTail = new StringBuilder();
	
	public GraphicCreator() {
		builderHead.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n")
		.append("<gexf xmlns:viz=\"http:///www.gexf.net/1.1draft/viz\" version=\"1.1\" xmlns=\"http://www.gexf.net/1.1draft\">\n")
		.append("<meta lastmodifieddate=\"2010-03-03+23:44\">\n")
		.append("<creator>Gephi 0.7</creator>\n")
		.append("</meta>\n")
		.append("<graph defaultedgetype=\"undirected\" idtype=\"string\" type=\"static\">\n");
		
		builderTail.append("</edges>\n")
		.append("</graph>\n")
		.append("</gexf>\n");

	}

	public String write(Collection<NodeSigma> nodes, Collection<EdgeSigma> edges) throws Exception {
		WebBundle bundle = new WebBundle();
//		FileWriter writer = null;

		try {
			StringBuilder builder = new StringBuilder();
			builder.append(builderHead);
			builder.append("<nodes count=\"" + nodes.size() + "\">\n");
			for (NodeSigma node : nodes) {
					builder.append(node.getXml() + "\n");
			}
			builder.append("</nodes>\n");

			builder.append("<edges count=\"" + edges.size() + "\">\n");
			for (EdgeSigma edge : edges) {
				builder.append(edge.getXml() + "\n");
			}
			
			builder.append(builderTail);

//			File file = new File(path);
//			writer = new FileWriter(file, false);
//			writer.write(builder.toString());
			
			return builder.toString();
			
		} catch (Exception e) {
			logger.error(bundle.getText("erro_construcao_grafo"), e);
			throw new Exception(bundle.getText("erro_construcao_grafo"), e);
		}
//		finally{
//			writer.flush();
//			writer.close();
//		}
		
	}
}
