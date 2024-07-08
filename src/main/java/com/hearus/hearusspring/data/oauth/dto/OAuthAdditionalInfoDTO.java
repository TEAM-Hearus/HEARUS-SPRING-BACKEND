package com.hearus.hearusspring.data.oauth.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class OAuthAdditionalInfoDTO {

    @NotNull
    private String userEmail;
    private String userSchool;
    private String userMajor;
    private String userGrade;

}
