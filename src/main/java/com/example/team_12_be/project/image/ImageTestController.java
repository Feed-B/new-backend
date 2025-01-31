//package com.example.team_12_be.project.image;
//
//import com.example.team_12_be.project.domain.ProjectImage;
//import com.example.team_12_be.project.image.service.ProjectImageService;
//import com.example.team_12_be.project.image.service.ProjectImageUpdateService;
//import com.example.team_12_be.project.image.service.ProjectThumbnailService;
//import com.example.team_12_be.project.image.service.ProjectThumbnailUpdateService;
//import com.example.team_12_be.project.service.dto.request.ProjectImageDto;
//import com.example.team_12_be.project.service.dto.request.ProjectRequestDto;
//import com.example.team_12_be.project.service.dto.request.ProjectThumbnailDto;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//import java.util.stream.IntStream;
//@SecurityRequirement(name = "Bearer Authentication")
//@Tag(name = "이미지 업로드 테스트")
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//public class ImageTestController {
//
//    private final ProjectImageService projectImageService;
//    private final ProjectImageUpdateService projectImageUpdateService;
//    private final ProjectThumbnailUpdateService projectThumbnailUpdateService;
//    @PostMapping(value = "/s3/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public String create(@Valid @RequestPart ProjectRequestDto projectRequestDto,
//                           @RequestPart List<MultipartFile> multipartFiles,
//                           @RequestPart List<Integer> indexes) {
//          //file 형태를 데이터와 포함해서 요청하기엔 객체 바인딩 이슈로 개별로 받아서 record 생성
//        List<ProjectImageDto> projectImageRequestDtos = IntStream.range(0, multipartFiles.size())
//                .mapToObj(i -> new ProjectImageDto(multipartFiles.get(i),indexes.get(i)))
//                .toList();
//
//        projectImageService.upload(projectImageRequestDtos);
//        return "업로드 테스트";
//    }
//
//    @PostMapping(value = "/s3/update" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public List<String> update(@Valid @RequestPart ProjectRequestDto projectRequestDto,
//                         @RequestPart(required = false) List<MultipartFile> images,
//                         @RequestPart List<Integer> imageIndexes,
//                         @RequestPart(required = false) MultipartFile thumbnail,
//                         @RequestPart Integer thumbnailIndex)
//    {
//        List<ProjectImageDto> projectImageDtoList = IntStream.range(0, images.size())
//                .mapToObj(i -> new ProjectImageDto(images.get(i),imageIndexes.get(i)))
//                .toList();
//
//        ProjectThumbnailDto projectThumbnailDto = new ProjectThumbnailDto(thumbnail ,thumbnailIndex);
//      //  String thumbnailUrl = projectThumbnailUpdateService.updateProjectThumbnail(projectThumbnailDto);// 프로젝트 서비스 단에서 프로젝트으 ㅣ현재 url을 두번쨰 인자로
//    return null;
////        return projectImageUpdateService.update(projectImageDtoList , 1L).stream().map(ProjectImage::getUrl).toList();
//    }
//}
