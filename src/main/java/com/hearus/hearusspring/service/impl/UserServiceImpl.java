package com.hearus.hearusspring.service.impl;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.common.security.JwtTokenProvider;
import com.hearus.hearusspring.data.dto.TokenDTO;
import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.data.handler.UserHandler;
import com.hearus.hearusspring.data.oauth.dto.OAuthAdditionalInfoDTO;
import com.hearus.hearusspring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl  implements UserService {
    UserHandler userHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserHandler userHandler, JwtTokenProvider jwtTokenProvider) {
        this.userHandler = userHandler;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public CommonResponse userLogin(UserDTO user) {
        LOGGER.info("[UserService]-[userLogin] UserHandler로 로그인 요청 : {}", user.getUserEmail());
        try {
            UserDTO loginResult = userHandler.loginUserEntity(user);

            // 로그인 실패 여부 검증
            if(loginResult == null)
                return new CommonResponse(false, HttpStatus.UNAUTHORIZED,"Invalid User Info");

            TokenDTO loginToken = jwtTokenProvider.generateToken(loginResult);
            LOGGER.info("[UserService]-[userLogin] 로그인 성공 : {}", jwtTokenProvider.getTokenInfo(loginToken.getAccessToken()));
            return new CommonResponse(true, HttpStatus.OK,"Login Success", loginToken);
        }catch (Exception e){
            LOGGER.info("[UserService]-[userLogin] 로그인 도중 Exception 발생 \n {}", e.toString());
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,"Internal Server Error");
        }
    }

    @Override
    public CommonResponse userSignup(UserDTO user) {
        LOGGER.info("[UserService]-[userSignup] UserHandler로 회원가입 요청 : {}", user.getUserEmail());
        return userHandler.signupUserEntitiy(user);
    }

    @Override
    public CommonResponse getUserById(String targetUserId) {
        LOGGER.info("[UserService]-[getUserById] UserHadler로 유저 정보 찾기 요청");
        return userHandler.getUserById(targetUserId);
    }

    @Override
    public CommonResponse addUserInfo(OAuthAdditionalInfoDTO oAuthAdditionalInfoDTO) {
        LOGGER.info("[UserService]-[addInfoUser] UserHadler로 유저 정보 추가 요청");
        return userHandler.addUserInfo(oAuthAdditionalInfoDTO);
    }

}