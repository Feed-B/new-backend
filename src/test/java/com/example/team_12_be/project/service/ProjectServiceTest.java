package com.example.team_12_be.project.service;

import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.member.domain.vo.Job;
import com.example.team_12_be.member.repository.MemberRepository;
import com.example.team_12_be.project.domain.Project;
import com.example.team_12_be.project.domain.ProjectImage;
import com.example.team_12_be.project.domain.vo.ImageType;
import com.example.team_12_be.project.image.service.ProjectImageService;
import com.example.team_12_be.project.image.service.ProjectThumbnailService;
import com.example.team_12_be.project.repository.ProejctJpaRepository;
import com.example.team_12_be.project.service.dto.request.*;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class ProjectServiceTest {

    @Autowired
    ProjectService projectService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProejctJpaRepository proejctJpaRepository;

    ProjectRequestDto requestDtoFixture;

    Member memberFixture;

    @MockBean
    ProjectImageService projectImageService;

    @MockBean
    ProjectThumbnailService projectThumbnailService;

    @BeforeEach
    void setup() {
        memberFixture = createMemberFixture();
        requestDtoFixture = createTestFixture();
        memberRepository.save(memberFixture);
    }

    Member createMemberFixture() {
        return new Member(
                "tes123t",
                "te123st",
                "tes123t",
                Job.IOS
        );
    }

    ProjectRequestDto createTestFixture() {
        String title = "Test Title";
        String introduction = "Test Introduction";
        String content = "Test Content";
        String serviceUrl = "http://localhost:8080";
        ImageType imageType = ImageType.WEB;
        List<String> projectTechStacks = Arrays.asList("Java", "Spring", "JUnit");
        List<ProjectTeammateRequestDto> projectTeammates = new ArrayList<>();
        List<ProjectLinkRequestDto> projectLinks = new ArrayList<>();

        return new ProjectRequestDto(
                title,
                introduction,
                content,
                serviceUrl,
                imageType,
                projectTechStacks,
                projectTeammates,
                projectLinks
        );
    }

    @Test
    void testSave() {
        // given
        List<ProjectImageDto> projectImageDtoList = List.of(mock(ProjectImageDto.class));
        ProjectThumbnailDto projectThumbnailDto = mock(ProjectThumbnailDto.class);
        projectService.saveProject(requestDtoFixture, memberFixture, projectImageDtoList, projectThumbnailDto);

        List<ProjectImage> projectImages = new ArrayList<>();
        when(projectImageService.upload(projectImageDtoList))
                .thenReturn(projectImages);
        when(projectThumbnailService.upload(projectThumbnailDto))
                .thenReturn("");
        Project project = proejctJpaRepository.findAll().getFirst();

        SoftAssertions.assertSoftly(
                softly -> {
                    softly.assertThat(project.getProjectTechStacks()).hasSameSizeAs(requestDtoFixture.projectTechStacks());
                    softly.assertThat(project.getProjectTeammates()).hasSameSizeAs(requestDtoFixture.projectTeammates());
                    softly.assertThat(project.getProjectLinks()).hasSameSizeAs(requestDtoFixture.projectLinks());
                }
        );
    }

}