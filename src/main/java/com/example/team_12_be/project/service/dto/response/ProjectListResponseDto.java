package com.example.team_12_be.project.service.dto.response;

import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.domain.ProjectTechStack;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectListResponseDto(
        Long projectId,
        String thumbnailUrl,
        List<String> stackList,
        Long likeCount,
        boolean isLiked,
        String projectTitle,
        String introduction,
        Long viewCount,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static ProjectListResponseDto of(Project project, Long likeCount, boolean isLiked, Long viewCount) {
        return new ProjectListResponseDto(
                project.getId(),
                project.getThumbnailUrl(),
                project.getProjectTechStacks().stream().map(ProjectTechStack::getTechStack).toList(),
                likeCount,
                isLiked,
                project.getTitle(),
                project.getIntroductions(),
                viewCount,
                project.getCreatedAt(),
                project.getModifiedAt()
        );
    }
}
