package com.example.oneinkedoneproject.filter;

import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import com.example.oneinkedoneproject.service.auth.JwtTokenProvider;

import com.example.oneinkedoneproject.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRefreshTokenFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String refreshToken;
        final String userEmail;

        // JWT 토큰이 존재하는지 확인. 존재 안하면 컷
        if(authHeader == null ||authHeader.startsWith(("Bearer "))){
            filterChain.doFilter(request, response);
            return;
        }
        //if (authHeader != null && authHeader.startsWith("Bearer "))
        else {//토큰이 존재한다면?
            //리프레쉬 토큰과 유저 이메일 추출
            refreshToken = authHeader.substring(7);
            userEmail = jwtService.extractUsername(refreshToken);

            //유저의 이메일이 존재하면서 유저가 인증받지 않았다면
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                try {
                    if (jwtService.isTokenValid(refreshToken, userDetails)) {
                        // 여기서 null 대신에 리프레시 토큰과 연관된 유저 정보를 얻는 방법을 사용할 수도 있습니다.
                        TokenInfo newTokenInfo = jwtService.generateTokenInfo(userDetails);

                        // 응답 헤더에 새로운 액세스, 리프레쉬 토큰 추가
                        response.setHeader("Authorization", "Bearer " + newTokenInfo.getAccessToken());
                        response.setHeader("Refresh-Token", "Bearer " + newTokenInfo.getRefreshToken());

                    } else {//리프레쉬 토큰이 valid하지 않다면 invalid 메세지 추가
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Refresh token is invalid or expired");
                        return;
                    }
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("Could not refresh tokens");
                    return;
                }
            }
            filterChain.doFilter(request, response);
        }
    }
}