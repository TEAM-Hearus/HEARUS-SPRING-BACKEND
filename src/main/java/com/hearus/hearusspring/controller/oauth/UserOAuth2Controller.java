package com.hearus.hearusspring.controller.oauth;


import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.data.oauth.PrincipalDetails;
import com.hearus.hearusspring.data.oauth.dto.OAuthAdditionalInfoDTO;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1/oauth")
public class UserOAuth2Controller {

    @GetMapping(value = "/add-info")
    private String enterAdditionalInformation(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody OAuthAdditionalInfoDTO additionalInfoDTO){


        return principalDetails.getUserDTO().getUserName();


    }



}
