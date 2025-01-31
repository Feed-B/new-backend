package com.example.team_12_be.project.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.service.dto.request.ProjectThumbnailDto;
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
public class ProjectThumbnailUpdateService {

    private final AmazonS3 amazonS3;
    private String bucketName = "feedb-bucket";
    private String bucketFolder = "image/";

    @Transactional
    public void updateProjectThumbnail(ProjectThumbnailDto projectThumbnailDto, Project project) {
        String currentUrl = project.getThumbnailUrl();

        if (Objects.isNull(projectThumbnailDto)) {
            throw new IllegalArgumentException("File must not be empty or null");
        }
        //idx - 변경 - 0  / 기본이미지 - 1
        if (projectThumbnailDto.index() == 0) {
            currentUrl = updateImage(projectThumbnailDto);
        }

        project.addThumbnailUrl(currentUrl);
    }

    private String updateImage(ProjectThumbnailDto projectThumbnailDto) {
        this.validateImageFileExtention(projectThumbnailDto);
        try {
            return this.uploadImageToS3(projectThumbnailDto);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred during image upload", e);
        }
    }

    //S3에 이미지 저장
    private String uploadImageToS3(ProjectThumbnailDto projectThumbnailDto) throws IOException {

        MultipartFile image = projectThumbnailDto.image();
        String originalFilename = image.getOriginalFilename(); // 원본 파일
        String extention = originalFilename.substring(originalFilename.lastIndexOf(".")); //파일 확장자
        String dateFolder = new SimpleDateFormat("yyyy.MM").format(new Date()) + "/";

        String s3FileName = bucketFolder + dateFolder + UUID.randomUUID().toString().substring(0, 10) + "_" + originalFilename; // s3 저장 파일명 ex) 랜덤값_원본파일명

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

        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    //파일 형태 검증 메소드
    private void validateImageFileExtention(ProjectThumbnailDto projectThumbnailDto) {

        String filename = projectThumbnailDto.image().getOriginalFilename();
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
