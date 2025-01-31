package com.example.team_12_be.project.service.dto.response;

import com.example.team_12_be.member.domain.vo.Job;

import java.util.List;

public record JobWithTeammateResponseDto(
        Job job,
        List<ProjectTeammateResponseDto> teammateList
) {
}
