package com.team1.dojang_crush.domain.post.repository;

import com.team1.dojang_crush.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
