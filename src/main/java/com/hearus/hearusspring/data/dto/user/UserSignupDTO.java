package com.hearus.hearusspring.data.dto.user;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserSignupDTO {

    @Email(message = "Email format is incorrect")
    @NotBlank(message = "userEmail not found in request")
    String userEmail;
    @NotBlank(message = "userPassword not found in request")
    String userPassword;
    boolean userIsOAuth;
    @NotBlank(message = "userName not found in request")
    String userName;

    public UserDTO toDTO(){

        UserDTO build = UserDTO.builder()
                .userEmail(this.userEmail)
                .userPassword(this.userPassword)
                .userName(this.userName)
                .userIsOAuth(this.userIsOAuth)
                .build();

        return build;
    }

}
