package com.hearus.hearusspring.data.oauth.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.TokenDTO;
import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.data.oauth.PrincipalDetails;
import com.hearus.hearusspring.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    /**
     *
     * @description : OAuth 로그인 후처리의 최종 지점.
     *
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        //CustomOAuth2UserService에서 반환한 PrincipalDetails 의 UserDTO 가져오기
        UserDTO userDTO = ((PrincipalDetails) authentication.getPrincipal()).getUserDTO();
        log.info("[OAuth2SuccessHandler]-[onAuthenticationSuccess] success User load. name={}, email={}", userDTO.getUserName(), userDTO.getUserEmail());

        //이미 signUp이 되어 있는지 확인
        CommonResponse signUpResultResponse =  userService.userSignup(userDTO);

        //signUp이 되어있지 않다면 그대로 결과 반환 이후 추가 정보 Post 요청 필요 (school, major, grade)
        if(signUpResultResponse.isSuccess()){

            log.info("[OAuth2SuccessHandler]-[onAuthenticationSuccess] Need Signup. Try Signup");
            log.info("[OAuth2SuccessHandler]-[onAuthenticationSuccess] Signup Result. Http status={}",signUpResultResponse.getStatus());

            //HttpResponse Header Mapping
            response.setStatus(signUpResultResponse.getStatus().value());
            response.setContentType("application/json");

            //Write HttpResponse Body
            response.getWriter().write(convertCommonResponseToJson(signUpResultResponse));
        }
        //signUp이 되어 있다면 로그인 시도 후 결과 반환
        else{

            log.info("[OAuth2SuccessHandler]-[onAuthenticationSuccess] Already Sign upped User. Try Login");
            CommonResponse loginResultResponse = userService.userLogin(userDTO);
            log.info("[OAuth2SuccessHandler]-[onAuthenticationSuccess] Login Result. Http status={}, JWT Token={}",loginResultResponse.getStatus(), ((TokenDTO)loginResultResponse.getObject()).getAccessToken());


            //HttpResponse Header Mapping
            response.setStatus(loginResultResponse.getStatus().value());
            response.setContentType("application/json");

            //Write HttpResponse Body
            response.getWriter().write(convertCommonResponseToJson(loginResultResponse));

        }

    }


    /**
     *
     * @description : CommonResponse 객체를 Json 형태로 변환
     * @return : Json 형식의 String
     *
     */
    private String convertCommonResponseToJson(CommonResponse response) {

        // JSON 변환 (Jackson 라이브러리 사용)
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(response);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON", e);
        }
    }
}
