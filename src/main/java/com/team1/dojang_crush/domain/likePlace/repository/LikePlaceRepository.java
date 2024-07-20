package com.team1.dojang_crush.domain.likePlace.repository;

import com.team1.dojang_crush.domain.likePlace.domain.LikePlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikePlaceRepository extends JpaRepository<LikePlace, Long> {
}
