package com.example.team_12_be.project.rating.comment.service.dto;

import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.project.domain.ProjectRating;
import com.example.team_12_be.project.rating.comment.domain.RatingReply;
import jakarta.validation.constraints.NotNull;

public record ProjectCommentRequestDto(
        Long parentId,

        @NotNull
        String comment
) {
    public RatingReply toEntity(ProjectRating projectRating, Member member) {
        if (parentId == null || parentId == 0) {
            return new RatingReply(null, comment, member, projectRating);
        }
        return new RatingReply(parentId, comment, member, projectRating);
    }
}