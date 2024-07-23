package com.team1.dojang_crush.domain.auth.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        if (this.attribute == null) {
            throw new IllegalStateException("Attributes are not initialized.");
        }
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("nickname").toString();
    }

    @Override
    public String getProfileUrl() {
        return attribute.get("picture").toString();
    }
}
