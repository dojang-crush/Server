package com.team1.dojang_crush.domain.place.controller;

import com.team1.dojang_crush.domain.likePlace.domain.LikePlace;
import com.team1.dojang_crush.domain.likePlace.service.LikePlaceService;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.service.MemberService;
import com.team1.dojang_crush.domain.place.domain.Place;
import com.team1.dojang_crush.domain.place.dto.AllPlacesResponseDto;
import com.team1.dojang_crush.domain.place.dto.PlaceResponseDto;
import com.team1.dojang_crush.domain.place.service.PlaceService;
import com.team1.dojang_crush.global.oauth.AuthUser;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {

    private final PlaceService placeService;
    private final LikePlaceService likePlaceService;
    private final MemberService memberService;

    // 장소 전체 조회
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public AllPlacesResponseDto getAllPlaces() {
        List<PlaceResponseDto> list = new ArrayList<>();
        List<Place> places = placeService.findAllPlace();
        for (Place place : places) {
            PlaceResponseDto dto = PlaceResponseDto.from(place);
            list.add(dto);
        }

        return new AllPlacesResponseDto(list);

    }

    // 내가 좋아요 한 장소 조회
//    @GetMapping("liked/{memberId}")
//    @ResponseStatus(value = HttpStatus.OK)
//    public AllPlacesResponseDto getLikedPlaces(@PathVariable(name = "memberId") Long memberId) {

    @GetMapping("liked")
    @ResponseStatus(value = HttpStatus.OK)
    public AllPlacesResponseDto getLikedPlaces(@AuthUser Member member) {

        List<Place> likedPlaceList = new ArrayList<>();
//        Member member = memberService.findMemberById(memberId);

        List<LikePlace> likePlaces = likePlaceService.findLikePlaceByMember(member);
        for (LikePlace likePlace : likePlaces) {
            Place likedPlace = likePlace.getPlace();
            likedPlaceList.add(likedPlace);
        }

        List<PlaceResponseDto> list = new ArrayList<>();

        for (Place place : likedPlaceList) {
            PlaceResponseDto dto = PlaceResponseDto.from(place);
            list.add(dto);
        }

        return new AllPlacesResponseDto(list);

    }


    // 장소 테마별 조회
    @GetMapping("{theme}")
    @ResponseStatus(value = HttpStatus.OK)
    public AllPlacesResponseDto getThemePlaces(@PathVariable(name = "theme") String theme) {
        List<PlaceResponseDto> list = new ArrayList<>();
        List<Place> places = placeService.findPlaceByTheme(theme);
        for (Place place : places) {
            PlaceResponseDto dto = PlaceResponseDto.from(place);
            list.add(dto);
        }

        return new AllPlacesResponseDto(list);

    }

    // 내가 좋아요 한 장소 테마별 조회
//    @GetMapping("liked/{memberId}/{theme}")
//    @ResponseStatus(value = HttpStatus.OK)
//    public AllPlacesResponseDto getLikedThemePlaces(@PathVariable(name = "memberId") Long memberId,
//                                                    @PathVariable(name = "theme") String theme) {

    @GetMapping("liked/{theme}")
    @ResponseStatus(value = HttpStatus.OK)
    public AllPlacesResponseDto getLikedThemePlaces(@AuthUser Member member,
                                                    @PathVariable(name = "theme") String theme) {

        // 1: 테마로 먼저 <Place> 필터링 -> 이게 전체집합이 되는걸로..해서 이후엔 "내가 좋아요 한 장소 조회" 로직과 동일하게
        List<Place> places = placeService.findPlaceByTheme(theme);

        List<Place> likedThemePlaceList = new ArrayList<>();

//        Member member = memberService.findMemberById(memberId);
        List<LikePlace> likePlaces = likePlaceService.findLikePlaceByMember(member);

        for (LikePlace likePlace : likePlaces) {

            for (Place themePlace : places) {
                if (themePlace.getPlaceId() == likePlace.getPlace().getPlaceId()) {
                    likedThemePlaceList.add(themePlace);
                }
            }

        }

        List<PlaceResponseDto> list = new ArrayList<>();

        for (Place place : likedThemePlaceList) {
            PlaceResponseDto dto = PlaceResponseDto.from(place);
            list.add(dto);
        }

        return new AllPlacesResponseDto(list);


    }


    //장소검색
    @GetMapping("search")
    @ResponseStatus(value = HttpStatus.OK)
    public AllPlacesResponseDto searchPlace(@RequestParam("searchKeyword") String searchKeyword){
        List<PlaceResponseDto> searchPlaceList = placeService.searchPlace(searchKeyword);
        return new AllPlacesResponseDto(searchPlaceList);
    }
}

