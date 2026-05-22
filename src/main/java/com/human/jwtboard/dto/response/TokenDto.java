package com.human.jwtboard.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String grantType;           // "Bearer" — 헤더 접두어
    private String accessToken;         // 실제 API 호출에 사용
    private String refreshToken;        // accessToken 재발급용
    private Long   accessTokenExpiresIn; // 만료 시각 (Unix timestamp ms)
}
