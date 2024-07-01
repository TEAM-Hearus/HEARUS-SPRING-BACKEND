package com.hearus.hearusspring.service.oauth;


import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.common.enumType.RoleType;
import com.hearus.hearusspring.data.dao.UserDAO;
import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.handler.UserHandler;
import com.hearus.hearusspring.data.oauth.OAuth2UserInfo;
import com.hearus.hearusspring.data.oauth.PrincipalDetails;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;


//Security Config 에서 로그인 성공 이후 사용자 정보를 가져오는 클래스

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. 유저 정보(attributes) 가져오기 (Json 형태, third-party 마다 형식이 다름)
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();
        log.info("[CustomOAuth2UserService]-[loadUser] OAuth 로그인 성공");

        // 2. resistrationId 가져오기 (third-party id)
        // ex) google, kakao, naver
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("[CustomOAuth2UserService]-[loadUser] registrationID: {}", registrationId);


        // 3.OAuth 유저 정보 생성
        // third-party 마다 다르므로 OAuth2UserInfo class에서 처리
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);
        log.info("[CustomOAuth2UserService]-[loadUser] userName: {}, userEmail: {}", oAuth2UserInfo.getName(), oAuth2UserInfo.getEmail());

        // 4. OAUth 유저 정보를 이용하여 UserDTO 생성
        UserDTO OAuthLoginUserDTO = UserDTO.builder()
                .userId(null)
                .userName(oAuth2UserInfo.getName())
                .userEmail(oAuth2UserInfo.getEmail())
                .userPassword(oAuth2UserInfo.getId()) //유저 식별자 사용
                .userRole(RoleType.USER.getKey())
                .userIsOAuth(true)
                .userOAuthType(registrationId)
                .userSchool(null)
                .userMajor(null)
                .userGrade(null)
                .userSavedLectures(new ArrayList<>())
                .userSchedule(new ArrayList<>())
                .build();

        // 5. PrincipalDetails (extends OAuth2User)로 반환
        return new PrincipalDetails(OAuthLoginUserDTO, oAuth2UserAttributes);

    }

}
