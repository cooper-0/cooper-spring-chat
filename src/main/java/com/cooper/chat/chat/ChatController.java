package com.cooper.chat.chat;


import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.cooper.chat.chat.model.Chat;
import com.cooper.chat.chat.model.ChatMessageDto;
import com.cooper.chat.chat.model.ChatB;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    @GetMapping("/previous")
    public List<Chat> getPreviousChats() {
        return chatService.getPreviousChats();
    }


    @MessageMapping("/{roomId}")
    @SendTo("/room/{roomId}")
    public ChatMessageDto chat(@DestinationVariable String roomId, ChatMessageDto message) {
        if ("roomA".equals(roomId)) {
            // 채팅 저장
            Chat chat = chatService.createChat(roomId, message.getSenderID(), message.getSenderEmail(), message.getMessage());

            return ChatMessageDto.builder()
                    .messageType(ChatMessageDto.MessageType.TALK)
                    .roomId(roomId)
                    .senderID(chat.getSenderID())
                    .senderEmail(chat.getSenderEmail())
                    .message(chat.getMessage())
                    .build();
        } else if ("roomB".equals(roomId)) {
            // 채팅 저장
            ChatB chat_b = chatService.createChatB(roomId, message.getSenderID(), message.getSenderEmail(), message.getMessage());

            return ChatMessageDto.builder()
                    .messageType(ChatMessageDto.MessageType.TALK)
                    .roomId(roomId)
                    .senderID(chat_b.getSenderID())
                    .senderEmail(chat_b.getSenderEmail())
                    .message(chat_b.getMessage())
                    .build();
        } else {
            // 예외 처리 등
            return null;
        }
    }
}
