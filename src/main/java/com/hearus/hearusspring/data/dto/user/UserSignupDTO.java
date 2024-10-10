package com.hearus.hearusspring.data.dto.user;

import com.hearus.hearusspring.common.enumType.RoleType;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;

@Data
public class UserSignupDTO {

    @Email(message = "Email format is incorrect")
    @NotBlank(message = "userEmail not found in request")
    String userEmail;

    @NotBlank(message = "userPassword not found in request")
    @Min(value = 0, message = "userPassword is too short")
    String userPassword;

    boolean userIsOAuth = false;

    @NotBlank(message = "userName not found in request")
    String userName;

    public UserDTO toDTO(){

        UserDTO build = UserDTO.builder()
                .userEmail(this.userEmail)
                .userPassword(this.userPassword)
                .userName(this.userName)
                .userIsOAuth(this.userIsOAuth)
                .userRole(RoleType.USER.getKey())
                .userSchedule(new ArrayList<>())
                .userSavedLectures(new ArrayList<>())
                .userId("")
                .build();

        return build;
    }

}
