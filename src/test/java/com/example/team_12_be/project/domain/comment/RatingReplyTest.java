package com.example.team_12_be.project.domain.comment;

import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.domain.ProjectRating;
import com.example.team_12_be.project.rating.comment.domain.RatingReply;
import com.example.team_12_be.project.rating.comment.repository.comment.ProjectCommentJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DataJpaTest
class RatingReplyTest {

    @Autowired
    ProjectCommentJpaRepository projectCommentRepository;

    @Test
    @DisplayName("부모 댓글 정보를 등록하는 데 성공한다.")
    void testAssignParentIdFrom() {
        Member member = mock(Member.class);
        ProjectRating projectRating = mock(ProjectRating.class);

        // 부모 코멘트
        RatingReply parentComment = new RatingReply(null, "comment1", member, projectRating);
        projectCommentRepository.save(parentComment);

        // 부모 코멘트에 대한 대댓글
        RatingReply childComment = new RatingReply(parentComment.getId(), "comment2", member, projectRating);
        projectCommentRepository.save(childComment);
        childComment.assignParentIdFrom(parentComment);

        assertThat(childComment.getParentId()).isEqualTo(parentComment.getId());
    }

    @Test
    @DisplayName("부모 댓글이 부모 댓글(즉 최상단 댓글) 을 가질 경우, 생성되는 자식 댓글은 최상단 댓글을 참조한다.")
    void testTopParentCommentIsAssigned() {
        Member member = mock(Member.class);
        ProjectRating projectRating = mock(ProjectRating.class);

        // 최상위 코멘트
        RatingReply topComment = new RatingReply(null, "comment3", member, projectRating);
        projectCommentRepository.save(topComment);

        // 부모 코멘트
        RatingReply parentComment = new RatingReply(topComment.getId(), "comment1", member, projectRating);
        projectCommentRepository.save(parentComment);
        parentComment.assignParentIdFrom(topComment);

        // 부모 코멘트에 대한 대댓글
        RatingReply childComment = new RatingReply(parentComment.getId(), "comment2", member, projectRating);
        projectCommentRepository.save(childComment);
        childComment.assignParentIdFrom(parentComment);

        // 최하위 대댓글도 최상위 부모 댓글을 참조한다
        assertThat(childComment.getParentId()).isEqualTo(topComment.getId());
    }
}