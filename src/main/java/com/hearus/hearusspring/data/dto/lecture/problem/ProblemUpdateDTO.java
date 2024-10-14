package com.hearus.hearusspring.data.dto.lecture.problem;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ProblemUpdateDTO {

    @NotEmpty
    String lectureId;

    ProblemDTO problem;


}
