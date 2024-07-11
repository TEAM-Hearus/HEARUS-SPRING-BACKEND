package com.hearus.hearusspring.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private List<String> processedScript;

    private String scheduleElementId;

    private Date lectureDate;

    private Date createdAt;

    private List<Problem> problems;

    public void addProblem(Problem problem) {
        if (problems == null) {
            problems = new ArrayList<>();
        }
        problem.setId(UUID.randomUUID().toString());
        problems.add(problem);
    }

    public void updateProblem(String problemId, Problem newProblem) {
        if (problems != null) {
            for (int i = 0; i < problems.size(); i++) {
                Problem problem = problems.get(i);
                if (problem.getId().equals(problemId)) {
                    newProblem.setId(problemId);
                    problems.set(i, newProblem);
                    break;
                }
            }
        }
    }

    public void deleteProblem(String problemId) {
        if (problems != null) {
            problems.removeIf(problem -> problem.getId().equals(problemId));
        }
    }
}