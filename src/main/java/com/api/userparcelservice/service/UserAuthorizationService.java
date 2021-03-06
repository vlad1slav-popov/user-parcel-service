package com.api.userparcelservice.service;



import com.api.userparcelservice.domain.RegisterUserRequest;
import com.api.userparcelservice.entity.RoleEntity;
import com.api.userparcelservice.entity.Status;
import com.api.userparcelservice.entity.UserEntity;
import com.api.userparcelservice.exception.UserException;
import com.api.userparcelservice.repository.RoleRepository;
import com.api.userparcelservice.repository.UserLoginRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class UserAuthorizationService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserLoginRepository userLoginRepository;

    private final RoleRepository roleRepository;

    public UserAuthorizationService(BCryptPasswordEncoder bCryptPasswordEncoder,
                                    UserLoginRepository userLoginRepository,
                                    RoleRepository roleRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userLoginRepository = userLoginRepository;
        this.roleRepository = roleRepository;
    }


    public UserEntity getRegisterResponse(RegisterUserRequest request) {

        if (Objects.isNull(request.getPassword()) ||
                Objects.isNull(request.getUsername())) {
            throw new UserException("Username or password are empty", "002");
        }

        if (request.getPassword().trim().isEmpty() ||
                request.getUsername().trim().isEmpty()) {
            throw new UserException("password or username is empty", "003");
        }

        if (Objects.nonNull(userLoginRepository.findUserEntityByUsername(request.getUsername()))) {
            throw new UserException("Username already exists", "004");
        }

        String encodedPass = bCryptPasswordEncoder.encode(request.getPassword());
        RoleEntity user = roleRepository.findRoleByName("ROLE_USER");
//        System.out.println(user);
        System.out.println(user);
        List<RoleEntity> roleEntityList = new ArrayList<>();
        roleEntityList.add(user);
        System.out.println(roleEntityList);



        UserEntity userEntity = UserEntity.builder()
                .username(request.getUsername())
                .password(encodedPass)
                .roles(roleEntityList)
                .status(Status.ACTIVE)
                .build();


        userLoginRepository.save(userEntity);



        return userEntity;

    }


}
