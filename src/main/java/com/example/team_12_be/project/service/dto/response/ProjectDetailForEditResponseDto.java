package com.example.team_12_be.project.service.dto.response;

import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.domain.ProjectImage;
import com.example.team_12_be.project.domain.vo.ImageType;

import java.util.List;

public record ProjectDetailForEditResponseDto(
        String title,
        String introduction,
        String content,
        String serviceUrl,
        ImageType imageType,
        List<ProjectLinkResponseDto> projectLinks,
        List<ProjectTeammateResponseDto> projectTeammates,
        List<ProjectTechStackResponseDto> techStacks,
        String thumbnailUrl,
        List<String> imageUrlList
) {
    public static ProjectDetailForEditResponseDto of(Project project) {
        return new ProjectDetailForEditResponseDto(
                project.getTitle(),
                project.getIntroductions(),
                project.getContent(),
                project.getServiceUrl(),
                project.getImageType(),
                project.getProjectLinks().stream().map(ProjectLinkResponseDto::of).toList(),
                project.getProjectTeammates().stream().map(ProjectTeammateResponseDto::of).toList(),
                project.getProjectTechStacks().stream().map(ProjectTechStackResponseDto::of).toList(),
                project.getThumbnailUrl(),
                project.getProjectImages().stream().map(ProjectImage::getUrl).toList()
        );
    }
}
