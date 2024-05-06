package com.example.oneinkedoneproject.filter;

import com.example.oneinkedoneproject.service.auth.JwtService;
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

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterUnitTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext(); // Ensure it is clean before each test

    }

    @Test
    @DisplayName("Valid Access Token 테스트")
    void testValidAccessToken() throws Exception {
        //mock request, response 객체 생성
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
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
        //mock request, response 객체 생성
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
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

        //응답 상태코드가 401인지 검증
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        //응답 메세지가 제대로 전달됐는지 검증
        assertThat(response.getContentAsString()).contains("Access token is invalid or expired");
    }

    @Test
    @DisplayName("Invalid Access Token Error 테스트")
    void testInvalidAccessTokenError() throws Exception {
        //mock request, response 객체 생성
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
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

        //응답 상태코드가 500인지 검증
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        //응답 메세지가 제대로 전달됐는지 검증
        assertThat(response.getContentAsString()).contains("Could not gain Authentication");
    }

    @Test
    @DisplayName("Token does not exist 테스트")
    void testNonExistingToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String BlankToken = "";
        //비어있는 토큰 전달
        request.addHeader("Authorization", BlankToken);

        jwtAuthenticationFilter.doFilterInternal(request, response, (req, res) -> {});

        //401 상태코드 실행
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        //응답 메세지가 제대로 전달됐는지 검증
        assertThat(response.getContentAsString()).contains("Unauthorized: No valid Bearer token provided");
    }

    @Test
    @DisplayName("Token does not start with Bearer 테스트")
    void testNonBearerToken() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String errorToken = "does not start with Bearer";
        request.addHeader("Authorization", errorToken);

        jwtAuthenticationFilter.doFilterInternal(request, response, (req, res) -> {});

        //401 상태코드 실행
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        //응답 메세지가 제대로 전달됐는지 검증
        assertThat(response.getContentAsString()).contains("Unauthorized: No valid Bearer token provided");
    }

    @Test
    @DisplayName("token claim 내에 이메일이 없을때 테스트 테스트")
    void testNonNoEmailInToken() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String errorToken = "Bearer "+"noEmail";
        request.addHeader("Authorization", errorToken);

        when(jwtService.extractUsername(errorToken)).thenReturn("");

        jwtAuthenticationFilter.doFilterInternal(request, response, (req, res) -> {});

        //401 상태코드 실행
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        //응답 메세지가 제대로 전달됐는지 검증
        assertThat(response.getContentAsString()).contains("Unauthorized: token does not contain a valid email");
    }

}