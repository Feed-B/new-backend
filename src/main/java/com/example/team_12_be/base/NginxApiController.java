package com.example.team_12_be.base;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Tag(name = "서버 Nginx 배포 설정 API")
@RestController
@RequiredArgsConstructor
public class NginxApiController {

    private final Environment env;

    @GetMapping("/nginx/profile")
    public String getProfile() {
        return Arrays.stream(env.getActiveProfiles())
                .findFirst()
                .orElse("");
    }
}
