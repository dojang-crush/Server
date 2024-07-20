package com.team1.dojang_crush.domain.post.repository;

import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.post.domain.Post;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllByMember(Member member);

    @Query("SELECT p FROM Post p WHERE p.visitedDate = :visitedDate AND p.member.group.groupId = :groupId")
    List<Post> findByGroupIdAndVisitedDate(@Param("groupId") Long groupId, @Param("visitedDate") Date visitedDate);
}
