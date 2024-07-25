package com.team1.dojang_crush.domain.post.dto;

import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.place.domain.Place;
import com.team1.dojang_crush.domain.post.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WriterDto {
    private Long memberId;
    private String name;
    private String profileImageUrl;
    private String email;

    public WriterDto (Long memberId, String name, String profileImageUrl){
        this.memberId = memberId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }
}
