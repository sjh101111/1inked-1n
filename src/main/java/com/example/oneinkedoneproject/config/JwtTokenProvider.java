package com.example.oneinkedoneproject.config;

import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.TokenInfo;
import com.example.oneinkedoneproject.repository.UserRepository;
import io.jsonwebtoken.*;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class JwtTokenProvider {
    // JWT 비밀키와 토큰 유효 시간 설정
    private final Key key;

    private long validityInMilliseconds = 3600000; // 토큰 만료 시간(1시간)

    // 사용자 이름을 기반으로 JWT 토큰 생성
    public JwtTokenProvider(@Value("${jwt.secret}") String superSecretKey){
        byte[] keyBytes = Decoders.BASE64.decode(superSecretKey); //슈퍼시크릿키를 바이트 배열 형태로 디코딩
        this.key = Keys.hmacShaKeyFor(keyBytes);// HMAX SHA 알고리즘으로 암호 키를 생성
    }

    //사용자의 정보와 키를 사용하여 JWT 토큰 발행
    public TokenInfo createToken(Authentication authentication) {


        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")); //사용자의 권한 목록을 콤마로 구분
        /*
        if (authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
        }
         */
        User curUser = (User)authentication.getPrincipal();

        long now = (new Date()).getTime();
        Date accessTokenExpirationTime = new Date(now+validityInMilliseconds);//액세스 토큰 만기 시간 설정(1시간)
        Date refreshTokenExpirationTime = new Date(now+validityInMilliseconds*24);//리프레쉬 토큰 만기 시간 설정 3일

        //액세스 토큰 생성. 액세스 토큰으로 인증을 하고
        String accessToken = Jwts.builder()
                .setSubject("Authorization")
                .claim("id", curUser.getId())
                .claim("name", curUser.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(accessTokenExpirationTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        //리프레시 토큰 생성 액세스 토큰이 만료될 시 리프레시 토큰으로 새로운 액세스 토큰을 가져온다.
        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(refreshTokenExpirationTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer") // 토큰 타입 설정
                .accessToken(accessToken) // 액세스 토큰 설정
                .refreshToken(refreshToken) // 리프레시 토큰 설정
                .build(); // TokenInfo 객체 생성

    }

    //액세스 토큰을 파싱하여 유저의 claim 페이로드 정보를 가져오는 메소드
    private Claims parseClaims(String accessToken){
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }

    }

    // 토큰에서 사용자 ID 추출
    // 토큰에서 사용자 이름 추출
    /*

    private String getUsername(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().get("");
    }
    */

    // 토큰의 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }
}