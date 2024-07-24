package com.team1.dojang_crush.domain.member.service;

import com.team1.dojang_crush.domain.group.domain.Group;
import com.team1.dojang_crush.domain.group.repository.GroupRepository;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.repository.MemberRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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

    public Member createMember(String name, String imgUrl, String email) {
        Group defaultGroup = groupRepository.findByGroupCode("DEFAULT");
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


}