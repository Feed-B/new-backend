package com.example.team_12_be.project.repository;

import com.example.team_12_be.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProejctJpaRepository extends JpaRepository<Project, Long> {
    @Modifying
    @Query("update Project p set p.viewCount = p.viewCount + 1 where p.id = :projectId")
    void addViewCount(@Param("projectId") Long projectId);
}
