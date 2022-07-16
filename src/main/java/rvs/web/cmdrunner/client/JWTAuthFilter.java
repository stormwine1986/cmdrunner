package rvs.web.cmdrunner.client;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.util.StringUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import rvs.web.cmdrunner.context.RequestContext;

public class JWTAuthFilter implements Filter {
	
	private WRVSClientProperties properties;

	public JWTAuthFilter(WRVSClientProperties properties) {
		this.properties = properties;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		String token = RequestContext.getCurrent().getToken();
		try {
			checkToken(token);
			String user = JWT.decode(token).getClaim("wrvs.user0").asString();
			RequestContext.getCurrent().setUser(user);
			
			String refreshToken = refreshToken();
			InternalResponseWrapper responseWrapper = new InternalResponseWrapper((HttpServletResponse) response);
			responseWrapper.setHeader("RefreshedToken", refreshToken);
		}catch(Exception e) {
			e.printStackTrace();
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);				
			return;
		}
		
		chain.doFilter(request, response);	
	}

	private String refreshToken() {
		Algorithm algorithm = Algorithm.HMAC256(properties.getSecretKey());
		Calendar calendar = Calendar.getInstance();
		Date time0 = calendar.getTime();
		calendar.add(Calendar.MINUTE, 15);
		Date time1 = calendar.getTime();
		return JWT.create()
			.withIssuer("wrvs.web0")
			.withIssuedAt(time0)
			.withExpiresAt(time1)
			.withClaim("wrvs.user0", RequestContext.getCurrent().getUser())
			.sign(algorithm);
	}

	private void checkToken(String token) {
		if(StringUtils.isEmpty(token)) {
			throw new IllegalStateException("No AuthorizationToken!");
		}
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(properties.getSecretKey())).build();
		verifier.verify(token);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	
	
	public class InternalResponseWrapper extends HttpServletResponseWrapper {
	    public InternalResponseWrapper(HttpServletResponse response) {
			super(response);
		}
	}
}
