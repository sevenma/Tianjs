package com.tianjs.mq;

import java.io.IOException;

import javax.jms.JMSException;

import org.apache.activemq.transport.TransportListener;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class FailoverListener implements TransportListener {

	private Logger logger = Logger.getLogger(FailoverListener.class);

	private FastFailProducer failProducer;

	public FailoverListener(FastFailProducer failProducer) {
		super();
		this.failProducer = failProducer;
	}

	public FailoverListener() {
		super();
	}

	@Override
	public void onCommand(Object arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onException(IOException arg0) {
		logger.error("onException -> 消息服务器连接错误......", arg0);

	}

	@Override
	public void transportInterupted() {
		logger.warn("transportInterupted -> 消息服务器连接发生中断...");
		try {
			failProducer.disconnect();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void transportResumed() {
		logger.info("transportResumed -> 消息服务器连接已恢复...");
		if (!failProducer.isConnected()) {
			try {
				failProducer.init();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
