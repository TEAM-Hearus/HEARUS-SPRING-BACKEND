package com.hearus.hearusspring.data.dao.impl;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dao.UserDAO;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserDAOImpl implements UserDAO {

    UserRepository userRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(UserDAOImpl.class);

    @Autowired
    public UserDAOImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity getUserById(String userId){
        LOGGER.info("[UserDAO]-[userLogin] UserID로 User 정보 찾기");
        return userRepository.findFirstById(userId);
    }

    @Override
    public UserEntity userLogin(UserEntity user) {
        LOGGER.info("[UserDAO]-[userLogin] UserEmail로 정보 가져오기 : {}", user.getEmail());
        return userRepository.findFirstByEmail(user.getEmail());
    }

    @Override
    public CommonResponse userSignup(UserEntity user) {
        LOGGER.info("[UserDAO]-[userSignup] UserEntitiy 저장 : {}", user.getEmail());

        // 이메일 중복 여부 확인
        if(userRepository.existsByEmail(user.getEmail())){
            LOGGER.info("[UserDAO]-[userSignup] 회원 이메일 중복 : {}", user.getEmail());
            return new CommonResponse(false, HttpStatus.CONFLICT,"User Already Exists");
        }

        userRepository.save(user);
        LOGGER.info("[UserDAO]-[userSignup] UserEntitiy 저장 성공 : {}", user.getEmail());
        return new CommonResponse(true,HttpStatus.CREATED,"Signup Success");
    }
}
