package com.tianjs.mq;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.jms.Connection;
import javax.jms.Queue;

@Component
public class FastFailProducer {
	
	private Logger logger = Logger.getLogger(FastFailProducer.class);
	volatile boolean connected = false;
	private ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
			"failover:(tcp://localhost:61616)?timeout=5000");;
	private static FailoverListener failoverListener;
	private Connection connection;
	private Session session;
	private Queue queue;
	private MessageProducer messageProducer;
	private MessageConsumer consumer;
	
	public void init() throws JMSException {
		logger.info("正在创建MQ连接");
	    System.out.println("!!!!!!!CONNECTING!!!!!!!!");
	    connection = factory.createConnection();
	    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    connection.start();
	    ((ActiveMQConnection) connection).addTransportListener(failoverListener);
	    queue = session.createQueue("TEST");
	    messageProducer = session.createProducer(queue);
	    consumer = session.createConsumer(queue);
	    logger.info("创建MQ连接完毕");
	    System.out.println("!!!!!!!CONNECTING COMPLETE!!!!!!!!");
	    connected = true;
	}

	public void run(String message) throws Exception {
	    // send messages
	        if (connected) {
	            if (session != null & messageProducer != null & queue != null) {
	                // send a message
	                messageProducer.send(session.createTextMessage(message));
	                System.out.println("Sent message " + message);
	            }
	        } else {
	            // execute your backup logic
	            System.out.println("Message " + message + " not sent");
	        }
	    messageProducer.close();
	    session.close();
	    connection.close();
	    System.exit(0);
	}
	
	public void disconnect() throws JMSException {
	    System.out.println("!!!!!!!DISCONNECTING!!!!!!!!");
	    consumer.close();
	    session.close();
	    session = null;
	    messageProducer.close();
	    messageProducer = null;
	    connection = null;
	    connected = false;
	    System.out.println("!!!!!!!DISCONNECTED!!!!!!!!");
	}
	
	public boolean isConnected() {
	    return connected;
	}

	public void setConnected(boolean connected) {
	    this.connected = connected;
	}
}
