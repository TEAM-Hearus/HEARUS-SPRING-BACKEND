package com.hearus.hearusspring.data.dao;

import com.hearus.hearusspring.data.dto.UserDTO;
import com.hearus.hearusspring.data.oauth.dto.OAuthAdditionalInfoDTO;

import java.util.Optional;

public interface UserDAO {
    Optional<UserDTO> getUserByEmail(String email);
    boolean saveUser(UserDTO userDTO);
    Optional<UserDTO> getUserById(String userId);
    boolean addUserData(OAuthAdditionalInfoDTO oAuthAdditionalInfoDTO);
}
