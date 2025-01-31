package com.example.team_12_be.project.service.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record ProjectThumbnailDto(MultipartFile image, int index) {
}
