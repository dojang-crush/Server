package com.team1.dojang_crush.domain.group.dto;

import com.team1.dojang_crush.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroupMemberDto {
    private Long memberId;
    private String name;
    private String profileImageUrl;
    private String email;
    private boolean isLeader;


    public static GroupMemberDto from(Member e) {
        return GroupMemberDto.builder()
                .memberId(e.getMemberId())
                .name(e.getName())
                .profileImageUrl(e.getImgUrl())
                .email(e.getEmail())
                .isLeader(e.isLead())
                .build();
    }
}
