package com.example.oneinkedoneproject.controller.user;

import com.example.oneinkedoneproject.dto.auth.TokenInfo;
import com.example.oneinkedoneproject.dto.user.LoginUserRequestDto;
import com.example.oneinkedoneproject.service.auth.JwtService;
import com.example.oneinkedoneproject.service.user.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final JwtService jwtService;
    private final LoginService loginService;
    @PostMapping("/login")
    public ResponseEntity<String> signup(@RequestBody LoginUserRequestDto request){
        HttpHeaders httpHeaders = new HttpHeaders();
        Authentication auth;
        try {
            auth = loginService.login(request);
        }catch(BadCredentialsException e){
            return  new ResponseEntity<>("Login Error : Invalid username or password",httpHeaders,  HttpServletResponse.SC_UNAUTHORIZED);
        }

        if(auth !=null && auth.isAuthenticated()){
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            TokenInfo tokenInfo = jwtService.generateTokenInfo(userDetails);
            httpHeaders.set("Authorization", "Bearer " + tokenInfo.getAccessToken());
            httpHeaders.set("Refresh-Token", "Bearer "+ tokenInfo.getRefreshToken());
            // ResponseEntity를 생성할 때, body, headers, status code를 지정합니다.
            return new ResponseEntity<>("환영합니다!", httpHeaders, HttpServletResponse.SC_ACCEPTED);
        }else{
            return  new ResponseEntity<>("Login Error : Invalid username or password",httpHeaders,  HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
