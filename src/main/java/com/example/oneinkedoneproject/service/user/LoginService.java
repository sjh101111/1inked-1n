package com.example.oneinkedoneproject.service.user;

import com.example.oneinkedoneproject.dto.user.LoginUserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthenticationManager authenticationManager;
    @Transactional
    public Authentication login(LoginUserRequestDto request){
        //리퀘스트바디에서 email, password 추출
        String email = request.getEmail();
        String password = request.getPassword();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
