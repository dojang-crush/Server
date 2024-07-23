package com.team1.dojang_crush.domain.post.controller;

import com.team1.dojang_crush.domain.comment.domain.Comment;
import com.team1.dojang_crush.domain.comment.service.CommentService;
import com.team1.dojang_crush.domain.likePost.service.LikePostService;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.repository.MemberRepository;
import com.team1.dojang_crush.domain.post.domain.Post;
import com.team1.dojang_crush.domain.post.dto.AllPostResponseDto;
import com.team1.dojang_crush.domain.post.dto.PostResponseDto;
import com.team1.dojang_crush.domain.post.dto.RecentCommentDto;
import com.team1.dojang_crush.domain.post.dto.WriterDto;
import com.team1.dojang_crush.domain.post.service.PostService;
import com.team1.dojang_crush.domain.postImgUrl.domain.PostImgUrl;
import com.team1.dojang_crush.domain.postImgUrl.service.PostImgUrlService;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final LikePostService likePostService;
    private final CommentService commentService;
    private final PostImgUrlService postImgUrlService;

    // 게시글 생성
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public PostResponseDto createPost(@RequestParam("memberId") Long memberId,
                                      @RequestParam("content") String content,
                                      @RequestParam("placeId") Long placeId,
                                      @RequestParam("groupId") Long groupId,
                                      @RequestParam("visitedDate") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate visitedDate,
                                      @RequestParam(value = "images", required = false) List<MultipartFile> images){
        Post savedPost = postService.createPost(memberId, content,placeId,groupId,visitedDate, images);
        Member member = savedPost.getMember();
        PostImgUrl postImgUrl = postImgUrlService.findImgUrlByPost(savedPost);
        return PostResponseDto.from(savedPost, savedPost.getPlace(),
                new WriterDto(member.getMemberId(), member.getName(), member.getImgUrl()),
                likePostService.isExistsByMemberAndPost(member,savedPost),postImgUrl);
    }

    // 그룹 전체 게시글 조회
    @GetMapping("/all/{groupId}")
    @ResponseStatus(value = HttpStatus.OK)
    public AllPostResponseDto getAllPosts(@PathVariable(name = "groupId")Long groupId){
        // 그룹의 모든 post 찾기
        List<Post> posts = postService.findAllPosts(groupId);

        // post -> PostResponseDto
        List<PostResponseDto> list = new ArrayList<>();
        RecentCommentDto recentCommentDto;

        for(Post post : posts){
            //post의 젤 최근 댓글 찾기
            Comment recentComment = commentService.findRecentComment(post);

            if(recentComment == null){ // 댓글이 없는 경우
                recentCommentDto = null;
            }
            else{
                Member recentCommentWriter = recentComment.getMember();
                //댓글 writer
                WriterDto commentWriter = new WriterDto(recentCommentWriter.getMemberId(),recentCommentWriter.getName(),recentCommentWriter.getImgUrl());

                //commentDto
                recentCommentDto = RecentCommentDto.from(recentComment,commentWriter);
            }

            //게시물 writer
            WriterDto postWriter = new WriterDto(post.getMember().getMemberId(), post.getMember().getName(), post.getMember().getImgUrl());

            PostImgUrl postImgUrl = postImgUrlService.findImgUrlByPost(post);

            // 임시
            Member member = findMember(1l);

            PostResponseDto dto = PostResponseDto.from(post, post.getPlace(), postWriter,
                    likePostService.isExistsByMemberAndPost(member,post),
                    recentCommentDto, postImgUrl);
            list.add(dto);
        }

        return new AllPostResponseDto(groupId, list, list.size());
    }

    // 그룹 날짜별 게시글 조회
    @GetMapping("/date/{groupId}")
    @ResponseStatus(value = HttpStatus.OK)
    public AllPostResponseDto getDatePosts(@PathVariable(name = "groupId")Long groupId,
                                           @RequestParam("visitedDate") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate visitedDate){


        // 그룹과 날짜로 posts 찾기
        List<Post> posts = postService.findPostsByDate1(groupId, visitedDate);

        // post -> PostResponseDto
        List<PostResponseDto> list = new ArrayList<>();

        // 임시
        Member member = findMember(1l);

        for(Post post : posts){
            //게시물 writer
            WriterDto postWriter = new WriterDto(post.getMember().getMemberId(), post.getMember().getName(), post.getMember().getImgUrl());

            PostImgUrl postImgUrl = postImgUrlService.findImgUrlByPost(post);

            PostResponseDto dto = PostResponseDto.from(post, post.getPlace(), postWriter,
                    likePostService.isExistsByMemberAndPost(member,post), postImgUrl);
            list.add(dto);
        }

        return new AllPostResponseDto(groupId, list, list.size());
    }

    // 게시글 1개 조회
    @GetMapping("/{postId}")
    @ResponseStatus(value = HttpStatus.OK)
    public PostResponseDto getOnePost(@PathVariable(name = "postId")Long postId){
        Post post = postService.findPostById(postId);
        PostImgUrl postImgUrl = postImgUrlService.findImgUrlByPost(post);

        // 임시
        Member member = findMember(1l);

        return PostResponseDto.from(post, post.getPlace(),
                new WriterDto(post.getMember().getMemberId(), post.getMember().getName(), post.getMember().getImgUrl()),
                likePostService.isExistsByMemberAndPost(member,post), postImgUrl);
    }

    // 게시글 수정
    @PatchMapping("/{postId}")
    @ResponseStatus(value = HttpStatus.OK)
    public PostResponseDto updatePost(@PathVariable(name = "postId")Long postId,
                                      @RequestParam("content") String content,
                                      @RequestParam("placeId") Long placeId,
                                      @RequestParam("groupId") Long groupId,
                                      @RequestParam("visitedDate") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate visitedDate,
                                      @RequestParam(value = "images", required = false) List<MultipartFile> images){
        Post post = postService.updatePost(postId, content, placeId, groupId, visitedDate, images);
        PostImgUrl postImgUrl = postImgUrlService.findImgUrlByPost(post);
        return PostResponseDto.from(post, post.getPlace(),
                new WriterDto(post.getMember().getMemberId(), post.getMember().getName(), post.getMember().getImgUrl()),
                likePostService.isExistsByMemberAndPost(post.getMember(),post), postImgUrl);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deletePost(@PathVariable(name = "postId")Long postId){
        postService.deletePost(postId);

        return "게시글이 삭제되었습니다.";
    }



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
