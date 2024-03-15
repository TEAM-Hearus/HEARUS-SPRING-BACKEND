package com.hearus.hearusspring.data.dao;

import com.hearus.hearusspring.data.entitiy.UserEntity;

public interface UserDAO {
    UserEntity userLogin(UserEntity user);
    boolean userSignup(UserEntity user);
}
