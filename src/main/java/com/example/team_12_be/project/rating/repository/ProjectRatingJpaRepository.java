package com.example.team_12_be.project.rating.repository;

import com.example.team_12_be.project.domain.ProjectRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRatingJpaRepository extends JpaRepository<ProjectRating, Long> {

    boolean existsByMemberIdAndProjectId(Long memberId, Long projectId);

    Optional<ProjectRating> findByMemberIdAndProjectId(Long memberId, Long projectId);

    long countByProjectId(Long projectId);

    Page<ProjectRating> findAllByProjectIdOrderByCreatedAtDesc(Long projectId, Pageable pageable);

}
