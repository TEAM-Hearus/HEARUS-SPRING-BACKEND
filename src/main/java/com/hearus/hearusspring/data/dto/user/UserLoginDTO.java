package com.hearus.hearusspring.data.dto.user;

import com.hearus.hearusspring.common.enumType.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;

@Data
public class UserLoginDTO {
    @Email(message = "Email format is incorrect")
    @NotBlank(message = "userEmail not found in request")
    String userEmail;
    @NotBlank(message = "userPassword not found in request")
    String userPassword;
    boolean userIsOAuth = false;


    public UserDTO toDTO(){

        UserDTO build = UserDTO.builder()
                .userEmail(this.userEmail)
                .userPassword(this.userPassword)
                .userIsOAuth(this.userIsOAuth)
                .userRole(RoleType.USER.getKey())
                .userSchedule(new ArrayList<>())
                .userSavedLectures(new ArrayList<>())
                .userId("")
                .build();

        return build;
    }

}
