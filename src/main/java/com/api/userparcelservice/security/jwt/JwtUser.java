package com.api.userparcelservice.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JwtUser implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

//    public JwtUser(Long id,
//                   String username,
//                   String password,
//                   Boolean enabled,
//                   Collection<? extends GrantedAuthority> authorities) {
//
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.enabled = enabled;
//        this.authorities = authorities;
//    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
