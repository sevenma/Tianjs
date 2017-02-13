package com.tianjs.mq.send;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

/**
 * 发送服务
 * @author yidanlin
 *
 */
@Component
public class SenderService {

	@Autowired
	JmsTemplate  jmsTemplate;
	
	public void sendMsg(){
		
		jmsTemplate.send("my-mq", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
            	Message textMessage = session.createTextMessage("村上春树");
                return textMessage;
            }
        });
	}
	
}
