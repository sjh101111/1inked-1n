package com.example.oneinkedoneproject.controller.passwordQuestion;

import com.example.oneinkedoneproject.controller.password.PasswordQuestionController;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.service.password.PasswordQuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PasswordQuestionControllerUnitTest {
    @InjectMocks
    private PasswordQuestionController passwordQuestionController;

    @Mock
    private PasswordQuestionService passwordQuestionService;

    MockMvc mockMvc;

    private List<PasswordQuestion> passwordQuestions;

    @BeforeEach
    void init(){
        passwordQuestions = new ArrayList<>();
        passwordQuestions.add(new PasswordQuestion("1" ,"hi"));
        passwordQuestions.add(new PasswordQuestion("2" ,"hiadawd"));

        mockMvc = MockMvcBuilders.standaloneSetup(passwordQuestionController).build();
    }

    @Test
    @DisplayName("조회 controller 테스트")
    void findPasswordQuestionTest() throws Exception {
        //given

        //when
        doReturn(passwordQuestions)
            .when(passwordQuestionService)
            .findPasswordQuestion();

        ResultActions actions = mockMvc.perform(get("/api/passwordquestion"));

        //then
        actions.andExpect(status().isOk()).andDo(print());
    }
}
