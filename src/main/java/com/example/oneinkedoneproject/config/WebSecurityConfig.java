package com.example.oneinkedoneproject.config;

import com.example.oneinkedoneproject.filter.JwtAuthenticationFilter;
import com.example.oneinkedoneproject.filter.JwtRefreshTokenFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtRefreshTokenFilter jwtRefreshTokenFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public WebSecurityCustomizer configure() {      // 스프링 시큐리티 기능 비활성화
        return web -> web.ignoring().requestMatchers("/static/**","/css/**", "/js/**", "/media/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html");
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(auth -> auth.disable())
                .sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않도록 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                        //"/login", "/signup", "/user", "/findId", "/findPw", "/user/find/id","/user/find/email", "/user/find/pw" , "/api/v1/auth/**")
                        "/login","/").permitAll()
                        .anyRequest().authenticated())
                .formLogin(auth -> auth.loginPage("/login").defaultSuccessUrl("/"))
                .logout(auth -> auth.logoutSuccessUrl("/login").invalidateHttpSession(true))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtRefreshTokenFilter, UsernamePasswordAuthenticationFilter.class)  // JwtAuthenticationFilter 적용
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // JwtRefreshTokenFilter 적용

        return httpSecurity.build();
    }
}
