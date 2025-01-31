package com.example.team_12_be.project.service.usecase.update.dto.request;

import com.example.team_12_be.project.domain.vo.ImageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ProjectUpdateRequestDto(
        @NotBlank
        String title,
        @NotBlank
        String introduction,
        String content,
        @NotBlank
        String serviceUrl,
        @NotBlank
        ImageType imageType,
        @NotEmpty
        List<ProjectTechStackUpdateDto> projectTechStacks,
        @NotEmpty
        List<ProjectTeammateUpdateRequestDto> projectTeammates,
        List<ProjectLinkUpdateRequestDto> projectLinks
) {
}
