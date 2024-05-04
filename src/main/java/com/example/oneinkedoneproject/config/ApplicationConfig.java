package com.example.oneinkedoneproject.config;

import com.example.oneinkedoneproject.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        //인증 매니저 생성
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        //사용자 상세 정보 제공하는 빈 생성
        //username(사실 email임) 이용하여 User정보 찾아옴
        //찾은 사용자 정보가 없으면 UsernameNotFoundException 생성
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //패스워드 인코더 생성 후 반환
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        //인증프로바이더 생성
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());//UserDetailsService를 적용
        authProvider.setPasswordEncoder(passwordEncoder());// 패스워드 인코더 적용
        return authProvider; //해당 내용들이 정의된 인증 프로바이더 리턴
    }
}
