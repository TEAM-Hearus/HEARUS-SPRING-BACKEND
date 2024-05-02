package com.hearus.hearusspring.config;

import com.hearus.hearusspring.common.enumType.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // TODO : Spring Security 설정, CSRF 등
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // TODO : main API 구현시 Spring Security 설정
                .securityMatcher("/api/v1/main")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().hasRole(RoleType.USER_FREE.getKey())
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}