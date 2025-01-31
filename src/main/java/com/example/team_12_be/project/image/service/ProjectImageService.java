package com.example.team_12_be.project.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.example.team_12_be.project.domain.ProjectImage;
import com.example.team_12_be.project.domain.ProjectPort;
import com.example.team_12_be.project.service.dto.request.ProjectImageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectImageService { //TODO 단일책임의 원칙을 최대한 적용해보자.

    private final AmazonS3 amazonS3;

    private String bucketName = "feedb-bucket";
    private String bucketFolder = "image/";

    private final ProjectPort projectPort;

    @Transactional
    public List<ProjectImage> upload(List<ProjectImageDto> projectImageDtoList) {
        if (Objects.isNull(projectImageDtoList) || projectImageDtoList.isEmpty() || Objects.isNull(projectImageDtoList.getFirst().image().getOriginalFilename())) {
            throw new IllegalArgumentException("File must not be empty or null");
        }
        return this.uploadImage(projectImageDtoList);
    }

    private List<ProjectImage> uploadImage(List<ProjectImageDto> projectImageDtoList) {
        this.validateImageFileExtention(projectImageDtoList);
        try {
            return this.uploadImageToS3(projectImageDtoList);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred during image upload", e);
        }
    }

    //S3에 이미지 저장
    private List<ProjectImage> uploadImageToS3(List<ProjectImageDto> projectImageDtoList) throws IOException {
        List<ProjectImage> projectImageList = new ArrayList<>();
        String dateFolder = new SimpleDateFormat("yyyy.MM").format(new Date()) + "/";

        for (ProjectImageDto projectImageDto : projectImageDtoList) {
            MultipartFile image = projectImageDto.image();
            String originalFilename = image.getOriginalFilename(); // 원본 파일
            String extention = originalFilename.substring(originalFilename.lastIndexOf(".")); //파일 확장자

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

            ProjectImage projectImage = new ProjectImage(url, projectImageDto.index());
            projectImageList.add(projectImage);
        }

        //projectImageList.stream().forEach(value -> log.info("index , url = " + value.getIndex() + " , " + value.getUrl()));
        return projectImageList;
    }

    //파일 형태 검증 메소드
    private void validateImageFileExtention(List<ProjectImageDto> projectImageDtoList) {

        for (ProjectImageDto projectImageRequestDto : projectImageDtoList) {
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
