package com.team1.dojang_crush.domain.comment.repository;

import com.team1.dojang_crush.domain.comment.domain.Comment;
import com.team1.dojang_crush.domain.post.domain.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findFirstByPostOrderByCreatedAtDesc(Post post);
}
