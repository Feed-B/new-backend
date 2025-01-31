package com.example.team_12_be.member.presentation;

import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.member.service.dto.MemberService;
import com.example.team_12_be.member.service.dto.request.MemberEditRequestDto;
import com.example.team_12_be.member.service.dto.request.MemberSignUpRequest;
import com.example.team_12_be.member.service.dto.response.MemberIdResponseDto;
import com.example.team_12_be.member.service.dto.response.MemberResponseDto;
import com.example.team_12_be.member.service.dto.response.TokenResponseDto;
import com.example.team_12_be.security.CustomUserDetails;
import com.example.team_12_be.security.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@Tag(name = "계정(유저 정보) 관련 컨트롤러")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @GetMapping("/login/{service}")
    @Operation(description = "{service} = kakao 또는 naver ")
    public RedirectView login(@PathVariable("service") String service) {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String redirectUrl = switch (service.toLowerCase()) {
            case "kakao" -> baseUrl + "/oauth2/authorization/kakao";
            case "naver" -> baseUrl + "/oauth2/authorization/naver";
            default -> throw new IllegalArgumentException("Unsupported service : " + service);
        };

        log.info("baseUrl = " + baseUrl);

        return new RedirectView(redirectUrl);
    }

    @GetMapping("/token")
    public String getTokenTest() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        memberService.saveRandomTestUser(uuid);
        return jwtProvider.createToken(uuid);
    }

    //회원가입
    @PostMapping("/signUp")
    public TokenResponseDto signUp(@RequestBody MemberSignUpRequest memberSignUpRequest) {

        Member member = memberService.signUp(memberSignUpRequest);
        String token = jwtProvider.createToken(member.getEmail());

        return new TokenResponseDto(token);
    }

    @GetMapping("/profile")
    @Operation(description = "현재 로그인된 유저 id 조회")
    public MemberIdResponseDto getMemberId(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return new MemberIdResponseDto(customUserDetails.getMember().getId());
    }

    @GetMapping("/profile/{userId}")
    @Operation(description = "유저 정보 조회")
    public MemberResponseDto getMemberInfo(@PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Member member = memberService.findById(userId);

        return MemberResponseDto.of(member);
    }

    @PutMapping(value = "/profile/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "유저 정보 수정")
    public MemberResponseDto updateMemberInfo(@RequestPart(required = false) MultipartFile image,
                                              @RequestPart Integer imageIdx,
                                              @RequestPart MemberEditRequestDto memberEditRequestDto,
                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Member updateMember = memberService.updateMemberInfo(memberEditRequestDto, image, imageIdx);
        return MemberResponseDto.of(updateMember);
    }

}




