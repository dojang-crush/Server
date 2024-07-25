package com.team1.dojang_crush.domain.member.repository;

import com.team1.dojang_crush.domain.group.domain.Group;
import com.team1.dojang_crush.domain.member.domain.Member;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByGroup(Group group);
    Optional<Member> findByMemberId(Long memberId);
    Optional<Member> findByName(String name);
    Optional<Member> findByEmail(String email);
}
