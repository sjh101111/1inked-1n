package com.example.oneinkedoneproject.repository.password;

import com.example.oneinkedoneproject.OneinkedOneProjectApplication;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = OneinkedOneProjectApplication.class)
public class PasswordQuestionTest {
    @Autowired
    private PasswordRepository passwordRepository;

    private PasswordQuestion passwordQuestion;

    @BeforeEach
    void beforeTest(){
        // 테스트 전에 테이블을 모두 초기화한다.
        // passwordRepository.deleteAll();
        passwordQuestion = PasswordQuestion
                .builder()
                .id("1")
                .question("당신이 세상에서 가장 사랑하는 사람은?")
                .build();
    }

    @Test
    @DisplayName("비밀번호 질문을 삽입한다.")
    void insertTest(){
        //given

        //when
        passwordRepository.save(passwordQuestion);

        //then

        //한번 삽입했으므로 하나만 존재
        assertThat(passwordRepository.findAll().size()).isEqualTo(1);
    }


    @Test
    @DisplayName("가장 앞에 있는 비밀번호 질문을 조회한다.")
    void showTest(){
        //given
        passwordRepository.save(passwordQuestion);

        //when
        PasswordQuestion selectedQuestion = passwordRepository.findAll().get(0);

        //then
        assertThat(selectedQuestion.getId()).isEqualTo(passwordQuestion.getId());
        assertThat(selectedQuestion.getQuestion()).isEqualTo(passwordQuestion.getQuestion());
    }

    @Test
    // Entity 더티체킹(값 업데이트시)위한 annotation
    @Transactional
    @DisplayName("insert한 비밀번호 질문을 update한다.")
    void updateTest(){
        //given
        String updateQuestionString = "세상에서 가장 싫어하는 사람은?";
        passwordRepository.save(passwordQuestion);
        //선택한 question
        PasswordQuestion selectedQuestion = passwordRepository.findAll().get(0);

        //when
        selectedQuestion.updateQuestion(updateQuestionString);
        PasswordQuestion afterUpdateQuestion = passwordRepository.findAll().get(0);

        //then
        //id는 같고, question 내용만 업데이트 되었을것이다.
        assertThat(afterUpdateQuestion.getId()).isEqualTo(selectedQuestion.getId());
        assertThat(afterUpdateQuestion.getQuestion()).isEqualTo(updateQuestionString);
    }

    @Test
    @DisplayName("insert한 비밀번호 질문을 delete한다.")
    void deleteTest(){
        //given
        passwordRepository.save(passwordQuestion);

        //when
        passwordRepository.delete(passwordQuestion);

        //then
        //passwordQuestion Repsotiroy의 사이즈는 반드시 0이다.
        assertThat(passwordRepository.findAll().size()).isEqualTo(0);
    }
}
