package com.hearus.hearusspring.data.model;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {
    @Id
    @Column(unique = true)
    private String id;

    // MultipleChoice : 객관식, Option은 네개, 즉 사지선다형
    // ShrotAnswer : 단답형
    // BlanckQuestion : 빈칸 뚫기 문제
    // OXChoice : O X 문제
    private String type;

    private String direction;

    private List<String> options;

    private String answer;
}