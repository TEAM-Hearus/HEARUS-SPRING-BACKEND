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
import java.util.List;
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
    String userEmail;
    String userPassword;

    String userRole = RoleType.USER.getKey();

    boolean userIsOAuth;
    String userOAuthType;

    String userSchool;
    String userMajor;
    String userGrade;

    List<String> userSavedLectures = new ArrayList<>();
    ArrayList<String> userSchedule = new ArrayList<String>();

    String userUsePurpose;

    // 비밀번호 인코딩 없이 비밀번호를 업데이트하기 위해서 오버로딩한 메소드
    public UserEntity toEntitiy(){

        String stringSchedule = null;
        if(!userSchedule.isEmpty())
            stringSchedule = String.join(",", userSchedule);

        return UserEntity.builder()
                .id(userId)
                .name(userName)
                .email(userEmail)
                .password(userPassword)
                .role(userRole)
                .isOAuth(userIsOAuth)
                .oauthType(userOAuthType)
                .school(userSchool)
                .major(userMajor)
                .grade(userGrade)
                .savedLectures(userSavedLectures)
                .schedule(stringSchedule)
                .usePurpose(userUsePurpose)
                .build();
    }

    public UserEntity toEntitiy(PasswordEncoder passwordEncoder){

        String stringSchedule = null;
        if(!userSchedule.isEmpty())
            stringSchedule = String.join(",", userSchedule);

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
                .savedLectures(userSavedLectures)
                .schedule(stringSchedule)
                .usePurpose(userUsePurpose)
                .build();
    }
}
