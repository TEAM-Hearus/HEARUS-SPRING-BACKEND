package com.hearus.hearusspring.data.oauth.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class OAuthAdditionalInfoDTO {


    private String userSchool;
    private String userMajor;
    private String userGrade;

}
