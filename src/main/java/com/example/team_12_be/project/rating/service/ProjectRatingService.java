package com.example.team_12_be.project.rating.service;

import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.member.service.dto.MemberService;
import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.domain.ProjectRating;
import com.example.team_12_be.project.domain.vo.StarRank;
import com.example.team_12_be.project.rating.comment.service.ProjectCommentService;
import com.example.team_12_be.project.rating.repository.ProjectRatingJpaRepository;
import com.example.team_12_be.project.rating.service.dto.request.ProjectRatingRequestDto;
import com.example.team_12_be.project.service.ProjectQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectRatingService {

    private final ProjectRatingJpaRepository projectRatingJpaRepository;

    private final ProjectCommentService projectCommentService;

    private final ProjectQueryService projectQueryService;

    private final MemberService memberService;

    private ProjectRating getProjectRating(Long projectRatingId) {
        return projectRatingJpaRepository.findById(projectRatingId).orElseThrow(
                () -> new IllegalArgumentException("없는 레이팅")
        );
    }

    public Long addRating(Long projectId, Long memberId, ProjectRatingRequestDto projectRatingRequestDto) {
        Project project = projectQueryService.findById(projectId);
        Member member = memberService.findById(memberId);
        ProjectRating projectRating = projectRatingRequestDto.toEntity(member, project);
        project.addProjectRating(projectRating);
        return projectRating.getId();
    }

    public void modifyRating(Long authorId, Long ratingId, ProjectRatingRequestDto projectRatingRequestDto) {
        ProjectRating projectRating = getProjectRating(ratingId);
        if (!projectRating.getMember().getId().equals(authorId)) {
            throw new IllegalArgumentException("내 레이팅만 변경 가능");
        }
        StarRank starRank = projectRatingRequestDto.toStarRank();
        projectRating.updateRank(starRank);
        projectRating.updateComment(projectRatingRequestDto.comment());
    }

    public void deleteRating(Long authorId, Long ratingId) {
        ProjectRating projectRating = getProjectRating(ratingId);
        if (!projectRating.getMember().getId().equals(authorId)) {
            throw new IllegalArgumentException("내 레이팅만 삭제 가능");
        }

        projectCommentService.deleteByRatingId(projectRating.getId());
        projectRatingJpaRepository.delete(projectRating);
    }

    public void deleteRating(ProjectRating projectRating){
        projectCommentService.deleteByRatingId(projectRating.getId());
        projectRatingJpaRepository.delete(projectRating);
    }
}
