package com.hearus.hearusspring.data.handler;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.dto.UserDTO;

public interface UserHandler {
    CommonResponse signupUserEntitiy(UserDTO user);
}
