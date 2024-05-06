package com.example.oneinkedoneproject.filter;
import com.example.oneinkedoneproject.service.auth.JwtService;
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
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");//액세스 토큰 찾음
        final String jwt;
        final String userEmail;

        if(authHeader == null || !authHeader.startsWith(("Bearer "))){ // 액세스 토큰이  아예 존재하지 않거나, Bearer로 시작하지 않고있다면
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
            response.getWriter().write("Unauthorized: No valid Bearer token provided"); // 오류 메시지 작성
            response.getWriter().flush(); //클라이언트에게 바로 리스폰스 전달
            return;
        }
        jwt = authHeader.substring(7); //Bearer 제외한 액세스 토큰 저장

        userEmail = jwtService.extractUsername(jwt);//토큰의 claim에서 유저 이메일을 추출

        if(userEmail !=null && SecurityContextHolder.getContext().getAuthentication()==null) { //유저의 이메일이 존재하면서 유저가 인증받지 않았다면
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
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Access token is invalid or expired");
                    response.getWriter().flush(); //클라이언트에게 바로 리스폰스 전달
                    return;
                }
            }
            catch (Exception e) { //토큰이 valid하지 않아 에러가 났다면
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Could not gain Authentication");
                response.getWriter().flush(); //클라이언트에게 바로 리스폰스 전달
                return;
            }
        }
        else if(userEmail==null){//claim에서 추출한 userEmail이 null일때
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: token does not contain a valid email");
            response.getWriter().flush();
            return;
        }
        filterChain.doFilter(request, response);
    }
}
