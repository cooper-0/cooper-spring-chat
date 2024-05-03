package com.cooper.chat.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import java.util.*;
import com.cooper.chat.chat.model.ChatRoom;
import com.cooper.chat.chat.model.Chat;
import com.cooper.chat.chat.repository.ChatMessageRepository;

@Slf4j
@Service
public class ChatService {

    private final ChatMessageRepository ChatMessageRepository;
    private Map<String, ChatRoom> chatRooms;

    @Autowired
    public ChatService(ChatMessageRepository ChatMessageRepository) {
        this.ChatMessageRepository = ChatMessageRepository;
    }


    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }

    // 수정된 createChat 메서드
    public Chat createChat(String roomId, String sender, String senderEmail, String message) {
        ChatRoom room = findRoomById(roomId);
        if (room == null) {
            // 채팅 방이 없으면 새로 생성
            room = createRoom("Default Room");
        }
        Chat chat = Chat.createChat(room, roomId, sender, senderEmail, message);
        return ChatMessageRepository.save(chat); // MongoDB에 채팅 저장
    }
}