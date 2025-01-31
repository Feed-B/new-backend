package com.example.team_12_be.project.service.dto.response;

import com.example.team_12_be.member.domain.vo.Job;

public record LikedMembersTechStackResponseDto(Job job, Long count) {

}
