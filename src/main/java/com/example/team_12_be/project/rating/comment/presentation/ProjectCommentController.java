package com.example.team_12_be.project.rating.comment.presentation;

import com.example.team_12_be.project.rating.comment.service.ProjectCommentService;
import com.example.team_12_be.project.rating.comment.service.dto.CommentUpdateRequestDto;
import com.example.team_12_be.project.rating.comment.service.dto.ProjectCommentRequestDto;
import com.example.team_12_be.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "프로젝트 댓글 C,U,D 컨트롤러")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ProjectCommentController {

    private final ProjectCommentService projectCommentService;

    @PostMapping("/projects/{ratingId}/comments")
    @Operation(description = "별점에 대한 댓글 생성")
    public Long addReply(@PathVariable Long ratingId,
                         @AuthenticationPrincipal CustomUserDetails customUserDetails,
                         @RequestBody ProjectCommentRequestDto projectCommentRequestDto) {
        return projectCommentService.addReply(ratingId, customUserDetails.getMember().getId(), projectCommentRequestDto);
    }

    @PutMapping("/projects/comments/{commentId}")
    @Operation(description = "댓글 수정")
    public void updateReply(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CommentUpdateRequestDto commentUpdateRequestDto) {
        projectCommentService.updateReply(commentId, customUserDetails.getMember().getId(), commentUpdateRequestDto);
    }

    @DeleteMapping("/projects/comments/{commentId}")
    @Operation(description = "댓글 삭제")
    public void deleteReply(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        projectCommentService.deleteComment(commentId, customUserDetails.getMember().getId());
    }
}
