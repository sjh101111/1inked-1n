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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


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
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/user","/api/**",
                                //"/login", "/signup", "/user", "/findId", "/findPw", "/user/find/id","/user/find/email", "/user/find/pw" , "/api/v1/auth/**")
                                "/login","/").permitAll()
                        .anyRequest().authenticated())
                .csrf(auth -> auth.disable())
                .sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않도록 설정
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtRefreshTokenFilter, UsernamePasswordAuthenticationFilter.class)  // JwtAuthenticationFilter 적용
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // JwtRefreshTokenFilter 적용


        httpSecurity.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(configurationSource());
        });


        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
