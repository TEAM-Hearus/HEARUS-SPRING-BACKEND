package com.hearus.hearusspring.data.dto.lecture;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LectureScriptPutDTO {

    @NotEmpty
    private String lectureId;
    @NotEmpty
    private String script;

}
