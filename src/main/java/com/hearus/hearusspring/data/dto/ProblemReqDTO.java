package com.hearus.hearusspring.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemReqDTO {
    private String lectureId;
    private String script;
    private String subject;
    private int problem_num;
    private String problem_types;
}
