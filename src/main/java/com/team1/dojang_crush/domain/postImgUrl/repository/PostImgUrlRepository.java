package com.team1.dojang_crush.domain.postImgUrl.repository;

import com.team1.dojang_crush.domain.post.domain.Post;
import com.team1.dojang_crush.domain.postImgUrl.domain.PostImgUrl;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImgUrlRepository extends JpaRepository<PostImgUrl, Long> {
    PostImgUrl findByPost(Post post);

    boolean existsByPost(Post post);
}
