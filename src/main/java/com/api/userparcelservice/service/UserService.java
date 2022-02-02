package com.api.userparcelservice.service;


import com.api.userparcelservice.domain.LogoutRequest;
import com.api.userparcelservice.domain.UserLoginRequest;
import com.api.userparcelservice.dto.OnUserLogoutSuccessEvent;
import com.api.userparcelservice.entity.UserEntity;
import com.api.userparcelservice.exception.UserNotFoundException;
import com.api.userparcelservice.repository.RoleRepository;
import com.api.userparcelservice.repository.UserLoginRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
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
    private final RoleRepository roleRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<UserEntity> getAllUsers() {
        return userLoginRepository.findAll();
    }

    public UserEntity getUserDataByUsername(String username) {
        return Optional.ofNullable(userLoginRepository.findUserEntityByUsername(username))
                .orElseThrow(() -> new UserNotFoundException("USER_NOT_FOUND"));
    }

    public UserEntity getUserDataByUsernameAndPassword(UserLoginRequest userLoginRequest) {
        UserEntity userEntity = Optional.ofNullable(userLoginRepository
                        .findUserEntityByUsername(userLoginRequest.getUsername()))
                .orElseThrow(() -> new UserNotFoundException("User with username: " +
                        userLoginRequest.getUsername() + " not found"));

        if (passwordEncoder.matches(userLoginRequest.getPassword(), userEntity.getPassword())) {
            return userEntity;
        } else
            throw new UserNotFoundException("Wrong password");
    }

    public ResponseEntity<String> logout(LogoutRequest request) {
        OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(
                request.getUsername(), request.getToken(), request);
//        logger.info("OnUserLogoutSuccessEvent: " + logoutSuccessEvent);
        applicationEventPublisher.publishEvent(logoutSuccessEvent);
        return ResponseEntity.ok("User has successfully logged out from the system!");
    }


}
