package com.hearus.hearusspring.data.dto;

import com.hearus.hearusspring.data.entitiy.UserEntity;
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

    ArrayList<String> userSavedLectures = new ArrayList<String>();

    String userSchedule;
    String userUsePurpose;

    public UserEntity toEntitiy(){
        String stringLecture = null;
        if(!userSavedLectures.isEmpty())
            stringLecture = String.join(",", userSavedLectures);

        return UserEntity.builder()
                .id(userId)
                .name(userName)
                .email(userEmail)
                .password(userPassword)
                .isOAuth(userIsOAuth)
                .oauthType(userOAuthType)
                .school(userSchool)
                .major(userMajor)
                .grade(userGrade)
                .savedLectures(stringLecture)
                .schedule(userSchedule)
                .usePurpose(userUsePurpose)
                .build();
    }
}
