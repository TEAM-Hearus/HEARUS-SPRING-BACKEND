package com.hearus.hearusspring.common.enumType;

public enum ProblemType {
    // MultipleChoice : 객관식, Option은 네개, 즉 사지선다형
    MultipleChoice("MultipleChoice"),
    // ShrotAnswer : 단답형
    ShrotAnswer("ShrotAnswer"),
    // BlanckQuestion : 빈칸 뚫기 문제
    BlanckQuestion("BlanckQuestion"),
    // OXChoice : O X 문제
    OXChoice("OXChoice");
    private final String key;

    ProblemType(String key) {
        this.key = key;
    }
}
