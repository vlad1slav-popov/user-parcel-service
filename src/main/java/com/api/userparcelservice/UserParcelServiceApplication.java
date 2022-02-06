package com.api.userparcelservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class UserParcelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserParcelServiceApplication.class, args);
    }

}
