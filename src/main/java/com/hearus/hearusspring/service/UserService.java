package com.hearus.hearusspring.service;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.data.oauth.dto.OAuthAdditionalInfoDTO;

public interface UserService {
    CommonResponse login(UserDTO user);
    CommonResponse signup(UserDTO user);
    CommonResponse getUserById(String targetUserId);
    CommonResponse addInformation(OAuthAdditionalInfoDTO oAuthAdditionalInfoDTO);
}
