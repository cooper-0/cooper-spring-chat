package com.cooper.chat.chat.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat_b")

public class ChatB {
    @Id // MongoDB 문서의 _id 필드를 나타냅니다.
    private String id; // ID 타입을 ObjectId로 변경합니다.
    private ChatRoom room;

    private String roomId; // 채팅 방 번호

    private String senderID; // 보낸이

    private String senderEmail; // 보낸 사람의 이메일

    private String message; // 메시지

    @CreatedDate // MongoDB에서는 자동 생성되는 날짜를 저장하기 위해 사용됩니다.
    private LocalDateTime sendDate;

    @Builder
    public ChatB(ChatRoom room, String roomId, String senderID, String senderEmail, String message) {
        this.room = room;
        this.roomId = roomId;
        this.senderID = senderID;
        this.senderEmail = senderEmail;
        this.message = message;
        this.sendDate = LocalDateTime.now();
    }
    public static ChatB createChatB(ChatRoom room, String roomId, String senderID, String senderEmail, String message) {
        return ChatB.builder()
                .room(room)
                .roomId(roomId)
                .senderID(senderID)
                .senderEmail(senderEmail)
                .message(message)
                .build();
    }
}



