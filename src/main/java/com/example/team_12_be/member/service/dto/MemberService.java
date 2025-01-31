package com.example.team_12_be.member.service.dto;

import com.example.team_12_be.member.domain.Member;
import com.example.team_12_be.member.domain.vo.Job;
import com.example.team_12_be.member.repository.MemberRepository;
import com.example.team_12_be.member.service.dto.request.MemberEditRequestDto;
import com.example.team_12_be.member.service.dto.request.MemberSignUpRequest;
import com.example.team_12_be.project.image.service.MemberImageUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberImageUpdateService memberImageUpdateService;

    public Member signUp(MemberSignUpRequest memberSignUpRequest) {
        if (memberRepository.findByEmail(memberSignUpRequest.email()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        return memberRepository.save(memberSignUpRequest.toEntity());
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Member with userId" + id + "not found"));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Member with email" + email + "not found"));
    }

    public Member updateMemberInfo(MemberEditRequestDto memberEditRequestDto, MultipartFile image, int idx) {


        Member findMember = memberRepository.findById(memberEditRequestDto.id()).orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));
        findMember.setNickName(memberEditRequestDto.nickName());
        findMember.setAboutMe(memberEditRequestDto.aboutMe());
        findMember.setMemberJob(memberEditRequestDto.job());

        //프로필 이미지 업로드
        String url = memberImageUpdateService.upload(image, findMember.getImageUrl(), idx);
        findMember.setImageUrl(url);

        return findMember;
    }

    public void saveRandomTestUser(String uuid) {
        if (memberRepository.findByEmail(uuid).isPresent()) {
            return;
        }

        memberRepository.save(new Member(uuid, uuid, uuid, Job.IOS));
    }
}
