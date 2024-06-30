package com.hearus.hearusspring.config;

import com.hearus.hearusspring.common.enumType.RoleType;
import com.hearus.hearusspring.config.securityfilter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    @Autowired
    private CorsConfig corsConfig;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilter(corsConfig.corsFilter())
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .securityMatcher("/api/v1/main")
                .securityMatcher("/api/v1/schedule")
                .securityMatcher("/api/v1/lecture")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().hasRole(RoleType.USER.getKey())
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}