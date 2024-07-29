package com.team1.dojang_crush.domain.likePlace.repository;

import com.team1.dojang_crush.domain.likePlace.domain.LikePlace;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.place.domain.Place;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikePlaceRepository extends JpaRepository<LikePlace, Long> {

    List<LikePlace> findByMember(Member member);

    boolean existsByMemberAndPlace(Member member, Place place);

    Optional<LikePlace> findByMemberAndPlace(Member member, Place place);

    List<LikePlace> findByPlacePlaceId(Long placeId);


}
