package com.example.team_12_be.project.repository.query;

import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.domain.ProjectLike;
import com.example.team_12_be.project.domain.ProjectQueryRepository;
import com.example.team_12_be.project.presentation.request.SortCondition;
import com.example.team_12_be.project.repository.ProjectLikeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DefaultProjectQueryRepository implements ProjectQueryRepository {

    private final ProjectQueryJpaRepository projectQueryJpaRepository;

    private final ProjectLikeJpaRepository projectLikeJpaRepository;

    private final ProjectQuerydslRepository projectQuerydslRepository;

    @Override
    public Optional<Project> findById(Long projectId) {
        return projectQueryJpaRepository.findById(projectId);
    }

    @Override
    public List<ProjectLike> findLikesByProjectIdWithMember(Long projectId) {
        return projectLikeJpaRepository.findByProjectIdWithMember(projectId);
    }

    @Override
    public long countLikeByProjectId(Long projectId) {
        return projectLikeJpaRepository.countByProjectId(projectId);
    }

    @Override
    public Page<Project> findProjectsProjectTechStacksOrderBySortCondition(SortCondition sortCondition, List<String> projectTechStacks, String searchString, Pageable pageable) {
        return projectQuerydslRepository.findProjectsProjectTechStacksOrderBySortCondition(sortCondition, projectTechStacks, searchString, pageable);
    }

    @Override
    public boolean existsLikeByMemberIdAndProjectId(Long memberId, Long projectId) {
        return projectLikeJpaRepository.existsByMemberIdAndProjectId(memberId, projectId);
    }

    @Override
    public Page<Project> findAllMyLikedProjects(Long memberId, Pageable pageable) {
        return projectQueryJpaRepository.findLikedProjectsByMemberId(memberId, pageable);
    }

    @Override
    public Page<Project> findAllByAuthorIdOrderByCreatedAtDesc(Long memberId, Pageable pageable) {
        return projectQueryJpaRepository.findAllByAuthorIdOrderByCreatedAtDesc(memberId, pageable);
    }
}
