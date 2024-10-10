package com.hearus.hearusspring.controller;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.user.UserDTO;
import com.hearus.hearusspring.data.dto.user.UserLoginDTO;
import com.hearus.hearusspring.data.dto.user.UserSignupDTO;
import com.hearus.hearusspring.service.UserService;
import jakarta.validation.Valid;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {
    private final Logger LOGGER = LoggerFactory.getLogger(UserAuthController.class);

    private final UserService userService;

    @Autowired
    public UserAuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value="/login")
    public ResponseEntity<CommonResponse> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO, BindingResult bindingResult){
        LOGGER.info("[UserAuthController]-[loginUser] API Call");

        // Request 데이터 검증
        if(bindingResult.hasErrors()){
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                LOGGER.info("[UserAuthController]-[loginUser] Failed : {}", error.getDefaultMessage());
                CommonResponse response = new CommonResponse(false, HttpStatus.BAD_REQUEST, error.getDefaultMessage());
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
        }

        // UserService로 요청받은 UserDTO 로그인 요청
        CommonResponse response = userService.login(userLoginDTO.toDTO());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(value="/signup")
    public ResponseEntity<CommonResponse> signupUser(@Valid @RequestBody UserSignupDTO userSignupDTO, BindingResult bindingResult){
        LOGGER.info("[UserAuthController]-[signupUser] API Call");

        // Request 데이터 검증
        if(bindingResult.hasErrors()){
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                LOGGER.info("[UserAuthController]-[signupUser] Failed : {}", error.getDefaultMessage());
                CommonResponse response = new CommonResponse(false, HttpStatus.BAD_REQUEST, error.getDefaultMessage());
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
        }

        // UserService로 요청받은 UserDTO 회원가입 요청
        CommonResponse response = userService.signup(userSignupDTO.toDTO());

        LOGGER.info("[UserAuthController]-[signupUser] {} : {}", response.getStatus(), response.getMsg());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
