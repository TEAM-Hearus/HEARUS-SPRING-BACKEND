package com.hearus.hearusspring.data.dao;

import com.hearus.hearusspring.data.dto.user.UserDTO;
import com.hearus.hearusspring.data.dto.oauth.OAuthAdditionalInfoDTO;

import java.util.Optional;

public interface UserDAO {
    Optional<UserDTO> getUserByEmail(String email);
    boolean saveUser(UserDTO userDTO);
    boolean updateUser(UserDTO userDTO);
    Optional<UserDTO> getUserById(String userId);
    boolean addUserData(OAuthAdditionalInfoDTO oAuthAdditionalInfoDTO);

    boolean deleteUser(String userEmail);
}
