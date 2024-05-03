package com.cooper.chat.chat.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.cooper.chat.chat.model.Chat;

@Repository
public interface ChatMessageRepository extends MongoRepository<Chat, String> {

    // 특정 방의 모든 채팅 메시지를 불러오는 메서드
    List<Chat> findByRoomId(String roomId);

    // 특정 사용자가 보낸 채팅 메시지를 불러오는 메서드
    List<Chat> findBySenderID(String senderID);

    // 특정 시간 이후에 전송된 채팅 메시지를 불러오는 메서드
    //List<Chat> findBySendDateAfter(LocalDateTime sendDate);

    // 기타 필요한 쿼리 메서드 추가 가능
}
