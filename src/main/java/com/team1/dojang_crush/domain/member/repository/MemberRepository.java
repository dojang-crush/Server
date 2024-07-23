package com.team1.dojang_crush.domain.member.repository;

import com.team1.dojang_crush.domain.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(Long memberId);
    Optional<Member> findByEmail(String email);
}
