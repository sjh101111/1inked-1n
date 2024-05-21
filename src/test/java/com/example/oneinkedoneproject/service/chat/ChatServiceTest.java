package com.example.oneinkedoneproject.service.chat;

import com.example.oneinkedoneproject.domain.Chat;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.chat.AddChatRequestDto;
import com.example.oneinkedoneproject.dto.chat.ChatResponseDto;
import com.example.oneinkedoneproject.dto.chat.ChatSummariesDto;
import com.example.oneinkedoneproject.repository.chat.ChatRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {
    @InjectMocks
    ChatService chatService;

    @Mock
    ChatRepository chatRepository;

    @Mock
    UserRepository userRepository;

    private List<ChatResponseDto> chatResponseDtoList = new ArrayList<>();

    private User sendUser;
    private User partner;
    private User user;
    private Chat sendchat;
    private Chat receivechat;
    private List<Chat> sendchatList = new ArrayList<>();
    @BeforeEach
    void setUp() {
        partner = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("test")
                .email("test@test.com")
                .password("test")
                .withdraw(false)
                .build();

        sendUser = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("test")
                .email("test@test.com")
                .password("test")
                .withdraw(false)
                .build();

        this.user = User.builder()
                .id(GenerateIdUtils.generateUserId())
                .realname("test")
                .email("test@test.com")
                .password("test")
                .withdraw(false)
                .build();

        sendchat = Chat.builder()
                .id("1").contents("test").sendUser(sendUser).receiverUser(partner)
                .sendAt(LocalDateTime.now()).build();
        sendchatList.add(sendchat);
        receivechat = Chat.builder()
                .id("2").contents("test").sendUser(partner).receiverUser(sendUser)
                .sendAt(LocalDateTime.now().minusMinutes(5)).build();
    }

    @Test
    void readChatSummaries() {
        Set<User> users = new HashSet<>();
        users.add(partner);

        sendchat = Chat.builder()
                .id("1").contents("test").sendUser(sendUser).receiverUser(partner)
                .isDeletedTo(false).isDeletedFrom(false)
                .sendAt(LocalDateTime.now()).build();

        List<ChatResponseDto> dto = new ArrayList<>();
        dto.add(sendchat.toDto());

        Mockito.doReturn(users).when(chatRepository).findAllUniqueChatPartners(any(String.class));
        Mockito.doReturn(Optional.of(partner)).when(userRepository).findByEmail(any(String.class));
        List<Chat> sendChatList = List.of(sendchat);
        Mockito.doReturn(sendChatList).when(chatRepository).findAllBySendUserAndReceiverUser(sendUser, partner);

        // Mock chat received by the user
        List<Chat> receiveChatList = List.of(receivechat);
        Mockito.doReturn(receiveChatList).when(chatRepository).findAllBySendUserAndReceiverUser(partner, sendUser);

        List<ChatSummariesDto> returnValues = chatService.readChatSummaries(sendUser);

        assertThat(returnValues.get(0).getLastMessage()).isEqualTo("test");
    }

    @Test
    void readChatWithPartnerTest() {
        // Mocking user repository to return the partner object
        Mockito.doReturn(Optional.of(partner)).when(userRepository).findByEmail(partner.getEmail());

        // Mock chat sent by the user
        List<Chat> sendChatList = List.of(sendchat);
        Mockito.doReturn(sendChatList).when(chatRepository).findAllBySendUserAndReceiverUser(user, partner);

        // Mock chat received by the user
        List<Chat> receiveChatList = List.of(receivechat);
        Mockito.doReturn(receiveChatList).when(chatRepository).findAllBySendUserAndReceiverUser(partner, user);

        // Execute the service method
        List<ChatResponseDto> allChats = chatService.readChatWithPartner(user, partner.getEmail());

        // Check that the results contain both sent and received chats
        assertThat(allChats).isNotEmpty();
        assertThat(allChats.get(0).getId()).isEqualTo(receivechat.getId());
        assertThat(allChats.get(1).getId()).isEqualTo(sendchat.getId());
    }

    @Test
    void createChatTest() {
        AddChatRequestDto addChatRequestDto =
                AddChatRequestDto.builder().contents("test").partnerEmail("test").build();
        Mockito.doReturn(Optional.of(partner)).when(userRepository).findByEmail(any(String.class));
        Mockito.doReturn(sendchat).when(chatRepository).save(any(Chat.class));
        ChatResponseDto chatResponseDto = chatService.createChat(user, addChatRequestDto);

        assertThat(chatResponseDto.getContents()).isEqualTo(sendchat.getContents());
    }

    @Test
    void updateDeleteChatOfReceiveUser() {
        Mockito.doReturn(Optional.of(partner)).when(userRepository).findByEmail(partner.getEmail());

        // Mock chat sent by the user
        List<Chat> sendChatList = List.of(sendchat);
        Mockito.doReturn(sendChatList).when(chatRepository).findAllBySendUserAndReceiverUser(user, partner);

        // Mock chat received by the user
        List<Chat> receiveChatList = List.of(receivechat);
        Mockito.doReturn(receiveChatList).when(chatRepository).findAllBySendUserAndReceiverUser(partner, user);
        chatService.updateIsDeletedOfChat(user,  partner.getEmail());
        assertThat(sendchat.isDeletedFrom()).isEqualTo(true);
        assertThat(receivechat.isDeletedTo()).isEqualTo(true);
    }

    @Test
    void deleteChat() {
        sendchat = Chat.builder()
                .id("1").contents("test").sendUser(sendUser).receiverUser(partner)
                .isDeletedTo(true).isDeletedFrom(true)
                .sendAt(LocalDateTime.now()).build();
        receivechat = Chat.builder()
                .id("2").contents("test").sendUser(partner).receiverUser(sendUser)
                .isDeletedTo(true).isDeletedFrom(true)
                .sendAt(LocalDateTime.now().minusMinutes(5)).build();
        List<Chat> sendChats = new ArrayList<>();
        List<Chat> receiveChats = new ArrayList<>();
        sendChats.add(sendchat);
        receiveChats.add(receivechat);

        Mockito.doReturn(Optional.of(partner)).when(userRepository).findByEmail(any(String.class));
        Mockito.doReturn(sendChats).when(chatRepository).findAllBySendUserAndReceiverUser(sendUser, partner);
        Mockito.doReturn(receiveChats).when(chatRepository).findAllBySendUserAndReceiverUser(partner, sendUser);

        chatService.deleteChat(sendUser, "test");

        Mockito.verify(chatRepository).deleteAll(Mockito.anyList());
    }
}
