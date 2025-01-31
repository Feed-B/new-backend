package com.example.team_12_be.project.service.usecase.update;

import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.domain.ProjectLink;
import com.example.team_12_be.project.service.usecase.update.dto.request.ProjectLinkUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectLinkUpdateUseCase {

    public void updateProjectLinks(List<ProjectLinkUpdateRequestDto> projectLinkUpdateRequestDtoList, Project project) {
        List<Long> requestIdList = projectLinkUpdateRequestDtoList.stream()
                .map(ProjectLinkUpdateRequestDto::id)
                .filter(Objects::nonNull)
                .toList();

        List<ProjectLink> projectLinks = project.getProjectLinks();

        deleteIfAbsent(project, projectLinks, requestIdList);
        updateLinksIfExists(projectLinkUpdateRequestDtoList, projectLinks);
        saveLinksIfNew(projectLinkUpdateRequestDtoList, project);
    }

    private void saveLinksIfNew(List<ProjectLinkUpdateRequestDto> projectLinkUpdateRequestDtoList, Project project) {
        // 3. 프로젝트 아이디가 없는 녀석은 생성한다.
        projectLinkUpdateRequestDtoList.stream()
                .filter(dto -> dto.id() == null || dto.id() == 0)
                .map(dto -> new ProjectLink(dto.siteType(), dto.url()))
                .forEach(project::addLink);
    }

    private void updateLinksIfExists(List<ProjectLinkUpdateRequestDto> projectLinkUpdateRequestDtoList, List<ProjectLink> projectLinks) {
        // 2. 프로젝트 아이디가 있는 녀석은 업데이트한다.
        projectLinkUpdateRequestDtoList.stream()
                .filter(dto -> dto.id() != null)
                .forEach(dto -> {
                    Optional<ProjectLink> optionalProjectLink = projectLinks.stream()
                            .filter(link -> link.getId().equals(dto.id()))
                            .findFirst();
                    optionalProjectLink.ifPresent(link -> link.update(dto.siteType(), dto.url()));
                });
    }

    private void deleteIfAbsent(Project project, List<ProjectLink> projectLinks, List<Long> requestIdList) {
        // 1. 프로젝트가 가진 리스트에는 존재하고, DTO 리스트에 없는(아이디 기준) 녀석들은 삭제한다.
        List<ProjectLink> toRemove = projectLinks.stream()
                .filter(link -> !requestIdList.contains(link.getId()))
                .toList();

        toRemove.forEach(project::removeLink);
    }
}
