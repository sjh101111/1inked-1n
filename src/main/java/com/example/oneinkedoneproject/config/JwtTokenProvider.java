package com.example.oneinkedoneproject.config;

import com.example.oneinkedoneproject.dto.TokenInfo;
import io.jsonwebtoken.*;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.http.HttpStatus;


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

        long now = (new Date()).getTime();
        Date accessTokenExpirationTime = new Date(now+validityInMilliseconds);//액세스 토큰 만기 시간 설정
        Date refreshTokenExpirationTime = new Date(now+validityInMilliseconds*3);//리프레쉬 토큰 만기 시간 설정
        //액세스 토큰 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth" , authorities)
                .setIssuedAt(new Date())
                .setExpiration(accessTokenExpirationTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        //리프레시 토큰 생성
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .claim("type","refresh")
                .setExpiration(refreshTokenExpirationTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer") // 토큰 타입 설정
                .accessToken(accessToken) // 액세스 토큰 설정
                .refreshToken(refreshToken) // 리프레시 토큰 설정
                .build(); // TokenInfo 객체 생성

    }

    //액세스 토큰을 파싱하여 유저의 auth정보를 빼오는 메소드
    private Claims parseClaims(String accessToken){
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }

    }

    // 토큰에서 인증 정보 추출
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        if(claims.get("auth") == null){
            throw new RuntimeException("권한 정보가 없음");
        }
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);

    }

    // 토큰에서 사용자 이름 추출
    private String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰의 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}