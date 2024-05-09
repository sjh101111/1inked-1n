package com.example.oneinkedoneproject.filter;

import com.example.oneinkedoneproject.dto.auth.ErrorResult;
import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import com.example.oneinkedoneproject.service.auth.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    private HttpServletResponse response;
    private final ObjectMapper objectMapper= new ObjectMapper();
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse responseParam,
            @NonNull FilterChain filterChain)
            throws IOException, ServletException {

        this.response = responseParam;

        //경로에 /refresh가 없다면 다음 필터체인으로 이동
        final String requestURI = request.getRequestURI();

        if(!"/refresh".equals(requestURI)){

            filterChain.doFilter(request, responseParam);

            return;
        }

        final String authHeader = request.getHeader("Refresh-Token");


        if(authHeader == null){ // 액세스 토큰이  아예 존재하지 않거나, Bearer로 시작하지 않고있다면
            createUnauthorizedResponse(HttpServletResponse.SC_UNAUTHORIZED,
                    "JWT refresh Token Error", "No token provided");

            return;
        }

        if(!authHeader.startsWith(("Bearer "))){ // 액세스 토큰이  아예 존재하지 않거나, Bearer로 시작하지 않고있다면
            createUnauthorizedResponse(HttpServletResponse.SC_UNAUTHORIZED,
                    "JWT refresh Token Error", "No valid Bearer token provided");
            return;

        }

        String refreshToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null) {
            createUnauthorizedResponse(HttpServletResponse.SC_UNAUTHORIZED,
                    "JWT refresh Token Error", "token does not contain a valid email");
            return;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        try {
            if (!jwtService.isTokenValid(refreshToken, userDetails)) {//토큰이 valid하지 않을 때
                createUnauthorizedResponse(HttpServletResponse.SC_UNAUTHORIZED,
                        "JWT refresh Token Error", "Refresh token is invalid or expired");
                return;
            }
        }catch(Exception e){
            createUnauthorizedResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "JWT refresh Token Error", "Could not gain Authentication");
            return;
        }

        TokenInfo newTokenInfo = jwtService.generateTokenInfo(userDetails);//새로운 토큰을 생성 후 리스폰스 헤더에 설정
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Authorization", "Bearer " + newTokenInfo.getAccessToken());
        response.setHeader("Refresh-Token", "Bearer " + newTokenInfo.getRefreshToken());
    }

    private void createUnauthorizedResponse(int httpResponse, String error, String message) throws IOException {
        response.setStatus(httpResponse);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(new ErrorResult(error,  message)));
        response.getWriter().flush();
    }
}
