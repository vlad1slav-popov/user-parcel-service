package com.api.userparcelservice.service;


import com.api.userparcelservice.domain.LogoutRequest;
import com.api.userparcelservice.domain.LogoutResponse;
import com.api.userparcelservice.domain.UserLoginRequest;
import com.api.userparcelservice.dto.OnUserLogoutSuccessEvent;
import com.api.userparcelservice.entity.UserEntity;
import com.api.userparcelservice.exception.UserException;
import com.api.userparcelservice.repository.RoleRepository;
import com.api.userparcelservice.repository.UserLoginRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserLoginRepository userLoginRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<UserEntity> getAllUsers() {
        return userLoginRepository.findAll();
    }

    public UserEntity getUserDataByUsername(String username) {
        return Optional.ofNullable(userLoginRepository.findUserEntityByUsername(username))
                .orElseThrow(() -> new UserException("User with username: " +
                        username + " not found", "005"));
    }

    public UserEntity getUserDataByUsernameAndPassword(UserLoginRequest userLoginRequest) {
        UserEntity userEntity = Optional.ofNullable(userLoginRepository
                        .findUserEntityByUsername(userLoginRequest.getUsername()))
                .orElseThrow(() -> new UserException("User with username: " +
                        userLoginRequest.getUsername() + " not found", "005"));

        if (passwordEncoder.matches(userLoginRequest.getPassword(), userEntity.getPassword())) {
            return userEntity;
        } else
            throw new UserException("Wrong password", "006");
    }

    public LogoutResponse logout(LogoutRequest request) {
        OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(
                request.getUsername(), request.getToken(), request);
        applicationEventPublisher.publishEvent(logoutSuccessEvent);
        return LogoutResponse.builder()
                .message("User has successfully logged out from the system!")
                .build();
    }


}
