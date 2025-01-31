package com.example.team_12_be.project.service.dto.response;

import com.example.team_12_be.member.domain.vo.Job;
import com.example.team_12_be.project.domain.ProjectTeammate;

public record ProjectTeammateResponseDto(
        Long id,
        String teammateName,
        Job job,
        String url
) {
    public static ProjectTeammateResponseDto of(ProjectTeammate projectTeammate) {
        return new ProjectTeammateResponseDto(projectTeammate.getId(), projectTeammate.getTeammateName(), projectTeammate.getJob(), projectTeammate.getUrl());
    }
}
