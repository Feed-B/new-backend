package com.example.team_12_be.security;

import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.member.repository.MemberRepository;
import com.example.team_12_be.security.exception.OAuth2UserNotFoundException;
import com.example.team_12_be.security.oauth_info.KakaoMemberInfo;
import com.example.team_12_be.security.oauth_info.NaverMemberInfo;
import com.example.team_12_be.security.oauth_info.OAuth2MemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("================OAuth2MemberService==================");
        DefaultOAuth2UserService delegeate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegeate.loadUser(userRequest);
        OAuth2MemberInfo memberInfo = null;
        //System.out.println("oauth2User.getAttributes() : " + oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        //System.out.println("registrationId : " + registrationId);
        if (registrationId.equals("kakao")) {
            memberInfo = new KakaoMemberInfo(oAuth2User.getAttributes());
        } else if (registrationId.equals("naver")) {
            memberInfo = new NaverMemberInfo((Map) oAuth2User.getAttributes().get("response"));
        }

        String provider = memberInfo.getProvider();
        String providerId = memberInfo.getProviderId();
        String jwtName = provider + "_" + providerId; //중복이 발생하지 않도록 provider와 providerId를 조합(토큰 발급시);

        String email = memberInfo.getEmail();
        String role = "ROLE_USER"; //TODO 권한 결정 후 변경
        Optional<Member> findMember = memberRepository.findByEmail(email);
        Member member = null;
        log.info("email = " + email);
        log.info("provider = " + provider);
        log.info("role = " + role);
        if (findMember.isEmpty()) { //계정 없을 때
            throw new OAuth2UserNotFoundException(email);
        } else {//계정 있을 때
            member = findMember.get();
            log.info("findMember.get = " + member);

        }
        return new CustomUserDetails(member, oAuth2User.getAttributes(), jwtName);
    }
}
