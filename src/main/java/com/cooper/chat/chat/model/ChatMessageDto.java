package com.cooper.chat.chat.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {
    public enum MessageType {
        ENTER, TALK
    }

    private MessageType messageType; // 메시지 타입
    private String roomId; // 방 번호
    private String senderID; // 채팅을 보낸 사람
    private String senderEmail; // 보낸 사람의 이메일
    private String message; // 메시지

    @Builder // @Builder 어노테이션 추가
    public ChatMessageDto(MessageType messageType, String roomId, String senderID, String senderEmail, String message) {
        this.messageType = messageType;
        this.roomId = roomId;
        this.senderID = senderID;
        this.senderEmail = senderEmail;
        this.message = message;
    }
}
