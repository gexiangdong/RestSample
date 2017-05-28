package cn.devmgr.sample.rest.resource;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;

import cn.devmgr.sample.domain.Order;
import cn.devmgr.sample.domain.User;
import cn.devmgr.sample.rest.exception.GenericException;
import cn.devmgr.sample.service.OrderService;


@Controller
@Path("/samples/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class SampleResource {
	private static final Log log = LogFactory.getLog(SampleResource.class);
	
	private @Value("${fileFolder}") String fileFolder;

	// @Resource注解是JSR-250标准；可替代spring @Autowired
	@Resource
	private OrderService orderService;
	
	@Resource
	private JmsTemplate jmsTemplate;

	@GET
	@Path("/hello")
	@PermitAll
	public Response sayHello() {
		if(log.isTraceEnabled()){
			log.trace("sayHello()");
		}
		Order order = orderService.getOneOrder(1);
		String result = "";
		if(order != null){
			result = "hello, " + " from " + order.getConsigneeAddress() + " at " + order.getOrderDate() + " \r\nid=" + order.getId() + "\r\n";
		}else{
			result = "order is null....";
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("orderId",  order.getId());
		map.put("date", new Date().getTime());
	    jmsTemplate.convertAndSend("order-response-queue", map);
       
	   
		result += "<br/> config.properties: fileFolder=" + fileFolder;
		return Response.status(200).entity(result).build();
	}
	
    @Path("/order/{id:\\d+}/") 
    @GET
	@RolesAllowed({"admin","user"})
	public Map<String, Object> getOrder(@PathParam("id") int id, @Context SecurityContext sc) throws GenericException{
    	if(log.isTraceEnabled()){
			log.trace("readArray()" + id + "; requestContext is " + sc.getClass().getName() + "; user=" + sc.getUserPrincipal() + "  ");
		}
    	Order order = orderService.getOrder(id);
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	result.put("order",  order);
    	result.put("otherthings", "no");
		// result.put("user", requestContext.getUser().getName() + "," +
		// requestContext.getUser().getId());
    	result.put("user",  sc.getUserPrincipal());
    	return result;
    }
	
    @PermitAll
    @Path("/order/")
    @POST
    public Map<String, Object> createOrder(@Valid Order order){
    	orderService.append(order);
    	Map<String, Object> result = new HashMap<String, Object>();
    	result.put("order",  order);
    	return result;
    }
    
    
	@PermitAll
	@Path("/users/") 
    @GET 
	public List<User> readPingjiaHistories(){
		return getPingjiaHistories(null, null);
	}
	
	@RolesAllowed({"admin","users"})
	@Path("/histories/{beginDate:\\d{4}-\\d{2}-\\d{2}},{endDate:\\d{4}-\\d{2}-\\d{2}}/") 
    @GET 
	public List<User> readPingjiaHistories(@PathParam("beginDate") String beginDate, 
    		@PathParam("endDate")  String endDate) throws ParseException {
		if(log.isTraceEnabled()){
			log.trace("readPingjiaHistories(" + beginDate + " ~ " + endDate + ")");
		}
		return getPingjiaHistories(null, null);
	}
	
	
	private List<User> getPingjiaHistories(Date beginDate, Date endDate){
		ArrayList<User> list = new ArrayList<User>();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		for(int i=0; i< 20; i++){
			User user = new User();
			list.add(user);
		}
		
		return list;
	}


	@POST
	@DenyAll
	public Map<String, Object> createSomething(@NotNull @FormParam("name") String name, @FormParam("score") int score){
		if(log.isTraceEnabled()){
			log.trace("createSomething......" + name + "; " + score);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", 888);
		result.put("name", name);
		result.put("score", score);
		return result;
	}
	
    @Path("/array/{id:\\d+}/") 
    @GET
    public Map<String, Object> readArray(@Context HttpHeaders hh, 
    		@QueryParam("q") String q, 
    		@HeaderParam("user-agent") String userAgent,  
    		@Context HttpServletRequest request,
    		@PathParam("id") int id) throws GenericException{
    	if(log.isTraceEnabled()){
			log.trace("readArray()" + id);
		}
    	if(id == 0){
    		throw new WebApplicationException(401);
    	}else if(id == 1){
    		throw new RuntimeException("iD=1 Exception");
    	}else if(id == 2){
    		throw  new GenericException(401, "用户未登录。");
    	}else if(id == 3){
    		GenericException ge = new GenericException(401, "用户未登录。", "请先调用登录接口，然后在http header中传递access_token参数", "http://192.168.1.9/wiki/login/accesstoken");
    		throw ge;
    	}else if(id == 4){
    		GenericException ge = new GenericException(411, "用户未登录。", "请先调用登录接口，然后在http header中传递access_token参数", "http://192.168.1.9/wiki/login/accesstoken");
    		String[] errInfo =  new String[]{"第一条信息长度不足20字。", "第2张图片尺寸太小。", "第三章图片文件过大。"};
    		ge.putAdditionMessage("detail", errInfo);
    		throw ge;
    	}
    	Map<String, Object> result = new HashMap<String, Object>();
    	String[] ary = new String[5];
    	for(int i=0; i<ary.length; i++){
    		ary[i] = "ary-" + i + "/" + id;
    	}
    	result.put("data", ary);
    	result.put("message", "OK");
    	result.put("count", 5);
    	result.put("q",  q);
    	result.put("sessionId", request.getSession().getId());
    	//result.put("user-agent", hh.getRequestHeader("user-agent").get(0));
    	result.put("user-agent", userAgent);
    	return result;
    }

}
