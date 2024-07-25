package com.team1.dojang_crush.domain.member.service;

import com.team1.dojang_crush.domain.group.domain.Group;
import com.team1.dojang_crush.domain.group.repository.GroupRepository;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.repository.MemberRepository;

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

    // 그룹에 속하는 member들 찾기
    public List<Member> findGroupMemberList(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다"));
        List<Member> memberList = memberRepository.findAllByGroup(group);
        return memberList;
    }

}

