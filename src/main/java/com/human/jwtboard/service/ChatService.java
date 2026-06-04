package com.human.jwtboard.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.human.jwtboard.dto.request.ChatMessageDto;
import com.human.jwtboard.dto.response.ChatRoomResDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ObjectMapper objectMapper; // JSON 변환 때문에 사용
    private Map<String, ChatRoomResDto> chatRooms;

    @PostConstruct  // 생성자 호출 이후
    private void init() {
        chatRooms = new LinkedHashMap<>();  // 삽입 순서를 유지
    }

    public List<ChatRoomResDto> findAllRoom() {  // 방 목록 반환
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoomResDto findRoomById(String roomId) {  // 해당 방번호 반환
        return chatRooms.get(roomId);
    }

    // 채팅방 생성
    public ChatRoomResDto createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoomResDto chatRoom = ChatRoomResDto.builder()
                .roomId(randomId)
                .name(name)
                .regDate(LocalDateTime.now())
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }
    // 채팅방 제거
    public void removeRoom(String roomId) {
        ChatRoomResDto room = chatRooms.get(roomId);
        if (room != null && room.isSessionEmpty()) {
            chatRooms.remove(roomId);
        }
    }
    // 채팅방 입장 : 세션 추가, 입장 메시지 브로드캐스트
    public void addSessionAndHandleEnter(String roomId,
                                         WebSocketSession session,
                                         ChatMessageDto chatMessage) {

        ChatRoomResDto room = findRoomById(roomId);
        if (room != null) {
            room.getSessions().add(session);
            if (chatMessage.getSender() != null) {
                chatMessage.setMessage(chatMessage.getSender() + "님이 입장 했습니다.");
                // 전체에게 메시지 전송
                sendMessageToAll(roomId, chatMessage);
            }
        }
    }
    // 채팅방 퇴장 : 세션 제거, 퇴장 메시지 브로드캐스트, 빈 방 삭제
    public void removeSessionAndHandleExit(String roomId,
                                           WebSocketSession session,
                                           ChatMessageDto chatMessage) {
        ChatRoomResDto room = findRoomById(roomId);
        if (room != null) {
            room.getSessions().remove(session);
            if (chatMessage.getSender() != null) {
                chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장했습니다.");
                // 전체 메시지 전송
                sendMessageToAll(roomId, chatMessage);
            }
            if (room.isSessionEmpty()) {
                removeRoom(roomId);
            }
        }
    }
    public void sendMessageToAll(String roomId, ChatMessageDto message) {
        ChatRoomResDto room = findRoomById(roomId);
        if (room != null) {
            room.getSessions().forEach(s -> sendMessage(s, message));
        }
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error("메시지 전송 실패: {}", e.getMessage());
        }
    }
}
