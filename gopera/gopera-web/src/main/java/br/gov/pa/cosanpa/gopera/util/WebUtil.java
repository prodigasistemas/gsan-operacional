package br.gov.pa.cosanpa.gopera.util;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jxl.common.Logger;

import org.apache.commons.lang3.time.DateUtils;

import br.gov.pa.cosanpa.gopera.model.RelatorioGerencial;

public class WebUtil {
	public static final String ATRIBUTO_REFERER = "referer";
	
	private static Logger logger = Logger.getLogger(WebUtil.class);
	
	private WebBundle bundle = new WebBundle();
	
	private String[] campos = {"gerencia_regional","unidade_negocio","municipio","localidade","unidade_operacional"};

	private Map<String, Entidade> descEntidades = new HashMap<String, Entidade>();
	

	public WebUtil() {
		try{
			Method metodo = RelatorioGerencial.class.getMethod("getNomeRegional");
			descEntidades.put("gerencia_regional", new Entidade(bundle.getText("gerencia_regional"), metodo));
			
			metodo = RelatorioGerencial.class.getMethod("getNomeUnidadeNegocio");
			descEntidades.put("unidade_negocio", new Entidade(bundle.getText("unidade_negocio"), metodo));
			
			metodo = RelatorioGerencial.class.getMethod("getNomeMunicipio");
			descEntidades.put("municipio", new Entidade(bundle.getText("municipio"), metodo));
			
			metodo = RelatorioGerencial.class.getMethod("getNomeLocalidade");
			descEntidades.put("localidade", new Entidade(bundle.getText("localidade"), metodo));
			
			metodo = RelatorioGerencial.class.getMethod("getNomeUnidadeOperacional");
			descEntidades.put("unidade_operacional", new Entidade(bundle.getText("unidade_operacional"), metodo));
		}catch(Exception e){
			logger.error("Erro ao montar classe WebUtil", e);
		}
	}
	
	public Date primeiroDiaMes(String referencia){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, Integer.parseInt(referencia.substring(3,7)));
		c.set(Calendar.MONTH, Integer.parseInt(referencia.substring(0,2)) -1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}
	
	public Date ultimoDiaMes(String referencia){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, Integer.parseInt(referencia.substring(3,7)));
		c.set(Calendar.MONTH, Integer.parseInt(referencia.substring(0,2)) -1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DAY_OF_MONTH, -1);
		return c.getTime();
	}
	
	public List<String> mesesPeriodoEmNome(Date dataInicial, Date dataFinal){
		List<String> meses = new LinkedList<String>();
		Locale locale = new Locale("pt", "BR");
		DateFormat format = new SimpleDateFormat("MMM", locale);
		Date inicio = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		Date fim = DateUtils.truncate(dataFinal, Calendar.DAY_OF_MONTH);
		while (!inicio.after(fim)) {
			meses.add(format.format(inicio).toUpperCase());
			inicio = DateUtils.addMonths(inicio, 1);
		}
		return meses;		
	}
	
	
	public List<String> mesesPeriodo(Date dataInicial, Date dataFinal) {
		List<String> meses = new LinkedList<String>();
		java.text.NumberFormat doisNumeros = java.text.NumberFormat.getInstance();
		Date inicio = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		Date fim = DateUtils.truncate(dataFinal, Calendar.DAY_OF_MONTH);
		doisNumeros.setMinimumIntegerDigits(2);
		while (!inicio.after(fim)) {
			meses.add(doisNumeros.format(DateUtils.toCalendar(inicio).get(Calendar.MONTH) + 1));
			inicio = DateUtils.addMonths(inicio, 1);
		}
		return meses;
	}

	public List<String> descricoesDeCamposNoAgrupamento(int tipoAgrupamento) {
		List<String> cabecalho = new LinkedList<String>();

		if (tipoAgrupamento < 0) {
			tipoAgrupamento = 0;
		}

		for (int i = campos.length - 1; i >= tipoAgrupamento; i--) {
			cabecalho.add(0, descEntidades.get(campos[i]).getDescricao());
		}

		return cabecalho;
	}

	public List<Method> metodosPeloAgrupamento(int tipoAgrupamento) {
		List<Method> metodos = new LinkedList<Method>();

		if (tipoAgrupamento < 0) {
			tipoAgrupamento = 0;
		}

		for (int i = campos.length - 1; i >= tipoAgrupamento; i--) {
			metodos.add(0, descEntidades.get(campos[i]).getMetodo());
		}

		return metodos;
	}
	
	public Map<String, GrupoProdutoQuimico> totalizadorGrupoProdutoQuimico(Map<String, GrupoProdutoQuimico> resumosProdutos, RelatorioGerencial produto, String descGrupo, String idGrupo, int leftPad, List<String> campos) throws Exception {
		GrupoProdutoQuimico grupo = resumosProdutos.get(idGrupo);
		
		boolean criaResumo = true;
		
		if (grupo != null) {
			ResumoProdutoQuimico resumo = grupo.getResumos().get(produto.getDescricaoProduto());
			if (resumo != null){
				for (String campo : campos) {
					ValoresResumo valores = resumo.getValores().get(campo);

					Method metodo = RelatorioGerencial.class.getMethod("getQtdConsumo" + campo);
					Double valor = (Double) metodo.invoke(produto);
					
					valores.addConsumo(valor);
					valores.incrementaTotal(produto.getValorTotal());
				}
				criaResumo = false;
			}
		} else {
			grupo = new GrupoProdutoQuimico(descGrupo, idGrupo, leftPad);
			resumosProdutos.put(idGrupo, grupo);
		}
		
		if (criaResumo){
			ResumoProdutoQuimico resumo = new ResumoProdutoQuimico(produto.getDescricaoProduto());
			
			for (String campo : campos) {
				Method metodo = RelatorioGerencial.class.getMethod("getQtdConsumo" + campo);
				Double valor = (Double) metodo.invoke(produto);
				ValoresResumo valores = new ValoresResumo(valor, produto.getValorUnitario(), produto.getValorTotal());
				resumo.getValores().put(campo, valores);
			}
			
			grupo.addResumo(resumo);
		}
		
		return resumosProdutos;
	}
	
	private static String md5(String s) {
		MessageDigest m;
		String retorno = "";
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(s.getBytes(), 0, s.length());
			retorno = new BigInteger(1, m.digest()).toString(16);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	public static String geraCodigoToken(String nomeUsuario) {
		String md5 = null;
		
		if (nomeUsuario != null){
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd-HH");
			md5 = md5(nomeUsuario + formato.format(Calendar.getInstance().getTime()));
		}
		
		return md5;
	}
	
}
