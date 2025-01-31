package com.example.team_12_be.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ProjectTechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String techStack;

    @ManyToOne(fetch = FetchType.LAZY) // LAZY 로 하지 않아도 무방할듯
    @JoinColumn(name = "post_id")
    private Project project;

    public ProjectTechStack(String techStack) {
        this.techStack = techStack;
    }

    public void assign(Project project) {
        this.project = project;
    }

    public void update(String techStack) {
        this.techStack = techStack;
    }
}
