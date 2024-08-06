package com.team1.dojang_crush.domain.member.controller;

import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.dto.MemberDetailResponseDto;
import com.team1.dojang_crush.domain.member.service.MemberService;
import com.team1.dojang_crush.global.oauth.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    //멤버 조회
    @GetMapping
    public ResponseEntity<MemberDetailResponseDto> getMemberByToken(@AuthUser Member member) {
        if (member == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        MemberDetailResponseDto responseDto = new MemberDetailResponseDto(memberService.findMemberById(member.getMemberId()));
        return ResponseEntity.ok(responseDto);
    }

    //멤버 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@AuthUser Member member) {
        memberService.deleteMember(member);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}