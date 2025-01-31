package com.example.team_12_be.member.domain;

import com.example.team_12_be.member.domain.vo.Job;
import com.example.team_12_be.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberTest {
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        Member member = new Member("test", "test", "test", Job.ANDROID);

        memberRepository.saveAndFlush(member);
    }

    @Test
    void softDeleteTest() {
        Member member = memberRepository.findByEmail("test").orElseThrow();
        memberRepository.delete(member);

        Optional<Member> memberOptional = memberRepository.findByIdIncludingDeleted(member.getId());
        assertThat(memberOptional).isPresent();

        Member reloadedMember = memberOptional.get();
        assertThat(reloadedMember.getIsDeleted()).isTrue();
    }
}