package com.example.team_12_be.project.presentation;

import com.example.team_12_be.global.page.CustomPageResponse;
import com.example.team_12_be.global.presentation.CustomPageRequest;
import com.example.team_12_be.project.presentation.request.SortCondition;
import com.example.team_12_be.project.service.ProjectQueryService;
import com.example.team_12_be.project.service.dto.response.*;
import com.example.team_12_be.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Tag(name = "프로젝트 GET(조회) 컨트롤러")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequiredArgsConstructor
public class ProjectQueryController {

    private final ProjectQueryService projectQueryService;

    @GetMapping("/projects/{projectId}")
    @Operation(description = "프로젝트 상세 조회")
    public ProjectDetailResponseDto getProjectDetail(@PathVariable Long projectId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long memberId = null;

        if (!Objects.isNull(customUserDetails)) {
            memberId = customUserDetails.getMember().getId();
        }

        return projectQueryService.getDetailById(projectId, memberId);
    }

    @GetMapping("/projects/{projectId}/edits")
    @Operation(description = "프로젝트에 수정 페이지를 위한 데이터 조회")
    public ProjectDetailForEditResponseDto getDetailForEditByProjectId(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long projectId) {
        Long memberId = userDetails.getMember().getId();
        return projectQueryService.getDetailForEditByProjectId(memberId, projectId);
    }

    @GetMapping("/projects/{projectId}/teammates")
    @Operation(description = "프로젝트 팀원 목록 조회")
    public List<JobWithTeammateResponseDto> getTeammateListByProjectId(@PathVariable Long projectId) {
        return projectQueryService.getTeammateListByProjectId(projectId);
    }

    @GetMapping("/projects/{projectId}/average-rating")
    @Operation(description = "프로젝트 별점 항목들 각각의 평균 조회")
    public StarRankResponseDto getProjectAverageStarRank(@PathVariable Long projectId) {
        return projectQueryService.getProjectAverageStarRank(projectId);
    }

    @GetMapping("/projects/{projectId}/likes")
    @Operation(description = "프로젝트에 좋아요 누른 횟수가 제일 많은 3개의 {job + 좋아요} 조회")
    public List<LikedMembersTechStackResponseDto> likedMembersTechStackList(@PathVariable Long projectId) {
        return projectQueryService.getLikedMembersTechStack(projectId);
    }

    @GetMapping("/projects")
    @Operation(description = "프로젝트 리스트 조회")
    public CustomPageResponse<ProjectListResponseDto> getProjectList(
            @RequestParam(name = "sortCondition") SortCondition sortCondition,
            @RequestParam(required = false) List<String> projectTechStacks,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false) String searchString,
            @ModelAttribute CustomPageRequest customPageRequest
    ) {
        Long memberId = null;

        if (!Objects.isNull(customUserDetails)) {
            memberId = customUserDetails.getMember().getId();
        }

        Pageable pageable = PageRequest.of(customPageRequest.page(), customPageRequest.size());

        return projectQueryService.getProjectList(sortCondition, projectTechStacks, memberId, searchString, pageable);
    }

    @GetMapping("/{memberId}/projects")
    @Operation(description = "내 프로젝트 리스트 조회(최신순)")
    public CustomPageResponse<ProjectListResponseDto> getMyProjectList(
            @PathVariable Long memberId,
            @ModelAttribute CustomPageRequest customPageRequest
    ) {
        Pageable pageable = PageRequest.of(customPageRequest.page(), customPageRequest.size());

        return projectQueryService.getMyProjectList(memberId, pageable);
    }

    @GetMapping("/{memberId}/projects/likes")
    @Operation(description = "좋아요 한 프로젝트 리스트 조회(최신순)")
    public CustomPageResponse<ProjectListResponseDto> getMyLikedProjectList(
            @PathVariable Long memberId,
            @ModelAttribute CustomPageRequest customPageRequest
    ) {
        Pageable pageable = PageRequest.of(customPageRequest.page(), customPageRequest.size());

        return projectQueryService.getLikedProjectList(memberId, pageable);
    }
}
