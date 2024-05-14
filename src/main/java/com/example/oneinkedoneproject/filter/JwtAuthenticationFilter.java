package com.example.oneinkedoneproject.filter;
import com.example.oneinkedoneproject.dto.auth.ErrorResult;
import com.example.oneinkedoneproject.service.auth.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private HttpServletResponse response;
    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse responseParam,
            @NonNull final FilterChain filterChain)
            throws ServletException, IOException {

        final String requestURI = request.getRequestURI();
        if(requestURI.startsWith("/swagger-ui")
                || requestURI.startsWith("/v3/api-docs")
                || "/login".equals(requestURI)
                || "/".equals(requestURI))
//                ||"/api/user".equals(requestURI))
        {
            filterChain.doFilter(request, responseParam);
            return;
        }

        // 특정 경로에 대해 필터 적용 제외
//        if (requestURI.startsWith("/api/user") || requestURI.startsWith("/api/passwordquestion") ||
//                requestURI.startsWith("/api/password") || requestURI.startsWith("/api/withdraw") ||
//                requestURI.startsWith("/api/profile")) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        if ("/api/passwordquestion".equals(requestURI)) {
            filterChain.doFilter(request, responseParam);
            return;
        }

        this.response = responseParam;

        final String authHeader = request.getHeader("Authorization");//액세스 토큰 찾음
        final String jwt;
        final String userEmail;
        ResponseEntity<ErrorResult> unauthorizedResponse;


        if(authHeader == null){ // 액세스 토큰이  아예 존재하지  않고있다면
            createUnauthorizedResponse(HttpServletResponse.SC_UNAUTHORIZED,
                    "JWT Access Token Error", "No token provided");
            return;
        }


        if(!authHeader.startsWith(("Bearer "))){ // 액세스 토큰이 Bearer로 시작하지 않고있다면
                createUnauthorizedResponse(HttpServletResponse.SC_UNAUTHORIZED,
                        "JWT Access Token Error", "No valid Bearer token provided");
                return;

        }

        jwt = authHeader.substring(7); //Bearer 제외한 액세스 토큰 저장
        userEmail = jwtService.extractUsername(jwt);//토큰의 claim에서 유저 이메일을 추출

        if(userEmail==null){//claim에서 추출한 userEmail이 null일때

            createUnauthorizedResponse(HttpServletResponse.SC_UNAUTHORIZED,
                    "JWT Access Token Error", "token does not contain a valid email");

            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || jwtService.isTokenExpired(jwt)) { //유저의 이메일이 존재하면서 유저가 인증받지 않았다면
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail); //유저 디테일 클래스 생성
            try {
                if (jwtService.isTokenValid(jwt, userDetails)) { //토큰이 valid하다면
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    ); //authentication 토큰 생성

                    authToken.setDetails(//요청에 대한 세부사항을 authentication 토큰에 업데이트
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);//토큰을 security contextholder에 업데이트. 인증 해준거임.
                } else {//액세스 토큰이 valid하지 않다면 invalid 메세지 추가
                    createUnauthorizedResponse(HttpServletResponse.SC_UNAUTHORIZED,
                            "JWT Access Token Error",  "Access token is invalid or expired");
                    return;
                }
            }
            catch (Exception e) { //토큰이 valid하지 않아 에러가 났다면
                createUnauthorizedResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "JWT Access Token Error",  e.getMessage());
                return;
            }
        }
        filterChain.doFilter(request, response);


    }

    private void createUnauthorizedResponse(int httpResponse, String error, String message) throws IOException {
        response.setStatus(httpResponse);
        response.setContentType( MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(new ErrorResult(error,  message)));
        response.getWriter().flush();
    }
}
