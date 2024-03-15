package com.hearus.hearusspring.common.enumType;

public enum GradeType {

    FRESHMEN("FRESHMEN"),
    SOPHOMORE("SOPHOMORE"),
    JUNIOR("JUNIOR"),
    SENIOR("SENIOR"),
    LEAVE_ABSENCE("LEAVE_ABSENCE"),
    GRADUATE("GRADUATE"),
    POST_GRADUATE("POST_GRADUATE");

    private final String key;

    GradeType(String key) {
        this.key = key;
    }
}