package org.example.spring_boot2.data.config;

import org.example.spring_boot2.data.interceptor.UrlCountInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author lifei
 */
@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    @Resource
    UrlCountInterceptor urlCountInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(urlCountInterceptor)
                .addPathPatterns("/mybatis/**")
                .addPathPatterns("/mybatis_plus/**");
    }
}
