package com.hearus.hearusspring.config.securityfilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
public class FilterConfig {

    private static List<String> filteringUris = new ArrayList<>(){{
        add("/api/v1/main");
        add("/api/v1/schedule");
        add("/api/v1/oauth");
    }
    };

    public static boolean isFilteringUri(String uri){
        //필터링할 uri인지 확인
        Optional<String> matchingUri = filteringUris.stream()
                .filter(uri::startsWith)
                .findAny();

        return matchingUri.isPresent();
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> JwtFilterRegistration() {
        FilterRegistrationBean<JwtFilter> bean = new FilterRegistrationBean<>(new JwtFilter());
        // Filter를 적용할 Route 설정
        filteringUris.stream().forEach(uri -> bean.addUrlPatterns(uri));

        bean.setOrder(0);
        return bean;
    }

}