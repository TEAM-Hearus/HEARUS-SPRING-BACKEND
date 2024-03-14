package com.hearus.hearusspring.data.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDTO {
    @Id
    @NotNull
    String userId;

    @NotNull
    String userName;

    @NotNull
    @Email
    String userEmail;

    @NotNull
    String userPassword;

    @NotNull
    boolean userIsOAuth;
    String userOAuthType;

    String userSchool;
    String userMajor;
    String userGrade;

    ArrayList<String> userSavedLectures;

    String userSchedule;
    String userUsePurpose;
}
