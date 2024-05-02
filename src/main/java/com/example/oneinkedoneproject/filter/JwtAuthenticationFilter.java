package com.example.oneinkedoneproject.filter;

import com.example.oneinkedoneproject.service.auth.JwtTokenProvider;

import com.example.oneinkedoneproject.service.auth.JwtService;
import com.example.oneinkedoneproject.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain)
    throws ServletException, IOException {
        //final String token = getParseJwt(request.getHeader("Authorization"));
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // JWT 토큰이 존재하는지 확인. 존재 안하면 컼ㅅ.
        if(authHeader == null ||authHeader.startsWith(("Bearer "))){
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        //유저의 이메일이 존재하면서 유저가 인증받지 않았다면
        if(userEmail !=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            //토큰이 valid하다면
            if(jwtService.isTokenValid(jwt, userDetails)){

                //authentication 토큰 생성
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                //요청에 대한 세부사항을 토큰에 업데이트
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                //토큰을 security contextholder에 업데이트
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);

    }
    private String getParseJwt(final String headerAuth) {

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
