package com.example.oneinkedoneproject.controller.chat;

import com.example.oneinkedoneproject.domain.Chat;
import com.example.oneinkedoneproject.domain.Grade;
import com.example.oneinkedoneproject.domain.PasswordQuestion;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.chat.AddChatRequestDto;
import com.example.oneinkedoneproject.dto.chat.ChatResponseDto;
import com.example.oneinkedoneproject.service.chat.ChatService;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ChatControllerUnitTest {
    @InjectMocks
    ChatController chatController;

    @Mock
    ChatService chatService;

    MockMvc mockMvc;

    private User user;
    private User partner;
    private Chat sendchat;
    private Chat receivechat;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
        user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("test")
                .email("test@test.com")
                .password("test")
                .withdraw(false)
                .passwordQuestion(PasswordQuestion.builder().question("a").build())
                .grade(Grade.ROLE_BASIC)
                .build();

        partner = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("test")
                .email("test@test.com")
                .password("test")
                .grade(Grade.ROLE_BASIC)
                .withdraw(false)
                .build();

        sendchat = Chat.builder()
                .id("1").contents("test").sendUser(user).receiverUser(partner)
                .isDeletedTo(false).isDeletedFrom(false)
                .sendAt(LocalDateTime.now()).build();

        receivechat = Chat.builder()
                .id("2").contents("test").sendUser(partner).receiverUser(user)
                .isDeletedFrom(false).isDeletedTo(false)
                .sendAt(LocalDateTime.now().minusMinutes(5)).build();
    }

    @Test
    void readChatWithPartnerTest() throws Exception {
        List<Chat> sendChatList = List.of(sendchat);
        // Mock chat received by the user
        List<Chat> receiveChatList = List.of(receivechat);
        List<Chat> allChats = new ArrayList<>(sendChatList);
        allChats.addAll(receiveChatList);
        List<ChatResponseDto> dto = allChats.stream().map(x -> x.toDto()).toList();
        Mockito.doReturn(dto).when(chatService).readChatWithPartner(any(User.class), any(String.class));

        ResultActions resultActions = mockMvc.perform(get("/api/chatWithPartner")
                        .param("partnerEmail", partner.getEmail()))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    void createChat() throws Exception {
        Mockito.doReturn(sendchat.toDto()).when(chatService).createChat(any(User.class), any(AddChatRequestDto.class));

        ResultActions resultActions = mockMvc.perform(post("/api/createChat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(AddChatRequestDto.builder()
                                .partnerEmail("test").contents("test").build()))) // 본문 추가)
                .andDo(print())
                .andExpect(jsonPath("$.contents").value(sendchat.getContents()));
    }

    @Test
    void updateIsDeleteTest() throws Exception {
        List<ChatResponseDto> dto = new ArrayList<>();
        sendchat.isDeletedToUpdate(true);
        dto.add(sendchat.toDto());
        Mockito.doReturn(dto).when(chatService).updateIsDeletedOfChat(any(User.class), any(String.class));

        ResultActions resultActions = mockMvc.perform(put("/api/updateIsDeleted")
                        .param("partnerEmail", "test@test.com") // 요청 파라미터 추가
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].isDeletedTo").value(true));
    }

    @Test
    void deleteChat() throws Exception {

        ResultActions resultActions = mockMvc.perform(delete("/api/deleteChat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString("test")))
                .andDo(print())
                .andExpect(status().isOk());

        verify(chatService).deleteChat(any(User.class), any(String.class));
    }
}
