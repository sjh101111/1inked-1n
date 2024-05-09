package com.example.oneinkedoneproject.service.password;

import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordQuestionService {
    private final PasswordRepository passwordRepository;

    public List<PasswordQuestion> findPasswordQuestion(){
        //단순 조회
        return passwordRepository.findAll();
    }
}
