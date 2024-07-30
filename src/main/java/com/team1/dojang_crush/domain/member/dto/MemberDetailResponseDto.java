package com.team1.dojang_crush.domain.member.dto;

import com.team1.dojang_crush.domain.group.dto.GroupDetailResponseDto;
import com.team1.dojang_crush.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberDetailResponseDto {
    private Long userId;
    private String name;
    private String profileImageUrl;
    private String socialEmail;
    private String refreshToken;
    private GroupDetailResponseDto group;

    @Builder
    public MemberDetailResponseDto(Long userId, String name, String profileImageUrl, String socialEmail, String refreshToken, GroupDetailResponseDto group) {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.socialEmail = socialEmail;
        this.refreshToken = refreshToken;
        this.group = group;
    }

    public MemberDetailResponseDto(Member member) {
        this.userId = member.getMemberId();
        this.name = member.getName();
        this.profileImageUrl = member.getImgUrl();
        this.socialEmail = member.getEmail();
        this.refreshToken = member.getAccessToken();
        this.group = new GroupDetailResponseDto(member.getGroup());
    }
}
