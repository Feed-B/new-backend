package com.example.team_12_be.project.service.dto.request;

import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.domain.vo.ImageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

// TODO : 메시지 정리, constraint 정리
public record ProjectRequestDto(
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
        List<String> projectTechStacks,
        @NotEmpty
        List<ProjectTeammateRequestDto> projectTeammates,
        @NotEmpty
        List<ProjectLinkRequestDto> projectLinks
) {
    public Project toEntity(Member author) {
        return new Project(
                title,
                introduction,
                content,
                author,
                serviceUrl,
                imageType
        );
    }
}