package com.example.team_12_be.member.domain;

import com.example.team_12_be.member.domain.vo.Job;
import com.example.team_12_be.member.service.dto.MemberService;
import com.example.team_12_be.member.service.dto.request.MemberSignUpRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SignUpTest {
    private static final Logger logger = LoggerFactory.getLogger(SignUpTest.class);

    @Autowired
    MemberService memberService;

    @Test
    void signUpTest() {
        MemberSignUpRequest memberRequest = new MemberSignUpRequest(
                "gkstjr8332@naver.com",
                "김한석",
                "나는 김한석입니다."
                , Job.BACKEND
        );


        Member saveMember = memberService.signUp(memberRequest);


        assertEquals(memberRequest.email(), saveMember.getEmail());
        assertEquals(memberRequest.nickName(), saveMember.getNickName());
        assertEquals(memberRequest.aboutMe(), saveMember.getAboutMe());
        assertEquals(memberRequest.job(), saveMember.getMemberJob());
        assertEquals(1L, saveMember.getId());

        MemberSignUpRequest memberRequest2 = new MemberSignUpRequest(
                "gkstjr8332@kakao.com",
                "김한석",
                "나는 김한석입니다."
                , Job.BACKEND
        );

        Member saveMember2 = memberService.signUp(memberRequest2);


        assertEquals(memberRequest2.email(), saveMember2.getEmail());
        assertEquals(memberRequest2.nickName(), saveMember2.getNickName());
        assertEquals(memberRequest2.aboutMe(), saveMember2.getAboutMe());
        assertEquals(memberRequest2.job(), saveMember2.getMemberJob());
        assertEquals(2L, saveMember2.getId());
    }
}
