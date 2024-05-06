package com.example.oneinkedoneproject.interceptor;
import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import com.example.oneinkedoneproject.service.auth.JwtService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.AuthorityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LoginInterceptorUnitTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication auth;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private LoginInterceptor loginInterceptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        //userdetails 임시 생성
        UserDetails userDetails = new User("user@example.com", "password", AuthorityUtils.createAuthorityList("USER"));
        // 모의 객체 생성
        auth = mock(Authentication.class);
        //us authenticated 물어볼때 항상 true 반나
        when(auth.isAuthenticated()).thenReturn(true); // 여기에서 isAuthenticated() 메서드를 모의 설정
        //현재 인증된 유저 요구할때 항상 userDetails 반납
        when(auth.getPrincipal()).thenReturn(userDetails);
        //인증 요청시 인증 된 것을 리턴
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
    }

    @Test
    @DisplayName("로그인 성공시")
    void loginSuccessful() throws Exception {
        // Given 임시 이메일, 패스워드
        when(request.getParameter("email")).thenReturn("user@example.com");
        when(request.getParameter("password")).thenReturn("password");

        //임시 TokenInfo 객체
        when(jwtService.generateTokenInfo(any(UserDetails.class))).thenReturn(new TokenInfo("access-token", "access-token", "refresh-token"));

        // When
        boolean result = loginInterceptor.preHandle(request, response, null);

        // Then
        assertTrue(result);

        verify(response).setHeader("Authorization", "Bearer " + "access-token");
        verify(response).setHeader("Refresh-Token", "Bearer " + "refresh-token");
    }

    @Test
    @DisplayName("로그인 실패시")
    void loginFailed() throws Exception {
        // Given
        when(request.getParameter("email")).thenReturn("wrong@example.com");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        //잘못된 이메일과 패스워드 전달시 invalide credentionals 에러 설정
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When
        boolean result = loginInterceptor.preHandle(request, response, null);

        // Then
        Assertions.assertThat(result).isFalse();
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password");
    }
}