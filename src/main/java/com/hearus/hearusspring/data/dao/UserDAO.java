package com.hearus.hearusspring.data.dao;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.entitiy.UserEntity;
import com.hearus.hearusspring.data.oauth.dto.OAuthAdditionalInfoDTO;

public interface UserDAO {
    CommonResponse getUserById(String userId);
    UserEntity userLogin(UserEntity user);
    CommonResponse userSignup(UserEntity user);
    CommonResponse addUserInfo(OAuthAdditionalInfoDTO oAuthAdditionalInfoDTO);
}
