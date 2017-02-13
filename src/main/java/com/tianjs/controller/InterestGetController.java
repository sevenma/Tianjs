package com.tianjs.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tianjs.model.InterestResponse;
import com.tianjs.model.User;
import com.tianjs.restful.HttpSendUtil;

@Controller
public class InterestGetController {
	private static final Logger _log = Logger.getLogger(InterestGetController.class);
	@Value("${InterestGet_URL}")
	private String interestGet_URL;
	
	@RequestMapping("/getInterest")
	public String getInterest(HttpServletRequest request,Model model,HttpSession session, HttpServletResponse response)
			throws Exception {
		User user = (User) session.getAttribute("user");
		System.out.println(user.getUsername());
//		String djUrl = "http://101.201.47.148:8080/nasdaq/product/interestGet?user=";
		String result = "";
		try {
			_log.info("");
			result = HttpSendUtil.sendHttp(interestGet_URL,"121");
		} catch (Exception ee) {
			_log.info("");
			throw new Exception("调用查询利息接口异常" + ee.getMessage());
		}

		InterestResponse interestResponse = new InterestResponse();
		JSONObject resultObj = new  JSONObject(result);
		// JSONObject.parseObject(result, InterestResponse.class);
		interestResponse.setInfo(resultObj.getString("info"));
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		resultMap.put("interestResponse", interestResponse);
		interestResponse.setInfo("2.3");
		model.addAttribute("interestResponse", interestResponse);
		// return "index";
		// return "redirect:/interest";
		return "interest";
	}
	
	 @RequestMapping("/profile")
   public String test( Model model) throws Exception {
		 System.out.println("测试权限");
		 return "profile";
	 }
}
