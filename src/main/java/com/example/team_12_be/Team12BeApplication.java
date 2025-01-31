package com.example.team_12_be;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Team12BeApplication {
    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:nginx-set.yml";

    public static void main(String[] args) {
        // 현재 작업 디렉토리를 로그로 출력(
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current working directory: " + currentDir);

        // 보안을 위한 .env 파일 사용
        Dotenv dotenv = Dotenv.configure()
                .directory(currentDir)  // 현재 작업 디렉토리로 설정
                .load();
        System.setProperty("AWS_ACCESS_KEY_ID", dotenv.get("AWS_ACCESS_KEY_ID"));
        System.setProperty("AWS_SECRET_ACCESS_KEY", dotenv.get("AWS_SECRET_ACCESS_KEY"));
        System.setProperty("AWS_REGION", dotenv.get("AWS_REGION"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));

        new SpringApplicationBuilder(Team12BeApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
