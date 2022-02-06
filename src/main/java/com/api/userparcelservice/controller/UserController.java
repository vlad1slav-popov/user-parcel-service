package com.api.userparcelservice.controller;

import com.api.userparcelservice.domain.LogoutRequest;
import com.api.userparcelservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class UserController {

//    private final Logger logger;
    private final UserService userService;

    @Secured("ROLE_USER")
    @GetMapping("hello")
    public String hello() {
//        logger.info("LOGGER WORKS!");
        return "hello";
    }

    @Secured("ROLE_USER")
    @PostMapping("user/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request) {
        return userService.logout(request);
    }


}
