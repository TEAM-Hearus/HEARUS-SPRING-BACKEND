package com.hearus.hearusspring.config.securityfilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtFilter> JwtFilterRegistration() {
        FilterRegistrationBean<JwtFilter> bean = new FilterRegistrationBean<>(new JwtFilter());
        // Filter를 적용할 Route 설정
        bean.addUrlPatterns("/api/v1/main");
        bean.addUrlPatterns("/api/v1/schedule");
        bean.addUrlPatterns("/api/v1/lecture");
        bean.setOrder(0);
        return bean;
    }
}