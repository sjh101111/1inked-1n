package com.example.oneinkedoneproject.service.auth;

import static org.junit.jupiter.api.Assertions.*;

import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;

@SpringBootTest
public class JwtServiceUnitTest {

    @InjectMocks
    private JwtService jwtService;

    @Value("${jwt.secret}")
    private String secretKey;// 가정된 시크릿 키

    private User userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);

        userDetails = new User("username", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    @DisplayName("TokenInfo 생성")
    void generateTokenInfoTest() {
        TokenInfo tokenInfo = jwtService.generateTokenInfo(userDetails);
        assertNotNull(tokenInfo);
        assertTrue(jwtService.isTokenValid(tokenInfo.getAccessToken(), userDetails));
    }


    @Test
    @DisplayName("AcessToken 생성 테스트")
    void generateTokenTest() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    @DisplayName("Token 만료 test")
    void isTokenExpiredTest() {


        String expiredToken = Jwts.builder()
                .setSubject("username")
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24))
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .compact();

        assertThrows(
                io.jsonwebtoken.ExpiredJwtException.class,
                () -> jwtService.isTokenValid(expiredToken, userDetails)
        );
    }

    @Test
    @DisplayName("TokenValidation 테스트")
    void tokenValidationTest() {
        TokenInfo tokenInfo = jwtService.generateTokenInfo(userDetails);
        assertTrue(jwtService.isTokenValid(tokenInfo.getAccessToken(), userDetails));

        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VybmFtZSJ9.bWFrZXN1cmV0b2t1cHlvdXJrZXlz";

        assertThrows(
                io.jsonwebtoken.security.SecurityException.class,
                () -> jwtService.isTokenValid(invalidToken, userDetails)
        );
    }

    @Test
    @DisplayName("유저네임 추출 테스트")
    void extractUsernameTest() {
        TokenInfo tokenInfo = jwtService.generateTokenInfo(userDetails);
        assertEquals("username", jwtService.extractUsername(tokenInfo.getAccessToken()));
    }

}
