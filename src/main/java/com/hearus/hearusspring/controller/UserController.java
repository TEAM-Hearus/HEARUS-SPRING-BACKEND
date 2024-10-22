package com.hearus.hearusspring.controller;


import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.user.UserDTO;
import com.hearus.hearusspring.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping(value="/updateUser")
    public ResponseEntity<CommonResponse> updateUser(@Valid @RequestBody UserDTO userDTO){
        log.info("[UserAuthController]-[updateUser] API Call");
        String userId = getUserIdFromContext();

        // UserService로 요청받은 UserDTO 수정 요청
        CommonResponse response = userService.updateUser(userId, userDTO);

        log.info("[UserAuthController]-[signupUser] {} : {}", response.getStatus(), response.getMsg());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping(value="/deleteUser/{userEmail}")
    public ResponseEntity<CommonResponse> deleteUser(@Valid @PathVariable String userEmail){
        log.info("[UserAuthController]-[deleteUser] API Call");

        // UserService로 요청받은 UserDTO 수정 요청
        CommonResponse response = userService.deleteUser(userEmail);

        log.info("[UserAuthController]-[deleteUser] {} : {}", response.getStatus(), response.getMsg());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // SecurityContext에서 Authentication으로 UserID를 받아온다
    private String getUserIdFromContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }
}
