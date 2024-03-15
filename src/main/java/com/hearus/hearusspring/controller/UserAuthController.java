package com.hearus.hearusspring.controller;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {
    private final Logger LOGGER = LoggerFactory.getLogger(UserAuthController.class);

    private final UserService userService;
    private CommonResponse response;


    @Autowired
    public UserAuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value="/signup")
    public ResponseEntity<CommonResponse> signupUser(@Valid @RequestBody UserDTO userDTO){
        LOGGER.info("[UserAuthController]-[signupUser] API Call");

        // 요구되는 데이터 존재 여부 검증
        if(userDTO.getUserName().isEmpty() || userDTO.getUserEmail().isEmpty() || userDTO.getUserPassword().isEmpty()){
            LOGGER.info("[UserAuthController]-[signupUser] Failed : Empty Variables");
            response = new CommonResponse(false,HttpStatus.BAD_REQUEST,"Empty Variables");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        // UserService로 요청받은 UserDTO 회원가입 요청
        response = userService.userSignup(userDTO);

        LOGGER.info("[UserAuthController]-[signupUser] Success");
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
