package com.team1.dojang_crush.domain.post.service;

import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.service.MemberService;
import com.team1.dojang_crush.domain.place.domain.Place;
import com.team1.dojang_crush.domain.place.service.PlaceService;
import com.team1.dojang_crush.domain.post.domain.Post;
import com.team1.dojang_crush.domain.post.dto.PostRequestDto;
import com.team1.dojang_crush.domain.post.repository.PostRepository;
import com.team1.dojang_crush.domain.postImgUrl.service.PostImgUrlService;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;
    private final PlaceService placeService;
    private final PostImgUrlService postImgUrlService;


    // 새로운 게시글 작성
    public Post createPost(Member member, String content, Long placeId, Long groupId, LocalDate visitedDate, List<MultipartFile> images) {
        
        Place place = placeService.findPlaceById(placeId);

        Post post = new Post(content,visitedDate,member,place);
        Post savedPost = postRepository.save(post);

        // 이미지가 있으면 생성
        if(images != null){
            try {
                postImgUrlService.createPostImgUrl(images,post);
            } catch (IOException e){
                e.printStackTrace();
                throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.");
            }

        }

        return savedPost;
    }



    // 그룹 게시글 찾기 (최신순)
    @Transactional(readOnly = true)
    public List<Post> findAllPosts(Long groupId) {
        // 그룹아이디로 해당 그룹 멤버들 찾음
        List<Member> members = memberService.findGroupMemberList(groupId);

        List<Post> posts = new ArrayList<>();
        // 멤버들로 post 찾음
        for(Member member : members){
            List<Post> memberPosts = postRepository.findAllByMember(member);
            for(Post post : memberPosts){
                posts.add(post);
            }
        }

        // 최신순으로 정렬해서 줘야함
        return posts.stream()
                .sorted((post1, post2) -> post2.getCreatedAt().compareTo(post1.getCreatedAt())) // 내림차순 정렬
                .collect(Collectors.toList());
    }


    // 그룹 날짜별 게시글 찾기
    @Transactional(readOnly = true)
    public List<Post> findPostsByDate(Long groupId, LocalDate visitedDate) {

        List<Post> posts = findAllPosts(groupId);

        // 날짜로
        List<Post> DatePosts = new ArrayList<>();
        for(Post post : posts){
            if(post.getVisitedDate().equals(visitedDate)) {
                DatePosts.add(post);
            }
        }

       return DatePosts;
    }

    // 그룹 날짜별 게시글 찾기
    @Transactional(readOnly = true)
    public List<Post> findPostsByDate1(Long groupId, LocalDate visitedDate) {
        return postRepository.findByGroupIdAndVisitedDate(groupId, visitedDate);
    }


    //postId로 게시글 찾기
    @Transactional(readOnly = true)
    public Post findPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFoundException("해당 id를 가진 게시글을 찾을 수 없습니다."+postId));
        return post;
    }

    // 게시글 수정
    public Post updatePost(Long postId, String content, Long placeId, Long groupId, LocalDate visitedDate, List<MultipartFile> images) {

        Post post = findPostById(postId);

        //게시글 내용 수정
        //Member member = memberService.findMemberById(dto.getMemberId());
        Place place = placeService.findPlaceById(placeId);
        post.update(content, place, visitedDate);

        if(images == null){
            // 이미지 삭제
            postImgUrlService.deletePostImgUrl(post);
        }
        else{
            //이미지 수정
            try {
                postImgUrlService.updatePostImgUrl(images,post);
            } catch (IOException e){
                e.printStackTrace();
                throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.");
            }
        }

        return post;
    }

    // 게시글 삭제
    public void deletePost(Long postId) {
        Post post = findPostById(postId);
        postRepository.delete(post);
    }


}