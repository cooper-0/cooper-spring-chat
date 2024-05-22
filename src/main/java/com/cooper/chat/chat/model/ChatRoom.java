package com.cooper.chat.chat.model;

import lombok.*;
import javax.persistence.*;


@Setter
@Getter
@Entity
public class ChatRoom {
    private String roomId;

    @Builder
    public ChatRoom(String roomId) {
        this.roomId = roomId;
    }

}
