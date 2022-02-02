package com.api.userparcelservice.dto;

import com.api.userparcelservice.domain.LogoutRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@ToString
public class OnUserLogoutSuccessEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;
    private final String userName;
    private final String token;
    private final transient LogoutRequest logOutRequest;
    private final Date eventTime;


    public OnUserLogoutSuccessEvent(String userName,
                                    String token,
                                    LogoutRequest logOutRequest) {
        super(userName);
        this.userName = userName;
        this.token = token;
        this.logOutRequest = logOutRequest;
        this.eventTime = Date.from(Instant.now());
    }




}
