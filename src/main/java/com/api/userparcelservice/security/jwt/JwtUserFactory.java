package com.api.userparcelservice.security.jwt;



import com.api.userparcelservice.entity.RoleEntity;
import com.api.userparcelservice.entity.Status;
import com.api.userparcelservice.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(UserEntity userEntity) {

        return JwtUser.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(getGrantedAuthorityList(new ArrayList<>(userEntity
                        .getRoles())))
                .enabled(userEntity.getStatus().equals(Status.ACTIVE))
                .build();
    }

    private static List<GrantedAuthority> getGrantedAuthorityList(List<RoleEntity> userRoleEntities) {

        log.info("get granted authority list: " + userRoleEntities.get(0).getName());

        return userRoleEntities.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
    }

}
