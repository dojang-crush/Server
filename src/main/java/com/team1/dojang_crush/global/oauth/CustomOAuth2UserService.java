package com.team1.dojang_crush.global.oauth;

import com.team1.dojang_crush.domain.group.domain.Group;
import com.team1.dojang_crush.domain.group.repository.GroupRepository;
import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    @Override
    @Transactional
    /* oauth2 라이브러리가 내부적으로 oauth2 로그인 처리 후 loadUser라는 메소드를 호출하는데,
    유효한 회원인지 확인하거나, 아니라면 회원가입을 진행하고, oauthuser 객체를 반환하는 메소드이다 */
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. user 정보 가져오기
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();
        // 2. 로그인 서버(kakao, google, naver...) 정보
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // 3. (** 잘 모르겠음) 로그인 서버의 고유한 name attribute : kakao의 경우 id
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();


        // 4. oauth2 서버별로 유저 정보 dto 생성
        OAuth2UserInfo oauth2UserInfo = OAuth2UserInfo.of(registrationId, attributes);
        // 5. db 확인 혹은 회원가입
        Member member = loginOrJoin(oauth2UserInfo);


        // 6. oauth2user 반환
        return new PrincipalDetails(member, attributes, userNameAttributeName);
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
