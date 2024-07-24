package com.team1.dojang_crush.domain.likePost.repository;

import com.team1.dojang_crush.domain.likePost.domain.LikePost;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.post.domain.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    Integer countByPost(Post post);

    List<LikePost> findByMember(Member member);

    boolean existsByMemberAndPost(Member member, Post post);

    Optional<LikePost> findByMemberAndPost (Member member, Post post);
}
