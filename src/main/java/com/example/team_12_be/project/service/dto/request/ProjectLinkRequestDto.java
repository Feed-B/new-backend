package com.example.team_12_be.project.service.dto.request;

import com.example.team_12_be.project.domain.ProjectLink;

public record ProjectLinkRequestDto(String siteType, String url) {
    public ProjectLink toEntity() {
        return new ProjectLink(siteType, url);
    }
}
