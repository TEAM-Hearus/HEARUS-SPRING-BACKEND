package com.hearus.hearusspring.service.impl;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.data.handler.UserHandler;
import com.hearus.hearusspring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl  implements UserService {
    UserHandler userHandler;
    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public CommonResponse userSignup(UserDTO user) {
        LOGGER.info("[UserService]-[userSignup] UserHandler로 회원가입 요청 : {}", user.getUserEmail());
        return userHandler.signupUserEntitiy(user);
    }
}