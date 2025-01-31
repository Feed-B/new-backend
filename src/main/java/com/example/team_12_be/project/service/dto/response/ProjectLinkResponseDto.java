package com.example.team_12_be.project.service.dto.response;

import com.example.team_12_be.project.domain.ProjectLink;

public record ProjectLinkResponseDto(
        Long id,
        String siteType,
        String url) {

    public static ProjectLinkResponseDto of(ProjectLink projectLink) {
        return new ProjectLinkResponseDto(projectLink.getId(), projectLink.getSiteType(), projectLink.getUrl());
    }
}
