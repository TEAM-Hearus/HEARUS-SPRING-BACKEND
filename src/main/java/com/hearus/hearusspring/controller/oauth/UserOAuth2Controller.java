package com.hearus.hearusspring.controller.oauth;


import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.data.oauth.PrincipalDetails;
import com.hearus.hearusspring.data.oauth.dto.OAuthAdditionalInfoDTO;
import com.hearus.hearusspring.data.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/oauth")
@Slf4j
@RequiredArgsConstructor
public class UserOAuth2Controller {

    private final UserRepository userRepository;

    @PostMapping(value = "/add-info")
    private ResponseEntity<String> enterAdditionalInformation(){


        return ResponseEntity.status(HttpStatus.OK).body("");


    }

    // SecurityContext에서 Authentication으로 UserID를 받아온다
    private String getUserIdFromContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }



}
