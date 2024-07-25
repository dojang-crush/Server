package com.team1.dojang_crush.global.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.dojang_crush.domain.member.domain.dto.MemberRequestDTO;
import com.team1.dojang_crush.global.utils.JWTUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtils jwtUtils;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 정보 추출
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        MemberRequestDTO dto = MemberRequestDTO.builder()
                .nickname(principal.getUsername())
                .email(principal.getEmail())
                .imgUrl(principal.getImgUrl())
                .build();

        // 토큰 형성
        String token = jwtUtils.createToken(dto);
        Oauth2Response oauth2Response = new Oauth2Response("Bearer "+token);

        // http response 보내기
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(oauth2Response));

    }

    @AllArgsConstructor
    @Getter
    class Oauth2Response{
        private String data;
    }
}
