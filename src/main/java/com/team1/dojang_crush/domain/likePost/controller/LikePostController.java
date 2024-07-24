package com.team1.dojang_crush.domain.likePost.controller;

import com.team1.dojang_crush.domain.likePost.service.LikePostService;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class LikePostController {
    private final LikePostService likePostService;


    //////////////////// 임시
    private final MemberRepository memberRepository;
    private Member findMember(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다."));
        return member;
    }


    //좋아요
    @PostMapping("/{postId}/like")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String createLikePost(@PathVariable(name = "postId")Long postId){

        Member member = findMember(1l);

        likePostService.create(postId, member.getMemberId());
        return "좋아요를 눌렀습니다.";
    }

    //좋아요 취소
    @DeleteMapping("/{postId}/like")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteLikePost(@PathVariable(name = "postId")Long postId){

        Member member = findMember(1l);

        likePostService.delete(postId, member.getMemberId());
        return "좋아요를 취소했습니다.";
    }
}
