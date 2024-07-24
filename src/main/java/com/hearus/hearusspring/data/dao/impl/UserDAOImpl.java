package com.hearus.hearusspring.data.dao.impl;

import com.hearus.hearusspring.data.dao.UserDAO;
import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.dto.oauth.OAuthAdditionalInfoDTO;
import com.hearus.hearusspring.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDAOImpl implements UserDAO {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        log.info("[UserDAO]-[getUserByEmail] UserEmail로 User 정보 접근 {}", email);

        Optional<UserEntity> byEmail = userRepository.findByEmail(email);

        if(byEmail.isPresent()) {

            log.info("[UserDAO]-[getUserByEmail] UserEmail로 User 정보 찾기 성공 : {}", byEmail.get().getEmail());

            return Optional.of(byEmail.get().toDTO());
        }
        else {

            log.info("[UserDAO]-[getUserByEmail] UserEmail로 User 정보 찾기 실패");

            return Optional.empty();
        }

    }

    @Override
    @Transactional
    public boolean saveUser(UserDTO userDTO) {

        UserEntity user = userDTO.toEntitiy(passwordEncoder);
        log.info("[UserDAO]-[userSignup] UserEntitiy 저장 : {}", user.getEmail());

        // 이메일 중복 여부 확인
        if(userRepository.existsByEmail(user.getEmail())){
            log.info("[UserDAO]-[userSignup] 회원 이메일 중복 : {}", user.getEmail());
            return false;
        }

        userRepository.save(user);
        log.info("[UserDAO]-[userSignup] UserEntitiy 저장 성공 : {}", user.getEmail());

        return true;
    }

    @Override
    public Optional<UserDTO> getUserById(String userId){

        log.info("[UserDAO]-[getUserById] UserID로 User 정보 접근");
        Optional<UserEntity> byId = userRepository.findById(userId);

        if(byId.isPresent()) {

            log.info("[UserDAO]-[getUserById] UserID로 User 정보 찾기 성공 : {}", byId.get().getEmail());

            return Optional.of(byId.get().toDTO());
        }
        else {

            log.info("[UserDAO]-[getUserById] UserID로 User 정보 찾기 실패");
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public boolean addUserData(OAuthAdditionalInfoDTO oAuthAdditionalInfoDTO) {

        log.info("[UserDAO]-[addUserInfo] UserEntitiy 추가 정보 입력 : {}", oAuthAdditionalInfoDTO.getUserEmail());

        //data access
        Optional<UserEntity> byEmail = userRepository.findByEmail(oAuthAdditionalInfoDTO.getUserEmail());

        if(byEmail.isPresent()) {

            UserEntity target = byEmail.get();

            target.setGrade(oAuthAdditionalInfoDTO.getUserGrade());
            target.setMajor(oAuthAdditionalInfoDTO.getUserMajor());
            target.setSchool(oAuthAdditionalInfoDTO.getUserSchool());

            log.info("[UserDAO]-[addUserInfo] 유저 정보 추가 성공.");

            return true;

        }

        log.info("[UserDAO]-[addUserInfo] 일치하는 유저 정보가 없음.");
        return false;

    }
}
