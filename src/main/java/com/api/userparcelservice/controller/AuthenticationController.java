package com.api.userparcelservice.controller;


import com.api.userparcelservice.dto.MqDTO;
import com.api.userparcelservice.domain.RegisterUserRequest;
import com.api.userparcelservice.domain.UserLoginRequest;
import com.api.userparcelservice.entity.UserEntity;
import com.api.userparcelservice.service.UserAuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {


    private final JmsTemplate jmsTemplate;

//    private final AuthenticationService authenticationService;
    private final UserAuthorizationService userAuthorizationService;

    public AuthenticationController(JmsTemplate jmsTemplate,
//                                    AuthenticationService authenticationService,
                                    UserAuthorizationService userAuthorizationService) {
        this.jmsTemplate = jmsTemplate;
//        this.authenticationService = authenticationService;
        this.userAuthorizationService = userAuthorizationService;
    }


    @PostMapping("/login")
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



//        return authenticationService.getLoginResponse(requestDto);
    }


    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterUserRequest registerUserRequest) {
        jmsTemplate.convertAndSend("requestqueue", registerUserRequest);

        return ResponseEntity.ok((UserEntity) jmsTemplate
                .receiveAndConvert("responsequeue"));
    }

    @GetMapping(value = "/test/{name}", produces = "application/json")
    public UserEntity test(@PathVariable String name) {

        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername(name);
        userLoginRequest.setPassword("123456");

        jmsTemplate.convertAndSend("testqueue", userLoginRequest);

        return (UserEntity) jmsTemplate
                .receiveAndConvert("respqueue");

    }


}
