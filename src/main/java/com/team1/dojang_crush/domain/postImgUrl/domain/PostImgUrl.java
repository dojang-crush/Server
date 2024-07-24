package com.team1.dojang_crush.domain.postImgUrl.domain;
import com.team1.dojang_crush.domain.BaseEntity;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.place.domain.Place;
import com.team1.dojang_crush.domain.post.domain.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class PostImgUrl extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "piu_id", updatable = false)
    private Long piuId;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "post_img_url")
    private List<String> postImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    @NotNull
    private Post post;

    @Builder
    public PostImgUrl(List<String> postImgUrl, Post post){
        this.postImgUrl = postImgUrl;
        this.post = post;
    }

    public void update(List<String> uploadedUrls) {
        this.postImgUrl = uploadedUrls;
    }
}
