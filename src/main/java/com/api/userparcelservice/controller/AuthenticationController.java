package com.api.userparcelservice.controller;




import com.api.userparcelservice.domain.RegisterUserRequest;
import com.api.userparcelservice.domain.UserLoginRequest;
import com.api.userparcelservice.entity.UserEntity;
import com.api.userparcelservice.service.AuthenticationService;
import com.api.userparcelservice.service.UserAuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserAuthorizationService userAuthorizationService;

    public AuthenticationController(AuthenticationService authenticationService,
                                    UserAuthorizationService userAuthorizationService) {
        this.authenticationService = authenticationService;
        this.userAuthorizationService = userAuthorizationService;
    }


    @PostMapping("/login")
    public ResponseEntity<UserEntity> login(@RequestBody UserLoginRequest requestDto) {
        return authenticationService.getLoginResponse(requestDto);
    }


    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterUserRequest request) {
        return userAuthorizationService.getRegisterResponse(request);
    }

}
