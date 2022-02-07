package com.api.userparcelservice.service;


import com.api.userparcelservice.dto.OnUserLogoutSuccessEvent;
import com.api.userparcelservice.security.jwt.LoggedOutJwtTokenCache;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OnUserLogoutSuccessEventListener implements ApplicationListener<OnUserLogoutSuccessEvent> {

    private final LoggedOutJwtTokenCache tokenCache;
    private final Logger logger;

    @Autowired
    public OnUserLogoutSuccessEventListener(LoggedOutJwtTokenCache tokenCache,
                                            Logger logger) {
        this.tokenCache = tokenCache;
        this.logger = logger;
    }

    public void onApplicationEvent(OnUserLogoutSuccessEvent event) {
        logger.info(String.format("Log out success event received for user [%s] ", event.getUserName()));
        tokenCache.markLogoutEventForToken(event);
    }
}
