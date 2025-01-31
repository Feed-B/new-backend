package com.example.team_12_be.project.service;

import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.domain.ProjectPort;
import com.example.team_12_be.project.image.service.ProjectImageUpdateService;
import com.example.team_12_be.project.image.service.ProjectThumbnailUpdateService;
import com.example.team_12_be.project.service.dto.request.ProjectImageDto;
import com.example.team_12_be.project.service.dto.request.ProjectThumbnailDto;
import com.example.team_12_be.project.service.usecase.update.ProjectLinkUpdateUseCase;
import com.example.team_12_be.project.service.usecase.update.ProjectTeammateUpdateUseCase;
import com.example.team_12_be.project.service.usecase.update.ProjectTechStackUpdateUseCase;
import com.example.team_12_be.project.service.usecase.update.dto.request.ProjectUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultProjectUpdateService {

    private final ProjectPort projectPort;

    private final ProjectLinkUpdateUseCase projectLinkUpdateUseCase;

    private final ProjectTeammateUpdateUseCase projectTeammateUpdateUseCase;

    private final ProjectTechStackUpdateUseCase projectTechStackUpdateUseCase;

    private final ProjectImageUpdateService projectImageUpdateService;

    private final ProjectThumbnailUpdateService projectThumbnailUpdateService;

    public void updateProject(Long projectId, Long memberId, ProjectUpdateRequestDto projectUpdateRequestDto, List<ProjectImageDto> imageList, ProjectThumbnailDto projectThumbnailDto) {
        Project project = projectPort.findById(projectId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트이다."));
        if (!project.getAuthor().getId().equals(memberId)) {
            throw new IllegalArgumentException("자신의 게시글만 삭제 할 수 있다.");
        }

        project.updateProject(
                projectUpdateRequestDto.title(),
                projectUpdateRequestDto.introduction(),
                projectUpdateRequestDto.content(),
                projectUpdateRequestDto.serviceUrl(),
                projectUpdateRequestDto.imageType()
        );

        projectLinkUpdateUseCase.updateProjectLinks(projectUpdateRequestDto.projectLinks(), project);
        projectTeammateUpdateUseCase.updateProjectTeammates(projectUpdateRequestDto.projectTeammates(), project);
        projectTechStackUpdateUseCase.updateProjectTechStack(projectUpdateRequestDto.projectTechStacks(), project);
        projectImageUpdateService.updateProjectImages(imageList, project);
        projectThumbnailUpdateService.updateProjectThumbnail(projectThumbnailDto, project);
    }
}
