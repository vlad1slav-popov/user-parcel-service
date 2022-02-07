package com.api.userparcelservice.security.jwt;


import com.api.userparcelservice.dto.OnUserLogoutSuccessEvent;
import com.api.userparcelservice.entity.RoleEntity;
import com.api.userparcelservice.entity.Status;
import com.api.userparcelservice.entity.UserEntity;
import com.api.userparcelservice.exception.JwtAuthenticationException;
import com.api.userparcelservice.security.JwtUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private Long validInMilliSeconds;


    private final JwtUserDetailsService userDetailsService;
    private final LoggedOutJwtTokenCache loggedOutJwtTokenCache;


    public JwtTokenProvider(LoggedOutJwtTokenCache loggedOutJwtTokenCache,
                            JwtUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.loggedOutJwtTokenCache = loggedOutJwtTokenCache;
    }


    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(UserEntity userEntity, List<RoleEntity> roleEntities) {

        Claims claims = Jwts.claims().setSubject(userEntity.getUsername());
        claims.put("roles", getRoleNames(roleEntities));
        claims.put("id", userEntity.getId());
        claims.put("password", userEntity.getPassword());
        claims.put("status", userEntity.getStatus());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validInMilliSeconds);

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(
                getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails,
                "",
                userDetails.getAuthorities());
    }


    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }



    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        log.info("Token value: " + bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            validateTokenIsNotForALoggedOutDevice(token);
            return true;
        } catch (JwtException | IllegalArgumentException | JwtAuthenticationException exception) {
            log.info(exception.getMessage());
            return false;
        }
    }

    private void validateTokenIsNotForALoggedOutDevice(String authToken) {
        OnUserLogoutSuccessEvent previouslyLoggedOutEvent = loggedOutJwtTokenCache
                .getLogoutEventForToken(authToken);
        if (previouslyLoggedOutEvent != null) {
            String userEmail = previouslyLoggedOutEvent.getUserName();
            Date logoutEventDate = previouslyLoggedOutEvent.getEventTime();
            String errorMessage = String.format("Token corresponds to an already " +
                            "logged out user " +
                            "[%s] at [%s]. Please login again",
                    userEmail, logoutEventDate);
            throw new JwtAuthenticationException(errorMessage);
        }
    }

    private List<String> getRoleNames(List<RoleEntity> userRoleEntities) {
        List<String> result = new ArrayList<>();

        userRoleEntities.forEach(role -> {
            result.add(role.getName());
        });

        return result;
    }

}
