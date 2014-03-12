package br.gov.pa.cosanpa.gopera.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.gov.pa.cosanpa.gopera.util.WebUtil;

@WebFilter(filterName="ControleAcessoFilter", urlPatterns={"/*"})
public class ControleAcessoFilter implements Filter {

    public ControleAcessoFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		HttpSession session = httpRequest.getSession(false);
		
		String nomeUsuario = "";
		String token       = "";
		if (session == null){
			session = httpRequest.getSession(true);
			
			nomeUsuario = httpRequest.getHeader("nomeUsuario");
			token       = httpRequest.getHeader("token");
			
			session.setAttribute("nomeUsuario", nomeUsuario);
			session.setAttribute("token", token);
		}
		
		nomeUsuario = String.valueOf(session.getAttribute("nomeUsuario"));
		token       = String.valueOf(session.getAttribute("token"));
		
		String tokenOperacional = WebUtil.geraCodigoToken(nomeUsuario);
		
		if (tokenOperacional.equals(token)){
			chain.doFilter(request, response);
		}else{
			response.getOutputStream().println("Acesso negado");
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}
}
