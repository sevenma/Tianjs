package com.tianjs.restful;

import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

@SuppressWarnings("deprecation")
public class HttpSendUtil {
	private static final Logger _log = Logger.getLogger(HttpSendUtil.class);
	 
	@SuppressWarnings({ "resource" })
	public static String sendHttp(String url,String json) throws Exception{
        String result = "";
        try {
        	HttpPost httpPost = new HttpPost(url); 
	        StringEntity  entity = new StringEntity(json, HTTP.UTF_8);
	        entity.setContentEncoding("UTF-8");
	        entity.setContentType("application/json");
	        httpPost.setEntity(entity);
	        HttpClient client1 = new DefaultHttpClient();
	        
	        //请求超时
	        client1.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000); 
	        //读取超时
	        client1.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
			HttpResponse  response = client1.execute(httpPost);
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
		}catch(ConnectTimeoutException e){
			_log.error("服务器请求"+url+"超时"+e.getMessage());
			throw new ConnectTimeoutException("服务器请求超时");
		}catch(SocketTimeoutException eee){
			_log.error(url+"响应失败"+eee.getMessage());
			throw new SocketTimeoutException("响应失败");
		}catch(Exception ee){
			_log.error("调用"+url+"异常："+ee.getMessage());
			throw new Exception("其他异常");
		}
		return result;
	}
}	
