package com.team1.dojang_crush.domain.comment.domain.dto;

import com.team1.dojang_crush.domain.comment.domain.Comment;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.domain.dto.MemberSimpleResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentCreatedResponseDTO {
    Long commentId;
    Long postId;
    String content;
    LocalDateTime createdDate;
    LocalDateTime modifiedDate;
    int cDepth;
    Long parentCId;
    MemberSimpleResponseDTO writer;

    public static CommentCreatedResponseDTO from(Comment e, Member member){
        return CommentCreatedResponseDTO.builder()
                .commentId(e.getCommentId())
                .postId(e.getPost().getPostId())
                .content(e.getCommentContent())
                .createdDate(e.getCreatedAt())
                .modifiedDate(e.getModifiedAt())
                .cDepth(e.getCommentDepth())
                .parentCId(e.getParentComment().getCommentId())
                .writer(MemberSimpleResponseDTO.from(member))
                .build();
    }



}
