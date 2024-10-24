package com.hearus.hearusspring.data.dto.lecture.problem;

import com.hearus.hearusspring.data.model.Problem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProblemAddDTO {

    @NotEmpty(message = "Lecture ID cannot be empty")
    String lectureId;

    @Valid
    @NotNull(message = "Problem cannot be null")
    ProblemDTO problem;

}
