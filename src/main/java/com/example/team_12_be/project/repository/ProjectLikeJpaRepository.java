package com.example.team_12_be.project.repository;

import com.example.team_12_be.project.domain.ProjectLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectLikeJpaRepository extends JpaRepository<ProjectLike, Long> {

    @Query("SELECT pl FROM ProjectLike pl JOIN FETCH pl.member WHERE pl.project.id = :projectId")
    List<ProjectLike> findByProjectIdWithMember(@Param("projectId") Long projectId);

    boolean existsByMemberIdAndProjectId(Long memberId, Long projectId);

    void deleteLikeByMemberIdAndProjectId(Long memberId, Long projectId);

    long countByProjectId(Long projectId);
}
