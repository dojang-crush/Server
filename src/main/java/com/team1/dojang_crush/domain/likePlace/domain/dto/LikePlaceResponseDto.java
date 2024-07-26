package com.team1.dojang_crush.domain.likePlace.domain.dto;

import com.team1.dojang_crush.domain.likePlace.domain.LikePlace;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LikePlaceResponseDto {

    private Long placeId;
    private Long memberId;
    private Long likePlaceId;

    public static LikePlaceResponseDto from(LikePlace createdLikePlace) {
        return new LikePlaceResponseDto(
                createdLikePlace.getPlace().getPlaceId(),
                createdLikePlace.getMember().getMemberId(),
                createdLikePlace.getLikePlaceId()
        );

    }
}

