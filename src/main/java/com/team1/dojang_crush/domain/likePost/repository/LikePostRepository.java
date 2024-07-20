package com.team1.dojang_crush.domain.likePost.repository;

import com.team1.dojang_crush.domain.likePost.domain.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikePostRepository extends JpaRepository<LikePost, Long> {
}
