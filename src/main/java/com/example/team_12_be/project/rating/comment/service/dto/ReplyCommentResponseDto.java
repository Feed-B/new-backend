package com.example.team_12_be.project.rating.comment.service.dto;

import com.example.team_12_be.member.domain.vo.Job;
import com.example.team_12_be.project.rating.comment.domain.RatingReply;

public record ReplyCommentResponseDto(
        Long replyId,
        Long userId,
        Job job,
        String author,
        String comment
) {
    public static ReplyCommentResponseDto of(RatingReply ratingReply) {
        return new ReplyCommentResponseDto(
                ratingReply.getId(),
                ratingReply.getMember().getId(),
                ratingReply.getMember().getMemberJob(),
                ratingReply.getMember().getNickName(),
                ratingReply.getComment()
        );
    }
}
