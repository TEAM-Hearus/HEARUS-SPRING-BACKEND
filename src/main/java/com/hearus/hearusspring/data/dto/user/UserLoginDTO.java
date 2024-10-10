package com.hearus.hearusspring.data.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

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
                .build();

        return build;
    }

}
