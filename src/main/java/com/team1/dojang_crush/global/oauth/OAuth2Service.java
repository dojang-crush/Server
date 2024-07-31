package com.team1.dojang_crush.global.oauth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.dojang_crush.domain.group.domain.Group;
import com.team1.dojang_crush.domain.group.repository.GroupRepository;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.dto.MemberRequestDTO;
import com.team1.dojang_crush.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    public String getAccessOAuth2Token(String clientId, String code) throws IOException {
        String reqURL = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id="+clientId
                +"&redirect_id="+redirectUri
                +"&code="+code
                +"&client_secret="+clientSecret;
        URL url=new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line="";
        String result="";

        while ((line=br.readLine())!=null){
            result+=line;
        }

        ObjectMapper objectMapper= new ObjectMapper();
        Map<String, Object> response = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
        });

        String accessToken=(String) response.get("access_token");

        return accessToken;

    }

    public MemberRequestDTO getMemberInfo(String accessToken) throws IOException{
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer "+accessToken);
        conn.setRequestProperty("Content-type"," application/x-www-form-urlencoded;charset=utf-8");

        //응답 받기
        int responseCode = conn.getResponseCode();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line="";
        String result="";

        while((line=br.readLine())!=null){
            result+=line;
        }

        //응답에서 사용자 정보 추출
        ObjectMapper objectMapper=new ObjectMapper();
        Map<String,Object> response = objectMapper.readValue(result, new TypeReference<Map<String, Object>>(){ });

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of("kakao", response);

        Member member = loginOrJoin(oAuth2UserInfo);

        MemberRequestDTO principal = MemberRequestDTO.builder()
                .nickname(member.getName())
                .email(member.getEmail())
                .imgUrl(member.getImgUrl())
                .build();

        return principal;


    }

    @Transactional
    private Member loginOrJoin(OAuth2UserInfo oauth2UserInfo){
        Group defaultGroup = groupRepository.findByGroupCode("2e7a5b12-3fae-403d-b01d-45c9e4c25b82");

        Optional<Member> member = memberRepository.findByEmail(oauth2UserInfo.email());
        if (member.isPresent()){
            return member.get();
        }else{
            Member newMember = oauth2UserInfo.toEntity(defaultGroup);
            return memberRepository.save(newMember);
        }


    }
}
