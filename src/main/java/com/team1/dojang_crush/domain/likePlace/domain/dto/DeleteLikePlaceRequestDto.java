package com.team1.dojang_crush.domain.likePlace.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DeleteLikePlaceRequestDto {

    private Long placeId;
    private Long memberId;

}



