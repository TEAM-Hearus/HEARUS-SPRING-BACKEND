package com.hearus.hearusspring.data.model;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Document 어노테이션은 해당 클래스가 MongoDB에 저장될 문서임을 나타냄
@Document(collection = "lecture")
public class LectureModel {
    @Id
    @Column(unique = true)
    private String id;

    @Column(unique = true)
    private String name;

    private String processedScript;

    private String scheduleElementId;

    private LocalDateTime lectureDate;

    private LocalDateTime createdAt;

    // problems 필드는 ProblemClass 리스트로 설정되어 있음
    private List<Problem> problems;
}