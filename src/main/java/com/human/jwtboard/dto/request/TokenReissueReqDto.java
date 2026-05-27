package com.human.jwtboard.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenReissueReqDto {  // Access Token 재발행
    private String accessToken;  // 만료된 Access Token
    private String refreshToken; // 유효한 Refresh Token
}
