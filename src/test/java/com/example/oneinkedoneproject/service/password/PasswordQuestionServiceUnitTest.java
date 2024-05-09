package com.example.oneinkedoneproject.service.password;

import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class PasswordQuestionServiceUnitTest {
    @InjectMocks
    private PasswordQuestionService passwordQuestionService;

    @Mock
    private PasswordRepository passwordRepository;

    private List<PasswordQuestion> passwordQuestions;

    @BeforeEach
    void init(){
        passwordQuestions = new ArrayList<>();
        passwordQuestions.add(new PasswordQuestion("1" ,"hi"));
        passwordQuestions.add(new PasswordQuestion("2" ,"hiadawd"));
    }

    @Test
    @DisplayName("조회 서비스 테스트")
    void findPasswordQuestionTest(){
        //given

        //when
        doReturn(passwordQuestions)
                .when(passwordRepository)
                .findAll();
        List<PasswordQuestion> results = passwordQuestionService.findPasswordQuestion();

        //then
        assertThat(results.size()).isEqualTo(passwordQuestions.size());
    }

}
