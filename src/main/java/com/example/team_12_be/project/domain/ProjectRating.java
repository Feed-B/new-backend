package com.example.team_12_be.project.domain;

import com.example.team_12_be.base.TimeStamp;
import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.project.domain.vo.StarRank;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(indexes = {
        @Index(name = "idx_project_member", columnList = "member_id, project_id", unique = true)
})
public class ProjectRating extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Embedded
    private StarRank starRank;

    private String comment;

    public ProjectRating(Member member, Project project, StarRank starRank, String comment) {
        this.member = member;
        this.project = project;
        this.starRank = starRank;
        this.comment = comment;
    }

    public void updateRank(StarRank starRank) {
        this.starRank = starRank;
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }

    public void assignToProject(Project project) {
        this.project = project;
    }
}
