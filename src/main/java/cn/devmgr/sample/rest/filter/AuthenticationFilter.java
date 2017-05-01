package  cn.devmgr.sample.rest.filter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.devmgr.sample.component.RequestContext;



/**
 * 身份验证  
 **/
@ Provider
public class AuthenticationFilter implements ContainerRequestFilter{
	private static final Log log = LogFactory.getLog(AuthenticationFilter.class);
	@Context
	private ResourceInfo resourceInfo;
	@Context
	private HttpServletRequest servletRequest;
	
	@Autowired
	private RequestContext requestContext;
	
	public static final String WXOPENID = "WXOPENID";
	public static final String ROLE = "ROLE";
	public static final String OAUTHACCESSTOKEN = "OAUTHACCESSTOKEN";
	
	@Override
	public void filter(ContainerRequestContext containerRequestContext) {
		Method method = resourceInfo.getResourceMethod();
		if(log.isTraceEnabled()){
			log.trace("Authentication filter... " + method.getName());
		}
		requestContext.init(servletRequest);

		// Access denied for all
		if (method.isAnnotationPresent(DenyAll.class)) {
			containerRequestContext.abortWith(getResponse("DenyAll,此方法禁止任何人访问"));
			if(log.isTraceEnabled()){
				log.trace("DenyAll, request forbidden.");
			}
			return;
		}
		
		List<String> roles = requestContext.getLogin().getRoles(); 
		if (!method.isAnnotationPresent(PermitAll.class)) {
			// Verify user access
			if (method.isAnnotationPresent(RolesAllowed.class)) {
				RolesAllowed rolesAnnotation = method
						.getAnnotation(RolesAllowed.class);
				Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

				// Is user valid?
				boolean valid = false;
				for(String role : roles){
					if(!rolesSet.contains(role)){
						valid = true;
						break;
					}
				}
				if(!valid){
					containerRequestContext.abortWith(getResponse("此方法仅允许" + rolesSet + "访问。"));
					return;
				}
			}
		}else{
			if(log.isTraceEnabled()){
				log.trace("Permit All...");
			}
		}
	
	}
	/**
	 * 不可访问的response
	 * @param developerMessage
	 * @return
	 */
	private Response getResponse(String developerMessage){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("message", "请登录后访问。");
		if(developerMessage != null){
			map.put("developerMessage", developerMessage);
		}
		return Response
				.status(Response.Status.UNAUTHORIZED)
				.entity(map).type(MediaType.APPLICATION_JSON).build();
	}
}
