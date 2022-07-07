package org.pjia.wrvs.web.cmdrunner;

import java.util.Locale;

/**
 * 请求上下文
 * 
 * @author pjia
 *
 */
public class RequestContext {
	
	private static ThreadLocal<RequestContext> local = new ThreadLocal<RequestContext>();
	
	public static RequestContext getCurrent() {
		
		if(local.get() == null) {
			local.set(new RequestContext());
		}
		
		return local.get();
	}
	
	public static void clear() {
		if(local.get() != null) {
			local.remove();
		}
	}
	
	private String token;
	private String user;
	private String GUID;
	private Locale locale;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
