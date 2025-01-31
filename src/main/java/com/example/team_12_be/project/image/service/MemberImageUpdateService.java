package com.example.team_12_be.project.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberImageUpdateService {

    private final AmazonS3 amazonS3;
    private String bucketName = "feedb-bucket";
    private String bucketFolder = "image/";

    @Transactional
    public String upload(MultipartFile image, String currentImageUrl, int idx) {
        //idx 0 - 이미지 없음 , 1 - 기존 이미지 , 2 - 변경 이미지

        if (idx == 0 && Objects.isNull(currentImageUrl)) {
            return null;
        }
        if (idx == 0) { //기존 이미지 있지만 업로드 이미지 x
            // this.deleteImageToS3(currentImageUrl);
            return null;
        }

        if (idx == 2) {
            //if(!Objects.isNull(currentImageUrl)) this.deleteImageToS3(currentImageUrl);
            currentImageUrl = this.uploadImage(image);
        }

        return currentImageUrl;
    }

    private void deleteImageToS3(String currentImageUrl) {
        try {
            String key = extractKeyFromUrl(currentImageUrl);
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            throw new RuntimeException("S3 deleted failed", e);
        }
    }

    private String extractKeyFromUrl(String currentImageUrl) {
        String[] parts = currentImageUrl.split(".com/"); //s3 객체에서 파일명만 추출

        return URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
    }

    private String uploadImage(MultipartFile image) {
        this.validateImageFileExtention(image);
        try {
            return this.uploadImageToS3(image);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred during image upload", e);
        }
    }

    private String uploadImageToS3(MultipartFile image) throws IOException {

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

    private void validateImageFileExtention(MultipartFile image) {
        String filename = image.getOriginalFilename();
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
