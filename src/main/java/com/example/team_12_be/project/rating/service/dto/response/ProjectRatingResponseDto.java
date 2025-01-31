package com.example.team_12_be.project.rating.service.dto.response;

import com.example.team_12_be.member.domain.vo.Job;
import com.example.team_12_be.project.domain.ProjectRating;
import com.example.team_12_be.project.domain.vo.StarRank;

public record ProjectRatingResponseDto(Long ratingId,
                                       float averageRank,
                                       float ideaRank,
                                       float designRank,
                                       float functionRank,
                                       float completionRank,
                                       String comment,
                                       Long childCommentCount,
                                       String authorProfileImageUrl,
                                       Long authorId,
                                       String authorName,
                                       Job memberJob
                                       ) {
    public static ProjectRatingResponseDto of(ProjectRating projectRating, Long childCommentCount) {
        StarRank starRank = projectRating.getStarRank();
        return new ProjectRatingResponseDto(
                projectRating.getId(),
                starRank.getAverageRank(),
                starRank.getIdeaRank(),
                starRank.getDesignRank(),
                starRank.getFunctionRank(),
                starRank.getCompletionRank(),
                projectRating.getComment(),
                childCommentCount,
                projectRating.getMember().getImageUrl(),
                projectRating.getMember().getId(),
                projectRating.getMember().getNickName(),
                projectRating.getMember().getMemberJob()
        );
    }
}
