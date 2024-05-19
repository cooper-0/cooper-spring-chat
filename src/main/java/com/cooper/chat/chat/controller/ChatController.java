package com.cooper.chat.chat.controller;


import com.cooper.chat.chat.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import com.cooper.chat.chat.model.Chat;
import com.cooper.chat.chat.model.ChatMessageDto;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // 이전 채팅을 반환하는 엔드포인트
    @GetMapping("/previous/{collectionName}")
    public List<Chat> getPreviousChats(@PathVariable String collectionName) {
        return chatService.getPreviousChats(collectionName);
    }

    @DeleteMapping("/delete")
    public void deleteMessage(@RequestParam String messageId, String collectionName) {
        chatService.deleteMessage(messageId, collectionName);
    }



    @MessageMapping("/{roomId}")
    @SendTo("/room/{roomId}")
    public ChatMessageDto chat(@DestinationVariable String roomId, ChatMessageDto message) {
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
