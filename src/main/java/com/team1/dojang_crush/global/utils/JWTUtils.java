package com.team1.dojang_crush.global.utils;

import com.team1.dojang_crush.domain.member.domain.dto.MemberRequestDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtils {
    private Key key;
    private Key rKey;

    @Autowired
    public JWTUtils(@Value("${spring.jwt.secret}")String secretKey, @Value("${spring.jwt.refresh-secret}")String refreshKey ){
        byte[] decodeKey = Decoders.BASE64.decode(secretKey);
        byte[] decodeRKey = Decoders.BASE64.decode(refreshKey);
        key= Keys.hmacShaKeyFor(decodeKey);
        rKey=Keys.hmacShaKeyFor(decodeRKey);
    }

    public String createToken(MemberRequestDTO dto){
        Claims claims=Jwts.claims();
        claims.put("name",dto.getNickname());
        claims.put("email",dto.getEmail());
        claims.put("imgUrl",dto.getImgUrl()); /** 이미지 url 추가*/

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+60*60*2*1000*200))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(MemberRequestDTO dto){
        Claims claims= Jwts.claims();
        claims.put("nickname",dto.getNickname());
        claims.put("email",dto.getEmail());
        claims.put("imgUrl",dto.getImgUrl()); /** 이미지 url 추가*/

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+60*60*24*14*1000))
                .signWith(rKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isExpired(String token){

        return Jwts.parserBuilder().setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public String getEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    public String getNickname(String token) {
        return Jwts.parserBuilder().setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("nickname", String.class);
    }

    public String getImgUrl(String token) {
        return Jwts.parserBuilder().setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("imgUrl", String.class);
    }
}