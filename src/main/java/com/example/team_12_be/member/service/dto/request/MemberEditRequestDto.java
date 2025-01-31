package com.example.team_12_be.member.service.dto.request;

import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.member.domain.vo.Job;

public record MemberEditRequestDto(
        Long id,
        String nickName,
        String aboutMe,
        Job job
) {

    public Member toEntity() {
        return new Member(
                id,
                nickName,
                aboutMe,
                job
        );
    }
}

