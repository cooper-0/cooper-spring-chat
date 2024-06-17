package com.cooper.chat.chat.controller;


import com.cooper.chat.chat.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import com.cooper.chat.chat.model.Chat;
import com.cooper.chat.chat.model.ChatMessageDto;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RestController
//@RequestMapping("/cooper-chat")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // 채팅 메시지 저장 엔드포인트
    @PostMapping("/cooper-chat/message")
    public ResponseEntity<String> saveChatMessage(@RequestBody ChatMessageDto messageDto, @RequestParam String collectionName) {
        Chat chat = chatService.saveChatMessage(messageDto, collectionName);
        return ResponseEntity.ok("채팅 메시지가 성공적으로 저장되었습니다.");
    }

    // 채팅 내용 불러오는 엔드포인트
    @GetMapping("/cooper-chat/previous/{collectionName}")
    public List<Chat> getPreviousChats(@PathVariable String collectionName) {
        return chatService.getPreviousChats(collectionName);
    }

    // 입력한 채팅 삭제 엔드포인트
    @DeleteMapping("/cooper-chat/deleteChat")
    public void deleteMessage(@RequestParam String messageId, String collectionName) {
        chatService.deleteMessage(messageId, collectionName);
    }

    @GetMapping("/cooper-chat/test")
    public String getChatPage() {
        return "/index"; // src/main/resources/templates/index.html
    }


//브로드캐스트 부분
    @MessageMapping("/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessageDto chat(@DestinationVariable String roomId, ChatMessageDto message) {
        log.info("채팅 메시지");
        String collectionName = getCollectionName(roomId);
        Chat chat = chatService.createChat(collectionName, roomId, message.getSenderID(), message.getSenderEmail(), message.getMessage());

        return buildChatMessageDto(roomId, chat.getSenderID(), chat.getSenderEmail(), chat.getMessage());
    }

    private String getCollectionName(String roomId) {
        return roomId;
    }

    private ChatMessageDto buildChatMessageDto(String roomId, String senderID, String senderEmail, String message) {
        return ChatMessageDto.builder()
                .messageType(ChatMessageDto.MessageType.TALK)
                .roomId(roomId)
                .senderID(senderID)
                .senderEmail(senderEmail)
                .message(message)
                .build();
    }
}
