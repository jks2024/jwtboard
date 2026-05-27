package com.human.jwtboard.repository;

import com.human.jwtboard.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberId(Long memberId);
    Optional<RefreshToken> findByTokenValue(String tokenValue);
    void deleteByMemberId(Long memberId);  // 로그아웃 시 삭제
}
