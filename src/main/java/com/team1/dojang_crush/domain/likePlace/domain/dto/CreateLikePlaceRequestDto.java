package com.team1.dojang_crush.domain.likePlace.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateLikePlaceRequestDto {

    private Long placeId;
//    private Long memberId;

//    public LikePlace toEntity(Member member) {
//        return Post.builder()
//                .member(member)
//                .title(this.title)
//                .content(this.content)
//                .build();
//    }

}

