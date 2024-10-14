package com.hearus.hearusspring.data.dto.lecture.problem;

import com.hearus.hearusspring.data.model.Problem;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProblemDTO {

    private String id;

    // MultipleChoice : 객관식, Option은 네개, 즉 사지선다형
    // ShrotAnswer : 단답형
    // BlanckQuestion : 빈칸 뚫기 문제
    // OXChoice : O X 문제
    @NotEmpty(message = "Problem type cannot be empty")
    private String type;

    @NotEmpty(message = "Problem direction cannot be empty")
    private String direction;

    private List<String> options = new ArrayList<>();

    @NotEmpty(message = "Answer cannot be empty")
    private String answer;

    public Problem toEntity(){
        Problem problem = new Problem();
        problem.setType(type);
        problem.setDirection(direction);
        problem.setOptions(options);
        problem.setAnswer(answer);

        return problem;
    }
}
