package com.example.oneinkedoneproject.controller.chat;

import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.chat.AddChatRequestDto;
import com.example.oneinkedoneproject.dto.chat.ChatResponseDto;
import com.example.oneinkedoneproject.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/api/chatWithPartner")
    public ResponseEntity<List<ChatResponseDto>> readChatWithPartner(@AuthenticationPrincipal User user,
                                                                     @RequestParam String partnerEmail) {
        return ResponseEntity.ok(chatService.readChatWithPartner(user, partnerEmail));
    }

    @PostMapping("/api/createChat")
    public ResponseEntity<ChatResponseDto> createChat(@AuthenticationPrincipal User user, @RequestBody AddChatRequestDto addChatRequestDto) {
        return ResponseEntity.ok(chatService.createChat(user, addChatRequestDto));
    }

    @PutMapping("/api/updateIsDeleted")
    public ResponseEntity<List<ChatResponseDto>> updateIsDeletedOfChat(@AuthenticationPrincipal User user,
                                                      @RequestBody String chatPartnerEmail) {
        return ResponseEntity.ok(chatService.updateIsDeletedOfChat(user, chatPartnerEmail)) ;

    }

    //sender, receiver 각각의 isDeleteTo, isDeleteFrom이 모두 true일 경우 chat 자동 삭제
    @DeleteMapping("/api/deleteChat")
    public ResponseEntity<Void> deleteChat(@AuthenticationPrincipal User user,
                                           @RequestBody String chatPartnerEmail) {
        chatService.deleteChat(user, chatPartnerEmail);
        return ResponseEntity.ok().build();
    }
}
