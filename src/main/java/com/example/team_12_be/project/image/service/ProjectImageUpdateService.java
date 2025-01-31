package com.example.team_12_be.project.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.domain.ProjectImage;
import com.example.team_12_be.project.domain.ProjectPort;
import com.example.team_12_be.project.service.dto.request.ProjectImageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectImageUpdateService {

    private final AmazonS3 amazonS3;
    private final ProjectPort projectPort;
    private String bucketName = "feedb-bucket";
    private String bucketFolder = "image/";


    public void updateProjectImages(List<ProjectImageDto> projectImageDtoList, Project currentProject) {
        if (Objects.isNull(projectImageDtoList) || projectImageDtoList.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty or null");
        }

        currentProject.replaceProjectImages(updateImage(projectImageDtoList, currentProject));
    }

    private List<ProjectImage> updateImage(List<ProjectImageDto> projectImageDtoList, Project currentProject) {
        this.validateImageFileExtention(projectImageDtoList);
        try {
            return this.orderImagesByIndex(projectImageDtoList, currentProject);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred during image upload", e);
        }
    }

    private List<ProjectImage> orderImagesByIndex(List<ProjectImageDto> projectImageDtoList, Project currentProject) throws IOException {
        //index - 기본 - db 인덱스 , 변경 - 0
        List<ProjectImage> projectImageList = new ArrayList<>();
        List<ProjectImage> removeProjectImgList = currentProject.getProjectImages();
        List<Integer> toRemove = new ArrayList<>();
        int newImageIdx = 0;

        for (ProjectImageDto projectImageDto : projectImageDtoList) { //변경 후 넘어온 이미지 순서대로 loop
            int originImageIdx = projectImageDto.index();
            newImageIdx++; // loop 마다 +1
            ProjectImage projectImage = new ProjectImage();
            if (originImageIdx == 0) { //새로운 이미지 업로드
                String url = this.uploadImageToS3(projectImageDto);
                projectImage = new ProjectImage(url, newImageIdx);
            } else { //순서만 변경 or 변경 x
                String url = projectPort.findByIdx(originImageIdx,currentProject.getId()).getUrl();
                projectImage = new ProjectImage(url, newImageIdx);
                removeProjectImgList.removeIf(image -> image.getIndex() == originImageIdx); // 재사용 이미지 인덱스 제거 배열에서 제외
            }
            projectImageList.add(projectImage);
        }

        //deleteImageToS3(removeProjectImgList); //사용 안 하는 이미지 S3 삭제

        return projectImageList;
    }

    private String uploadImageToS3(ProjectImageDto projectImageDto) throws IOException {

        MultipartFile image = projectImageDto.image();
        String originalFilename = image.getOriginalFilename(); // 원본 파일
        String extention = originalFilename.substring(originalFilename.lastIndexOf(".")); //파일 확장자
        String dateFolder = new SimpleDateFormat("yyyy.MM").format(new Date()) + "/";

        String s3FileName = bucketFolder + dateFolder + UUID.randomUUID().toString().substring(0, 10) + "_" + originalFilename;

        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extention);
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try {
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest); // put image to S3
        } catch (Exception e) {
            throw new IOException("S3 upload failed", e);
        } finally {
            byteArrayInputStream.close();
            is.close();
        }

        String url = amazonS3.getUrl(bucketName, s3FileName).toString();

        return url;
    }

    private void deleteImageToS3(List<ProjectImage> removeProjectImgList) throws IOException {
        try {

            List<String> keys = removeProjectImgList.stream()
                    .map(ProjectImage::getUrl)
                    .map(this::extractKeyFromUrl)
                    .toList();

            DeleteObjectsRequest delObjReq = new DeleteObjectsRequest(bucketName)
                    .withKeys(keys.toArray(new String[0]));

            DeleteObjectsResult delObjRes = amazonS3.deleteObjects(delObjReq);
//            delObjRes.getDeletedObjects().forEach(deletedObject ->
//                    log.info("Deleted: " + deletedObject.getKey()));
        } catch (Exception e) {
            throw new IOException("S3 deleted failed", e);
        }
    }

    private String extractKeyFromUrl(String url) {
        String[] parts = url.split(".com/"); //s3 객체에서 파일명만 추출

        return URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
    }

    //파일 형태 검증 메소드
    private void validateImageFileExtention(List<ProjectImageDto> projectImageDtoList) {

        for (ProjectImageDto projectImageRequestDto : projectImageDtoList) {
            if (projectImageRequestDto.image() == null) continue;
            String filename = projectImageRequestDto.image().getOriginalFilename();
            int lastDotIndex = filename.lastIndexOf(".");
            if (lastDotIndex == -1) {
                throw new IllegalArgumentException("File extension is missing"); //TODO 에러 응답 형태 테스트!
            }

            String extention = filename.substring(lastDotIndex + 1).toLowerCase();
            List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

            if (!allowedExtentionList.contains(extention)) {
                throw new IllegalArgumentException("Invalid file extention: " + extention);
            }
        }
    }
}

