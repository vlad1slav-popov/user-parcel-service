package com.api.userparcelservice.security;



import com.api.userparcelservice.entity.UserEntity;
import com.api.userparcelservice.exception.UserNotFoundException;
import com.api.userparcelservice.security.jwt.JwtUser;
import com.api.userparcelservice.security.jwt.JwtUserFactory;
import com.api.userparcelservice.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = Optional.ofNullable(userService.getUserDataByUsername(username))
                .orElseThrow(() -> new UserNotFoundException("USER_NOT_FOUND"));

        JwtUser jwtUser = JwtUserFactory.create(userEntity);

        log.info("IN loadByUserName jwt user: " + jwtUser);
        return jwtUser;
    }
}
