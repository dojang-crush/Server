package com.team1.dojang_crush.domain.comment.service;

import com.team1.dojang_crush.domain.comment.domain.Comment;
import com.team1.dojang_crush.domain.comment.repository.CommentRepository;
import com.team1.dojang_crush.domain.post.domain.Post;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;


    // post의 제일 최근 댓글 찾기
    public Comment findRecentComment(Post post) {
        return commentRepository.findFirstByPostOrderByCreatedAtDesc(post)
                .orElse(null);
    }
}
