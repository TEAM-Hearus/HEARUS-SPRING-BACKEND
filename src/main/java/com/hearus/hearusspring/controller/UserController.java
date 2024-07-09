package com.hearus.hearusspring.controller;


import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dao.UserDAO;
import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.repository.UserRepository;
import com.hearus.hearusspring.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;


    /**
     *
     * @description: 현재 로그인된 유저 정보를 가져온다
     * @return: CommonResponse(...,...,...,UserDTO)
     */
    @GetMapping("/present-user")
    public ResponseEntity<CommonResponse> getPresentUser() {

        log.info("[UserController]-[getPresentUser] API Call");
        String userIdFromContext = getUserIdFromContext();

        CommonResponse result = userService.getUserById(userIdFromContext);
        return ResponseEntity.status(result.getStatus()).body(result);

    }


    // SecurityContext에서 Authentication으로 UserID를 받아온다
    private String getUserIdFromContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }
}
