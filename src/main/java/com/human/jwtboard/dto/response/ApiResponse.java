package com.human.jwtboard.dto.response;
// 공통 응답 래퍼 : 응답 형태가 제각각이면 프론트앤드와 협업이 어렵고, 오류 처리 코드가 중복 됨

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;  // 성공/실패
    private String message;   // 전달 할 메시지
    private T data;           // 전달할 데이터

    // 성공 응답 (데이터만)
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "OK", data);
    }

    // 성공 응답 (메시지 + 데이터)
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    // 실패 응답
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
