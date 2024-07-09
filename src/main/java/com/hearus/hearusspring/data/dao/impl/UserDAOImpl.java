package com.hearus.hearusspring.data.dao.impl;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dao.UserDAO;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.oauth.dto.OAuthAdditionalInfoDTO;
import com.hearus.hearusspring.data.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDAOImpl implements UserDAO {

    UserRepository userRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(UserDAOImpl.class);

    @Autowired
    public UserDAOImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CommonResponse getUserById(String userId){

        LOGGER.info("[UserDAO]-[getUserById] UserID로 User 정보 찾기");
        Optional<UserEntity> byId = userRepository.findById(userId);

        if(byId.isPresent()) {

            LOGGER.info("[UserDAO]-[getUserById] UserID로 User 정보 찾기 성공 : {}", byId.get().getEmail());
            return new CommonResponse(true, HttpStatus.OK, "Success Search User", byId.get().toDTO());
        }
        else {

            LOGGER.info("[UserDAO]-[getUserById] UserID로 User 정보 찾기 실패");
            return new CommonResponse(false, HttpStatus.NOT_FOUND, "User not found");
        }
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

    @Override
    @Transactional
    public CommonResponse addUserInfo(OAuthAdditionalInfoDTO oAuthAdditionalInfoDTO) {

        LOGGER.info("[UserDAO]-[addUserInfo] UserEntitiy 추가 정보 입력 : {}", oAuthAdditionalInfoDTO.getUserEmail());

        UserEntity target = userRepository.findFirstByEmail(oAuthAdditionalInfoDTO.getUserEmail());

        if(target == null) {
            LOGGER.info("[UserDAO]-[addUserInfo] 일치하는 유저 정보가 없음.");
            return new CommonResponse(false, HttpStatus.BAD_REQUEST, "Fail Add User Info : fail search user");
        }

        target.setGrade(oAuthAdditionalInfoDTO.getUserGrade());
        target.setMajor(oAuthAdditionalInfoDTO.getUserMajor());
        target.setSchool(oAuthAdditionalInfoDTO.getUserSchool());


        return new CommonResponse(true, HttpStatus.OK, "Success Add User Info", target.getEmail());
    }
}
