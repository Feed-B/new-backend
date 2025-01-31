package com.example.team_12_be.project.domain;

import com.example.team_12_be.base.TimeStamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ProjectImage extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private String url;

    @Column(name = "idx")
    private int index;

    public ProjectImage(String url, int index) {
        this.url = url;
        this.index = index;
    }

    public void assign(Project project) {
        this.project = project;
    }
}
