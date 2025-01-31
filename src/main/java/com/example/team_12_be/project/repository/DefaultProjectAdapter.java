package com.example.team_12_be.project.repository;

import com.example.team_12_be.project.domain.*;
import com.example.team_12_be.project.rating.repository.ProjectRatingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// TODO : 루트 애그리거트인 Project 의 처리 범위가 늘어감에 따라 Repository 라는 이름보다 Port 라는 이름을 사용하는 것이 더 명확해 질 수 있다.
@Repository
@RequiredArgsConstructor
public class DefaultProjectAdapter implements ProjectPort {

    private final ProejctJpaRepository proejctJpaRepository;

    private final ProjectRatingJpaRepository projectRatingJpaRepository;

    private final ProjectLikeJpaRepository projectLikeJpaRepository;

    private final ProjectImageJpaRepository projectImageJpaRepository;

    /**
     * 자주 사용될 것이므로 QueryRepository 에 넘기지 않는다.
     *
     * @param projectId Id
     * @return Optional<Project>
     */
    @Override
    public Optional<Project> findById(Long projectId) {
        return proejctJpaRepository.findById(projectId);
    }

    @Override
    public Project saveProject(Project project) {
        return proejctJpaRepository.save(project);
    }

    @Override
    public void deleteById(Long projectId) {
        proejctJpaRepository.deleteById(projectId);
    }

    @Override
    public boolean existsRatingByMemberAndProject(Long memberId, Long projectId) {
        return projectRatingJpaRepository.existsByMemberIdAndProjectId(memberId, projectId);
    }

    @Override
    public ProjectRating saveProjectRating(ProjectRating projectRating) {
        return projectRatingJpaRepository.save(projectRating);
    }

    @Override
    public boolean likeExistsByMemberIdAndProjectId(Long memberId, Long projectId) {
        return projectLikeJpaRepository.existsByMemberIdAndProjectId(memberId, projectId);
    }

    @Override
    public ProjectLike saveLike(ProjectLike projectLike) {
        return projectLikeJpaRepository.save(projectLike);
    }

    @Override
    public void deleteLikeByMemberIdAndProjectId(Long memberId, Long projectId) {
        projectLikeJpaRepository.deleteLikeByMemberIdAndProjectId(memberId, projectId);
    }

    @Override
    public ProjectImage saveImage(ProjectImage projectImage) {
        return projectImageJpaRepository.save(projectImage);
    }

    @Override
    public ProjectImage findByIdx(int idx , Long projectId) {
        return projectImageJpaRepository.findByIndex(idx , projectId);
    }

    @Override
    public void addViewCount(Long projectId) {
        proejctJpaRepository.addViewCount(projectId);
    }
}
