package com.hearus.hearusspring.service;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.user.UserDTO;
import com.hearus.hearusspring.data.dto.oauth.OAuthAdditionalInfoDTO;

public interface UserService {
    CommonResponse login(UserDTO user);
    CommonResponse signup(UserDTO user);
    CommonResponse getUserById(String targetUserId);
    CommonResponse addInformation(OAuthAdditionalInfoDTO oAuthAdditionalInfoDTO);
    CommonResponse updateUser(String userId, UserDTO user);

    CommonResponse deleteUser(String userEmail);
}
