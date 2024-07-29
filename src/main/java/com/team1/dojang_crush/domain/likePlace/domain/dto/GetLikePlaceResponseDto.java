package com.team1.dojang_crush.domain.likePlace.domain.dto;

import com.team1.dojang_crush.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GetLikePlaceResponseDto {

    private Long memberId;

    public static GetLikePlaceResponseDto from(Member likePlaceMember) {
        return new GetLikePlaceResponseDto(likePlaceMember.getMemberId());
    }


}
















