package com.example.team_12_be.project.rating.service.dto.response;

import com.example.team_12_be.project.domain.ProjectRating;

public record MyProjectRatingResponseDto(
        boolean exists,
        ProjectRatingResponseDto projectRating
) {
    public static MyProjectRatingResponseDto of(ProjectRating projectRating, Long commentCount){
        ProjectRatingResponseDto projectRatingResponseDto = ProjectRatingResponseDto.of(projectRating, commentCount);
        return new MyProjectRatingResponseDto(true, projectRatingResponseDto);
    }

    public static MyProjectRatingResponseDto ofNull(){
        return new MyProjectRatingResponseDto(false, null);
    }
}
