package com.cooper.chat.chat.controller;

import com.cooper.chat.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cooper.chat.chat.model.ChatRoom;
import java.util.List;

@RestController
@RequestMapping("/cooper-chat")
public class ChatRoomController {

    private final ChatService chatService;

    @Autowired
    public ChatRoomController(ChatService chatService) {
        this.chatService = chatService;
    }
    // 채팅방 생성 엔드포인트
    @PostMapping("/create")
    public ResponseEntity<String> createRoom(@RequestParam String roomId) {
        chatService.createChatCollection(roomId);
        return ResponseEntity.ok("채팅방이 생성되었습니다.");
    }
    // 채팅방 삭제 엔드포인트
    @DeleteMapping("/deleteRoom")
    public ResponseEntity<String> deleteRoom(@RequestParam String roomId) {
        chatService.deleteRoom(roomId);
        return ResponseEntity.ok("채팅방이 삭제되었습니다.");
    }

    // 채팅방 리스트 엔드포인트
    @GetMapping("/list")
    public List<String> getChatRooms() {
        return chatService.getCollectionNames();
    }
}
