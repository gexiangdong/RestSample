package cn.devmgr.sample.component.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import cn.devmgr.sample.component.RequestContext;
import cn.devmgr.sample.component.Login;

@Component
@Qualifier("requestContext") 
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES) 
public class RequestContextImpl implements RequestContext {
	private static final Log log = LogFactory.getLog(RequestContextImpl.class);

	private Login user;
	private String accessToken;
	private String ip;
	private HttpServletRequest request;

	@Override
	public void init(HttpServletRequest request) {
		this.request = request;
		this.ip = request.getRemoteAddr();
		
		String accessToken = request.getHeader("access-token");
		if(accessToken == null){
			//尝试从cookie获取
			Cookie[] cookie = request.getCookies();
            if(cookie!=null){
	            for (Cookie cookie2 : cookie) {
					if("accessToken".equals(cookie2.getName())){
						accessToken = cookie2.getValue();
						break;
					}
				}
            }
		}
		if(log.isTraceEnabled()){
			log.trace("access token is " + accessToken);
		}
		this.accessToken = accessToken;
		// If no authorization information present; block access
		if (accessToken != null && accessToken.trim().length() > 0) {
			//设置当前用户为guest
			UserImpl guest = new UserImpl();
			List<String> roles = new ArrayList<String>();
			roles.add("guest");
			guest.setName("guest");
			guest.setRoles(roles);
			this.user = guest;
		}else{
			//TODO:设置当前用户
			UserImpl user = new UserImpl();
			List<String> roles = new ArrayList<String>();
			roles.add("user");
			roles.add("admin");
			user.setRoles(roles);
			this.user = user;
			this.user.setName("Wyatt");
		}
	}
	
	
	public Login getLogin() {
		return user;
	}
	public void setLogin(Login user) {
		this.user = user;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	
}
