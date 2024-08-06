package com.team1.dojang_crush.domain.member.service;

import com.team1.dojang_crush.domain.group.domain.Group;
import com.team1.dojang_crush.domain.group.repository.GroupRepository;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.repository.MemberRepository;

import com.team1.dojang_crush.global.exception.AppException;
import com.team1.dojang_crush.global.exception.ErrorCode;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(GroupRepository groupRepository, MemberRepository memberRepository) {
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
    }

    public long findMemberIdByEmail(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        return member.getMemberId();
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(()->new IllegalArgumentException("계정이 존재하지 않습니다."));
    }

    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    public Member createMember(String name, String imgUrl, String email) {
        Group defaultGroup = groupRepository.findByGroupCode("2e7a5b12-3fae-403d-b01d-45c9e4c25b82");
        if (defaultGroup == null) {
            throw new IllegalStateException("Default group not found");
        }
        Member member = new Member(name, imgUrl, email, defaultGroup);
        return memberRepository.save(member);
    }

    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Member not found with email: " + email)));
    };

    // group 업데이트
    public void updateGroup(Group group, Member member) {
        member.updateGroup(group);
        memberRepository.save(member);
    }

    public void updateLeader(Member member, boolean b) {
        member.updateLead(b);
        memberRepository.save(member);
    }
}

