package com.hearus.hearusspring.data.repository;

import com.hearus.hearusspring.data.entitiy.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    // Find By
    UserEntity findFirstById(String id);
    UserEntity findFirstByEmail(String email);
    UserEntity findFirstByEmailAndPassword(String email, String password);


    // Exist By
    boolean existsByEmail(String email);
}
