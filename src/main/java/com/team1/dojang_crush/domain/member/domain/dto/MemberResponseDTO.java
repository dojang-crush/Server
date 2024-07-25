package com.team1.dojang_crush.domain.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberResponseDTO {
    private Long id;
    private String nickname;
    private String email;
    private String imgUrl;
}