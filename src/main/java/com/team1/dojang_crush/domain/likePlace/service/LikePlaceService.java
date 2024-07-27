package com.team1.dojang_crush.domain.likePlace.service;

import com.team1.dojang_crush.domain.likePlace.domain.LikePlace;
import com.team1.dojang_crush.domain.likePlace.repository.LikePlaceRepository;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.service.MemberService;
import com.team1.dojang_crush.domain.place.domain.Place;
import com.team1.dojang_crush.domain.place.service.PlaceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikePlaceService {

    private final LikePlaceRepository likePlaceRepository;
    private final PlaceService placeService;
    private final MemberService memberService;


    public LikePlace create(Long placeId, Long memberId) {

        Member member = memberService.findMemberById(memberId);
        Place place = placeService.findPlaceById(placeId);

        if (isExistsByMemberAndPlace(member, place)) {
            throw new RuntimeException("이미 좋아요한 장소입니다.");
        }

        LikePlace likePlace = LikePlace.builder()
                .place(place)
                .member(member)
                .build();

        return likePlaceRepository.save(likePlace);

    }


    // 좋아요 삭제
    public void delete(Long placeId, Long memberId) {
        Place place = placeService.findPlaceById(placeId);
        Member member = memberService.findMemberById(memberId);

        LikePlace likePlace = likePlaceRepository.findByMemberAndPlace(member, place)
                .orElseThrow(() -> new RuntimeException("좋아요가 존재하지 않습니다."));
        likePlaceRepository.delete(likePlace);
    }


    public boolean isExistsByMemberAndPlace(Member member, Place place) {
        return likePlaceRepository.existsByMemberAndPlace(member, place);
    }

    public List<LikePlace> findLikePlaceByMember(Member member) {
        return likePlaceRepository.findByMember(member);
    }


}
