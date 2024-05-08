package com.example.oneinkedoneproject.filter;

import com.example.oneinkedoneproject.dto.auth.ErrorResult;
import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import com.example.oneinkedoneproject.service.auth.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.DataInput;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class JwTRefreshTokenFilterUnitTest {
    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtRefreshTokenFilter jwtRefreshTokenFilter;

    private ObjectMapper objectMapper= new ObjectMapper();
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setRequestURI("/refresh");
        SecurityContextHolder.clearContext(); // Ensure it is clean before each test
    }

    @Test
    @DisplayName("경로가 /refresh 가 아닐 때 필터체인 호출 확인")
    void testPassToNextFilterIfNotRefreshPath() throws Exception {
        // Set up a request that does not match the "/refresh" path
        request.setRequestURI("/api/data");

        // Execute the filter method
        jwtRefreshTokenFilter.doFilterInternal(request, response, filterChain);

        // Verify that the filter chain's doFilter method was called, indicating that the request was passed along the chain
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("경로가 /refresh일 때 필터체인 비호출 확인")
    void testNotPassToNextFilterIfRefreshPath() throws Exception {
        // Set up a request that matches the "/refresh" path
        request.setRequestURI("/refresh");

        // Execute the filter method
        jwtRefreshTokenFilter.doFilterInternal(request, response, filterChain);

        // Verify that the filter chain's doFilter method was not called
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("토큰이 존재하지 않을떄")
    void testNoTokenProvided() throws Exception {//아무 값도 없는 reauest, response 전달
        jwtRefreshTokenFilter.doFilterInternal(request, response, mock(FilterChain.class));

        ErrorResult errorResult = objectMapper.readValue(response.getContentAsString(), ErrorResult.class);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("JWT refresh Token Error", errorResult.getError());
        assertEquals("No token provided",  errorResult.getMessage());
    }

    @Test
    @DisplayName("잘못된 토큰 포멧")
    void testInvalidTokenFormat() throws Exception {
        request.addHeader("Refresh-Token", "InvalidTokenFormat");//Bearer로 시작하지 않는 토큰
        jwtRefreshTokenFilter.doFilterInternal(request, response, mock(FilterChain.class));


        ErrorResult errorResult = objectMapper.readValue(response.getContentAsString(), ErrorResult.class);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("JWT refresh Token Error", errorResult.getError());
        assertEquals("No valid Bearer token provided",  errorResult.getMessage());
    }

    @Test
    @DisplayName("유저이메일이 존재하지 않는 토큰")
    void testTokenWithoutValidEmail() throws Exception {
        request.addHeader("Refresh-Token", "Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn(null);//이메일이 추출되지 않음
        jwtRefreshTokenFilter.doFilterInternal(request, response, mock(FilterChain.class));

        ErrorResult errorResult = objectMapper.readValue(response.getContentAsString(), ErrorResult.class);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("JWT refresh Token Error", errorResult.getError());
        assertEquals("token does not contain a valid email",  errorResult.getMessage());
        }

    @Test
    @DisplayName("invalid한 refresh token")
    void testInvalidRefreshToken() throws Exception {
        request.addHeader("Refresh-Token", "Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("user@example.com");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid("validToken", userDetails)).thenReturn(false);//valid 메소드에서 false 반환

        jwtRefreshTokenFilter.doFilterInternal(request, response,  mock(FilterChain.class));

        ErrorResult errorResult = objectMapper.readValue(response.getContentAsString(), ErrorResult.class);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("JWT refresh Token Error", errorResult.getError());
        assertEquals("Refresh token is invalid or expired",  errorResult.getMessage());

    }

    @Test
    @DisplayName("invalid한 Exception 테스트")
    void testInvalidRefreshTokenException() throws Exception {
        request.addHeader("Refresh-Token", "Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("user@example.com");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid("validToken", userDetails)).thenThrow(new UnsupportedJwtException("Unsupported JWT Token"));

        jwtRefreshTokenFilter.doFilterInternal(request, response, mock(FilterChain.class));
        ErrorResult errorResult = objectMapper.readValue(response.getContentAsString(), ErrorResult.class);
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals("JWT refresh Token Error", errorResult.getError());
        assertEquals("Could not gain Authentication",  errorResult.getMessage());
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