package com.team1.dojang_crush.domain.auth.kakoLogin;

import com.team1.dojang_crush.domain.auth.dto.LoginResponseDTO;
import com.team1.dojang_crush.domain.member.dto.MemberRequestDTO;
import com.team1.dojang_crush.domain.member.service.MemberService;
import com.team1.dojang_crush.global.exception.CustomException;
import com.team1.dojang_crush.global.exception.ErrorCode;
import com.team1.dojang_crush.global.utils.JWTUtils;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    private final KaKaoLoginService kaKaoLoginService;
    private final JWTUtils jwtUtils;
    private final MemberService memberService;

    @GetMapping("/login")
    public ResponseEntity<String> kakaoLogin(){
        String oauth2_url="https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+clientId+"&redirect_uri="+redirectUri;
        return ResponseEntity.ok().body(oauth2_url);
    }

    @GetMapping("/oauth2/kakao")
    public ResponseEntity<LoginResponseDTO> kakaoLoginCallback(@RequestParam("code") String code){
        //클라이언트 서버에서 보내온 코드로 사용자 정보 확인
        String accessToken="";
        MemberRequestDTO userInfo=null;
        try {
            accessToken = kaKaoLoginService.getAccessTokenFromKakao(clientId, code);
            userInfo = kaKaoLoginService.getUserInfo(accessToken);
        }catch (IOException e){
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        //사용자 엑세스 토큰과 리프레시 토큰 생성
        String userAccessToken = "Bearer "+jwtUtils.createToken(userInfo);
        String userRefreshToken= jwtUtils.createRefreshToken(userInfo);
        long memberId = memberService.findMemberIdByEmail(userInfo.getEmail());

        LoginResponseDTO responseDto = LoginResponseDTO.builder()
                .memberInfo(userInfo)
//                .memberId(memberId)
                .accessToken(userAccessToken)
                .refreshToken(userRefreshToken)
                .build();
        return ResponseEntity.ok().body(responseDto);
    }
}
