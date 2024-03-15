package com.hearus.hearusspring.service;

import com.hearus.hearusspring.data.dto.UserDTO;

public interface UserService {
    boolean userSignup(UserDTO user);
}
