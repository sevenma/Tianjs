package com.tianjs.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.tianjs.service.UserService;

@Configuration
public class ShiroConfiguration {
	
	 private static final Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

	    public EhCacheManager getEhCacheManager() {  
	        EhCacheManager em = new EhCacheManager();  
	        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");  
	        return em;  
	    }  

	    /**
	 * 注册DelegatingFilterProxy（Shiro）
	 *
	 * @param dispatcherServlet
	 * @return
	 * @author YIDANLIN
	 * @create 2016年1月13日
	 */
	    @Bean
	    public FilterRegistrationBean filterRegistrationBean() {
	        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
	        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
	        // 该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理 
	        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
	        filterRegistration.setEnabled(true);
	        filterRegistration.addUrlPatterns("/*");
	        return filterRegistration;
	    }

	    @Bean(name = "lifecycleBeanPostProcessor")
	    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
	        return new LifecycleBeanPostProcessor();
	    }

	    @Bean
	    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
	        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
	        daap.setProxyTargetClass(true);
	        return daap;
	    }

	    @Bean(name = "securityManager")
	    public DefaultWebSecurityManager getDefaultWebSecurityManager(MyShiroRealm myShiroRealm) {
	        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
	        dwsm.setRealm(myShiroRealm);
	// <!-- 用户授权/认证信息Cache, 采用EhCache 缓存 --> 
	        dwsm.setCacheManager(getEhCacheManager());
	        return dwsm;
	    }

	    @Bean
	    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(MyShiroRealm myShiroRealm) {
	        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
	        aasa.setSecurityManager(getDefaultWebSecurityManager(myShiroRealm));
	        return new AuthorizationAttributeSourceAdvisor();
	    }

	    /**
	 * 加载shiroFilter权限控制规则（从数据库读取然后配置）
	 *
	 * @author YIDANLIN
	 * @create 2016年1月14日
	 */
	    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean){
	        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
	       
	        // 配置退出过滤器,其中的具体代码Shiro已经替我们实现了
	        filterChainDefinitionMap.put("/logout", "logout");
	        // <!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
	        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
	        filterChainDefinitionMap.put("/css/**", "anon");
	        filterChainDefinitionMap.put("/js/**", "anon");
	        filterChainDefinitionMap.put("/img/**", "anon");
	        filterChainDefinitionMap.put("/fonts/**", "anon");
	        filterChainDefinitionMap.put("/login", "anon");
	        filterChainDefinitionMap.put("/registration", "anon");
	        filterChainDefinitionMap.put("/", "anon");//anon 可以理解为不拦截
	        // anon：它对应的过滤器里面是空的,什么都没做
	        logger.info("##################从数据库读取权限规则，加载到shiroFilter中##################");
//	        userService.findPermissions("");
	        filterChainDefinitionMap.put("/profile", "authc,perms[user:query]");//指定什么权限才能访问某个url 暂时写死
	        filterChainDefinitionMap.put("/**", "authc");

	        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
	    }

	    /**
	 * ShiroFilter<br/>
	 * 注意这里参数中的 StudentService 和 IScoreDao 只是一个例子，因为我们在这里可以用这样的方式获取到相关访问数据库的对象，
	 * 然后读取数据库相关配置，配置到 shiroFilterFactoryBean 的访问规则中。实际项目中，请使用自己的Service来处理业务逻辑。
	 *
	 * @param myShiroRealm
	 * @param stuService
	 * @param scoreDao
	 * @return
	 * @author YIDANLIN
	 * @create 2016年1月14日
	 */
	    @Bean(name = "shiroFilter")
	    public ShiroFilterFactoryBean getShiroFilterFactoryBean(MyShiroRealm myShiroRealm, UserService userService) {

	        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
	        // 必须设置 SecurityManager O
	        shiroFilterFactoryBean.setSecurityManager(getDefaultWebSecurityManager(myShiroRealm));
	      
	        // authc：该过滤器下的页面必须验证后才能访问，它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
	        // 如果不设置默认会自动寻找Web工程根目录下的"/login"页面
	        shiroFilterFactoryBean.setLoginUrl("/login");
	        // 登录成功后要跳转的链接
	        shiroFilterFactoryBean.setSuccessUrl("/home");
	        // 未授权界面;
	        shiroFilterFactoryBean.setUnauthorizedUrl("/");

	        loadShiroFilterChain(shiroFilterFactoryBean);
	        return shiroFilterFactoryBean;
	    }


}
