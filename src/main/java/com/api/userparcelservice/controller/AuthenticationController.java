package com.api.userparcelservice.controller;


import com.api.userparcelservice.domain.RegisterUserRequest;
import com.api.userparcelservice.domain.UserLoginRequest;
import com.api.userparcelservice.dto.MqDTO;
import com.api.userparcelservice.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {


    private final JmsTemplate jmsTemplate;


    @PostMapping("/user/login")
    public ResponseEntity<UserEntity> login(@RequestBody UserLoginRequest userLoginRequest) {
        jmsTemplate.convertAndSend("requestqueue", userLoginRequest);

        MqDTO mqDTO = (MqDTO) jmsTemplate
                .receiveAndConvert("responsequeue");

        UserEntity userEntity = mqDTO.getUserEntity();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + mqDTO.getToken());


        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(userEntity);
    }


    @PostMapping("/user/register")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterUserRequest registerUserRequest) {
        jmsTemplate.convertAndSend("requestqueue", registerUserRequest);
        return ResponseEntity.ok((UserEntity) jmsTemplate
                .receiveAndConvert("responsequeue"));
    }


}
