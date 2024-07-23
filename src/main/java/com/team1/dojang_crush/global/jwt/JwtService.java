package com.team1.dojang_crush.global.jwt;

import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final MemberRepository memberRepository;

    @Transactional
    public void updateAccessToken(Member member, String accessToken) {
        member.updateAccessToken(accessToken);
        memberRepository.save(member);
    }

    @Transactional
    public Member getOrCreateMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElse(Member.builder()
                        .email(email)
                        .build());
    }
}