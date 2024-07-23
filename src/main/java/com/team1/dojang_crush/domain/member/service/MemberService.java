package com.team1.dojang_crush.domain.member.service;

import com.team1.dojang_crush.domain.group.domain.Group;
import com.team1.dojang_crush.domain.group.repository.GroupRepository;
import com.team1.dojang_crush.domain.group.service.GroupService;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    // memberId로 회원 찾기
    @Transactional(readOnly = true)
    public Member findMemberById(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다.id="+memberId));

        return member;
    }


    // 그룹에 속하는 member들 찾기
    @Transactional
    public List<Member> findGroupMemberList(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다"));
        List<Member> memberList = memberRepository.findAllByGroup(group);
        return memberList;
    }

}
