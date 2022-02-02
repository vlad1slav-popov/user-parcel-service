package com.api.userparcelservice.controller;

import com.api.userparcelservice.domain.LogoutRequest;
import com.api.userparcelservice.entity.UserEntity;
import com.api.userparcelservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class UserController {

    private final Logger logger;
    private final UserService userService;

    @GetMapping("hello")
    public String hello() {
        logger.info("LOGGER WORKS!");
        return "hello";
    }

    @PostMapping("user/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request) {
        return userService.logout(request);
    }

    @PostMapping("users/all")
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("user")
    public ResponseEntity<UserEntity> getUser(@RequestParam String username) {
        return ResponseEntity.ok(userService.getUserDataByUsername(username));
    }

}
