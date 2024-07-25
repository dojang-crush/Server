package com.team1.dojang_crush.global.oauth.jwt;

import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.domain.member.repository.MemberRepository;
import com.team1.dojang_crush.global.exception.AppException;
import com.team1.dojang_crush.global.exception.ErrorCode;
import com.team1.dojang_crush.global.oauth.PrincipalDetails;
import com.team1.dojang_crush.global.utils.JWTUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private JWTUtils jwtUtils;
    private MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Authorization header 내용 가져오기
        String authorization = request.getHeader("Authorization");

        try {
            // 1. 토큰 유무 확인
            if (authorization==null || !authorization.startsWith("Bearer ")){
                throw new JWTAuthenticationException("토큰이 없습니다. 로그인을 다시 진행합니다.");
            }

            String token = authorization.split(" ")[1];


            // 2. 토큰 기한 만료 여부 확인
            if (jwtUtils.isExpired(token)){
                throw new JWTAuthenticationException("토큰의 기한이 만료되었습니다. 다시 로그인을 진행하십시오.");
            }

            // 3. spring security context에 UserDetails 저장하기
            String email = jwtUtils.getEmail(token);
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_USER,"사용자를 찾지 못했습니다"));
            PrincipalDetails principalDetails = new PrincipalDetails(member, new HashMap<String, Object>(), "kakao");

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }catch (JWTAuthenticationException e){
            System.out.println("JWTFilter.doFilterInternal : 에러 발생");
        }

        filterChain.doFilter(request,response);

    }
}
