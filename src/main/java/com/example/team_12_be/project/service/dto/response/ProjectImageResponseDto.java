package com.example.team_12_be.project.service.dto.response;

import com.example.team_12_be.project.domain.ProjectImage;
import com.example.team_12_be.project.domain.vo.ImageType;

public record ProjectImageResponseDto(
        Long id,
        String url,
        ImageType imageType,
        int idx
) {
    public static ProjectImageResponseDto of(ProjectImage projectImage) {
        return new ProjectImageResponseDto(
                projectImage.getId(),
                projectImage.getUrl(),
                projectImage.getProject().getImageType(),
                projectImage.getIndex()
        );
    }
}
