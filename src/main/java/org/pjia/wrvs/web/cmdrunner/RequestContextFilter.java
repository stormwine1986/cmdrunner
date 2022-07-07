package org.pjia.wrvs.web.cmdrunner;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

/**
 * 产生请求GUID，初始化请求上下文
 * 
 * 
 * @author pjia
 *
 */
public class RequestContextFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			// GUID
			RequestContext current = RequestContext.getCurrent();
			current.setGUID("REQID-" + System.currentTimeMillis() + "-" + UUID.randomUUID());
			
			// TOKEN
			HttpServletRequest httpr = (HttpServletRequest) request;
			String token = httpr.getHeader("AuthorizationToken");
			if(StringUtils.isEmpty(token)) {
				token = httpr.getParameter("AuthorizationToken");
			}
			current.setToken(token);
			
			// Local
			String lang = httpr.getHeader("lang");
			if(StringUtils.isEmpty(lang)) {
				lang = httpr.getParameter("lang");
			}
			if(!StringUtils.isEmpty(lang) && "zh".equalsIgnoreCase(lang)) {
				current.setLocale(Locale.CHINA);
			}
			if(!StringUtils.isEmpty(lang) && "en".equalsIgnoreCase(lang)) {
				current.setLocale(Locale.US);
			}
			
			chain.doFilter(request, response);
		}finally {
			RequestContext.clear();
		}
	}

	@Override
	public void destroy() {
	}

}
