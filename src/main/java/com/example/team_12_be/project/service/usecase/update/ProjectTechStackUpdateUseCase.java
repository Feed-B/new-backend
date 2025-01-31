package com.example.team_12_be.project.service.usecase.update;

import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.domain.ProjectTechStack;
import com.example.team_12_be.project.service.usecase.update.dto.request.ProjectTechStackUpdateDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ProjectTechStackUpdateUseCase {

    public void updateProjectTechStack(List<ProjectTechStackUpdateDto> projectLinkUpdateRequestDtoList, Project project) {
        List<Long> requestIdList = projectLinkUpdateRequestDtoList.stream()
                .map(ProjectTechStackUpdateDto::id)
                .filter(Objects::nonNull)
                .toList();

        List<ProjectTechStack> projectLinks = project.getProjectTechStacks();

        deleteIfAbsent(project, projectLinks, requestIdList);
        updateIfExists(projectLinkUpdateRequestDtoList, projectLinks);
        saveIfNew(projectLinkUpdateRequestDtoList, project);
    }

    private void saveIfNew(List<ProjectTechStackUpdateDto> projectLinkUpdateRequestDtoList, Project project) {
        // 3. 프로젝트 아이디가 없는 녀석은 생성한다.
        projectLinkUpdateRequestDtoList.stream()
                .filter(dto -> dto.id() == null || dto.id() == 0)
                .map(dto -> new ProjectTechStack(dto.techStack()))
                .forEach(project::addTechStack);
    }

    private void updateIfExists(List<ProjectTechStackUpdateDto> projectLinkUpdateRequestDtoList, List<ProjectTechStack> projectLinks) {
        // 2. 프로젝트 아이디가 있는 녀석은 업데이트한다.
        projectLinkUpdateRequestDtoList.stream()
                .filter(dto -> dto.id() != null)
                .forEach(dto -> {
                    Optional<ProjectTechStack> optionalProjectLink = projectLinks.stream()
                            .filter(each -> each.getId().equals(dto.id()))
                            .findFirst();

                    optionalProjectLink.ifPresent(each -> each.update(dto.techStack()));
                });
    }

    private void deleteIfAbsent(Project project, List<ProjectTechStack> projectLinks, List<Long> requestIdList) {
        // 1. 프로젝트가 가진 리스트에는 존재하고, DTO 리스트에 없는(아이디 기준) 녀석들은 삭제한다.
        List<ProjectTechStack> toRemove = projectLinks.stream()
                .filter(link -> !requestIdList.contains(link.getId()))
                .toList();

        toRemove.forEach(project::removeTechStack);
    }
}
