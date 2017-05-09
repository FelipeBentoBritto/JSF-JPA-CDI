package br.com.felipe.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.omg.CORBA.Request;

import br.com.felipe.model.Usuario;

@WebFilter("/sistema/*")
public class AutenticaoFilter implements Filter{

	public AutenticaoFilter() {
		
	}
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpSession httpSession = ((HttpServletRequest)request).getSession();
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		if(httpServletRequest.getRequestURI().indexOf("index.xhtml") <=-1) {
			
			Usuario usuario = (Usuario) httpSession.getAttribute("usuarioAutenticado");
			
			if(usuario == null) {
				httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+ "/index.xhtml");
			} 
			else {
				chain.doFilter(request, response);
			}
		}
		else {
			chain.doFilter(request, response);
		}
		
	}

	public void init(FilterConfig fConfig) throws ServletException {
		
	}
	
	
	
}
