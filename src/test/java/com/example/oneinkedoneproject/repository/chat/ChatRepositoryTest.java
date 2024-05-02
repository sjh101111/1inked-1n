package com.example.oneinkedoneproject.repository.chat;

import com.example.oneinkedoneproject.OneinkedOneProjectApplication;
import com.example.oneinkedoneproject.domain.Chat;
import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.repository.chat.ChatRepository;
import com.example.oneinkedoneproject.repository.password.PasswordRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = OneinkedOneProjectApplication.class)
public class ChatRepositoryTest {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

	@Autowired
	private PasswordRepository passwordRepository;

    private User sender;
    private User receiver;

    private PasswordQuestion passwordQuestion;

    private Chat chat;

    @BeforeEach
    void init(){
		passwordQuestion = new PasswordQuestion("1","질문");
        sender = User
                .builder()
                .id("1")
                .realname("1")
                .email("1@1.com")
                .password("test")
                .passwordQuestion(passwordQuestion)
                .passwordAnswer("1")
                .identity("?")
                .withdraw(false)
                .grade(Grade.ROLE_BASIC)
                .build();
        receiver = User
                .builder()
                .id("2")
                .realname("1")
                .email("1@1.com")
                .password("test")
                .passwordQuestion(passwordQuestion)
                .passwordAnswer("1")
                .identity("?")
                .withdraw(false)
                .grade(Grade.ROLE_BASIC)
                .build();
        chat = new Chat("1", "awf?", sender, receiver);
		passwordRepository.save(passwordQuestion);
        userRepository.save(sender);
        userRepository.save(receiver);
    }

    @Test
    @DisplayName("insert 단위 테스트")
    void insertTest(){
        //given

        //when
        Chat insertChat = chatRepository.save(chat);

        //then
        assertThat(chatRepository.findAll().size()).isEqualTo(1);
        assertThat(chat.getId()).isEqualTo(insertChat.getId());
    }

    @Test
    @DisplayName("select 단위 테스트")
    void selectTest(){
        //given
        Chat savedChat = chatRepository.save(chat);

        //when
        Chat selectedChat = chatRepository.findAll().get(0);

        //then
        assertThat(savedChat.getId()).isEqualTo(selectedChat.getId());
    }

    @Test
    @Transactional
    @DisplayName("update 단위 테스트")
    void updateTest(){
        //given
        String newContentString = "새로운 콘텐츠";
        Chat savedChat = chatRepository.save(chat);

        //when
        savedChat.updateContents(newContentString);
        Chat selectedChat = chatRepository.findAll().get(0);

        //then
        //같은 chat이지만, 콘텐츠는 업데이트되었음
        assertThat(selectedChat.getId()).isEqualTo(savedChat.getId());
        assertThat(selectedChat.getContents()).isEqualTo(newContentString);
    }

    @Test
    @DisplayName("delete 테스트")
    void deleteTest(){
        //given
        Chat savedChat = chatRepository.save(chat);

        //when
        chatRepository.deleteAll();

        //then
        assertThat(chatRepository.findById(savedChat.getId()).isEmpty()).isTrue();
    }
}
