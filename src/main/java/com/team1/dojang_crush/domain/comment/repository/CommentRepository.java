package com.team1.dojang_crush.domain.comment.repository;

import com.team1.dojang_crush.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
