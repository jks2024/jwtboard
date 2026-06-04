package com.human.jwtboard.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {
    public enum MessageType {
        ENTER,  // 채팅방 입장
        TALK,   // 일반 메시지
        CLOSE   // 채팅방 퇴장
    }
    private MessageType type;
    private String message;
    private String sender;
    private String roomId;  // 채팅방 ID
}
