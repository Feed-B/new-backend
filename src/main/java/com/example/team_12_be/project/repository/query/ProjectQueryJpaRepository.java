package com.example.team_12_be.project.repository.query;

import com.example.team_12_be.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectQueryJpaRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p INNER JOIN p.projectLikes like WHERE like.member.id = :memberId ORDER BY p.createdAt DESC")
    Page<Project> findLikedProjectsByMemberId(@Param("memberId") Long memberId, Pageable pageable);


    Page<Project> findAllByAuthorIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

}
