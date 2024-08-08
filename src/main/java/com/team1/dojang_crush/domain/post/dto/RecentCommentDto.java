package com.team1.dojang_crush.domain.post.dto;

import com.team1.dojang_crush.domain.comment.domain.Comment;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecentCommentDto {
    private Long commentId;
    private Long postId;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Integer cDepth;
    private Long parentCId;
    private WriterDto writerDto;

    public static RecentCommentDto from(Comment recentComment, WriterDto commentWriter) {
        Long parentId;
        if (recentComment.getParentComment()==null) parentId=0l;
        else parentId=recentComment.getParentComment().getCommentId();

        return new RecentCommentDto(
                recentComment.getCommentId(),
                recentComment.getPost().getPostId(),
                recentComment.getCommentContent(),
                recentComment.getCreatedAt(),
                recentComment.getModifiedAt(),
                recentComment.getCommentDepth(),
                parentId,
                commentWriter
        );
    }
}
