package com.example.oneinkedoneproject.service.chat;

import com.example.oneinkedoneproject.domain.Chat;
import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.chat.AddChatRequestDto;
import com.example.oneinkedoneproject.dto.chat.ChatResponseDto;
import com.example.oneinkedoneproject.dto.chat.ChatSummariesDto;
import com.example.oneinkedoneproject.repository.chat.ChatRepository;
import com.example.oneinkedoneproject.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;

    private final UserRepository userRepository;

    public List<ChatSummariesDto> readChatSummaries(User currentUser) {
        Set<User> partners = chatRepository.findAllUniqueChatPartners(currentUser.getId());// Method to find all unique chat partners
        List<ChatSummariesDto> summaries = new ArrayList<>();

        List<ChatSummariesDto> chatSummaries = new ArrayList<>();

        for (User partner : partners) {
            List<ChatResponseDto> chatsWithPartner = readChatWithPartner(currentUser, partner.getEmail());
            if (!chatsWithPartner.isEmpty()) {
                ChatResponseDto lastChat = chatsWithPartner.get(chatsWithPartner.size() - 1); // Get the last chat message

                User me;
                if (currentUser.equals(lastChat.getSendUser())) {
                    me = currentUser;

                } else {
                    me = partner;
                    partner = currentUser;
                }

                ChatSummariesDto summary = new ChatSummariesDto(
                        lastChat.getId(),
                        lastChat.getContents(),
                        lastChat.getSendAt(),
                        partner, me,
                        lastChat.getIsDeletedTo() || lastChat.getIsDeletedFrom()
                );

                chatSummaries.add(summary);
            }
        }
        return chatSummaries;
    }

    public List<ChatResponseDto> readChatWithPartner(User user, String chatPartnerEmail) {
        User chatPartner = userRepository.findByEmail(chatPartnerEmail).orElseThrow(
                () ->new IllegalArgumentException("이메일에 해당하는 유저가 없습니다" + chatPartnerEmail)
        );
        List<ChatResponseDto> sendChat = chatRepository.findAllBySendUserAndReceiverUser(user, chatPartner)
                .stream().map(chat -> chat.toDto()).toList();
        List<ChatResponseDto> receivedChat = chatRepository.findAllBySendUserAndReceiverUser(chatPartner, user)
                .stream().map(chat -> chat.toDto()).toList();

        //Combine and sort messages
        List<ChatResponseDto> allChats = new ArrayList<>(sendChat);
        allChats.addAll(receivedChat);
        allChats.sort(Comparator.comparing(ChatResponseDto::getSendAt));
        return allChats;
    }

    public ChatResponseDto createChat(User user, AddChatRequestDto addChatRequestDto) {
        User receiveUser = userRepository.findByEmail(addChatRequestDto.getPartnerEmail()).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다" + addChatRequestDto.getPartnerEmail())
        );

        return chatRepository.save(Chat.builder()
                .contents(addChatRequestDto.getContents()).sendUser(user).receiverUser(receiveUser)
                .isDeletedTo(false).isDeletedFrom(false).build())
                .toDto();
    }

    @Transactional
    public List<ChatResponseDto> updateIsDeletedOfChat(User user, String chatPartnerEmail) {
        User chatPartner = userRepository.findByEmail(chatPartnerEmail).orElseThrow(
                () ->new IllegalArgumentException("이메일에 해당하는 유저가 없습니다")
        );

        List<Chat> sentChats = chatRepository.findAllBySendUserAndReceiverUser(user, chatPartner);
        for (Chat chat : sentChats) {
            chat.isDeletedFromUpdate(true); // Sender's perspective
        }

        // Fetch messages received by the current user from the chat partner
        List<Chat> receivedChats = chatRepository.findAllBySendUserAndReceiverUser(chatPartner, user);
        for (Chat chat : receivedChats) {
            chat.isDeletedToUpdate(true); // Receiver's perspective
        }

        List<ChatResponseDto> allChats = new ArrayList<>(sentChats.stream()
                .map(x -> x.toDto()).toList());
        allChats.addAll(receivedChats.stream()
                .map(x -> x.toDto()).toList());
        allChats.sort(Comparator.comparing(ChatResponseDto::getSendAt));
        return allChats;
    }

    public void deleteChat(User user, String chatPartnerEmail) {
        User chatPartner = userRepository.findByEmail(chatPartnerEmail).orElseThrow(
                () ->new IllegalArgumentException("이메일에 해당하는 유저가 없습니다")
        );

        List<Chat> sentChats = chatRepository.findAllBySendUserAndReceiverUser(user, chatPartner);
        List<Chat> receivedChats = chatRepository.findAllBySendUserAndReceiverUser(chatPartner, user);

        // Delete only if both parties marked the chat as deleted
        List<Chat> chatsToDelete = Stream.concat(sentChats.stream(), receivedChats.stream())
                .filter(chat -> chat.isDeletedTo() && chat.isDeletedFrom())
                .toList();

        if (!chatsToDelete.isEmpty()) {
            // Perform deletion only if the list is not empty
            chatRepository.deleteAll(chatsToDelete);
        }
    }
}
