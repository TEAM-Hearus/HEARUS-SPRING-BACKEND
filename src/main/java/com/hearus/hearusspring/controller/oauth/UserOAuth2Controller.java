package com.hearus.hearusspring.controller.oauth;


import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.oauth.OAuthAdditionalInfoDTO;
import com.hearus.hearusspring.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/oauth")
@Slf4j
@RequiredArgsConstructor
public class UserOAuth2Controller {

    private final UserService userService;

    /**
     *
     * @description: OAuth 회원가입 이후 추가 정보를 저장한다.
     * @return: CommonResponse(...,...,..., String Email)
     */
    @PostMapping(value = "/add-info")
    public ResponseEntity<CommonResponse> enterAdditionalInformation(@Valid @RequestBody OAuthAdditionalInfoDTO oAuthAdditionalInfoDTO){

        log.info("[UserOAuth2Controller]-[enterAdditionalInformation] API Call");

        if(oAuthAdditionalInfoDTO.getUserEmail() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        CommonResponse result = userService.addInformation(oAuthAdditionalInfoDTO);

        return ResponseEntity.status(result.getStatus()).body(result);
    }


}
