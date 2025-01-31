package com.example.team_12_be.project.service;

import com.example.team_12_be.global.page.CustomPageResponse;
import com.example.team_12_be.member.domain.vo.Job;
import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.domain.ProjectLike;
import com.example.team_12_be.project.domain.ProjectQueryRepository;
import com.example.team_12_be.project.domain.ProjectTeammate;
import com.example.team_12_be.project.domain.vo.StarRank;
import com.example.team_12_be.project.presentation.request.SortCondition;
import com.example.team_12_be.project.rating.service.ProjectRatingQueryService;
import com.example.team_12_be.project.service.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectQueryService {

    private final ProjectQueryRepository projectQueryRepository;

    private final ProjectRatingQueryService projectRatingQueryService;

    public Project findById(Long id) {
        return projectQueryRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Project with projectId " + id + " not found")
        );
    }

    public StarRankResponseDto getProjectAverageStarRank(Long projectId) {
        Project project = this.findById(projectId);
        Long count = projectRatingQueryService.countByProjectId(projectId);
        StarRank starRank = project.calculateAverageStarRank();
        return StarRankResponseDto.of(starRank, count);
    }

    public ProjectDetailResponseDto getDetailById(Long projectId, Long memberId) {
        Project project = this.findById(projectId);
        long likeCount = projectQueryRepository.countLikeByProjectId(projectId);
        boolean isMine = isMine(memberId, project);
        boolean isLiked = isLiked(memberId, project);

        return ProjectDetailResponseDto.of(project, likeCount, isMine, isLiked);
    }

    private static boolean isMine(Long memberId, Project project) {
        if (Objects.isNull(memberId)) {
            return false;
        }
        return project.getAuthor().getId().equals(memberId);
    }

    public ProjectDetailForEditResponseDto getDetailForEditByProjectId(Long memberId, Long projectId) {
        Project project = this.findById(projectId);
        if (!Objects.equals(project.getAuthor().getId(), memberId)) {
            throw new IllegalArgumentException("회원만 수정 뷰를 가져올 수 있다.");
        }

        return ProjectDetailForEditResponseDto.of(project);
    }

    public List<JobWithTeammateResponseDto> getTeammateListByProjectId(Long projectId) {
        Project project = this.findById(projectId);
        List<ProjectTeammate> projectTeammates = project.getProjectTeammates();

        // 팀메이트들을 Job 별로 그룹화하고 ProjectTeammateResponseDto로 변환
        Map<Job, List<ProjectTeammateResponseDto>> groupedByJob = projectTeammates.stream()
                .map(teammate -> new ProjectTeammateResponseDto(teammate.getId(), teammate.getTeammateName(), teammate.getJob(), teammate.getUrl()))
                .collect(Collectors.groupingBy(ProjectTeammateResponseDto::job));

        // 그룹화된 결과를 JobWithTeammateResponseDto 형식의 리스트로 변환
        return groupedByJob.entrySet().stream()
                .map(entry -> new JobWithTeammateResponseDto(entry.getKey(), entry.getValue()))
                .toList();
    }


    public List<LikedMembersTechStackResponseDto> getLikedMembersTechStack(Long projectId) {
        List<ProjectLike> projectLikesWithMembers = projectQueryRepository.findLikesByProjectIdWithMember(projectId);
        List<Job> memberTechStacks = projectLikesWithMembers.stream()
                .map(each -> each.getMember().getMemberJob())
                .toList();

        return memberTechStacks.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())) // 내림차순 정렬
                .limit(3) // 상위 3개만 선택
                .map(entry -> new LikedMembersTechStackResponseDto(entry.getKey(), entry.getValue()))
                .toList();
    }

    public CustomPageResponse<ProjectListResponseDto> getProjectList(SortCondition sortCondition, List<String> projectTechStacks, Long memberId, String searchString, Pageable pageable) {
        Page<Project> projects = projectQueryRepository.findProjectsProjectTechStacksOrderBySortCondition(sortCondition, projectTechStacks, searchString, pageable);

        List<ProjectListResponseDto> projectListResponseDtoList = projects.stream()
                .map(project -> getProjectListResponseDto(memberId, project))
                .toList();

        return new CustomPageResponse<>(projectListResponseDtoList, pageable, projects.getTotalElements());
    }

    public CustomPageResponse<ProjectListResponseDto> getMyProjectList(Long memberId, Pageable pageable) {
        Page<Project> projects = projectQueryRepository.findAllByAuthorIdOrderByCreatedAtDesc(memberId, pageable);

        List<ProjectListResponseDto> projectListResponseDtoList = projects.stream()
                .map(project -> getProjectListResponseDto(memberId, project))
                .toList();

        return new CustomPageResponse<>(projectListResponseDtoList, pageable, projects.getTotalElements());
    }

    public CustomPageResponse<ProjectListResponseDto> getLikedProjectList(Long memberId, Pageable pageable) {
        Page<Project> myLikedProjects = projectQueryRepository.findAllMyLikedProjects(memberId, pageable);

        List<ProjectListResponseDto> projectListResponseDtoList = myLikedProjects.stream()
                .map(project -> getProjectListResponseDto(memberId, project))
                .toList();

        return new CustomPageResponse<>(projectListResponseDtoList, pageable, myLikedProjects.getTotalElements());
    }

    private ProjectListResponseDto getProjectListResponseDto(Long memberId, Project project) {
        Long likeCount = projectQueryRepository.countLikeByProjectId(project.getId());
        boolean isLiked = isLiked(memberId, project);
        Long viewCount = project.getViewCount();
        return ProjectListResponseDto.of(project, likeCount, isLiked, viewCount);
    }

    private boolean isLiked(Long memberId, Project project) {
        if (Objects.isNull(memberId)) {
            return false;
        }

        return projectQueryRepository.existsLikeByMemberIdAndProjectId(memberId, project.getId());
    }
}
