package com.team1.dojang_crush.domain.member.domain;

import com.team1.dojang_crush.domain.BaseEntity;
import com.team1.dojang_crush.domain.comment.domain.Comment;
import com.team1.dojang_crush.domain.group.domain.Group;
import com.team1.dojang_crush.domain.group.repository.GroupRepository;
import com.team1.dojang_crush.domain.likePlace.domain.LikePlace;
import com.team1.dojang_crush.domain.likePost.domain.LikePost;
import com.team1.dojang_crush.domain.post.domain.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long memberId;

    @Column(name = "member_name")
    @NotNull
    private String name;

    @Column(name = "member_img_url")
    private String imgUrl;

    @Column(name = "is_lead")
    @NotNull
    private boolean isLead;

    @Column(name = "social_email")
    @NotBlank
    private String email;

    @Column(name = "access_token")
    private String accessToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    @NotNull
    private Group group;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LikePlace> likedPlaces;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LikePost> likedPosts;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> posts;

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Member(String name, String imgUrl, String email) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.email = email;
        this.isLead = false; // 기본값 설정
    }
}