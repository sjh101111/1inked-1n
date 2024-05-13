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

import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;

    private final UserRepository userRepository;

    public List<ChatSummariesDto> readChatSummaries(User currentUser) {
        Set<User> partners = chatRepository.findAllUniqueChatPartners(currentUser.getId());// Method to find all unique chat partners

        List<ChatSummariesDto> chatSummaries = new ArrayList<>();

        for (User partner : partners) {
            List<ChatResponseDto> chatsWithPartner = readChatWithPartner(currentUser, partner.getEmail());
            if (!chatsWithPartner.isEmpty()) {
                ChatResponseDto lastChat = chatsWithPartner.get(chatsWithPartner.size() - 1); // Get the last chat message

                User chatPartner = lastChat.getSendUser().getEmail().equals(currentUser.getEmail()) ? lastChat.getReceiveUser() : lastChat.getSendUser();
                ChatResponseDto firstChatForIsDeleted = chatsWithPartner.get(0);
                boolean isDeleted;
                if (currentUser.getEmail().equals(firstChatForIsDeleted.getSendUser().getEmail())) {
                    // currentUser is the sender, check if they have deleted the message
                    isDeleted = firstChatForIsDeleted.getIsDeletedFrom();
                } else {
                    // currentUser is not the sender, thus the receiver, check if they have deleted the message
                    isDeleted = firstChatForIsDeleted.getIsDeletedTo();
                }

                ChatSummariesDto summary = ChatSummariesDto.builder().
                id(lastChat.getId()).lastMessage(lastChat.getContents()).lastMessageAt(lastChat.getSendAt())
                        .isDeleted(isDeleted).me(currentUser).partner(chatPartner).build();
                System.out.println(chatPartner.getEmail() + currentUser.getEmail() + lastChat.getSendUser().getEmail()
                + lastChat.getReceiveUser().getEmail());
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
                .stream().map(chat -> ChatResponseDto.builder().
                        id(chat.getId()).sendAt(chat.getSendAt()).partner(chatPartner).me(user)
                                .receiveUser(chat.getReceiverUser()).sendUser(chat.getSendUser())
                                .contents(chat.getContents()).isDeletedTo(chat.isDeletedTo())
                                .isDeletedFrom(chat.isDeletedFrom()).
                        build()).toList();
        List<ChatResponseDto> receivedChat = chatRepository.findAllBySendUserAndReceiverUser(chatPartner, user)
                .stream().map(chat -> ChatResponseDto.builder().
                        id(chat.getId()).sendAt(chat.getSendAt()).partner(chatPartner).me(user)
                        .receiveUser(chat.getReceiverUser()).sendUser(chat.getSendUser())
                        .contents(chat.getContents()).isDeletedTo(chat.isDeletedTo())
                        .build()).toList();

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
        System.out.println(chatPartnerEmail);
        User chatPartner = userRepository.findByEmail(chatPartnerEmail).orElseThrow(
                () ->new IllegalArgumentException("이메일에 해당하는 유저가 없습니다"+chatPartnerEmail)
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
