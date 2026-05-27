package com.human.jwtboard.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long memberId;

    @Column(nullable = false, length = 512)
    private String tokenValue;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Builder
    public RefreshToken(Long memberId, String tokenValue, LocalDateTime expiresAt) {
        this.memberId   = memberId;
        this.tokenValue = tokenValue;
        this.expiresAt  = expiresAt;
    }

    // 토큰 갱신 (Sliding 방식 대응)
    public void updateToken(String newToken, LocalDateTime newExpiry) {
        this.tokenValue = newToken;
        this.expiresAt  = newExpiry;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }


}
