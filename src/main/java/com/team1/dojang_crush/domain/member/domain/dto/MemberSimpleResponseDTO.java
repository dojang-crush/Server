package com.team1.dojang_crush.domain.member.domain.dto;

import com.team1.dojang_crush.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSimpleResponseDTO {
    Long memberId;
    String name;
    Long groupId;
    String profileImageUrl;

    public static MemberSimpleResponseDTO from(Member e){
        return MemberSimpleResponseDTO.builder()
                .memberId(e.getMemberId())
                .name(e.getName())
                .groupId(e.getGroup().getGroupId())
                .profileImageUrl(e.getImgUrl())
                .build();
    }
}
