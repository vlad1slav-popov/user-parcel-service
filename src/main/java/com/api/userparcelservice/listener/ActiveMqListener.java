package com.api.userparcelservice.listener;

import com.api.userparcelservice.domain.LogoutRequest;
import com.api.userparcelservice.domain.LogoutResponse;
import com.api.userparcelservice.domain.RegisterUserRequest;
import com.api.userparcelservice.domain.UserLoginRequest;
import com.api.userparcelservice.dto.MqDTO;
import com.api.userparcelservice.entity.UserEntity;
import com.api.userparcelservice.exception.UserException;
import com.api.userparcelservice.service.AuthenticationService;
import com.api.userparcelservice.service.UserAuthorizationService;
import com.api.userparcelservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ActiveMqListener {

    private final JmsTemplate jmsTemplate;
    private final AuthenticationService authenticationService;
    private final UserAuthorizationService userAuthorizationService;
    private final UserService userService;


    @JmsListener(destination = "user-login-req-queue")
    public void getLoginValue(UserLoginRequest userLoginRequest) {
        log.info("message received: " + userLoginRequest);
        try {
            MqDTO loginResponse = authenticationService.getLoginResponse(userLoginRequest);
            jmsTemplate.convertAndSend("user-login-res-queue", loginResponse);
        } catch (UserException e) {
            MqDTO loginResponse = new MqDTO();
            UserEntity entity = new UserEntity();
            entity.setErrCode(e.getCode());
            entity.setErrDescription(e.getMessage());
            loginResponse.setUserEntity(entity);
            jmsTemplate.convertAndSend("user-login-res-queue", loginResponse);
        }

    }

    @JmsListener(destination = "user-register-req-queue")
    public void getRegisterValue(RegisterUserRequest registerUserRequest) {
        log.info("message received: " + registerUserRequest);
        try {
            UserEntity registerResponse = userAuthorizationService
                    .getRegisterResponse(registerUserRequest);
            jmsTemplate.convertAndSend("user-register-res-queue", registerResponse);
        } catch (UserException e) {
            UserEntity registerResponse = new UserEntity();
            registerResponse.setErrCode(e.getCode());
            registerResponse.setErrDescription(e.getMessage());
            jmsTemplate.convertAndSend("user-register-res-queue", registerResponse);
        }

    }

    @JmsListener(destination = "user-logout-req-queue")
    public void logout(LogoutRequest request) {
        log.info("message received: " + request);
        LogoutResponse logoutResponse = userService.logout(request);
        jmsTemplate.convertAndSend("user-logout-res-queue", logoutResponse);
    }


}
