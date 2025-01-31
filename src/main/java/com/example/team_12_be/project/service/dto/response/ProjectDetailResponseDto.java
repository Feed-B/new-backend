package com.example.team_12_be.project.service.dto.response;

import com.example.team_12_be.member.domain.vo.Job;
import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.domain.vo.ImageType;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectDetailResponseDto(
        // 프로젝트 정보, 댓글 8개(최대)
        Long projectId,
        Long memberId,
        String authorName,
        Job authorJob,
        LocalDateTime createdAt,
        Long likeCount,
        String title,
        String content,
        String introductions,
        String serviceUrl,
        ImageType imageType,
        List<ProjectLinkResponseDto> projectLinks,
        List<ProjectTechStackResponseDto> projectTechStacks,
        String thumbnailUrl,
        List<ProjectImageResponseDto> imageUrlList,
        boolean isMine,
        boolean isLiked
) {
    public static ProjectDetailResponseDto of(Project project, Long likeCount, boolean isMine, boolean isLiked) {
        return new ProjectDetailResponseDto(
                project.getId(),
                project.getAuthor().getId(),
                project.getAuthor().getNickName(),
                project.getAuthor().getMemberJob(),
                project.getCreatedAt(),
                likeCount,
                project.getTitle(),
                project.getContent(),
                project.getIntroductions(),
                project.getServiceUrl(),
                project.getImageType(),
                project.getProjectLinks().stream().map(ProjectLinkResponseDto::of).toList(),
                project.getProjectTechStacks().stream().map(ProjectTechStackResponseDto::of).toList(),
                project.getThumbnailUrl(),
                project.getProjectImages().stream().map(ProjectImageResponseDto::of).toList(),
                isMine,
                isLiked
        );
    }
}