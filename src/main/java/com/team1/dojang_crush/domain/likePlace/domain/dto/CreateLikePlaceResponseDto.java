package com.team1.dojang_crush.domain.likePlace.domain.dto;

import com.team1.dojang_crush.domain.likePlace.domain.LikePlace;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateLikePlaceResponseDto {

    private Long placeId;
    private Long memberId;
    private Long likePlaceId;

    public static CreateLikePlaceResponseDto from(LikePlace createdLikePlace) {
        return new CreateLikePlaceResponseDto(
                createdLikePlace.getPlace().getPlaceId(),
                createdLikePlace.getMember().getMemberId(),
                createdLikePlace.getLikePlaceId()
        );

    }
}



