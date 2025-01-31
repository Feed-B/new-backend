package com.example.team_12_be.project.rating.service;

import com.example.team_12_be.global.page.CustomPageResponse;
import com.example.team_12_be.project.domain.ProjectRating;
import com.example.team_12_be.project.rating.comment.service.ProjectCommentQueryService;
import com.example.team_12_be.project.rating.repository.ProjectRatingJpaRepository;
import com.example.team_12_be.project.rating.service.dto.response.MyProjectRatingResponseDto;
import com.example.team_12_be.project.rating.service.dto.response.ProjectRatingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectRatingQueryService {

    private final ProjectRatingJpaRepository projectQueryRepository;

    private final ProjectCommentQueryService projectCommentQueryService;

    public ProjectRating findRatingById(Long ratingId) {
        return projectQueryRepository.findById(ratingId).orElseThrow(
                () -> new NoSuchElementException("Project with projectId " + ratingId + " not found")
        );
    }

    public CustomPageResponse<ProjectRatingResponseDto> getProjectRatingsByProjectId(Long projectId, Pageable pageable){
        Page<ProjectRating> projectRatings = projectQueryRepository.findAllByProjectIdOrderByCreatedAtDesc(projectId, pageable);
        List<ProjectRatingResponseDto> projectRatingResponseDtos = projectRatings
                .stream()
                .map(each -> ProjectRatingResponseDto.of(each, projectCommentQueryService.countCommentByRatingId(each.getId())))
                .toList();
        return new CustomPageResponse<>(projectRatingResponseDtos, pageable, projectRatings.getTotalElements());
    }

    public ProjectRatingResponseDto getProjectRatingDetail(Long ratingId){
        ProjectRating projectRating = findRatingById(ratingId);
        long commentCount = projectCommentQueryService.countCommentByRatingId(projectRating.getId());

        return ProjectRatingResponseDto.of(projectRating, commentCount);
    }

    public MyProjectRatingResponseDto getMyProjectRating(Long projectId, Long memberId) {
        Optional<ProjectRating> projectCommentOpt = projectQueryRepository.findByMemberIdAndProjectId(memberId, projectId);

        if (projectCommentOpt.isEmpty()) {
            return MyProjectRatingResponseDto.ofNull();
        }

        ProjectRating projectRating = projectCommentOpt.get();
        long commentCount = projectCommentQueryService.countCommentByRatingId(projectRating.getId());
        return MyProjectRatingResponseDto.of(projectRating, commentCount);
    }

    public Long countByProjectId(Long projectId) {
        return projectQueryRepository.countByProjectId(projectId);
    }
}
