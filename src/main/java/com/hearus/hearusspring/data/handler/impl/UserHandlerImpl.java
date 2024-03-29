package com.hearus.hearusspring.data.handler.impl;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dao.UserDAO;
import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.handler.UserHandler;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserHandlerImpl implements UserHandler {
    UserDAO userDAO;
    private final Logger LOGGER = LoggerFactory.getLogger(UserHandlerImpl.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserHandlerImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDTO loginUserEntity(UserDTO user) {
        LOGGER.info("[UserHandler]-[signupUserEntitiy] UserDAO로 로그인 요청 : {}", user.getUserEmail());
        UserEntity userEntity = user.toEntitiy(passwordEncoder);
        if(!passwordEncoder.matches(user.getUserPassword(), userEntity.getPassword()))
            return null;
        return userDAO.userLogin(userEntity).toDTO();
    }

    @Override
    public CommonResponse signupUserEntitiy(UserDTO user) {
        LOGGER.info("[UserHandler]-[signupUserEntitiy] UserDAO로 UserEntitiy 회원가입 요청 : {}", user.getUserEmail());
        UserEntity userEntity = user.toEntitiy(passwordEncoder);
        return userDAO.userSignup(userEntity);
    }
}
