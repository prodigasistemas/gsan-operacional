package br.gov.pa.cosanpa.gopera.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import br.gov.model.operacao.UsuarioProxy;
import br.gov.pa.cosanpa.gopera.util.WebUtil;

@WebFilter(filterName="ControleAcessoFilter", urlPatterns={"/*"}, 
	initParams = {
		@WebInitParam(name="exception01", value="/resources/images/"),
		@WebInitParam(name="exception02", value="/resources/css/")
	}
)
public class ControleAcessoFilter implements Filter {
	
	private final String ATRIBUTO_USUARIO = "usuario";
	private final String ATRIBUTO_TOKEN   = "token";
	
	private FilterConfig filterConfig;
	
    public ControleAcessoFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		boolean repassar = usuarioAutenticado(httpRequest);
		if (!repassar){
			repassar = tokenValido(httpRequest) || exceptionRequest(httpRequest);
		}
		
		if (repassar){
			chain.doFilter(request, response);
		}else{
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}
	
	private boolean usuarioAutenticado(HttpServletRequest httpRequest) {
		HttpSession session = httpRequest.getSession();
		if (session != null){
			UsuarioProxy usuarioProxy  = (UsuarioProxy) session.getAttribute("usuarioProxy");
			if (usuarioProxy != null){
				return usuarioProxy.getLogado();
			}
		}
		
		return false;
	}

	private boolean exceptionRequest(HttpServletRequest httpRequest) {
		Enumeration<String> params = filterConfig.getInitParameterNames();
		String path = httpRequest.getRequestURL().toString();
		while (params.hasMoreElements()) {
			String name = (String) params.nextElement();
			if (path.contains(filterConfig.getInitParameter(name))){
				return true;
			}
		}
		
		return false;
	}

	private boolean tokenValido(HttpServletRequest httpRequest){
		String usuario = "";
		String token   = "";
		Map<String, String> dados = new HashMap<String, String>();
		
		if (existemDadosAcessoNaRequest(httpRequest)){
			dados = recuperaDadosAcessoNaRequest(httpRequest);
			insereDadosNaSessao(httpRequest, dados);
		}else if (existemDadosAcessoNaSessao(httpRequest)){
			dados = recuperaDadosAcessoNaSessao(httpRequest);
		}

		
		if (dados.size() > 0 ){
			usuario = dados.get(ATRIBUTO_USUARIO);
			token   = dados.get(ATRIBUTO_TOKEN);
		}
		
		
		String tokenOperacional = WebUtil.geraCodigoToken(usuario);	
		
		return tokenOperacional.equals(token);
	}

	private void insereDadosNaSessao(HttpServletRequest httpRequest, Map<String, String> dados) {
		HttpSession session = httpRequest.getSession(false);
		if (session == null){
			session = httpRequest.getSession(true);
		}

		session.setAttribute(ATRIBUTO_USUARIO, dados.get(ATRIBUTO_USUARIO));
		session.setAttribute(ATRIBUTO_TOKEN, dados.get(ATRIBUTO_TOKEN));
		if (StringUtils.isNotBlank(httpRequest.getHeader(WebUtil.ATRIBUTO_REFERER))){
			session.setAttribute(WebUtil.ATRIBUTO_REFERER, httpRequest.getHeader(WebUtil.ATRIBUTO_REFERER));
		}
	}

	private Map<String, String> recuperaDadosAcessoNaRequest(HttpServletRequest httpRequest) {
		Map<String, String> dados = new HashMap<String, String>();
		dados.put(ATRIBUTO_USUARIO, httpRequest.getParameter(ATRIBUTO_USUARIO));
		dados.put(ATRIBUTO_TOKEN, httpRequest.getParameter(ATRIBUTO_TOKEN));
		
		return dados;
	}

	private boolean existemDadosAcessoNaRequest(HttpServletRequest httpRequest) {
		boolean contemDados = false;
		String usuario = httpRequest.getParameter(ATRIBUTO_USUARIO);
		String token   = httpRequest.getParameter(ATRIBUTO_TOKEN);
		if (StringUtils.isNotEmpty(usuario) && StringUtils.isNotEmpty(token)){
			contemDados = true;
		}
		return contemDados;
	}

	private boolean existemDadosAcessoNaSessao(HttpServletRequest httpRequest) {
		HttpSession session = httpRequest.getSession(false);
		boolean contemDados = false;
		if (session != null){
			String usuario = String.valueOf(session.getAttribute(ATRIBUTO_USUARIO));
			String token   = String.valueOf(session.getAttribute(ATRIBUTO_TOKEN));
			if (StringUtils.isNotEmpty(usuario) && StringUtils.isNotEmpty(token)){
				contemDados = true;
			}
		}

		return contemDados;
	}
	
	private Map<String, String> recuperaDadosAcessoNaSessao(HttpServletRequest httpRequest) {
		HttpSession session = httpRequest.getSession(false);
		Map<String, String> dados = new HashMap<String, String>();
		dados.put(ATRIBUTO_USUARIO, String.valueOf(session.getAttribute(ATRIBUTO_USUARIO)));
		dados.put(ATRIBUTO_TOKEN, String.valueOf(session.getAttribute(ATRIBUTO_TOKEN)));
		
		return dados;
	}

	public void init(FilterConfig fConfig) throws ServletException {
		this.filterConfig = fConfig;
	}
}
