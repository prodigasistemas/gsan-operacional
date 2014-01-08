package br.gov.pa.cosanpa.gopera.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.jboss.logging.Logger;

import br.gov.pa.cosanpa.gopera.util.DateUtil;
import br.gov.pa.cosanpa.gopera.util.Mes;
import br.gov.pa.cosanpa.gopera.util.TotalMensal;
import br.gov.pa.cosanpa.gopera.util.TotalProdutoQuimicoMensal;

public class ReportConsultService {
	private static Logger logger = Logger.getLogger(Logger.class);
	
	public static Collection<TotalProdutoQuimicoMensal> getTotais(String restriction, Date dataIni, Date dataFim){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("SELECT C.prod_nmproduto as produto, ")
		.append(" substr(cast(B.cons_data as text), 1, 7) as referencia,")
		.append(" D.umed_sigla as medida, sum(B.conp_quantidade) as quantidade")
		.append(" FROM (SELECT DISTINCT A.greg_id, A.greg_nmregional, U.uneg_id, U.uneg_nmunidadenegocio,")
		.append(" E.muni_id, E.muni_nmmunicipio, C.loca_id, C.loca_nmlocalidade ")
		.append(" FROM cadastro.gerencia_regional A ")
		.append(" INNER JOIN cadastro.unidade_negocio U ON A.greg_id = U.greg_id") 
		.append(" INNER JOIN cadastro.localidade C ON A.greg_id = C.greg_id AND U.uneg_id = C.uneg_ID") 
		.append(" INNER JOIN cadastro.setor_comercial D ON C.loca_id = D.loca_id ")
		.append(" INNER JOIN cadastro.municipio E ON D.muni_id = E.muni_id) AS A ")
		.append(" INNER JOIN (SELECT A.greg_id, A.uneg_id, A.muni_id, A.loca_id,")
		.append("            C.eta_id AS unop_id, C.eta_nome AS unop_nmunidadeoperacional, 2 AS unop_tipo,")
		.append("            A.cons_data, B.prod_id, B.conp_quantidade")
		.append("            FROM operacao.consumoeta A")
		.append("            INNER JOIN operacao.consumoeta_produto B ON A.cons_id = B.cons_id")
		.append("            INNER JOIN operacao.eta C ON A.eta_id = C.eta_id")
		.append("            UNION ALL ")
		.append("            SELECT A.greg_id, A.uneg_id, A.muni_id, A.loca_id,")
		.append("                   C.eeat_id AS unop_id, C.eeat_nome AS unop_nmunidadeoperacional, 3 AS unop_tipo,")
		.append("                   A.cons_data, B.prod_id, B.conp_quantidade")
		.append("            FROM operacao.consumoeat A")
		.append("            INNER JOIN operacao.consumoeat_produto B ON A.cons_id = B.cons_id")
		.append("            INNER JOIN operacao.eeat C ON A.eat_id = C.eeat_id")
		.append("            )")
		.append("  B ON A.greg_id = B.greg_id AND A.uneg_id = B.uneg_id AND A.muni_id = B.muni_id AND A.loca_id = B.loca_id") 
		.append(" INNER JOIN operacao.produto C ON B.prod_id = C.prod_id ")
		.append(" INNER JOIN operacao.unidademedida D ON C.umed_id = D.umed_id") 
		.append(" WHERE B.cons_data BETWEEN ")
		.append("'" + format.format(dataIni) + "'")
		.append(" AND ")
		.append("'" + format.format(dataFim) + "'")
		.append("  AND B.conp_quantidade <> 0")
		.append("  AND ")
		.append(restriction)
		.append(" GROUP BY C.prod_nmproduto, substr(cast(B.cons_data as text), 1, 7), D.umed_sigla")
		.append(" ORDER BY  C.prod_nmproduto, substr(cast(B.cons_data as text), 1, 7) ");
		
		EntityManagerFactory entityManagerFactory = null;
		EntityManager em = null;
		
		Map<String, TotalProdutoQuimicoMensal> resumos = new HashMap<String, TotalProdutoQuimicoMensal>();
		
		try {
			Context ctx = new InitialContext();
			DataSource dataSource = (DataSource) ctx.lookup("java:/gopera");
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(builder.toString());
			
			DateUtil util = new DateUtil();
			List<Mes> meses = util.numeralMesesPeriodo(dataIni, dataFim);
			
			while(result.next()){
				TotalProdutoQuimicoMensal total = resumos.get(result.getString(1));
				
				if (total == null){
					total = new TotalProdutoQuimicoMensal();
					total.setProduto(result.getString(1));
					total.setMedida(result.getString(3));
					resumos.put(result.getString(1), total);
					for (Mes mes : meses) {
						TotalMensal mensal = new TotalMensal();
						mensal.setMes(mes.getNumeral());
						mensal.setValor(0.0);
						total.addResumoMensal(mensal);
					}
				}
				
				TotalMensal mensal = new TotalMensal();
				mensal.setMes(Integer.valueOf(result.getString(2).substring(5)));
				mensal.setValor(result.getDouble(4));
				total.addResumoMensal(mensal);
			}
			
			statement.close();
			connection.close();
		} catch (Exception e) {
			logger.error("Erro ao chamar service.", e);
		}

		return resumos.values();
	}	
}
