package com.api.userparcelservice.listener;

import com.api.userparcelservice.domain.RegisterUserRequest;
import com.api.userparcelservice.domain.UserLoginRequest;
import com.api.userparcelservice.dto.MqDTO;
import com.api.userparcelservice.entity.UserEntity;
import com.api.userparcelservice.service.AuthenticationService;
import com.api.userparcelservice.service.UserAuthorizationService;
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


    @JmsListener(destination = "requestqueue")
    public void getLoginValue(UserLoginRequest userLoginRequest) {
        log.info("message received: " + userLoginRequest);

        MqDTO loginResponse = authenticationService.getLoginResponse(userLoginRequest);
        jmsTemplate.convertAndSend("responsequeue", loginResponse);
    }

    @JmsListener(destination = "requestqueue")
    public void getRegisterValue(RegisterUserRequest registerUserRequest) {
        log.info("message received: " + registerUserRequest);

        UserEntity registerResponse = userAuthorizationService
                .getRegisterResponse(registerUserRequest).getBody();

        jmsTemplate.convertAndSend("responsequeue", registerResponse);
    }

}
