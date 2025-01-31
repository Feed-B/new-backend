package com.example.team_12_be.project.rating.comment.service;

import com.example.team_12_be.global.page.CustomPageResponse;
import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.project.rating.comment.domain.ProjectCommentRepository;
import com.example.team_12_be.project.rating.comment.domain.RatingReply;
import com.example.team_12_be.project.rating.comment.service.dto.ProjectCommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProjectCommentQueryService {

    private final ProjectCommentRepository projectCommentRepository;

    public CustomPageResponse<ProjectCommentResponseDto> findProjectCommentsByProjectId(Long ratingId, Pageable pageable) {
        Slice<RatingReply> projectCommentsPage = projectCommentRepository.findAllByProjectIdWithMember(ratingId, pageable);
        List<ProjectCommentResponseDto> projectCommentResponses = projectCommentsPage.stream()
                .map(this::getProjectCommentResponseDto)
                .toList();

        return new CustomPageResponse<>(projectCommentResponses, pageable, projectCommentsPage.getNumberOfElements());
    }

    public ProjectCommentResponseDto getProjectCommentResponseDto(Long ratingId, Long commentId) {
        RatingReply ratingReply = projectCommentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글")
        );

        if (!Objects.equals(ratingReply.getProjectRating().getId(), ratingId)){
            throw new IllegalArgumentException("해당 별점에 대한 댓글이 아님");
        }

        return getProjectCommentResponseDto(ratingReply);
    }

    private ProjectCommentResponseDto getProjectCommentResponseDto(RatingReply ratingReply) {
        Member commentAuthor = ratingReply.getMember();

        return ProjectCommentResponseDto.of(ratingReply, commentAuthor);
    }

    public long countCommentByRatingId(Long ratingId){
        return projectCommentRepository.countByProjectRatingId(ratingId);
    }
}