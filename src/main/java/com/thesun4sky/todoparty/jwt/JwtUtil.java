package com.thesun4sky.todoparty.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    // 1. secretKey를
     @Value("${jwt.secret.key}")
    private String secretKey;

     // 2. 이 알고리즘을 통하여
     private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

     // 3. key로 생성
     private Key key;

     @PostConstruct // bean 객체가 생성될 때 이 메서드가 호출됨
    private void init() {
         byte[] bytes = Base64.getDecoder().decode(secretKey); // secretKey를 기준으로 Base64로 디코딩
         key = Keys.hmacShaKeyFor(bytes);
     }

     // token 뽑아오는 것
    public String resolveToken(HttpServletRequest request) {
         String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
         if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
             return bearerToken.substring(7);
         }
         return null;
    }

    // key값으로 암호화한 애인지 검증
    public boolean validateToken(String token) {
         try {
             Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
             return true;
         } catch (SecurityException | MalformedJwtException e) {
             log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
         } catch (ExpiredJwtException e) {
             log.error("Expired JWT token, 만료된 JWT token 입니다.");
         } catch (UnsupportedJwtException e) {
             log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
         } catch (IllegalArgumentException e) {
             log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
         }
         return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String createToken(String username) {
        Date date = new Date();

        // 토큰 만료시간 60분
        long TOKNE_TIME = 60*60*1000;
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .setExpiration(new Date(date.getTime() + TOKNE_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }
}
