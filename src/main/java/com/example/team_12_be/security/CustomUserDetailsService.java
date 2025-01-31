package com.example.team_12_be.security;

import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String oauthId) throws UsernameNotFoundException {
        Member member = userRepository.findByEmail(oauthId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을수 없습니다."));

        return new CustomUserDetails(member);
    }
}

