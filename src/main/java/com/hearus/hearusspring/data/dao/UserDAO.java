package com.hearus.hearusspring.data.dao;

import com.hearus.hearusspring.common.CommonResponse;
import com.hearus.hearusspring.data.entitiy.UserEntity;

public interface UserDAO {
    UserEntity getUserById(String userId);
    UserEntity userLogin(UserEntity user);
    CommonResponse userSignup(UserEntity user);
}
