package com.tianjs.shiro;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{

  @Bean
  public ViewResolver viewResolver() {
    FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
    resolver.setCache(true);
    resolver.setPrefix("");
    resolver.setSuffix(".html");
    resolver.setContentType("text/html; charset=UTF-8");
    return resolver;
  }
}
