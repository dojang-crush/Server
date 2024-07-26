package com.team1.dojang_crush.domain.likePlace.controller;

import com.team1.dojang_crush.domain.likePlace.domain.LikePlace;
import com.team1.dojang_crush.domain.likePlace.domain.dto.CreateLikePlaceRequestDto;
import com.team1.dojang_crush.domain.likePlace.domain.dto.CreateLikePlaceResponseDto;
import com.team1.dojang_crush.domain.likePlace.domain.dto.DeleteLikePlaceRequestDto;
import com.team1.dojang_crush.domain.likePlace.service.LikePlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    // 장소 좋아요 생성
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CreateLikePlaceResponseDto createLikePlace(@RequestBody final CreateLikePlaceRequestDto requestDto) {
        LikePlace createdLikePlace = likePlaceService.create(requestDto.getPlaceId(), requestDto.getMemberId());
        return CreateLikePlaceResponseDto.from(createdLikePlace);
    }

    // 장소 좋아요 삭제
    @DeleteMapping
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteLikePlace(@RequestBody final DeleteLikePlaceRequestDto requestDto) {
        likePlaceService.delete(requestDto.getPlaceId(), requestDto.getMemberId());
        return "장소 좋아요가 성공적으로 삭제되었습니다.";
    }


}
