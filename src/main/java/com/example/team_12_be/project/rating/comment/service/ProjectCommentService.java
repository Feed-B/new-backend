package com.example.team_12_be.project.rating.comment.service;

import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.member.service.dto.MemberService;
import com.example.team_12_be.project.domain.ProjectRating;
import com.example.team_12_be.project.rating.comment.domain.ProjectCommentRepository;
import com.example.team_12_be.project.rating.comment.domain.RatingReply;
import com.example.team_12_be.project.rating.comment.service.dto.CommentUpdateRequestDto;
import com.example.team_12_be.project.rating.comment.service.dto.ProjectCommentRequestDto;
import com.example.team_12_be.project.rating.service.ProjectRatingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectCommentService {

    private final ProjectCommentRepository projectCommentRepository;

    private final ProjectRatingQueryService projectRatingQueryService;

    private final MemberService memberService;

    public Long addReply(Long ratingId, Long memberId, ProjectCommentRequestDto projectCommentRequestDto) {
        ProjectRating rating = projectRatingQueryService.findRatingById(ratingId);
        Member replyAuthor = memberService.findById(memberId);

        RatingReply ratingReply = projectCommentRequestDto.toEntity(rating, replyAuthor);
        this.assignParentId(projectCommentRequestDto, ratingReply);

        projectCommentRepository.save(ratingReply);
        return ratingReply.getId();
    }

    public void updateReply(Long replyId, Long memberId, CommentUpdateRequestDto commentUpdateRequestDto) {
        RatingReply ratingReply = projectCommentRepository.findByIdAndMemberId(replyId, memberId).orElseThrow(
                () -> new IllegalArgumentException("없는 코멘트")
        );

        ratingReply.updateContent(commentUpdateRequestDto.comment());
    }

    public void deleteComment(Long replyId, Long memberId) {
        RatingReply ratingReply = projectCommentRepository.findByIdAndMemberId(replyId, memberId).orElseThrow(
                () -> new IllegalArgumentException("없는 코멘트")
        );

        deleteChildIfPresent(ratingReply);
        projectCommentRepository.delete(ratingReply);
    }

    private void deleteChildIfPresent(RatingReply ratingReply) {
        List<RatingReply> childComments = projectCommentRepository.findAllByParentId(ratingReply.getId());
        if (childComments.isEmpty()) {
            return;
        }
        projectCommentRepository.deleteAll(childComments);
    }

    public void deleteByRatingId(Long ratingId) {
        projectCommentRepository.deleteAllByProjectRatingId(ratingId);
    }

    private void assignParentId(ProjectCommentRequestDto projectCommentRequestDto, RatingReply newComment) {
        // 만약 요청으로 들어온 부모 아이디가 부모 아이디를 가진다면, 이 코멘트 또한 최상단 부모아이디를 참조한다.
        Long requestParentId = projectCommentRequestDto.parentId();

        if (requestParentId == null || requestParentId == 0) {
            return;
        }

        RatingReply parentReply = projectCommentRepository.findById(requestParentId).orElseThrow(
                () -> new IllegalArgumentException("부모 댓글 아이디가 부적절하다.")
        );

        newComment.assignParentIdFrom(parentReply);
    }

}
