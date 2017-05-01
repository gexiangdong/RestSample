package cn.devmgr.sample.rest.app;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import cn.devmgr.sample.rest.exception.GenericExceptionMapper;
import cn.devmgr.sample.rest.exception.JsonMappingExceptionMapper;
import cn.devmgr.sample.rest.exception.NullPointerExceptionMapper;
import cn.devmgr.sample.rest.filter.AuthenticationFilter;


public class SampleAppConfig extends ResourceConfig {

	public SampleAppConfig() {
		//设置哪些包下面的类会用于REST API
		packages("cn.devmgr.sample.rest.resource");
		register(JacksonJsonProvider.class);
		register(MultiPartFeature.class);
		
		//登记身份验证过滤器，类上增加Provider注解，这里不需要登记(使用spring+jersey后，Provider注解不起作用了，需要手工登记)
		//Provider不起作用是jersey < v2.5 bug; https://java.net/jira/browse/JERSEY-2175
		//this should be helpful  http://stackoverflow.com/questions/25905941/jersey-global-exceptionhandler-doesnt-work
		register(AuthenticationFilter.class);
		//登记异常转换用类
		register(NullPointerExceptionMapper.class);
		register(JsonMappingExceptionMapper.class);
		register(GenericExceptionMapper.class);
	}
}
