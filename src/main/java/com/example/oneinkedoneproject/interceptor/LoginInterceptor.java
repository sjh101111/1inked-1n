package com.example.oneinkedoneproject.interceptor;

import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import com.example.oneinkedoneproject.service.auth.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@RequiredArgsConstructor
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;




    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("LoginInterceptor preHandle()");

        //리퀘스트바디에서 email, password 추출
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        //로그인 시도 시도
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (authentication.isAuthenticated()) {

                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                TokenInfo tokenInfo = jwtService.generateTokenInfo(userDetails);

                response.setHeader("Authorization", "Bearer "+ tokenInfo.getAccessToken());
                response.setHeader("Refresh-Token", "Bearer "+ tokenInfo.getRefreshToken());
                return true;
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }catch (BadCredentialsException e) {//실패시 에러 리스폰드
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password");
        }
        return false;
    }


}
