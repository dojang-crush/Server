package com.team1.dojang_crush.domain.comment.domain.dto;

import com.team1.dojang_crush.domain.comment.domain.Comment;
import com.team1.dojang_crush.domain.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class PostCommentListResponseDTO {
    Long postId;
    List<CommentResponseDTO> commentList;

    public static PostCommentListResponseDTO from(List<Comment> list, Post post){
        return PostCommentListResponseDTO.builder()
                .postId(post.getPostId())
                .commentList(list.stream().map(CommentResponseDTO::from).collect(Collectors.toList()))
                .build();
    }
}
