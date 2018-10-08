package fmss.action;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DwrFilter implements Filter{
	
		public void destroy() {
		}
		
		//DWR过滤器，解决广发提出，访问任意DWR路径,皆返回200的问题。
		public void doFilter(ServletRequest arg0, ServletResponse arg1,FilterChain chain) throws IOException, ServletException {
	
	 		HttpServletRequest request = (HttpServletRequest)arg0;
			HttpServletResponse response = (HttpServletResponse)arg1;
			
 	 		String urlFilter = "/dwrAsynService.";
 			String url = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/"));
			
	 		if(url.indexOf(urlFilter)>-1){
				chain.doFilter(request, response);
			}else{//当访问非法请求，返回404
				 response.setStatus(404);
 				 chain.doFilter(request, response);
			}
	 		
 	 	}

		public void init(FilterConfig arg0) throws ServletException {
 		}
}
