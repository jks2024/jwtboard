package com.human.jwtboard.dto.response;
// 채팅방 정보를 담고, WebSocket 세션 목록을 내부적으로 관리

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Slf4j
public class ChatRoomResDto {
    private String roomId;   // 채팅방 ID
    private String name;     // 채팅방 이름
    private LocalDateTime regDate;  // 채팅방 생성 시간

    @JsonIgnore  // JSON 직렬화에서 제외
    private Set<WebSocketSession> sessions;;

    public boolean isSessionEmpty() {
        return this.sessions.isEmpty();
    }

    @Builder
    public ChatRoomResDto(String roomId, String name, LocalDateTime regDate) {
        this.roomId = roomId;
        this.name = name;
        this.regDate = regDate;
        this.sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());  // 멀티스레드 환경에서 안전
    }
}
