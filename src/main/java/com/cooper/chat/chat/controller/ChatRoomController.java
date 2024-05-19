package com.cooper.chat.chat.controller;

import com.cooper.chat.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room")
public class ChatRoomController {

    private final ChatService chatService;

    @Autowired
    public ChatRoomController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRoom(@RequestParam String roomId) {
        // 채팅방 이름으로 컬렉션 생성
        chatService.createChatCollection(roomId);
        return ResponseEntity.ok("Chat room created successfully");
    }
}
