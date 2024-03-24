package com.hearus.hearusspring.data.dto;

import com.hearus.hearusspring.common.enumType.RoleType;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDTO {
    @Id
    String userId = "";

    String userName;

    @Email
    @NotNull
    String userEmail;
    @NotNull
    String userPassword;

    String userRole = RoleType.USER_FREE.getKey();

    boolean userIsOAuth;
    String userOAuthType;

    String userSchool;
    String userMajor;
    String userGrade;

    ArrayList<String> userSavedLectures = new ArrayList<String>();

    String userSchedule;
    String userUsePurpose;

    public UserEntity toEntitiy(PasswordEncoder passwordEncoder){
        String stringLecture = null;
        if(!userSavedLectures.isEmpty())
            stringLecture = String.join(",", userSavedLectures);

        return UserEntity.builder()
                .id(userId)
                .name(userName)
                .email(userEmail)
                .password(passwordEncoder.encode(userPassword))
                .role(userRole)
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
