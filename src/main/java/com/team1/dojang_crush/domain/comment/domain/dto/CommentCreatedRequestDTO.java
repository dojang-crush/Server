package com.team1.dojang_crush.domain.comment.domain.dto;

import com.team1.dojang_crush.domain.comment.domain.Comment;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.post.domain.Post;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;

@Getter
@NoArgsConstructor
public class CommentCreatedRequestDTO {

    @NotNull
    @Size(max = 500)
    String content;

    @Nullable
    Long parentId;

    @Builder
    public Comment from(Post post, Member member, Comment parentComment){

        return Comment.builder()
                .commentContent(this.content)
                .commentDepth(parentComment.getCommentDepth()+1)
                .parentComment(parentComment)
                .member(member)
                .post(post)
                .build();

    }

    @Builder
    public Comment from(Post post, Member member){
        return Comment.builder()
                .commentContent(this.content)
                .commentDepth(1)
                .post(post)
                .member(member)
                .build();
    }

}
