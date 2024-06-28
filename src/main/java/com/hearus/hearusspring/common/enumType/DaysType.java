package com.hearus.hearusspring.common.enumType;

public enum DaysType {
    MON("MON"),
    TUE("TUE"),
    WED("WED"),
    THU("THU"),
    FRI("FRI"),
    SAT("SAT"),
    SUN("SUN");

    private final String key;

    DaysType(String key) {
        this.key = key;
    }
}
