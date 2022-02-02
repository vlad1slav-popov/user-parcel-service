package com.api.userparcelservice.security.jwt;

import com.api.userparcelservice.dto.OnUserLogoutSuccessEvent;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class LoggedOutJwtTokenCache {


    @Value("${jwt.token.secret}")
    private String secret;
    private final ExpiringMap<String, OnUserLogoutSuccessEvent> tokenEventMap;
    private final Logger logger;

    @Autowired
    public LoggedOutJwtTokenCache(Logger logger) {
        this.tokenEventMap = ExpiringMap.builder()
                .variableExpiration()
                .maxSize(1000)
                .build();
        this.logger = logger;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));
    }

    public void markLogoutEventForToken(OnUserLogoutSuccessEvent event) {
        String token = event.getToken();
        if (tokenEventMap.containsKey(token)) {
            logger.info(String.format("Log out token for user [%s] is " +
                            "already present in the cache",
                    event.getUserName()));
        } else {
//            logger.info("Token event map: " + tokenEventMap);
            Date tokenExpiryDate = getTokenExpiryFromJWT(token);
            long ttlForToken = getTTLForToken(tokenExpiryDate);
            logger.info(String.format("Logout token cache set for [%s] with a TTL of [%s] " +
                            "seconds." +
                    " Token is due expiry at [%s]", event.getUserName(),
                    ttlForToken, tokenExpiryDate));
            tokenEventMap.put(token, event, ttlForToken, TimeUnit.SECONDS);
//            logger.info("Token event map after put: " + tokenEventMap);
        }
    }

    public OnUserLogoutSuccessEvent getLogoutEventForToken(String token) {
//        logger.info("Token event map: " + tokenEventMap);
        return tokenEventMap.get(token);
    }

    private long getTTLForToken(Date date) {
        long secondAtExpiry =  date.toInstant().getEpochSecond();
        long secondAtLogout =  Instant.now().getEpochSecond();
        return Math.max(0, secondAtExpiry - secondAtLogout);
    }

    public Date getTokenExpiryFromJWT(String token) {
//        logger.info("secret: " + secret);
//        logger.info("GetTokenExpiryFromJWT: " + token);

//        secret = Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        Key key = Keys.hmacShaKeyFor(keyBytes);
//        logger.info("Get token expiry key: " + Arrays.toString(key.getEncoded()));

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.trim())
                .getBody().getExpiration();
    }
}
