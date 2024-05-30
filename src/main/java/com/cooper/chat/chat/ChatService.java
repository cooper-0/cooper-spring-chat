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
import com.cooper.chat.chat.model.ChatMessageDto;


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



    // 채팅 메시지 저장 기능
    public Chat saveChatMessage(ChatMessageDto messageDto, String collectionName) {
        Chat chat = Chat.builder()
                .senderID(messageDto.getSenderID())
                .senderEmail(messageDto.getSenderEmail())
                .message(messageDto.getMessage())
                .build();
        return mongoTemplate.save(chat, collectionName);
    }

    // 수정된 createChat 메서드
    public Chat createChat(String collectionName, String roomId, String senderID, String senderEmail, String message) {
        Chat chat = Chat.createChat(null, roomId, senderID, senderEmail, message);
        mongoTemplate.save(chat, collectionName);
        return chat;
    }

    public void createChatCollection(String collectionName) {
        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
        }
    }
    // 채팅방 삭제 메서드
    public void deleteRoom(String roomId) {
        chatRooms.remove(roomId);
        mongoTemplate.dropCollection(roomId);
    }

    // 채팅방 목록 불러오기 메서드
    public List<String> getCollectionNames() {
        return new ArrayList<>(mongoTemplate.getCollectionNames());
    }

}