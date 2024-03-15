package com.hearus.hearusspring.common.enumType;

public enum OAuthType {
    KAKAO("KAKAO"),
    GOOGLE("GOOGLE"),
    NAVER("NAVER");

    private final String key;
    OAuthType(String key) {
        this.key = key;
    }
}
