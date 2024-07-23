package com.team1.dojang_crush.domain.post.dto;

import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.place.domain.Place;
import com.team1.dojang_crush.domain.post.domain.Post;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostRequestDto {

    @NotBlank(message = "회원 id는 필수입니다.")
    private Long memberId;

    @NotBlank(message = "그룹 id는 필수입니다.")
    private Long groupId;

    private String content;
    private LocalDate visitedDate;

    @NotBlank(message = "장소 id는 필수입니다.")
    private Long placeId;

    public Post toEntity(Member member, Place place){
        return Post.builder()
                .member(member)
                .postContent(this.content)
                .visitedDate(this.visitedDate)
                .place(place)
                .build();
    }
}