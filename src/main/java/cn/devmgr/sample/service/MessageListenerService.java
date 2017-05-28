package cn.devmgr.sample.service;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

@Service
public class MessageListenerService {
	private final static Log log = LogFactory.getLog(MessageListenerService.class);
	
	private static final String ACTIVEMQ_QUEUE = "testSpringQueue";
	
    @JmsListener(destination = ACTIVEMQ_QUEUE)
	public void onMesageReceived(Message<Object> message){
		if(log.isTraceEnabled()){
			log.trace("onMessageReceived()");
			
		}
        MessageHeaders headers =  message.getHeaders();
        
        log.trace("onMesageReceived : headers received : {}" + headers);
         
        Object response = message.getPayload();
        if(response instanceof Map){
        	Map<?, ?> map = (Map<?, ?>) response;
        	log.trace("onMesageReceived  MESSAGE: id=" + map.get("orderId") + "; date=" + map.get("date"));
        }
        log.trace("onMesageReceived : response received : {}" + response.getClass().getName() + response);
         
        

	}
}
