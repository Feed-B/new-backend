package com.example.team_12_be.project.presentation;

import com.example.team_12_be.project.domain.ProjectImage;
import com.example.team_12_be.project.service.DefaultProjectUpdateService;
import com.example.team_12_be.project.service.ProjectService;
import com.example.team_12_be.project.service.dto.request.ProjectImageDto;
import com.example.team_12_be.project.service.dto.request.ProjectRequestDto;
import com.example.team_12_be.project.service.dto.request.ProjectThumbnailDto;
import com.example.team_12_be.project.service.usecase.update.dto.request.ProjectUpdateRequestDto;
import com.example.team_12_be.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Tag(name = "프로젝트 기능(C,U,D) 컨트롤러")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    private final DefaultProjectUpdateService projectUpdateService;

    @PostMapping("/{projectId}/views")
    @Operation(description = "프로젝트 조회수 증가")
    public void addViewCount(@PathVariable Long projectId) {
        projectService.addViewCount(projectId);
    }

    @PostMapping(value = "/projects", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "프로젝트를 생성")
    public ResponseEntity<Void> saveProject(@RequestPart ProjectRequestDto projectRequestDto,
                                            @RequestPart List<MultipartFile> images,
                                            @RequestPart List<Integer> imageIndexes,
                                            @RequestPart MultipartFile thumbnail,
                                            @RequestPart Integer thumbnailIndex,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        //file 형태를 데이터와 포함해서 요청하기엔 객체 바인딩 이슈로 개별로 받아서 record 생성
        List<ProjectImageDto> projectImageDtoList = IntStream.range(0, images.size())
                .mapToObj(i -> new ProjectImageDto(images.get(i), imageIndexes.get(i)))
                .toList();

        ProjectThumbnailDto projectThumbnailDto = new ProjectThumbnailDto(thumbnail, thumbnailIndex);

        Long projectId = projectService.saveProject(projectRequestDto, customUserDetails.getMember(), projectImageDtoList, projectThumbnailDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(projectId)
                .toUri();

        return ResponseEntity.created(location).build();

    }

    @DeleteMapping("/projects/{projectId}")
    @Operation(description = "프로젝트를 삭제")
    public void deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
    }

    @PostMapping("/projects/{projectId}/like")
    @Operation(description = "프로젝트에 대한 사용자의 좋아요 생성")
    public void likeProject(@PathVariable Long projectId,
                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long memberId = customUserDetails.getMember().getId();
        projectService.likeProject(memberId, projectId);
    }

    @DeleteMapping("/projects/{projectId}/unlike")
    @Operation(description = "프로젝트에 대한 사용자의 좋아요 삭제")
    public void unlikeProject(@PathVariable Long projectId,
                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long memberId = customUserDetails.getMember().getId();
        projectService.unlikeProject(memberId, projectId);
    }

    @PutMapping(value = "/projects/{projectId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProject(
            @PathVariable Long projectId,
            @RequestPart ProjectUpdateRequestDto projectRequestDto,
            @RequestPart(required = false) List<MultipartFile> images,
            @RequestPart List<Integer> imageIndexes,
            @RequestPart(required = false) MultipartFile thumbnail,
            @RequestPart Integer thumbnailIndex,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        //Todo log 관련 코드(주석처리)들 추후에 지우기!!
        log.info("---------------------------------------PUT 로그 확인 ----------------------------------------------");
//        images.stream().forEach(i -> log.info("images = " + i));
////        imageIndexes.stream().forEach(i -> log.info("imageIndexes = " + i));

//        List<ProjectImageDto> projectImageList = IntStream.range(0, imageIndexes.size())
//                .mapToObj(idx -> new ProjectImageDto(images != null && idx < images.size() && imageIndexes.get(idx) == 0 ? images.get(idx) : null, imageIndexes.get(idx)))
//                .toList();

        List<ProjectImageDto> projectImageList = new ArrayList<ProjectImageDto>();
        for(int i = 0; i < imageIndexes.size(); i++) {
            MultipartFile file;

            if(imageIndexes.get(i) == 0) {
                    file = images.getFirst();
                    images.removeFirst();
            }else {
                    file = null;
            }

            ProjectImageDto projectImageDto = new ProjectImageDto(file , imageIndexes.get(i));
            projectImageList.add(projectImageDto);
        }

        //projectImageList.stream().forEach(i ->log.info("imageList = " + i.image() + ", " + i.index()));

        ProjectThumbnailDto projectThumbnailDto = new ProjectThumbnailDto(thumbnail, thumbnailIndex);

        Long memberId = customUserDetails.getMember().getId();
        projectUpdateService.updateProject(projectId, memberId, projectRequestDto, projectImageList, projectThumbnailDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(projectId)
                .toUri();
        return ResponseEntity.created(location).build();
    }
}