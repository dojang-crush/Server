package com.team1.dojang_crush.domain.likePost.service;

import com.team1.dojang_crush.domain.likePost.domain.LikePost;
import com.team1.dojang_crush.domain.likePost.repository.LikePostRepository;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.service.MemberService;
import com.team1.dojang_crush.domain.post.domain.Post;
import com.team1.dojang_crush.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class LikePostService {
    private final LikePostRepository likePostRepository;
    private final PostService postService;
    private final MemberService memberService;

    // 좋아요 누르기
    public void create(Long postId, Long memberId){
        Member member = memberService.findMemberById(memberId);
        Post post = postService.findPostById(postId);

        if(isExistsByMemberAndPost(member, post)){
            throw new RuntimeException("이미 좋아요를 누름 게시물입니다.");
        }

        LikePost likePost = LikePost.builder()
                .post(post)
                .member(member)
                .build();

        likePostRepository.save(likePost);
    }


    //member와 post로 좋아요를 누른 상태인지 확인
    public boolean isExistsByMemberAndPost(Member member, Post post) {
        return likePostRepository.existsByMemberAndPost(member, post);
    }

    //좋아요 취소
    public void delete(Long postId, Long memberId){
        Post post = postService.findPostById(postId);
        Member member = memberService.findMemberById(memberId);

        LikePost likePost = likePostRepository.findByMemberAndPost(member,post)
                .orElseThrow(()-> new RuntimeException("좋아요가 존재하지 않습니다."));
        likePostRepository.delete(likePost);
    }

    // 좋아요 개수
    @Transactional(readOnly = true)
    public Integer countLikePost(Post post){
        Integer count = likePostRepository.countByPost(post);
        return count;
    }
    
    
}
