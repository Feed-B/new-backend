package com.example.team_12_be.member.service.dto.response;

import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.member.domain.vo.Job;

public record MemberResponseDto(
        Long id,
        String email,
        String nickName,
        String aboutMe,
        Job job,
        String imageUrl
) {
    // private String token;
    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getEmail(),
                member.getNickName(),
                member.getAboutMe(),
                member.getMemberJob(),
                member.getImageUrl()
        );
        // this.token = token;
    }
}