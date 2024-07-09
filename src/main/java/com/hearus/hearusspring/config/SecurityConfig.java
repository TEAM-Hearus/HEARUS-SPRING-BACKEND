package com.hearus.hearusspring.config;

import com.hearus.hearusspring.common.enumType.RoleType;
import com.hearus.hearusspring.config.securityfilter.JwtFilter;
import com.hearus.hearusspring.data.oauth.handler.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.regex.Matcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{


    private final CorsConfig corsConfig;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2UserService oAuth2UserService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable) // 403 에러 범인 csrf 비활성화 -> cookie를 사용하지 않으면 꺼도 된다. (cookie를 사용할 경우 httpOnly(XSS 방어), sameSite(CSRF 방어)로 방어해야 한다.)
                .formLogin(AbstractHttpConfigurer::disable) // 기본 폼 로그인 사용 X

                //.addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class) jwt 필터 두번 적용으로 삭제.
                .addFilter(corsConfig.corsFilter())
<<<<<<< HEAD
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .securityMatcher("/api/v1/main")
                .securityMatcher("/api/v1/schedule")
                .securityMatcher("/api/v1/lecture")
=======
>>>>>>> 68f2e9db34ff1805601540d0e7b3f71dc4cb6149
                .authorizeHttpRequests(authorize -> authorize
                        //        .requestMatchers("/**").permitAll()
                        .requestMatchers("/**").permitAll() //security 에서는 별도의 필터를 적용하지 않는다. JWT Filter 에서 필터링 적용.

                )
                .httpBasic(Customizer.withDefaults())

                //OAUth 2.0
                .oauth2Login(oauth -> 
                        // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정을 담당
                        oauth
                                .userInfoEndpoint(c -> c.userService(oAuth2UserService))
                                // 로그인 or 회원가입 핸들러
                                .successHandler(oAuth2SuccessHandler)
                );

        return http.build();
    }
}