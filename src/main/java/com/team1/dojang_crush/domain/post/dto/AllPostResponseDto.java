package com.team1.dojang_crush.domain.post.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AllPostResponseDto {
    private Long groupId;
    private  List<PostResponseDto> postList;
    private long count;
}