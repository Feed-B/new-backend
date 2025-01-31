package com.example.team_12_be.project.domain;

import com.example.team_12_be.member.domain.vo.Job;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProjectTeammate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectId", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Project project;

    private String teammateName;

    @Enumerated(EnumType.STRING)
    private Job job;

    private String url;

    public ProjectTeammate(String teammateName, Job job, String url) {
        this.teammateName = teammateName;
        this.job = job;
        this.url = url;
    }

    public void assign(Project project) {
        this.project = project;
    }

    public void update(String teammateName, Job job, String url) {
        this.teammateName = teammateName;
        this.job = job;
        this.url = url;
    }
}
