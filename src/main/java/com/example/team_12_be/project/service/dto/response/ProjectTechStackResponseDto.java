package com.example.team_12_be.project.service.dto.response;

import com.example.team_12_be.project.domain.ProjectTechStack;

public record ProjectTechStackResponseDto(
        Long id,
        String techStack) {

    public static ProjectTechStackResponseDto of(ProjectTechStack projectTechStack) {
        return new ProjectTechStackResponseDto(projectTechStack.getId(), projectTechStack.getTechStack());
    }


}
