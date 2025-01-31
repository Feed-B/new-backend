package com.example.team_12_be.project.rating.comment.presentation;

import com.example.team_12_be.global.page.CustomPageResponse;
import com.example.team_12_be.global.presentation.CustomPageRequest;
import com.example.team_12_be.project.rating.comment.service.ProjectCommentQueryService;
import com.example.team_12_be.project.rating.comment.service.dto.ProjectCommentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "프로젝트 댓글 GET(조회) 컨트롤러")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ProjectCommentQueryController {

    private final ProjectCommentQueryService projectCommentQueryService;

    @GetMapping("/projects/{ratingId}/comments")
    @Operation(description = "별점에 달린 댓글 리스트 조회")
    public CustomPageResponse<ProjectCommentResponseDto> getProjectComments(@PathVariable Long ratingId,
                                                                            @ModelAttribute CustomPageRequest customPageRequest) {
        Pageable pageable = PageRequest.of(customPageRequest.page(), customPageRequest.size());

        return projectCommentQueryService.findProjectCommentsByProjectId(ratingId, pageable);
    }
}