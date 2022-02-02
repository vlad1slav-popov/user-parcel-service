package com.api.userparcelservice.domain;


import lombok.Data;

@Data
public class UserLoginRequest {

    private String username;
    private String password;


}
