package com.hearus.hearusspring.data.entitiy.enumType;

public enum OAuthType {
    KAKAO("K"),
    GOOGLE("G"),
    NAVER("N");

    private final String typeKey;

    OAuthType(String itemTypeKey) {
        this.typeKey = itemTypeKey;
    }

    public static OAuthType fromString(String itemTypeKey) {
        for (OAuthType i : OAuthType.values()) {
            if (i.typeKey.equalsIgnoreCase(itemTypeKey)) {
                return i;
            }
        }
        return  null;
    }
}
