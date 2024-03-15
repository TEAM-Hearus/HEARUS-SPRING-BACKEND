package com.hearus.hearusspring.data.dao.impl;

import com.hearus.hearusspring.data.dao.UserDAO;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDAOImpl implements UserDAO {

    UserRepository userRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(UserDAOImpl.class);

    // Todo : Create Login Function
    @Override
    public UserEntity userLogin(UserEntity user) {
        return null;
    }

    @Override
    public boolean userSignup(UserEntity user) {
        LOGGER.info("[UserDAO]-[userSignup] UserEntitiy 저장 : {}", user.getEmail());

        // 이메일 중복 여부 확인
        if(userRepository.existsByEmail(user.getEmail())){
            LOGGER.info("[UserDAO]-[userSignup] 회원 이메일 중복 : {}", user.getEmail());
            return false;
        }

        userRepository.save(user);
        LOGGER.info("[UserDAO]-[userSignup] UserEntitiy 저장 성공 : {}", user.getEmail());
        return true;
    }
}
