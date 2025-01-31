package com.example.team_12_be.project.service.usecase.update.dto.request;

import com.example.team_12_be.member.domain.vo.Job;

public record ProjectTeammateUpdateRequestDto(
        Long id,
        String name,
        Job job,
        String url
) {
}
