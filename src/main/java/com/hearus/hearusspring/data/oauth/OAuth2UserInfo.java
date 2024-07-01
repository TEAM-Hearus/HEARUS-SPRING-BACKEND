package com.hearus.hearusspring.data.oauth;


import com.hearus.hearusspring.data.entitiy.UserEntity;
import jakarta.security.auth.message.AuthException;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Map;

import static com.hearus.hearusspring.common.exception.AuthException.ILLEGAL_REGISTRATION_ID;

@Builder
@Getter
public class OAuth2UserInfo {
    private String name;
    private String email;
    private String id;

    @SneakyThrows
    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) { // registration id별로 userInfo 생성
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            case "naver" -> ofNaver(attributes);
            default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
        };
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .id((String) attributes.get("sub"))
                .build();
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .id((String) attributes.get("id"))
                .build();
    }

    private static OAuth2UserInfo ofNaver(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("response");
        return OAuth2UserInfo.builder()
                .name((String) account.get("nickname"))
                .email((String) account.get("email"))
                .id((String) account.get("id"))
                .build();
    }

}
