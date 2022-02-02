package com.api.userparcelservice.service;




import com.api.userparcelservice.domain.UserLoginRequest;
import com.api.userparcelservice.entity.Status;
import com.api.userparcelservice.entity.UserEntity;
import com.api.userparcelservice.exception.UserException;
import com.api.userparcelservice.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtTokenProvider jwtTokenProvider,
                                 UserService userService,
                                 BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<UserEntity> getLoginResponse(UserLoginRequest requestDto) {
        try {
            String username = requestDto.getUsername();
            UserEntity user = userService.getUserDataByUsernameAndPassword(requestDto);

            if (!user.getStatus().equals(Status.ACTIVE)) {
                throw new UserException("USER STATUS IS: " + user.getStatus());
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                    requestDto.getPassword()));

            String token = jwtTokenProvider.createToken(username, user.getRoles());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer_" + token);



            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(user);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }


}
