package com.hearus.hearusspring.data.entitiy.enumType;

public enum GradeType {

    FRESHMEN("F_1"),
    SOPHOMORE("S_2"),
    JUNIOR("J_3"),
    SENIOR("S_4"),
    LEAVE_ABSENCE("LA"),
    GRADUATE("G"),
    POST_GRADUATE("PG");

    private final String typeKey;

    GradeType(String itemTypeKey) {
        this.typeKey = itemTypeKey;
    }

    // String인 typeKey를 넣으면 GradeType으로 변환
    public static GradeType fromString(String itemTypeKey) {
        for (GradeType i : GradeType.values()) {
            if (i.typeKey.equalsIgnoreCase(itemTypeKey)) {
                return i;
            }
        }
        return  null;
    }
}