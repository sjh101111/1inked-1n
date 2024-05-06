package com.example.oneinkedoneproject.filter;

import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import com.example.oneinkedoneproject.service.auth.JwtService;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwTRefreshTokenFilterUnitTest {
    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtRefreshTokenFilter jwtRefreshTokenFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("토큰이 존재하지 않을떄")
    void testNoTokenProvided() throws Exception {
        //아무 값도 없는 reauest, response 전달
        jwtRefreshTokenFilter.doFilterInternal(request, response, mock(FilterChain.class));
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Unauthorized: No valid Bearer token provided", response.getContentAsString());
    }

    @Test
    @DisplayName("잘못된 토큰 포멧")
    void testInvalidTokenFormat() throws Exception {
        request.addHeader("Refresh-Token", "InvalidTokenFormat");//Bearer로 시작하지 않는 토큰
        jwtRefreshTokenFilter.doFilterInternal(request, response, mock(FilterChain.class));
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Unauthorized: No valid Bearer token provided", response.getContentAsString());
    }

    @Test
    @DisplayName("유저이메일이 존재하지 않는 토큰")
    void testTokenWithoutValidEmail() throws Exception {
        request.addHeader("Refresh-Token", "Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn(null);//이메일이 추출되지 않음
        jwtRefreshTokenFilter.doFilterInternal(request, response, mock(FilterChain.class));
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Unauthorized: token does not contain a valid email", response.getContentAsString());
    }

    @Test
    @DisplayName("invalid한 refresh token")
    void testInvalidRefreshToken() throws Exception {
        request.addHeader("Refresh-Token", "Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("user@example.com");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid("validToken", userDetails)).thenReturn(false);//valid 메소드에서 false 반환

        jwtRefreshTokenFilter.doFilterInternal(request, response, mock(FilterChain.class));
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Refresh token is invalid or expired", response.getContentAsString());
    }

    @Test
    @DisplayName("invalid한 Exception 테스트")
    void testInvalidRefreshTokenException() throws Exception {
        request.addHeader("Refresh-Token", "Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("user@example.com");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid("validToken", userDetails)).thenThrow(new UnsupportedJwtException("Unsupported JWT Token"));//valid 메소드에서 false 반환

        jwtRefreshTokenFilter.doFilterInternal(request, response, mock(FilterChain.class));
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals("Could not gain Authentication", response.getContentAsString());
    }

    @Test
    @DisplayName("Valid Refresh Token")
    void testValidRefreshToken() throws Exception {
        request.addHeader("Refresh-Token", "Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("user@example.com");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid("validToken", userDetails)).thenReturn(true);//리프레쉬 토큰 인증 성공 모의

        TokenInfo tokenInfo = new TokenInfo("Bearer", "newAccessToken", "newRefreshToken");
        when(jwtService.generateTokenInfo(userDetails)).thenReturn(tokenInfo);
        jwtRefreshTokenFilter.doFilterInternal(request, response, mock(FilterChain.class));

        assertEquals("Bearer newAccessToken", response.getHeader("Authorization"));
        assertEquals("Bearer newRefreshToken", response.getHeader("Refresh-Token"));
    }
}