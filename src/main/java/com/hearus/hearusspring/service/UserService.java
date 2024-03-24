package com.hearus.hearusspring.service;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.UserDTO;

public interface UserService {
    CommonResponse userLogin(UserDTO user);
    CommonResponse userSignup(UserDTO user);
}
