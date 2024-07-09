package com.hearus.hearusspring.data.handler;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.data.oauth.dto.OAuthAdditionalInfoDTO;

public interface UserHandler {
    UserDTO loginUserEntity(UserDTO user);
    CommonResponse signupUserEntitiy(UserDTO user);
    CommonResponse getUserById(String targetUserId);
    CommonResponse addUserInfo(OAuthAdditionalInfoDTO oAuthAdditionalInfoDTO);
}
