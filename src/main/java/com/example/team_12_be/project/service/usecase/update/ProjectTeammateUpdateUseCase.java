package com.example.team_12_be.project.service.usecase.update;

import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.domain.ProjectTeammate;
import com.example.team_12_be.project.service.usecase.update.dto.request.ProjectTeammateUpdateRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ProjectTeammateUpdateUseCase {

    public void updateProjectTeammates(List<ProjectTeammateUpdateRequestDto> projectLinkUpdateRequestDtoList, Project project) {
        List<Long> requestIdList = projectLinkUpdateRequestDtoList.stream()
                .map(ProjectTeammateUpdateRequestDto::id)
                .filter(Objects::nonNull)
                .toList();

        List<ProjectTeammate> projectLinks = project.getProjectTeammates();

        deleteIfAbsent(project, projectLinks, requestIdList);
        updateIfExists(projectLinkUpdateRequestDtoList, projectLinks);
        saveIfNew(projectLinkUpdateRequestDtoList, project);
    }

    private void saveIfNew(List<ProjectTeammateUpdateRequestDto> projectLinkUpdateRequestDtoList, Project project) {
        // 3. 프로젝트 아이디가 없는 녀석은 생성한다.
        projectLinkUpdateRequestDtoList.stream()
                .filter(dto -> dto.id() == null || dto.id() == 0)
                .map(dto -> new ProjectTeammate(dto.name(), dto.job(), dto.url()))
                .forEach(project::addTeammate);
    }

    private void updateIfExists(List<ProjectTeammateUpdateRequestDto> projectLinkUpdateRequestDtoList, List<ProjectTeammate> projectLinks) {
        // 2. 프로젝트 아이디가 있는 녀석은 업데이트한다.
        projectLinkUpdateRequestDtoList.stream()
                .filter(dto -> dto.id() != null)
                .forEach(dto -> {
                    Optional<ProjectTeammate> optionalProjectLink = projectLinks.stream()
                            .filter(link -> link.getId().equals(dto.id()))
                            .findFirst();
                    optionalProjectLink.ifPresent(link -> link.update(dto.name(), dto.job(), dto.url()));
                });
    }

    private void deleteIfAbsent(Project project, List<ProjectTeammate> projectLinks, List<Long> requestIdList) {
        // 1. 프로젝트가 가진 리스트에는 존재하고, DTO 리스트에 없는(아이디 기준) 녀석들은 삭제한다.
        List<ProjectTeammate> toRemove = projectLinks.stream()
                .filter(link -> !requestIdList.contains(link.getId()))
                .toList();

        toRemove.forEach(project::removeTeammate);
    }
}
