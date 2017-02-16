package com.tianjs.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.code.kaptcha.Constants;
import com.tianjs.model.User;
import com.tianjs.service.UserService;

@Controller
public class UserController {

    private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    //跳转链接，跳转到主页
    @RequestMapping("")
    public String index(HttpServletResponse response) {
        //重定向到 /index
        return response.encodeRedirectURL("/");
    }

    @RequestMapping("/")
    public String home(Model model) {
        //对应到templates文件夹下面的index
        return "home";
    }

    //进入注册页面，使用Get请求
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registerGet() {
        return "register";
    }

    //注册用户，使用POST，传输数据
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registerPost(Model model,HttpServletRequest request,
                               //这里和模板中的th:object="${user}"对应起来
                               @ModelAttribute(value = "user") User user,
                               HttpServletResponse response) {
    	String kaptcha = request.getParameter("kaptcha");
        String captcha = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
	    System.out.println("用户输入的验证码是："+kaptcha+";系统生成的验证码是："+captcha);
		
		if("".equals(kaptcha)){
			model.addAttribute("result", "请输入验证码");
        	return "register";
		}else if(!captcha.equals(kaptcha)){
			model.addAttribute("result", "验证码错误");
        	return "register";
		}
        //使用userService处理业务
        String result = userService.save(user);
        if("注册成功".equals(result)){
        	//将结果放入model中，在模板中可以取到model中的值
//        	model.addAttribute("result", result);
        	logger.info("注册成功");
        	return response.encodeRedirectURL("/login");
        }else{
        	model.addAttribute("result", result);
        	return "register";
        }
    }

    @RequestMapping("/login")
    public String login(@ModelAttribute(value = "user") User user,Model model,HttpSession session,HttpServletRequest request,HttpServletResponse response){
      if(user.isEmpty()) {
        return "login";
      }else {
    	String kaptcha = request.getParameter("kaptcha");
        String captcha = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
  	    System.out.println("用户输入的验证码是："+kaptcha+";系统生成的验证码是："+captcha);
  		
  		if("".equals(kaptcha)){
  			model.addAttribute("result", "请输入验证码");
          	return "login";
  		}else if(!captcha.equals(kaptcha)){
  			model.addAttribute("result", "验证码错误");
          	return "login";
  		}
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
          subject.login(token);
         //登录后，可以用如下方式取得session  设置超时时间
          SecurityUtils.getSubject().getSession().setTimeout(600000); 
        }catch (UnknownAccountException|IncorrectCredentialsException e){
          model.addAttribute("result","用户名/密码错误");
          return "login";
        }catch (AuthenticationException e){
          model.addAttribute("result","用户被锁定");
          return "login";
        }
        //添加到session中，session是一次会话，在本次会话中都可以取到session中的值
        //若是session中有用户存在则会覆盖原来的user，当session中的user存在时判定用户存在
        session.setAttribute("user",user);
        logger.info("用户:"+user.getUsername()+"登录成功");
        return response.encodeRedirectURL("/home");
      }
    }

    @RequestMapping("/loginOut")
    public String logout(){
      Subject subject = SecurityUtils.getSubject();
      if (subject.isAuthenticated()){
        subject.logout();
      }
      return "home";
    }
}
