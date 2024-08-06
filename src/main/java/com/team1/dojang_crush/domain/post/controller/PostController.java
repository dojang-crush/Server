package com.team1.dojang_crush.domain.post.controller;

import com.team1.dojang_crush.domain.likePost.service.LikePostService;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.post.domain.Post;
import com.team1.dojang_crush.domain.post.dto.AllPostResponseDto;
import com.team1.dojang_crush.domain.post.dto.PostResponseDto;
import com.team1.dojang_crush.domain.post.dto.WriterDto;
import com.team1.dojang_crush.domain.post.service.PostService;
import com.team1.dojang_crush.domain.postImgUrl.domain.PostImgUrl;
import com.team1.dojang_crush.domain.postImgUrl.service.PostImgUrlService;
import com.team1.dojang_crush.global.oauth.AuthUser;
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
    private final PostImgUrlService postImgUrlService;

    // 게시글 생성
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public PostResponseDto createPost(@AuthUser Member member,
                                      @RequestParam("content") String content,
                                      @RequestParam("placeId") Long placeId,
                                      @RequestParam("groupId") Long groupId,
                                      @RequestParam("visitedDate") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate visitedDate,
                                      @RequestParam(value = "images", required = false) List<MultipartFile> images){

        Post savedPost = postService.createPost(member, content,placeId,groupId,visitedDate, images);
        PostImgUrl postImgUrl = postImgUrlService.findImgUrlByPost(savedPost);
        Integer countLike = likePostService.countLikePost(savedPost);

        return PostResponseDto.from(savedPost, savedPost.getPlace(),
                new WriterDto(member.getMemberId(), member.getName(), member.getImgUrl()),
                likePostService.isExistsByMemberAndPost(member,savedPost),postImgUrl, countLike);
    }


    // 그룹 전체 게시글 조회
    @GetMapping("/all")
    @ResponseStatus(value = HttpStatus.OK)
    public AllPostResponseDto getAllPosts(@RequestParam("page")int page,
                                          @AuthUser Member member){


        // 그룹의 모든 post 찾기
        List<Post> posts = postService.findAllPosts(member, page);

        List<PostResponseDto> list = postService.postsToDtos(posts, member, "all");
        return new AllPostResponseDto(member.getGroup().getGroupId(), list, list.size());
    }


    // 그룹 날짜별 게시글 조회
    @GetMapping("/date")
    @ResponseStatus(value = HttpStatus.OK)
    public AllPostResponseDto getDatePosts(@RequestParam("visitedDate") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate visitedDate,
                                           @AuthUser Member member){

        // 그룹과 날짜로 posts 찾기
        List<Post> posts = postService.findPostsByDate1(visitedDate, member);

        List<PostResponseDto> list = postService.postsToDtos(posts,member, "date");
        return new AllPostResponseDto(member.getGroup().getGroupId(), list, list.size());
    }


    // 그룹 월별 게시글 조회
    @GetMapping("/year/{year}/month/{month}")
    @ResponseStatus(value = HttpStatus.OK)
    public AllPostResponseDto getMonthPosts(@PathVariable(name = "year")Long year,
                                            @PathVariable(name = "month")Long month,
                                            @AuthUser Member member){

        // 그룹과 월로 posts 찾기
        List<Post> posts = postService.findPostsByMonth(year, month, member);

        List<PostResponseDto> list = postService.postsToDtos(posts,member, "date");
        return new AllPostResponseDto(member.getGroup().getGroupId(), list, list.size());
    }



    // 게시글 1개 조회
    @GetMapping("/{postId}")
    @ResponseStatus(value = HttpStatus.OK)
    public PostResponseDto getOnePost(@PathVariable(name = "postId")Long postId,
                                      @AuthUser Member member){

        Post post = postService.findOnePost(postId, member);
        PostImgUrl postImgUrl = postImgUrlService.findImgUrlByPost(post);

        Integer countLike = likePostService.countLikePost(post);

        return PostResponseDto.from(post, post.getPlace(),
                new WriterDto(post.getMember().getMemberId(), post.getMember().getName(), post.getMember().getImgUrl()),
                likePostService.isExistsByMemberAndPost(member,post), postImgUrl, countLike);
    }

    // 게시글 수정
    @PatchMapping("/{postId}")
    @ResponseStatus(value = HttpStatus.OK)
    public PostResponseDto updatePost(@PathVariable(name = "postId")Long postId,
                                      @RequestParam("content") String content,
                                      @RequestParam("placeId") Long placeId,
                                      @RequestParam("groupId") Long groupId,
                                      @RequestParam("visitedDate") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate visitedDate,
                                      @RequestParam(value = "images", required = false) List<MultipartFile> images,
                                      @AuthUser Member member){

        Post post = postService.updatePost(postId, content, placeId, groupId, visitedDate, images, member);
        PostImgUrl postImgUrl = postImgUrlService.findImgUrlByPost(post);
        Integer countLike = likePostService.countLikePost(post);

        return PostResponseDto.from(post, post.getPlace(),
                new WriterDto(post.getMember().getMemberId(), post.getMember().getName(), post.getMember().getImgUrl()),
                likePostService.isExistsByMemberAndPost(post.getMember(),post), postImgUrl, countLike);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deletePost(@PathVariable(name = "postId")Long postId,
                             @AuthUser Member member){
        postService.deletePost(postId, member);

        return "게시글이 삭제되었습니다.";
    }
}