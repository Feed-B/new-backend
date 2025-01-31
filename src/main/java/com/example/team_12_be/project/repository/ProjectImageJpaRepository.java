package com.example.team_12_be.project.repository;

import com.example.team_12_be.project.domain.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectImageJpaRepository extends JpaRepository<ProjectImage, Long> {
    @Query("SELECT pi FROM ProjectImage pi WHERE pi.index = :idx AND pi.project.id = :projectId")
    ProjectImage findByIndex(int idx , Long projectId);
}
