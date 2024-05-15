package com.example.oneinkedoneproject.controller.password;

import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.service.password.PasswordQuestionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Password Question", description = "비밀번호 찾기 질문 API")
public class PasswordQuestionController {
    private final PasswordQuestionService passwordQuestionService;

    @GetMapping("/api/passwordquestion")
    public ResponseEntity<List<PasswordQuestion>> findPasswordQuestion(){
        return ResponseEntity.ok(passwordQuestionService.findPasswordQuestion());
    }
}
