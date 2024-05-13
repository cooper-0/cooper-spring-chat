package com.cooper.chat.chat.repository;

import com.cooper.chat.chat.model.ChatB;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;


@Repository
public interface ChatBMessageRepository extends MongoRepository<ChatB, String> {

    // 특정 방의 모든 채팅 메시지를 불러오는 메서드
    List<ChatB> findByRoomId(String roomId);

    // 특정 시간 이후에 전송된 채팅 메시지를 불러오는 메서드
    List<ChatB> findBySendDateAfter(LocalDateTime sendDate);

    // 기타 필요한 쿼리 메서드 추가 가능

}
