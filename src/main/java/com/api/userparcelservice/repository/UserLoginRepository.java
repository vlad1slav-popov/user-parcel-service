package com.api.userparcelservice.repository;


import com.api.userparcelservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepository extends JpaRepository<UserEntity, Long> {


    UserEntity findUserEntityByUsername(String username);

    UserEntity findUserEntityByUsernameAndPassword(String username, String password);


}

