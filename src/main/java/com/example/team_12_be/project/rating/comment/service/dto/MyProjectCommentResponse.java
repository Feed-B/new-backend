package com.example.team_12_be.project.rating.comment.service.dto;

public record MyProjectCommentResponse(
        boolean exists,
        ProjectCommentResponseDto projectCommentResponseDto
) {
}
