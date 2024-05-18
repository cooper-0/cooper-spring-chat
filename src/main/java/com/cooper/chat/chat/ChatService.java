package com.cooper.chat.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import java.util.*;
import com.cooper.chat.chat.model.ChatRoom;
import com.cooper.chat.chat.model.Chat;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
@Service
public class ChatService {

    private final MongoTemplate mongoTemplate;
    private Map<String, ChatRoom> chatRooms;

    @Autowired
    public ChatService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<Chat> getPreviousChats(String collectionName) {
        return mongoTemplate.findAll(Chat.class, collectionName);
    }
//채팅 삭제 기능
    public void deleteMessage(String messageId, String collectionName) {
        Query query = new Query(Criteria.where("id").is(messageId));
        mongoTemplate.remove(query, Chat.class, collectionName);
    }

    // 필요없는 구문
    /*
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

    */

    // 수정된 createChat 메서드
    public Chat createChat(String collectionName, String roomId, String senderID, String senderEmail, String message) {
        Chat chat = Chat.createChat(null, roomId, senderID, senderEmail, message);
        mongoTemplate.save(chat, collectionName);
        return chat;
    }

}