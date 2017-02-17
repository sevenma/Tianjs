package com.tianjs;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.pagehelper.PageHelper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
//@EnableCaching
@MapperScan("com.tianjs.mapper")
public class Application  extends WebMvcConfigurerAdapter {
    private static Logger logger = Logger.getLogger(Application.class);

    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        return new org.apache.tomcat.jdbc.pool.DataSource();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
       // 分页插件 
    	PageHelper pageHelper = new PageHelper(); 
    	Properties properties = new Properties(); 
    	properties.setProperty("reasonable", "true"); 
    	properties.setProperty("supportMethodsArguments", "true"); 
    	properties.setProperty("returnPageInfo", "check"); 
    	properties.setProperty("params", "count=countSql"); 
        pageHelper.setProperties(properties); 
        //添加插件 
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    
    /*
     * ehcache 主要的管理器
     */
//    @Bean(name = "appEhCacheCacheManager")
//    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean bean){
//        return new EhCacheCacheManager (bean.getObject ());
//    }

    /*
     * 据shared与否的设置,Spring分别通过CacheManager.create()或new CacheManager()方式来创建一个ehcache基地.
     */
//    @Bean
//    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){
//        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean ();
//        cacheManagerFactoryBean.setConfigLocation (new ClassPathResource ("ehcache.xml"));
//        cacheManagerFactoryBean.setShared (true);
//        return cacheManagerFactoryBean;
//    }

    
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
      registry.addViewController("/").setViewName("home");
      registry.addViewController("/error").setViewName("error");
      registry.addViewController("/profile").setViewName("profile");
      registry.addViewController("/findproduct").setViewName("product");
      registry.addViewController("/productmq").setViewName("product");
      registry.addViewController("/getInterest").setViewName("interest");
      registry.addViewController("/registration").setViewName("register");
      registry.addViewController("/login").setViewName("login");
      registry.addViewController("/loginOut").setViewName("home");
      registry.addViewController("/kaptcha").setViewName(null);
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
      return container -> container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error"), new ErrorPage(HttpStatus.FORBIDDEN, "/error"));
    }
    
    @Bean
    public DefaultKaptcha captchaProducer() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "yes");
        properties.put("kaptcha.border.color", "lightGray");
        properties.put("kaptcha.textproducer.font.color", "blue");
        properties.put("kaptcha.image.width", "160");
        properties.put("kaptcha.image.height", "50");
        properties.put("kaptcha.textproducer.font.size", "40");
        properties.put("kaptcha.session.key", "kaptcha");
        properties.put("kaptcha.textproducer.char.length", "4");
        properties.put("kaptcha.background.clear.to", "gray");
        properties.put("kaptcha.noise.color", "black");//增加干扰线
//        properties.put("kaptcha.textproducer.font.size", "50px.");
//        properties.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");//去掉干扰线
        properties.put("kaptcha.textproducer.char.space", "6");
        properties.put("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");

        Config config = new Config(properties);
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);

        return kaptcha;
    }

    /**
     * Start
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("SpringBoot Start Success");
    }


}
