package com.api.userparcelservice.controller;

import com.api.userparcelservice.domain.LogoutRequest;
import com.api.userparcelservice.domain.LogoutResponse;
import com.api.userparcelservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class UserController {

    private final JmsTemplate jmsTemplate;

    @Secured("ROLE_COURIER, ROLE_ADMIN")
    @PostMapping("user/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest logoutRequest) {
        jmsTemplate.convertAndSend("requestqueue", logoutRequest);
        return ResponseEntity.ok((LogoutResponse) jmsTemplate
                .receiveAndConvert("responsequeue"));
    }
}
