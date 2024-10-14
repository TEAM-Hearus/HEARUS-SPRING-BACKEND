package com.hearus.hearusspring.data.dto.lecture;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LectureDeleteDTO {

    @NotEmpty
    String lectureId;
}
