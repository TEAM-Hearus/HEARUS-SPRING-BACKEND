package com.hearus.hearusspring.data.oauth;

import com.hearus.hearusspring.data.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PrincipalDetails implements OAuth2User, UserDetails {

    private UserDTO userDTO;
    private Map<String, Object> attributes;

    public PrincipalDetails(UserDTO userDTO, Map<String, Object> attributes) {
        this.userDTO = userDTO;
        this.attributes = attributes;

    }

    //user 이름 가져오기
    @Override
    public String getName() {
        return userDTO.getUserName();
    }

    //third-party 에서 가져온 유저 정보 Map 가져오기
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(userDTO.getUserRole()));
    }

    @Override
    public String getPassword() {
        return userDTO.getUserPassword();
    }

    @Override
    public String getUsername() {

        return userDTO.getUserName();

    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
