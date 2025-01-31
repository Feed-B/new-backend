package com.example.team_12_be.project.rating.comment.domain;

import com.example.team_12_be.base.TimeStamp;
import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.project.domain.ProjectRating;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class RatingReply extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rating_id")
    private ProjectRating projectRating;

    public RatingReply(Long parentId, String comment, Member member, ProjectRating projectRating) {
        this.parentId = parentId;
        this.comment = comment;
        this.projectRating = projectRating;
        this.member = member;
    }

    public void assignParentIdFrom(RatingReply parentComment) {
        if (parentComment.getParentId() != null) {
            this.parentId = parentComment.getParentId();
            return;
        }

        this.parentId = parentComment.getId();
    }

    public void updateContent(String content) {
        this.comment = content;
    }
}
