package com.team1.dojang_crush.global.oauth2;

import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.global.exception.CustomException;
import com.team1.dojang_crush.global.exception.ErrorCode;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2Attributes {
    private String oauth2UserEmail;
    private String oauth2UserName;
    private String oauth2UserProfileUrl;

    @Builder
    private OAuth2Attributes(String oauth2UserEmail, String oauth2UserName, String oauth2UserProfileUrl) {
        this.oauth2UserEmail = oauth2UserEmail;
        this.oauth2UserName = oauth2UserName;
        this.oauth2UserProfileUrl = oauth2UserProfileUrl;
    }

    public static OAuth2Attributes ofKakao(String registrationId, Map<String, Object> attributes) {
        if (!registrationId.equals("kakao"))
            throw new CustomException(ErrorCode.ILLEGAL_REGISTRATION_ID);

        Map<String, Object> accountAttributes = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profileAttributes = (Map<String, Object>) accountAttributes.get("profile");

        return OAuth2Attributes.builder()
                .oauth2UserEmail(String.valueOf(accountAttributes.get("email")))
                .oauth2UserName(String.valueOf(profileAttributes.get("nickname")))
                .oauth2UserProfileUrl(String.valueOf(profileAttributes.get("profile_image_url")))
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .email(oauth2UserEmail)
                .name(oauth2UserName)
                .imgUrl(oauth2UserProfileUrl)
                .build();
    }
}

