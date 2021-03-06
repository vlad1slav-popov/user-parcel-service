package com.api.userparcelservice.service;


import com.api.userparcelservice.domain.UserLoginRequest;
import com.api.userparcelservice.dto.MqDTO;
import com.api.userparcelservice.entity.Status;
import com.api.userparcelservice.entity.UserEntity;
import com.api.userparcelservice.exception.UserException;
import com.api.userparcelservice.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;


    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtTokenProvider jwtTokenProvider,
                                 UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    public MqDTO getLoginResponse(UserLoginRequest requestDto) {
        System.out.println("ok");
        try {
            String username = requestDto.getUsername();
            UserEntity user = userService.getUserDataByUsernameAndPassword(requestDto);
            System.out.println(user);

            if (!user.getStatus().equals(Status.ACTIVE)) {
                throw new UserException("USER STATUS IS: " + user.getStatus(), "001");
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                    requestDto.getPassword()));
            System.out.println("authenticated");


            String token = jwtTokenProvider.createToken(user, user.getRoles());
//            System.out.println(token);
//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.set("Authorization", "Bearer_" + token);


            return MqDTO.builder()
                    .token(token)
                    .userEntity(user)
                    .build();
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new BadCredentialsException("Invalid username or password");
        }
    }


}
