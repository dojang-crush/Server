package com.team1.dojang_crush.domain.comment.domain.dto;

import com.team1.dojang_crush.domain.comment.domain.Comment;
import com.team1.dojang_crush.domain.member.dto.MemberSimpleResponseDTO;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDTO {
    Long commentId;
    Long postId;
    String content;
    LocalDateTime createdDate;
    LocalDateTime modifiedDate;
    int cDepth;
    Long parentCId;
    MemberSimpleResponseDTO writer;

    public static CommentResponseDTO from(Comment e){
        if (e.getParentComment()==null){
            return CommentResponseDTO.builder()
                    .commentId(e.getCommentId())
                    .postId(e.getPost().getPostId())
                    .content(e.getCommentContent())
                    .createdDate(e.getCreatedAt())
                    .modifiedDate(e.getModifiedAt())
                    .cDepth(e.getCommentDepth())
                    .parentCId(null)
                    .writer(MemberSimpleResponseDTO.from(e.getMember()))
                    .build();

        }else{
            return CommentResponseDTO.builder()
                    .commentId(e.getCommentId())
                    .postId(e.getPost().getPostId())
                    .content(e.getCommentContent())
                    .createdDate(e.getCreatedAt())
                    .modifiedDate(e.getModifiedAt())
                    .cDepth(e.getCommentDepth())
                    .parentCId(e.getParentComment().getCommentId())
                    .writer(MemberSimpleResponseDTO.from(e.getMember()))
                    .build();

        }

    }

}
