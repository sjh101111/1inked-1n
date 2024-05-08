package com.example.oneinkedoneproject.filter;

import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import com.example.oneinkedoneproject.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
            throws IOException, ServletException {


        //경로에 /refresh가 없다면 다음 필터체인으로 이동
        final String requestURI = request.getRequestURI();

        if(!"/refresh".equals(requestURI)){
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Refresh-Token");

        if (authHeader == null || !authHeader.startsWith(("Bearer "))) {//일단 토큰은 존재
            unauthorizedResponse(response, "Unauthorized: No valid refresh Bearer token provided");
            return;
        }
        String refreshToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null) {
            unauthorizedResponse(response, "Unauthorized: token does not contain a valid email");
            return;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        try {
            if (!jwtService.isTokenValid(refreshToken, userDetails)) {//토큰이 valid하지 않을 때
                unauthorizedResponse(response, "Refresh token is invalid or expired");
                return;
            }
        }catch(Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Could not gain Authentication");
            return;
        }

        TokenInfo newTokenInfo = jwtService.generateTokenInfo(userDetails);//새로운 토큰을 생성 후 리스폰스 헤더에 설정
        response.setHeader("Authorization", "Bearer " + newTokenInfo.getAccessToken());
        response.setHeader("Refresh-Token", "Bearer " + newTokenInfo.getRefreshToken());
    }
    private void unauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401 상태코드 리턴
        response.getWriter().write(message);
        response.getWriter().flush();
    }
}
