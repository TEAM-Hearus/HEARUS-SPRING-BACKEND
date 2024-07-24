package com.hearus.hearusspring.data.oauth.callback.service;


import jakarta.security.auth.message.AuthException;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Map;

import static com.hearus.hearusspring.common.exception.AuthException.ILLEGAL_REGISTRATION_ID;

@Builder
@Getter
public class OAuth2UserInfoMaker {
    private String name;
    private String email;
    private String id;

    @SneakyThrows
    public static OAuth2UserInfoMaker of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) { // registration id별로 userInfo 생성
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            case "naver" -> ofNaver(attributes);
            default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
        };
    }

    private static OAuth2UserInfoMaker ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfoMaker.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .id((String) attributes.get("sub"))
                .build();
    }

    private static OAuth2UserInfoMaker ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfoMaker.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .id((String) attributes.get("id"))
                .build();
    }

    private static OAuth2UserInfoMaker ofNaver(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("response");
        return OAuth2UserInfoMaker.builder()
                .name((String) account.get("nickname"))
                .email((String) account.get("email"))
                .id((String) account.get("id"))
                .build();
    }

}
