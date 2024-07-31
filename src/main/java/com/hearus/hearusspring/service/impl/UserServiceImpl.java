package com.hearus.hearusspring.service.impl;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.common.security.JwtTokenProvider;
import com.hearus.hearusspring.data.dao.UserDAO;
import com.hearus.hearusspring.data.dto.TokenDTO;
import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.data.dto.oauth.OAuthAdditionalInfoDTO;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl  implements UserService {

    private final UserDAO userDAO;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CommonResponse login(UserDTO user) {

        log.info("[UserService]-[login] 로그인 요청 : {}", user.getUserEmail());

        try {
            //Search User
            Optional<UserDTO> userByEmail = userDAO.getUserByEmail(user.getUserEmail());
            UserDTO findUserDTO;

            //Search User Validation
            if(userByEmail.isPresent())
                //Success Search User
                findUserDTO = userByEmail.get();
            else{
                //Fail Search User
                log.info("[UserService]-[login] User not found : {}", user.getUserEmail());
                return new CommonResponse(false, HttpStatus.NOT_FOUND,"User not found");
            }

            //Password Validation
            if(!passwordEncoder.matches(user.getUserPassword(), findUserDTO.getUserPassword())) {
                //Fail Password Match
                log.info("[UserService]-[login] Invalid User Info");
                return new CommonResponse(false, HttpStatus.UNAUTHORIZED, "Invalid User Info");
            }

            //Generate Token
            TokenDTO loginToken = jwtTokenProvider.generateToken(findUserDTO);
            log.info("[UserService]-[login] 로그인 성공 : {}", jwtTokenProvider.getTokenInfo(loginToken.getAccessToken()));
            return new CommonResponse(true, HttpStatus.OK,"Login Success", loginToken);

        }catch (Exception e){
            log.info("[UserService]-[login] 로그인 도중 Exception 발생 \n {}", e.toString());
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,"Internal Server Error");
        }
    }

    @Override
    public CommonResponse signup(UserDTO user) {
        log.info("[UserService]-[signup] UserDAO에 회원 정보 저장 요청 : {}", user.getUserEmail());

        if(userDAO.saveUser(user)){
            return new CommonResponse(true,HttpStatus.CREATED,"Signup Success");
        }
        else{
            return new CommonResponse(false, HttpStatus.CONFLICT,"User Already Exists");
        }

    }

    @Override
    public CommonResponse getUserById(String targetUserId) {
        log.info("[UserService]-[getUserById] UserDAO로 유저 정보 찾기 요청");

        Optional<UserDTO> userById = userDAO.getUserById(targetUserId);

        if(userById.isPresent()){
            UserDTO userDTO = userById.get();
            userDTO.setUserPassword("");
            return new CommonResponse(true, HttpStatus.OK, "Success Search User", userDTO);
        }
        else{
            return new CommonResponse(false, HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Override
    public CommonResponse addInformation(OAuthAdditionalInfoDTO oAuthAdditionalInfoDTO) {

        log.info("[UserService]-[addInformation] UserDAO로 유저 정보 추가 요청");

        if(userDAO.addUserData(oAuthAdditionalInfoDTO)){
            log.info("[UserService]-[addInformation] 유저 정보 추가 성공.");
            return new CommonResponse(true, HttpStatus.OK, "Success Add User Info", oAuthAdditionalInfoDTO.getUserEmail());
        }
        else{
            log.info("[UserService]-[addInformation] 일치하는 유저 정보가 없음.");
            return new CommonResponse(false, HttpStatus.BAD_REQUEST, "Fail Add User Info : fail search user");
        }
    }

    @Override
    public CommonResponse updateUser(String userId, UserDTO userDTO) {
        try{
            UserDTO findUserDTO = userDAO.getUserById(userId).get();

            // Set Updated Data
            if(userDTO.getUserName() != null)
                findUserDTO.setUserName(userDTO.getUserName());
            findUserDTO.setUserSchool(userDTO.getUserSchool());
            findUserDTO.setUserMajor(userDTO.getUserMajor());
            findUserDTO.setUserGrade(userDTO.getUserGrade());
            findUserDTO.setUserUsePurpose(userDTO.getUserUsePurpose());

            // Save findUserDTO
            userDAO.updateUser(findUserDTO);

            log.info("[UserService]-[updateUser] User updated successfully : {}", userDTO.getUserEmail());
            return new CommonResponse(true, HttpStatus.OK, "User updated successfully");

        }catch (Exception e){
            log.error("Failed to Update User", e);
            return new CommonResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to Update User");
        }
    }
}