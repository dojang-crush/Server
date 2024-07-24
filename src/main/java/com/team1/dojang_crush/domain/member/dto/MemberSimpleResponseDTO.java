package com.team1.dojang_crush.domain.member.dto;

import com.team1.dojang_crush.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSimpleResponseDTO {
    Long memberId;
    Long groupId;
    String profileImageUrl;

    public static MemberSimpleResponseDTO from(Member e){
        return MemberSimpleResponseDTO.builder()
                .memberId(e.getMemberId())
                .groupId(e.getGroup().getGroupId())
                .profileImageUrl(e.getImgUrl())
                .build();
    }
}
