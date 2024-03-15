package com.hearus.hearusspring.data.handler.impl;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dao.UserDAO;
import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.handler.UserHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserHandlerImpl implements UserHandler {
    UserDAO userDAO;
    private final Logger LOGGER = LoggerFactory.getLogger(UserHandlerImpl.class);
    @Override
    public CommonResponse signupUserEntitiy(UserDTO user) {
        LOGGER.info("[UserHandler]-[signupUserEntitiy] UserDAO로 UserEntitiy 회원가입 요청 : {}", user.getUserEmail());
        UserEntity userEntity = user.toEntitiy();
        return userDAO.userSignup(userEntity);
    }
}
