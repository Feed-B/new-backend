package com.example.team_12_be.project.service.dto.response;

import com.example.team_12_be.project.domain.vo.StarRank;

public record StarRankResponseDto(float averageRank,
                                  float ideaRank,
                                  float designRank,
                                  float functionRank,
                                  float completionRank,
                                  long rankCount) {

    public static StarRankResponseDto of(StarRank rank, long rankCount) {
        return new StarRankResponseDto(
                rank.getAverageRank(),
                rank.getIdeaRank(),
                rank.getDesignRank(),
                rank.getFunctionRank(),
                rank.getCompletionRank(),
                rankCount
        );
    }
}
