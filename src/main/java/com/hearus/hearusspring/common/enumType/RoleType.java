package com.hearus.hearusspring.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    USER_FREE("ROLE","무료 Tier 사용자"),
    USER_PREM("ROLE_PREM","프리미엄 Tier 사용자"),
    ADMIN("ROLE_ADMIN","관리자");

    private final String key;
    private final String title;
}
