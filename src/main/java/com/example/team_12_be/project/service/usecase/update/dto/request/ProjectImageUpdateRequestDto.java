package com.example.team_12_be.project.service.usecase.update.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record ProjectImageUpdateRequestDto(Long id, MultipartFile image, int index) {
}
