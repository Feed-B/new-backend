package com.example.team_12_be.project.service;

import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.member.service.dto.MemberService;
import com.example.team_12_be.project.domain.*;
import com.example.team_12_be.project.image.service.ProjectImageService;
import com.example.team_12_be.project.image.service.ProjectThumbnailService;
import com.example.team_12_be.project.rating.service.ProjectRatingService;
import com.example.team_12_be.project.service.dto.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectPort projectPort;

    private final MemberService memberService;

    private final ProjectImageService projectImageService;

    private final ProjectThumbnailService projectThumbnailService;

    private final ProjectRatingService projectRatingService;

    public Long saveProject(ProjectRequestDto projectRequestDto, Member author, List<ProjectImageDto> projectImageDtoList, ProjectThumbnailDto projectThumbnailDto) {
        Project project = projectRequestDto.toEntity(author);
        projectPort.saveProject(project);

        List<ProjectTechStack> techStacks = projectRequestDto.projectTechStacks().stream().map(ProjectTechStack::new).toList();
        techStacks.forEach(project::addTechStack);

        List<ProjectTeammate> teammates = projectRequestDto.projectTeammates().stream().map(ProjectTeammateRequestDto::toEntity).toList();
        teammates.forEach(project::addTeammate);

        List<ProjectLink> links = projectRequestDto.projectLinks().stream().map(ProjectLinkRequestDto::toEntity).toList();
        links.forEach(project::addLink);

        List<ProjectImage> images = projectImageService.upload(projectImageDtoList);
        images.forEach(project::addProjectImage);

        String thumbnailUrl = projectThumbnailService.upload(projectThumbnailDto);
        project.addThumbnailUrl(thumbnailUrl);

        return project.getId();
    }

    public void deleteProject(Long projectId) {
        Project project = projectPort.findById(projectId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 프로젝트")
        );

        project.getProjectRatings().forEach(projectRatingService::deleteRating);
        projectPort.deleteById(projectId);
    }

    public void likeProject(Long memberId, Long projectId) {
        boolean isLikeExists = projectPort.likeExistsByMemberIdAndProjectId(memberId, projectId);

        if (isLikeExists) {
            throw new IllegalArgumentException("좋아요가 이미 존재한다");
        }

        Member member = memberService.findById(memberId);
        Project project = projectPort.findById(projectId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트이다."));

        ProjectLike projectLike = new ProjectLike(member, project);
        project.addProjectLike(projectLike);
    }

    public void unlikeProject(Long memberId, Long projectId) {
        boolean isLikeExists = projectPort.likeExistsByMemberIdAndProjectId(memberId, projectId);

        if (!isLikeExists) {
            throw new IllegalArgumentException("좋아요가 존재하지 않는다");
        }

        projectPort.deleteLikeByMemberIdAndProjectId(memberId, projectId);
    }

    public void addViewCount(Long projectId) {
        projectPort.addViewCount(projectId);
    }
}
