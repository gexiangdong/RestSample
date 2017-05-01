package cn.devmgr.sample.component;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component("requestContext")
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES) 
public interface RequestContext {
	
	public Login getLogin();
	public void setLogin(Login user);
	
	public String getAccessToken();
	public void setAccessToken(String accessToken);
	
	public String getIp();
	public void setIp(String ip);
	
	public HttpServletRequest getRequest();
	public void setRequest(HttpServletRequest request);
	
	public void init(HttpServletRequest request);
}
