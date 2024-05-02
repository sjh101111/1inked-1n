package com.example.oneinkedoneproject.auth;

import com.example.oneinkedoneproject.dto.auth.AuthenticationRequest;
import com.example.oneinkedoneproject.dto.auth.AuthenticationResponse;
import com.example.oneinkedoneproject.dto.auth.RegisterRequest;
import com.example.oneinkedoneproject.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class AuthenticationDemoController {

    private final AuthenticationService authenticationService;
    @GetMapping("/")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello From secured endpoint");

    }

    //회원가입 정보를 받고 토큰 발급해주는 컨트롤러
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    //인증용 request를 받고 토큰 발급해주는 컨트롤러
    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("not looged in");
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request//이메일과 패스워드 전달
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
