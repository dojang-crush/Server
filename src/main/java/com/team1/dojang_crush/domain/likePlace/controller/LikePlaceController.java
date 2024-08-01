package com.team1.dojang_crush.domain.likePlace.controller;

import com.team1.dojang_crush.domain.group.service.GroupService;
import com.team1.dojang_crush.domain.likePlace.domain.LikePlace;
import com.team1.dojang_crush.domain.likePlace.domain.dto.AllGetLikePlaceResponseDto;
import com.team1.dojang_crush.domain.likePlace.domain.dto.CreateLikePlaceRequestDto;
import com.team1.dojang_crush.domain.likePlace.domain.dto.CreateLikePlaceResponseDto;
import com.team1.dojang_crush.domain.likePlace.domain.dto.DeleteLikePlaceRequestDto;
import com.team1.dojang_crush.domain.likePlace.domain.dto.GetLikePlaceResponseDto;
import com.team1.dojang_crush.domain.likePlace.service.LikePlaceService;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.global.oauth.AuthUser;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class LikePlaceController {

    private final LikePlaceService likePlaceService;
    private final GroupService groupService;


    // 장소 좋아요 생성
//    @PostMapping
//    @ResponseStatus(value = HttpStatus.CREATED)
//    public CreateLikePlaceResponseDto createLikePlace(@RequestBody final CreateLikePlaceRequestDto requestDto) {
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CreateLikePlaceResponseDto createLikePlace(@AuthUser Member member,
                                                      @RequestBody final CreateLikePlaceRequestDto requestDto) {
        LikePlace createdLikePlace = likePlaceService.create(requestDto.getPlaceId(), member.getMemberId());
        return CreateLikePlaceResponseDto.from(createdLikePlace);
    }

    // 장소 좋아요 삭제
//    @DeleteMapping
//    @ResponseStatus(value = HttpStatus.OK)
//    public String deleteLikePlace(@RequestBody final DeleteLikePlaceRequestDto requestDto) {
    @DeleteMapping
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteLikePlace(@AuthUser Member member, @RequestBody final DeleteLikePlaceRequestDto requestDto) {
        likePlaceService.delete(requestDto.getPlaceId(), member.getMemberId());
        return "장소 좋아요가 성공적으로 삭제되었습니다.";
    }


    // 장소 좋아요 조회 (장소별로 그룹원들 중 좋아요 누른 사람들의 정보를 조회합니다.): placeId -> likePlace 리스트 -> groupId로  memberId 리스트 -> dto로, return
    @GetMapping("{placeId}/{groupId}")
    @ResponseStatus(value = HttpStatus.OK)
    public AllGetLikePlaceResponseDto getLikePlace(@AuthUser Member member,
                                                   @PathVariable(name = "placeId") Long placeId,
                                                   @PathVariable(name = "groupId") Long groupId) {
        System.out.println("0");

        // 1: placeId로 먼저 <likePlace> 필터링
        List<LikePlace> likePlaces = likePlaceService.findLikePlaceByPlaceId(placeId);
        List<Member> likeMembers = groupService.findGroupMemberList(groupId);

        // 2: likeMembers의 멤버들 중, placeId = x 인 LikePlace가 존재하는 Member들을 필터링하기
        List<Member> likePlaceMemberList = new ArrayList<>();

        String response = "";

        for (LikePlace likePlace : likePlaces) {
            System.out.println("1");

            for (Member likeMember : likeMembers) {
                if (likePlace.getMember().getMemberId() == likeMember.getMemberId()) {
                    System.out.println("해당 장소를 좋아요한 member의 Id:" + likeMember.getMemberId());
                    likePlaceMemberList.add(likeMember);

                    response = response + likeMember.getMemberId().toString() + " ";

                }
            }

        }
        System.out.println("likePlaceMemberList" + likePlaceMemberList);
        System.out.println(response);

        // 3: 2에서 얻은 List<Member> -> dto로

        List<GetLikePlaceResponseDto> list = new ArrayList<>();

        for (Member likePlaceMember : likePlaceMemberList) {
            GetLikePlaceResponseDto dto = GetLikePlaceResponseDto.from(likePlaceMember);
            list.add(dto);
        }

//        return new AllGetLikePlaceResponseDto(list);
        return new AllGetLikePlaceResponseDto(response);

    }


}
