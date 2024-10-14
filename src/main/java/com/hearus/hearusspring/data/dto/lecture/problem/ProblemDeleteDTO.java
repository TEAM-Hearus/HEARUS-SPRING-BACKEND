package com.hearus.hearusspring.data.dto.lecture.problem;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ProblemDeleteDTO {
    @NotEmpty
    String lectureId;

    @NotEmpty
    String problemId;
}
