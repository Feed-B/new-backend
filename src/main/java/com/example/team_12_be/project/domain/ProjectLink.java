package com.example.team_12_be.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ProjectLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    // TODO : 이 부분, 어떻게 처리할 지 논의. 데이터셋으로 넣어둔다?
    private String siteType; // figma ...

    private String url;

    public ProjectLink(String siteType, String url) {
        this.siteType = siteType;
        this.url = url;
    }

    public void assign(Project project) {
        this.project = project;
    }

    public void update(String siteType, String url) {
        this.siteType = siteType;
        this.url = url;
    }
}
