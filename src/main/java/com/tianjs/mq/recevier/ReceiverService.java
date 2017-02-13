package com.tianjs.mq.recevier;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
/**
 * 接收服务
 * @author yidanlin
 *
 */
@Component
public class ReceiverService {

	@JmsListener(destination = "my-mq")
    public void recevierMsg(String msg){
    	System.out.println("接受到的消息:"+msg);
    }
}
