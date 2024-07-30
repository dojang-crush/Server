package com.team1.dojang_crush.domain.post.service;

import com.team1.dojang_crush.domain.comment.domain.Comment;
import com.team1.dojang_crush.domain.comment.service.CommentService;
import com.team1.dojang_crush.domain.likePost.service.LikePostService;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.service.MemberService;
import com.team1.dojang_crush.domain.place.domain.Place;
import com.team1.dojang_crush.domain.place.service.PlaceService;
import com.team1.dojang_crush.domain.post.domain.Post;
import com.team1.dojang_crush.domain.post.dto.PostResponseDto;
import com.team1.dojang_crush.domain.post.dto.RecentCommentDto;
import com.team1.dojang_crush.domain.post.dto.WriterDto;
import com.team1.dojang_crush.domain.post.repository.PostRepository;
import com.team1.dojang_crush.domain.postImgUrl.domain.PostImgUrl;
import com.team1.dojang_crush.domain.postImgUrl.service.PostImgUrlService;
import com.team1.dojang_crush.global.exception.AppException;
import com.team1.dojang_crush.global.exception.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
    private final LikePostService likePostService;
    private final CommentService commentService;


    // 새로운 게시글 작성
    public Post createPost(Member member, String content, Long placeId, Long groupId, LocalDate visitedDate, List<MultipartFile> images) {
        
        Place place = placeService.findPlaceById(placeId);

        Post post = new Post(content,visitedDate, member, place);
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



    // post 리스트를 dto 리스트로 변환해서 반환
    public List<PostResponseDto> postsToDtos(List<Post> posts, Member member, String type){

        List<PostResponseDto> list = new ArrayList<>();

        if(type.equals("date")){
            for(Post post : posts){
                //게시물 writer
                WriterDto postWriter = toWriterDto(post.getMember());

                PostImgUrl postImgUrl = postImgUrlService.findImgUrlByPost(post);
                Integer countLike = likePostService.countLikePost(post);

                PostResponseDto dto = PostResponseDto.from(post, post.getPlace(), postWriter,
                        likePostService.isExistsByMemberAndPost(member,post), postImgUrl, countLike);
                list.add(dto);
            }
            return list;
        }
        else{

            RecentCommentDto recentCommentDto;
            for(Post post : posts){
                //post의 젤 최근 댓글 찾기
                Comment recentComment = commentService.findRecentComment(post);
                if(recentComment == null){ // 댓글이 없는 경우
                    recentCommentDto = null;
                }
                else{
                    //댓글 writer
                    WriterDto commentWriter = toWriterDto(recentComment.getMember());
                    //commentDto
                    recentCommentDto = RecentCommentDto.from(recentComment,commentWriter);
                }

                //게시물 writer
                WriterDto postWriter = toWriterDto(post.getMember());

                PostImgUrl postImgUrl = postImgUrlService.findImgUrlByPost(post);
                Integer countLike = likePostService.countLikePost(post);

                PostResponseDto dto = PostResponseDto.from(post, post.getPlace(), postWriter,
                        likePostService.isExistsByMemberAndPost(member,post),
                        recentCommentDto, postImgUrl, countLike);
                list.add(dto);
            }
            return list;
        }
    }


    public WriterDto toWriterDto(Member member){
        return new WriterDto(member.getMemberId(),member.getName(),member.getImgUrl());
    }

    // 그룹 게시글 찾기 (최신순)
    @Transactional(readOnly = true)
    public List<Post> findAllPosts(Long groupId, Member member) {

        if(member.getGroup().getGroupId().equals(groupId)){
            // 그룹아이디로 해당 그룹 멤버들 찾음
            List<Member> members = memberService.findGroupMemberList(groupId);

            List<Post> posts = new ArrayList<>();
            // 멤버들로 post 찾음
            for(Member member1 : members){
                List<Post> memberPosts = postRepository.findAllByMember(member1);
                for(Post post : memberPosts){
                    posts.add(post);
                }
            }

            // 최신순으로 정렬
            return posts.stream()
                    .sorted((post1, post2) -> post2.getCreatedAt().compareTo(post1.getCreatedAt())) // 내림차순 정렬
                    .collect(Collectors.toList());
        }
        else{
            throw new AppException(ErrorCode.INVALID_REQUEST,"해당 그룹의 게시글 조회 권한이 없습니다.");
        }
    }


    // 그룹 날짜별 게시글 찾기
    @Transactional(readOnly = true)
    public List<Post> findPostsByDate1(Long groupId, LocalDate visitedDate, Member member) {
        if(member.getGroup().getGroupId().equals(groupId)){
            return postRepository.findByGroupIdAndVisitedDate(groupId, visitedDate);
        }
        else{
            throw new AppException(ErrorCode.INVALID_REQUEST,"해당 그룹의 게시글 조회 권한이 없습니다.");
        }
    }


    //postId로 게시글 찾기
    @Transactional(readOnly = true)
    public Post findPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFoundException("해당 id를 가진 게시글을 찾을 수 없습니다."+postId));
        return post;
    }


    // 게시글 수정
    public Post updatePost(Long postId, String content, Long placeId, Long groupId, LocalDate visitedDate, List<MultipartFile> images, Member member) {

        Post post = findPostById(postId);

        if(post.getMember().equals(member)){
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
        else{
            throw new AppException(ErrorCode.INVALID_REQUEST, "수정 권한이 없습니다.");
        }
    }

    // 게시글 삭제
    public void deletePost(Long postId, Member member) {
        Post post = findPostById(postId);

        if(post.getMember().equals(member)){
            postRepository.delete(post);
        }
        else{
            throw new AppException(ErrorCode.INVALID_REQUEST, "삭제 권한이 없습니다.");
        }
    }

    //그룹 게시글 월별 찾기
    public List<Post> findPostsByMonth(Long groupId, Long month, Member member) {

        List<Post> posts = findAllPosts(groupId, member);

        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getVisitedDate().getMonthValue() == month)
                .collect(Collectors.toList());

        return filteredPosts;
    }
}