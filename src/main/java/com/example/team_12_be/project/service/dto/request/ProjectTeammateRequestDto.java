package com.example.team_12_be.project.service.dto.request;

import com.example.team_12_be.member.domain.vo.Job;
import com.example.team_12_be.project.domain.ProjectTeammate;

public record ProjectTeammateRequestDto(
        String name,
        Job job,
        String url
) {
    public ProjectTeammate toEntity() {
        return new ProjectTeammate(name, job, url);
    }
}
