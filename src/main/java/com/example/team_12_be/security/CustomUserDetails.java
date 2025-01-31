package com.example.team_12_be.security;

import com.example.team_12_be.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class CustomUserDetails implements OAuth2User, UserDetails {

    private final Member member;
    private Map<String, Object> attributes;
    private String jwtName;

    //oauth2용
    public CustomUserDetails(Member member, Map<String, Object> attributes, String jwtName) {
        this.member = member;
        this.attributes = attributes;
        this.jwtName = jwtName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //TODO : ROLE 정하기
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public String getUserEmail() {
        return member.getEmail();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return member.getNickName();
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

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return "name";
    }


}
