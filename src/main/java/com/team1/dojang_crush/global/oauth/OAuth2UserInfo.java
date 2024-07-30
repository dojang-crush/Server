package com.team1.dojang_crush.global.oauth;

import com.team1.dojang_crush.domain.group.domain.Group;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.global.exception.AppException;
import com.team1.dojang_crush.global.exception.ErrorCode;
import lombok.Builder;

import java.util.Map;

@Builder
public record OAuth2UserInfo(
        String nickname,
        String email,
        String image

) {
    /* 여러 서버 구별 */
    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attrubutes){
        return switch (registrationId){
            case "kakao" -> ofKakao(attrubutes);
            default -> throw new AppException(ErrorCode.NOT_FOUND_OAUTH2_REGISTRATION_ID,"제공하지 않는 OAUTH2 서버입니다");
        };

    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes){
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .nickname((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .image((String) profile.get("profile_image_url"))
                .build();
    }

    public Member toEntity(Group defaultGroup){
        return Member.builder()
                .name(nickname)
                .imgUrl(image)
                .isLead(false)
                .email(email)
                .group(defaultGroup)
                .build();

    }



}
