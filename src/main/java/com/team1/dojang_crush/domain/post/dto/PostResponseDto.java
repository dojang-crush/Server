package com.team1.dojang_crush.domain.post.dto;

import com.team1.dojang_crush.domain.place.domain.Place;
import com.team1.dojang_crush.domain.post.domain.Post;
import com.team1.dojang_crush.domain.postImgUrl.domain.PostImgUrl;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostResponseDto {

    private Long PostId;
    private String content;
    private String theme;
    private String placeTag;
    private LocalDateTime createdDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate visitedDate;
    private WriterDto writerDto;
    private Long groupId;
    private boolean postLike;
    private Integer countLike;
    private List<String> imageUrl;
    private RecentCommentDto recentCommentDto;



    public static PostResponseDto from(Post savedPost, Place place, WriterDto writerDto, boolean postLike, PostImgUrl postImgUrl, Integer countlIke) {

        // null 처리
        String content = savedPost.getPostContent() != null ? savedPost.getPostContent() : "";
        List<String> imgUrl = (postImgUrl != null && postImgUrl.getPostImgUrl() != null)
                ? postImgUrl.getPostImgUrl()
                : Collections.emptyList();

        return new PostResponseDto(
                savedPost.getPostId(),
                content,
                "#"+place.getTheme(),
                "#"+place.getPlaceName(),
                savedPost.getCreatedAt(),
                savedPost.getVisitedDate(),
                writerDto,
                savedPost.getMember().getGroup().getGroupId(),
                postLike,
                countlIke,
                imgUrl,
                null
        );
    }

    public static PostResponseDto from(Post savedPost, Place place, WriterDto writerDto, boolean postLike, RecentCommentDto recentCommentDto, PostImgUrl postImgUrl, Integer countLike) {

        // null 처리
        String content = savedPost.getPostContent() != null ? savedPost.getPostContent() : "";
        List<String> imgUrl = (postImgUrl != null && postImgUrl.getPostImgUrl() != null)
                ? postImgUrl.getPostImgUrl()
                : Collections.emptyList();


        return new PostResponseDto(
                savedPost.getPostId(),
                content,
                "#"+place.getTheme(),
                "#"+place.getPlaceName(),
                savedPost.getCreatedAt(),
                savedPost.getVisitedDate(),
                writerDto,
                savedPost.getMember().getGroup().getGroupId(),
                postLike,
                countLike,
                imgUrl,
                recentCommentDto
        );
    }
}
