package com.api.userparcelservice.domain;

import lombok.Data;

@Data
public class LogoutRequest {

    private String token;
    private String username;
}
