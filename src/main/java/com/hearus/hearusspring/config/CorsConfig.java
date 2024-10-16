package com.hearus.hearusspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 개발 단게에서는 setAllowCredentials(false) 로 설정
        // Setting this property has an impact on how origins, originPatterns, allowedMethods and allowedHeaders are processed
        // Be aware that this option establishes a high level of trust with the configured domains
        // and also increases the surface attack of the web application
        config.setAllowCredentials(true);

        // setAllowCredentials(true)일 경우 "*"로 설정 불가
        //config.addAllowedOrigin("*");
        // 동일 출처를 판단하는 기준 중 포트 번호를 명시한 출처는
        // 브라우저의 구현 로직에 따라 동일 출처로 판단하지 않을 수 있기 때문에
        // 포트번호를 생략한 URL을 CORS 설정의 허용 출처에 추가

        config.setAllowedOrigins(Arrays.asList("http://localhost", "http://localhost:5173", "https://www.hearus.site", "https://hearus.site", "https://hearus-spring-be.shop:8080", "https://www.hearus-spring-be.shop:8080"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","OPTIONS"));
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "Cookie", "Set-Cookie", "X-Requested-With"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);

    }


}
