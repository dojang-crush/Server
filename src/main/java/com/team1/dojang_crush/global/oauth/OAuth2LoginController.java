package com.team1.dojang_crush.global.oauth;

import com.team1.dojang_crush.domain.member.dto.MemberRequestDTO;
import com.team1.dojang_crush.global.exception.AppException;
import com.team1.dojang_crush.global.exception.ErrorCode;
import com.team1.dojang_crush.global.utils.JWTUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2/authorize")
public class OAuth2LoginController {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @AllArgsConstructor
    @Getter
    class Oauth2Response {
        private String data;
    }

    private final JWTUtils jwtUtils;
    private final OAuth2Service oAuth2Service;

    @GetMapping
    public ResponseEntity<Oauth2Response> oauth2Login(@RequestParam(name = "code")String code){
        String accessToken="";
        MemberRequestDTO dto=null;
        try{
            accessToken=oAuth2Service.getAccessOAuth2Token(clientId, code);
            dto=oAuth2Service.getMemberInfo(accessToken);

        }catch (IOException e){
            throw new AppException(ErrorCode.INVALID_REQUEST,"로그인 서버에 오류 발생 - 재시도 바람");
        }

        String token = "Bearer "+jwtUtils.createToken(dto);

        return ResponseEntity.ok().body(new Oauth2Response(token));

    }


}
