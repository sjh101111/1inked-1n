package com.example.oneinkedoneproject.service.auth;

import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.auth.AuthenticationRequest;
import com.example.oneinkedoneproject.dto.auth.AuthenticationResponse;
import com.example.oneinkedoneproject.dto.auth.RegisterRequest;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    //가입 로직
    public AuthenticationResponse register(RegisterRequest request) {
        //회원가입 중복 확인 로직 추가 필요(ex_이메일 중복)
        var user = User.builder()
                .id("userId")//TODO:user ID 포멧에 맞게 생성해서 넣기
                .realname(request.getRealname())
                .email(request.getRealname())
                .password(passwordEncoder.encode(request.getPassword()))
                .passwordQuestion(request.getPasswordQuestion())
                .passwordAnswer(request.getPasswordAnswer())
                .withdraw(true)
                .build();
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        //유저 인증 확인
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        //여기까지 오면 인증 된 것.
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
