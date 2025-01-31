package com.example.team_12_be.project.rating.presentation;

import com.example.team_12_be.global.page.CustomPageResponse;
import com.example.team_12_be.global.page.CustomPageable;
import com.example.team_12_be.global.presentation.CustomPageRequest;
import com.example.team_12_be.project.rating.service.ProjectRatingQueryService;
import com.example.team_12_be.project.rating.service.dto.response.MyProjectRatingResponseDto;
import com.example.team_12_be.project.rating.service.dto.response.ProjectRatingResponseDto;
import com.example.team_12_be.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Tag(name = "프로젝트 별점 조회 컨트롤러")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequiredArgsConstructor
public class ProjectRatingQueryController {

    private final ProjectRatingQueryService projectRatingQueryService;

    @GetMapping("/projects/{projectId}/ratings")
    @Operation(description = "프로젝트에 남긴 별점 목록 조회")
    public CustomPageResponse<ProjectRatingResponseDto> getMembersRatings(@PathVariable Long projectId,
                                                                          @ModelAttribute CustomPageRequest customPageRequest) {
        Pageable pageable = PageRequest.of(customPageRequest.page(), customPageRequest.size());
        return projectRatingQueryService.getProjectRatingsByProjectId(projectId, pageable);
    }

    @GetMapping("/projects/ratings/{ratingId}")
    @Operation(description = "프로젝트에 남긴 별점 상세 조회")
    public ProjectRatingResponseDto getRatingDetail(@PathVariable Long ratingId){
        return projectRatingQueryService.getProjectRatingDetail(ratingId);
    }

    @GetMapping("/projects/{projectId}/ratings/mine")
    @Operation(description = "프로젝트에 달린 나의 별점 조회 - (exists 로 존재여부 판별)")
    public MyProjectRatingResponseDto getMyComment(@PathVariable Long projectId,
                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return projectRatingQueryService.getMyProjectRating(projectId, customUserDetails.getMember().getId());
    }
}
