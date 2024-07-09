package com.hearus.hearusspring.config.securityfilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.common.security.JwtTokenProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class JwtFilter implements Filter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        // 필터 로직을 수행하지 않고 다음 필터로 이동
<<<<<<< HEAD
        if (!requestURI.startsWith("/api/v1/main") && !requestURI.startsWith("/api/v1/schedule") && !requestURI.startsWith("/api/v1/lecture")) {
=======
        if (!FilterConfig.isFilteringUri(requestURI)) {
>>>>>>> 68f2e9db34ff1805601540d0e7b3f71dc4cb6149
            chain.doFilter(request, response);
            return;
        }

        // 인증이 필요한 Route의 경우 필터 로직 수행
        // Bearer Token이 들어온 경우만을 가정하고 Authorization Header를 Split
        String token = httpRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.split(" ")[1];
            if (jwtTokenProvider.validateAccessToken(token)) {
                // 인증 정보를 SecurityContext에 저장
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(jwtTokenProvider.getTokenInfo(token), null, Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
                return;
            }
        }
        // 토큰이 유효하지 않은 경우 CommonResponse로 응답
        ObjectMapper objectMapper = new ObjectMapper();
        CommonResponse commonResponse = new CommonResponse(false, HttpStatus.UNAUTHORIZED, "Invalid Access Token", null);
        String jsonResponse = objectMapper.writeValueAsString(commonResponse);

        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpResponse.setContentType("application/json");
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.getWriter().write(jsonResponse);
    }
}
