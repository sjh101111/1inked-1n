package com.example.oneinkedoneproject.controller.password;

import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.service.password.PasswordQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PasswordQuestionController {
    private final PasswordQuestionService passwordQuestionService;

    @GetMapping("/api/passwordquestion")
    public ResponseEntity<List<PasswordQuestion>> findPasswordQuestion(){
        return ResponseEntity.ok(passwordQuestionService.findPasswordQuestion());
    }
}
