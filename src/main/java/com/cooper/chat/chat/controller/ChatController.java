package com.cooper.chat.chat.controller;


import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.cooper.chat.chat.ChatService;
import com.cooper.chat.chat.model.Chat;
import com.cooper.chat.chat.model.ChatMessageDto;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/{roomId}")
    @SendTo("/room/{roomId}")
    public ChatMessageDto chat(@DestinationVariable String roomId, ChatMessageDto message) {
        // 채팅 저장
        Chat chat = chatService.createChat(roomId, message.getSenderID(), message.getSenderEmail(), message.getMessage());

        return ChatMessageDto.builder()
                .messageType(ChatMessageDto.MessageType.TALK)
                .roomId(roomId)
                .senderID(chat.getSenderID())
                .senderEmail(chat.getSenderEmail())
                .message(chat.getMessage())
                .build();
    }
}
