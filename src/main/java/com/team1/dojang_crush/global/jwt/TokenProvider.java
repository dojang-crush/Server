package com.team1.dojang_crush.global.jwt;

import com.team1.dojang_crush.domain.member.domain.Member;
import com.team1.dojang_crush.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {

    @Value("${jwt.secret}")
    private String key;
    private SecretKey secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;
    private static final String KEY_ROLE = "role";
    private final JwtService jwtService;

    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
        log.info("비밀 키가 설정되었습니다.");
    }

    public String generateAccessToken(Authentication authentication) {
        String email = authentication.getName();
        log.info("이메일에 대한 액세스 토큰 생성 중: {}", email);
        Member member = jwtService.getOrCreateMemberByEmail(email);
        String currentToken = member.getAccessToken();
        if (currentToken != null && validateToken(currentToken)) {
            log.info("기존 토큰이 유효하여 현재 토큰을 반환합니다.");
            return currentToken;
        }
        String newAccessToken = generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
        jwtService.updateAccessToken(member, newAccessToken);
        log.info("새로운 액세스 토큰 생성: {}", newAccessToken);
        return newAccessToken;
    }

    private String generateToken(Authentication authentication, long expireTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        String token = Jwts.builder()
                .subject(authentication.getName())
                .claim(KEY_ROLE, authorities)
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();

        log.info("생성된 토큰: {}", token);
        return token;
    }

    public Authentication getAuthentication(String token) {
        log.info("토큰에 대한 인증 정보 가져오기: {}", token);
        Claims claims = parseClaims(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority(
                claims.get(KEY_ROLE).toString()));
    }

    public boolean validateToken(String token) {
        log.info("토큰 검증 중: {}", token);
        if (!StringUtils.hasText(token)) {
            log.warn("토큰이 비어 있거나 null입니다.");
            return false;
        }
        Claims claims = parseClaims(token);
        boolean isValid = claims.getExpiration().after(new Date());
        log.info("토큰 검증 결과: {}", isValid);
        return isValid;
    }

    private Claims parseClaims(String token) {
        try {
            log.info("토큰에서 클레임 파싱 중: {}", token);
            return Jwts.parser().setSigningKey(secretKey).build()
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.warn("토큰이 만료되었습니다: {}", e.getMessage());
            return e.getClaims();
        } catch (JwtException e) {
            log.error("토큰 클레임 파싱 오류: {}", e.getMessage());
            throw new TokenException(ErrorCode.INVALID_TOKEN);
        }
    }
}
