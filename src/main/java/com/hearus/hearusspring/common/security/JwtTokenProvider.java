package com.hearus.hearusspring.common.security;

import com.hearus.hearusspring.common.environment.ConfigUtil;
import com.hearus.hearusspring.data.dto.TokenDTO;
import com.hearus.hearusspring.data.dto.user.UserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key accessKey;
    private final Key refreshKey;

    @Autowired
    public JwtTokenProvider(ConfigUtil configUtil){
        byte[] accessByte = Decoders.BASE64.decode(configUtil.getProperty("JWT_ACCESS_SECRET"));
        byte[] refreshByte = Decoders.BASE64.decode(configUtil.getProperty("JWT_REFRESH_SECRET"));

        this.accessKey = Keys.hmacShaKeyFor(accessByte);
        this.refreshKey = Keys.hmacShaKeyFor(refreshByte);
    }

    // 유저 정보를 통해 Access Token, Refresh Token 생성하는 매소드
    public TokenDTO generateToken(UserDTO userDTO) {

        long now = (new Date()).getTime();
        // Access Token 생성
        // subject는 User의 ID
        // Access Token의 유효기간은 1시간
//        Date accessTokenExpiresIn = new Date(now + 3600000);
        // TODO : 임의로 테스트를 위해 Access Token의 exp를 1일로 설정 (추후 실 사용시 1시간으로 변경)
        Date accessTokenExpiresIn = new Date(now + 864000000);
        String accessToken = Jwts.builder()
                .setSubject(userDTO.getUserId())
                .claim("role", userDTO.getUserRole())
                .setExpiration(accessTokenExpiresIn)
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        // Refresh Token의 유효기간은 1일
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 86400000))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();

        return TokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰을 복호화하여 정보를 꺼내는 메소드
    public String getTokenInfo(String accessToken) {
        // 토큰 복호화
        Claims claims = parseAccessClaims(accessToken);

        if (claims.get("role") == null) {
            log.info("[JwtTokenProvider]-[getAuthentication] Failed : Token has no Role");
            throw new RuntimeException("Token has no Role");
        }

        // UserId인 claim의 subject를 return
        return claims.getSubject();
    }

    // Access Token 정보 검증
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("[JwtTokenProvider]-[validateAccessToken] Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("[JwtTokenProvider]-[validateAccessToken] Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("[JwtTokenProvider]-[validateAccessToken] Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("[JwtTokenProvider]-[validateAccessToken] JWT claims string is empty");
        }
        return false;
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("[JwtTokenProvider]-[validateRefreshToken] Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("[JwtTokenProvider]-[validateRefreshToken] Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("[JwtTokenProvider]-[validateRefreshToken] Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("[JwtTokenProvider]-[validateRefreshToken] JWT claims string is empty");
        }
        return false;
    }

    private Claims parseAccessClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
