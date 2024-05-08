package com.example.oneinkedoneproject.filter;

import com.example.oneinkedoneproject.dto.auth.ErrorResult;
import com.example.oneinkedoneproject.service.auth.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.DataInput;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterUnitTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private ObjectMapper objectMapper= new ObjectMapper();
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
         request = new MockHttpServletRequest();
         response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext(); // Ensure it is clean before each test

    }

    @Test
    @DisplayName("Valid Access Token 테스트")
    void testValidAccessToken() throws Exception {
        String jwt  = "validToken";
        //리퀘스트 헤더에 액세스 토큰 추가
        request.addHeader("Authorization", "Bearer "+ jwt);


        //mock 객체 valid 토큰을 줬을 시 claim에서 이메일 반환 구현
        when(jwtService.extractUsername(jwt)).thenReturn("user@example.com");
        //유저디테일스 mock 객체
        UserDetails mockUserDetails = mock(UserDetails.class);
        //이메일로 유저디테일스를 가져오면 mockUserDetails 리턴 설정
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(mockUserDetails);
        //토큰 유효성 검사 모의
        when(jwtService.isTokenValid(jwt, mockUserDetails)).thenReturn(true);

        //필터 실행
        jwtAuthenticationFilter.doFilterInternal(request, response, (req, res) -> {});

        //인증객체가 null이 아닌지 검증
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        //인증 객체가 인증됐는지 검증함
        assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()).isTrue();

        //인증 객체의 상세 검증
    }

    @Test
    @DisplayName("Invalid Access Token 테스트")
    void testInvalidAccessToken() throws Exception {
        String jwt = "invalidToken";
        //invalid한 토큰 생성
        request.addHeader("Authorization", "Bearer "+jwt);

        //invalid한 토큰에서 이메일 추출 모의
        when(jwtService.extractUsername(jwt)).thenReturn("user@example.com");
        //유저 디테일스 목 객체 생성
        UserDetails mockUserDetails = mock(UserDetails.class);
        //유저 디테일스 목 객체 반환
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(mockUserDetails);
        //istokenValid에서. false 반환하도록 모의
        when(jwtService.isTokenValid(jwt, mockUserDetails)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, (req, res) -> {});

        ErrorResult errorResult = objectMapper.readValue(response.getContentAsString(), ErrorResult.class);
        //응답 상태코드가 401인지 검증
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        //응답 메세지가 제대로 전달됐는지 검증
        assertEquals("JWT Access Token Error", errorResult.getError());
        assertEquals("Access token is invalid or expired",  errorResult.getMessage());
    }

    @Test
    @DisplayName("Invalid Access Token Error 테스트")
    void testInvalidAccessTokenError() throws Exception {
        String errorToken = "errorToken";
        //invalid한 토큰 생성
        request.addHeader("Authorization", "Bearer "+ errorToken);

        //invalid한 토큰에서 이메일 추출 모의
        when(jwtService.extractUsername(errorToken)).thenReturn("user@example.com");
        //유저 디테일스 목 객체 생성
        UserDetails mockUserDetails = mock(UserDetails.class);
        //유저 디테일스 목 객체 반환
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(mockUserDetails);
        //istokenValid에서. false 반환하도록 모의
        when(jwtService.isTokenValid(errorToken, mockUserDetails)).thenThrow(new RuntimeException("Unexpected error"));
        jwtAuthenticationFilter.doFilterInternal(request, response, (req, res) -> {});

        ErrorResult errorResult = objectMapper.readValue(response.getContentAsString(), ErrorResult.class);
        //응답 상태코드가 500인지 검증
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
        //응답 메세지가 제대로 전달됐는지 검증
        assertEquals("JWT Access Token Error", errorResult.getError());
        assertEquals("Unexpected error",  errorResult.getMessage());
    }

    @Test
    @DisplayName("Token does not exist 테스트")
    void testNonExistingToken() throws Exception {
        //비어있는 토큰 전달 아예 x
        jwtAuthenticationFilter.doFilterInternal(request, response, (req, res) -> {});
        //401 상태코드 실행
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        //응답 메세지가 제대로 전달됐는지 검증
        ErrorResult errorResult = objectMapper.readValue(response.getContentAsString(), ErrorResult.class);
        assertEquals("JWT Access Token Error", errorResult.getError());
        assertEquals("No token provided",  errorResult.getMessage());
    }

    @Test
    @DisplayName("Token does not start with Bearer 테스트")
    void testNonBearerToken() throws Exception{
        String errorToken = "does not start with Bearer";
        request.addHeader("Authorization", errorToken);

        jwtAuthenticationFilter.doFilterInternal(request, response, (req, res) -> {});
        ErrorResult errorResult = objectMapper.readValue(response.getContentAsString(), ErrorResult.class);
        //401 상태코드 실행
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        //응답 메세지가 제대로 전달됐는지 검증
        assertEquals("JWT Access Token Error", errorResult.getError());
        assertEquals("No valid Bearer token provided",  errorResult.getMessage());
    }

    @Test
    @DisplayName("token claim 내에 이메일이 없을때 테스트 테스트")
    void testNonNoEmailInToken() throws Exception{
        String errorToken = "Bearer "+"noEmail";
        request.addHeader("Authorization", errorToken);

        when(jwtService.extractUsername(errorToken)).thenReturn("");

        jwtAuthenticationFilter.doFilterInternal(request, response, (req, res) -> {});
        ErrorResult errorResult = objectMapper.readValue(response.getContentAsString(), ErrorResult.class);

        //401 상태코드 실행
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        //응답 메세지가 제대로 전달됐는지 검증
        assertEquals("JWT Access Token Error", errorResult.getError());
        assertEquals("token does not contain a valid email",  errorResult.getMessage());
    }

}