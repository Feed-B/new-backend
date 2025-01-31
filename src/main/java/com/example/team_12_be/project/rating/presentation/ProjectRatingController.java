package com.example.team_12_be.project.rating.presentation;

import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.project.rating.service.ProjectRatingService;
import com.example.team_12_be.project.rating.service.dto.request.ProjectRatingRequestDto;
import com.example.team_12_be.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "프로젝트 별점 C,U,D 컨트롤러")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ProjectRatingController {

    private final ProjectRatingService projectRatingService;

    @PostMapping("/projects/{projectId}/ratings")
    @Operation(description = "프로젝트에 대한 사용자의 별점 생성")
    public ResponseEntity<Void> addRating(@PathVariable Long projectId,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                          @RequestBody ProjectRatingRequestDto projectRatingRequestDto) {

        Long ratingId = projectRatingService.addRating(projectId, customUserDetails.getMember().getId(), projectRatingRequestDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ratingId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/projects/ratings/{ratingId}")
    @Operation(description = "프로젝트 별점 수정")
    public ResponseEntity<Long> updateComment(@PathVariable Long ratingId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails,
                                              @RequestBody ProjectRatingRequestDto projectRatingRequestDto) {

        Member member = userDetails.getMember();
        projectRatingService.modifyRating(member.getId(), ratingId, projectRatingRequestDto);

        return ResponseEntity.ok(ratingId);
    }

    @DeleteMapping("/projects/ratings/{ratingId}")
    @Operation(description = "프로젝트 별점 삭제")
    public void deleteComment(@PathVariable Long ratingId,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {

        Member member = userDetails.getMember();
        projectRatingService.deleteRating(member.getId(), ratingId);
    }

}
