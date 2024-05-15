package com.example.oneinkedoneproject.controller.chat;

import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.dto.chat.AddChatRequestDto;
import com.example.oneinkedoneproject.dto.chat.ChatResponseDto;
import com.example.oneinkedoneproject.dto.chat.ChatSummariesDto;
import com.example.oneinkedoneproject.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/api/chatSummaries")
    public ResponseEntity<List<ChatSummariesDto>> readChatSummaries(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(chatService.readChatSummaries(user));
    }

    @GetMapping("/api/chatWithPartner")
    public ResponseEntity<List<ChatResponseDto>> readChatWithPartner(@AuthenticationPrincipal User user,
                                                                     @RequestParam String partnerEmail) throws Exception{
        String decodedEmail = URLDecoder.decode(partnerEmail, StandardCharsets.UTF_8.toString());
        return ResponseEntity.ok(chatService.readChatWithPartner(user, decodedEmail));
    }

    @PostMapping("/api/createChat")
    public ResponseEntity<ChatResponseDto> createChat(@AuthenticationPrincipal User user, @RequestBody AddChatRequestDto addChatRequestDto) {
        return ResponseEntity.ok(chatService.createChat(user, addChatRequestDto));
    }

    @PutMapping(path ="/api/updateIsDeleted")
    public ResponseEntity<List<ChatResponseDto>> updateIsDeletedOfChat(@AuthenticationPrincipal User user,
                                                      @RequestParam("partnerEmail") String partnerEmail) throws Exception{
        System.out.println(partnerEmail);
        String decodedEmail = URLDecoder.decode(partnerEmail, StandardCharsets.UTF_8.toString());
        return ResponseEntity.ok(chatService.updateIsDeletedOfChat(user, decodedEmail)) ;

    }

    //sender, receiver 각각의 isDeleteTo, isDeleteFrom이 모두 true일 경우 chat 삭제
    @DeleteMapping("/api/deleteChat")
    public ResponseEntity<Void> deleteChat(@AuthenticationPrincipal User user,
                                           @RequestBody String partnerEmail) {
        chatService.deleteChat(user, partnerEmail);
        return ResponseEntity.ok().build();
    }
}
